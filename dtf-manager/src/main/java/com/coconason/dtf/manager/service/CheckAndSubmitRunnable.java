package com.coconason.dtf.manager.service;

import com.alibaba.fastjson.JSONObject;
import com.coconason.dtf.common.protobuf.MessageProto;
import com.coconason.dtf.common.protobuf.MessageProto.Message.ActionType;
import com.coconason.dtf.manager.cache.MessageForSubmitSyncCache;
import com.coconason.dtf.manager.cache.MessageSyncCache;
import com.coconason.dtf.manager.cache.ThreadsInfo;
import com.coconason.dtf.manager.message.TransactionMessageForAdding;
import com.coconason.dtf.manager.message.TransactionMessageForSubmit;
import com.coconason.dtf.manager.message.TransactionMessageGroup;
import com.coconason.dtf.manager.utils.LockAndCondition;
import com.coconason.dtf.manager.utils.MessageSender;
import com.coconason.dtf.manager.utils.SetUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Jason
 * @date: 2018/9/19-10:26
 */
public class CheckAndSubmitRunnable implements Runnable{

    private MessageProto.Message message;

    private ActionType actionType;

    private ChannelHandlerContext ctx;

    private MessageForSubmitSyncCache messageForSubmitSyncCache;

    private MessageSyncCache messageSyncCache;

    private ThreadsInfo threadsInfo;

    public CheckAndSubmitRunnable(MessageProto.Message message, ActionType actionType, ChannelHandlerContext ctx, MessageForSubmitSyncCache messageForSubmitSyncCache, MessageSyncCache messageSyncCache, ThreadsInfo threadsInfo) {
        this.message = message;
        this.actionType = actionType;
        this.ctx = ctx;
        this.messageForSubmitSyncCache = messageForSubmitSyncCache;
        this.messageSyncCache = messageSyncCache;
        this.threadsInfo = threadsInfo;
    }

    @Override
    public void run() {
        try{
            JSONObject info = JSONObject.parseObject(message.getInfo());
            String groupId = info.get("groupId").toString();
            Object obj = info.get("groupMemberSet");
            TransactionMessageForSubmit tmfs = obj == null ? messageForSubmitSyncCache.get(groupId):new TransactionMessageForSubmit(message);
            if(tmfs == null||tmfs.getMemberSet().isEmpty()||messageSyncCache.get(tmfs.getGroupId())==null){
                return;
            }
            Set setFromMessage =tmfs.getMemberSet();
            TransactionMessageGroup elementFromCache = messageSyncCache.get(tmfs.getGroupId());
            Set setFromCache = elementFromCache.getMemberSet();
            elementFromCache.setCtxForSubmitting(ctx);
            messageSyncCache.put(elementFromCache.getGroupId(),elementFromCache);
            List<TransactionMessageForAdding> memberList = elementFromCache.getMemberList();
            //check whether the member from message has the same element as the member from cache.
            if(!setFromMessage.isEmpty()){
                if(SetUtil.isSetEqual(setFromMessage,setFromCache)){
                    for (TransactionMessageForAdding messageForAdding: memberList) {
                        if(actionType == ActionType.ADD || actionType == ActionType.APPROVESUBMIT){
                            MessageSender.sendMsg(elementFromCache.getGroupId()+messageForAdding.getGroupMemberId(),ActionType.APPROVESUBMIT,messageForAdding.getCtx());
                        }else{
                            //success
                            LockAndCondition lc = new LockAndCondition(new ReentrantLock());
                            threadsInfo.put(elementFromCache.getGroupId(),lc);
                            lc.sendAndWaitForSignal(elementFromCache.getGroupId()+messageForAdding.getGroupMemberId(),ActionType.APPROVESUBMIT_STRONG,messageForAdding.getCtx(),"send approve submit strong fail");
                            //MessageSender.sendMsg(elementFromCache.getGroupId()+messageForAdding.getGroupMemberId(),ActionType.APPROVESUBMIT_STRONG,messageForAdding.getCtx());
                        }
                    }
                    if(actionType == ActionType.ADD || actionType == ActionType.APPROVESUBMIT){
                        messageSyncCache.clear(tmfs.getGroupId());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}