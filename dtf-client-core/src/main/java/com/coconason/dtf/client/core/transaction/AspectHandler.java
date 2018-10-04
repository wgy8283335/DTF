package com.coconason.dtf.client.core.transaction;

import com.coconason.dtf.client.core.annotation.DtfTransaction;
import com.coconason.dtf.client.core.beans.*;
import com.coconason.dtf.client.core.dbconnection.ClientLockAndCondition;
import com.coconason.dtf.client.core.dbconnection.ClientLockAndConditionInterface;
import com.coconason.dtf.client.core.dbconnection.DbOperationType;
import com.coconason.dtf.client.core.nettyclient.protobufclient.NettyService;
import com.coconason.dtf.client.core.utils.GroupidGenerator;
import com.coconason.dtf.common.protobuf.MessageProto;
import com.coconason.dtf.common.utils.UuidGenerator;
import com.google.common.cache.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.coconason.dtf.client.core.constants.Member.ORIGINAL_ID;

/**
 * @Author: Jason
 * @date: 2018/8/19-20:38
 */
@Component
public class AspectHandler implements AspectInterface {

    @Autowired
    @Qualifier("transactionMessageQueueProxy")
    private Queue queue;
    @Autowired
    private NettyService nettyService;
    @Autowired
    @Qualifier("threadLockCacheProxy")
    private Cache<String,ClientLockAndConditionInterface>  asyncFinalCommitThreadLockCacheProxy;

    @Autowired
    @Qualifier("threadLockCacheProxy")
    private Cache<String,ClientLockAndConditionInterface>  syncFinalCommitThreadLockCacheProxy;

    @Override
    public Object before(String info,ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        Object[] args = point.getArgs();
        Method currentMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
        DtfTransaction transaction = currentMethod.getAnnotation(DtfTransaction.class);
        TransactionType transactionType = TransactionType.newInstance(transaction.type());
        TransactionType.setCurrent(transactionType);
        Object result = null;
        Transactional transactional = currentMethod.getAnnotation(Transactional.class);
        if (transactional == null) {
            transactional = clazz.getAnnotation(Transactional.class);
        }
        //declare one transaction group
        //TransactionGroupInfo groupInfo;
        //1.When the service is creator,should create groupId and store in public object.
        //2.Then execute the program.And if the program has transactional operation in database,
        //should use database proxy to send transaction information to the transaction server.
        //3.At the end send submit request to the server, and listen the response from server.
        //If success, submit transaction by database proxy.If fail,cancel transaction by database proxy.
        if(TransactionType.ASYNC_FINAL == transactionType){
            if(info==null) {
                //1.
                String groupIdTemp = GroupidGenerator.getStringId(0, 0);
                BaseTransactionGroupInfo groupInfo = TransactionGroupInfoFactory.getInstanceWithGroupidMemid(groupIdTemp, ORIGINAL_ID);
                TransactionGroupInfo.setCurrent(groupInfo);
                //2.
                result = point.proceed();
                //3.Send confirm message to netty server, in order to commit all transaction in the service
                ClientLockAndConditionInterface asyncFinalCommitLc = new ClientLockAndCondition(new ReentrantLock(), DbOperationType.DEFAULT);
                asyncFinalCommitThreadLockCacheProxy.put(TransactionGroupInfo.getCurrent().getGroupId(), asyncFinalCommitLc);
                BaseTransactionServiceInfo serviceInfo = TransactionServiceInfoFactory.newInstanceForAsyncCommit(UuidGenerator.generateUuid(), MessageProto.Message.ActionType.ASYNC_COMMIT, TransactionGroupInfo.getCurrent().getGroupId(),TransactionGroupInfo.getCurrent().getGroupMembers());
                asyncFinalCommitLc.awaitLimitedTime(nettyService,serviceInfo,"commit async fail",10000, TimeUnit.MILLISECONDS);
            }else{
                result = point.proceed();
            }
        }else{
            BaseTransactionGroupInfo transactionGroupInfo = info == null ? null:TransactionGroupInfoFactory.getInstanceParseString(info);
            if(transactionGroupInfo == null) {
                //1.
                String groupIdTemp = GroupidGenerator.getStringId(0, 0);
                BaseTransactionGroupInfo groupInfo = TransactionGroupInfoFactory.getInstanceWithGroupidMemid(groupIdTemp, ORIGINAL_ID);
                TransactionGroupInfo.setCurrent(groupInfo);
                switchTransactionType(transactionType,groupInfo,method,args);
                //2.
                try {
                    result = point.proceed();
                }catch (Exception e){
                    if(ORIGINAL_ID.equals(TransactionGroupInfo.getCurrent().getMemberId())){
                        if(TransactionType.SYNC_FINAL == transactionType){
                            queue.add(TransactionServiceInfoFactory.newInstanceWithGroupidSet(UuidGenerator.generateUuid(), MessageProto.Message.ActionType.CANCEL,TransactionGroupInfo.getCurrent().getGroupId(),TransactionGroupInfo.getCurrent().getGroupMembers()));
                        }else if(TransactionType.SYNC_STRONG == transactionType){
                            queue.add(TransactionServiceInfoFactory.newInstanceWithGroupidSet(UuidGenerator.generateUuid(), MessageProto.Message.ActionType.CANCEL,TransactionGroupInfo.getCurrent().getGroupId(),TransactionGroupInfo.getCurrent().getGroupMembers()));
                        }
                    }
                }
                if(ORIGINAL_ID.equals(TransactionGroupInfo.getCurrent().getMemberId())){
                    if(TransactionType.SYNC_STRONG == transactionType){
                        if(syncFinalCommitThreadLockCacheProxy.getIfPresent(TransactionGroupInfo.getCurrent().getGroupId()).getState()==DbOperationType.WHOLE_FAIL){
                            throw new Exception("system transaction error");
                        }
                    }
                }
            }
            //1.When the service is follower,execute the program.And if the program has transactional operation in database,
            //should use database proxy to send transaction information to the transaction server.
            //2.And listen the response from server.
            //If the response is submit,submit transaction by database proxy.If the response is cancel,cancel transaction by database proxy.
            else{
                //if the thread does not have transactionGroupInfo,set current transaction group information
                BaseTransactionGroupInfo temp = TransactionGroupInfo.getCurrent();
                if(temp==null){
                    transactionGroupInfo.addNewMemeber();
                    TransactionGroupInfo.setCurrent(transactionGroupInfo);
                }
                //if the thread does not have transactionServiceInfo,set current transaction service information
                if(temp==null){
                    switchTransactionType(transactionType,transactionGroupInfo,method,args);
                }
                result =  point.proceed();
            }
        }
        System.out.println("Aspectandler return result;");
        return result;
    }

    private void switchTransactionType(TransactionType transactionType,BaseTransactionGroupInfo transactionGroupInfo,Method method,Object[] args){
        switch (transactionType){
            case SYNC_FINAL:
                BaseTransactionServiceInfo.setCurrent(TransactionServiceInfoFactory.newInstanceForSyncAdd(UuidGenerator.generateUuid(), MessageProto.Message.ActionType.ADD,transactionGroupInfo.getGroupId(),transactionGroupInfo.getMemberId(),method,args));
                break;
            case SYNC_STRONG:
                BaseTransactionServiceInfo.setCurrent(TransactionServiceInfoFactory.newInstanceForSyncAdd(UuidGenerator.generateUuid(), MessageProto.Message.ActionType.ADD_STRONG,transactionGroupInfo.getGroupId(),transactionGroupInfo.getMemberId(),method,args));
                break;
            default:
                break;
        }
    }
}
