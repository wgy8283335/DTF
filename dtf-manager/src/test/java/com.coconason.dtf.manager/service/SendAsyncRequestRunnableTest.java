package com.dtf.manager.service;

import com.dtf.common.protobuf.MessageProto;
import com.dtf.manager.cache.MessageAsyncCache;
import com.dtf.manager.cache.MessageCacheInterface;
import com.dtf.manager.message.TransactionMessageForSubmit;
import com.dtf.manager.message.TransactionMessageGroup;
import com.dtf.manager.message.TransactionMessageGroupInterface;
import org.junit.Test;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendAsyncRequestRunnableTest {
    
    @Test
    public void testRun() {
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        MessageCacheInterface messageAsyncCacheProxy = createMessageAsyncCacheProxy();
        TransactionMessageGroupInterface transactionMessageForSubmit = createTransactionMessageForSubmit();
        threadPool.execute(new SendAsyncRequestRunnable(messageAsyncCacheProxy, transactionMessageForSubmit,
        null, "345", MessageProto.Message.ActionType.ADD_ASYNC, null));
    }
    
    private MessageCacheInterface createMessageAsyncCacheProxy() {
        MessageAsyncCache result = new MessageAsyncCache();
        TransactionMessageGroupInterface transactionMessageGroup = new TransactionMessageGroup("345");
        result.put("345", transactionMessageGroup);
        return result;
    }
    
    private TransactionMessageGroupInterface createTransactionMessageForSubmit() {
        TransactionMessageGroupInterface result = new TransactionMessageForSubmit("345", new HashSet<>());
        return result;
    }
    
}
