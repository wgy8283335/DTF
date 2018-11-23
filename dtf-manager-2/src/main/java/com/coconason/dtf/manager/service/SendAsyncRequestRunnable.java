package com.coconason.dtf.manager.service;

import com.coconason.dtf.manager.cache.MessageCacheInterface;
import com.coconason.dtf.manager.message.MessageInfoInterface;
import com.coconason.dtf.manager.message.TransactionMessageGroupInterface;
import com.coconason.dtf.manager.utils.HttpClientUtil;

import java.util.Queue;
import java.util.Set;

/**
 * @Author: Jason
 * @date: 2018/9/6-13:04
 */
public final class SendAsyncRequestRunnable implements Runnable{

    private MessageCacheInterface messageAsyncCacheProxy;

    private TransactionMessageGroupInterface transactionMessageForSubmit;

    private Queue messageAsyncQueueProxy;

    public SendAsyncRequestRunnable(MessageCacheInterface messageAsyncCacheProxy, TransactionMessageGroupInterface transactionMessageForSubmit, Queue messageAsyncQueueProxy) {
        this.messageAsyncCacheProxy = messageAsyncCacheProxy;
        this.transactionMessageForSubmit = transactionMessageForSubmit;
        this.messageAsyncQueueProxy = messageAsyncQueueProxy;
    }

    @Override
    public void run() {
            //get the TransactionMessageGroupAsync from the messageAsyncCacheProxy
            TransactionMessageGroupInterface theMessageGroupAsync = messageAsyncCacheProxy.get(transactionMessageForSubmit.getGroupId());
            Set<MessageInfoInterface> theMemberSet = theMessageGroupAsync.getMemberSet();
            for(MessageInfoInterface messageInfo :theMemberSet){
                String url= messageInfo.getUrl();
                String obj = messageInfo.getObj().toString();
                String result = HttpClientUtil.doPostJson(url,obj,transactionMessageForSubmit.getGroupId());
                if("".equals(result)){
                    messageAsyncQueueProxy.add(messageInfo);
                }else{
                    messageInfo.setCommitted(true);
                }
            }
            messageAsyncCacheProxy.invalidate(transactionMessageForSubmit.getGroupId());
    }
}
