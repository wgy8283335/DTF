package com.coconason.dtf.manager.protobufserver.strategy;

import com.coconason.dtf.common.protobuf.MessageProto;
import com.coconason.dtf.manager.cache.MessageCacheInterface;
import com.coconason.dtf.manager.message.TransactionMessageFactory;
import com.coconason.dtf.manager.protobufserver.ServerTransactionHandler;
import com.coconason.dtf.manager.service.CheckAndSubmitRunnable;
import com.coconason.dtf.manager.thread.ServerThreadLockCacheProxy;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * Handle message for add strong action.
 * 
 * @Author: Jason
 */
public class HandleMessageForAddStrong implements HandleMessageStrategy {

    /**
     * Handle message for add strong action.
     * 
     * @param serverTransactionHandler server transaction handler
     * @param ctx channel handler context
     * @param msg message
     */
    @Override
    public void handleMessage(final ServerTransactionHandler serverTransactionHandler, final ChannelHandlerContext ctx, final Object msg) {
        MessageCacheInterface messageSyncCacheProxy = serverTransactionHandler.getMessageSyncCacheProxy();
        ExecutorService threadPoolForServerProxy = serverTransactionHandler.getThreadPoolForServerProxy();
        MessageCacheInterface messageForSubmitSyncCacheProxy = serverTransactionHandler.getMessageForSubmitSyncCacheProxy();
        ServerThreadLockCacheProxy serverThreadLockCacheProxy = serverTransactionHandler.getServerThreadLockCacheProxy();
        MessageProto.Message message = (MessageProto.Message) msg;
        messageSyncCacheProxy.putDependsOnCondition(TransactionMessageFactory.getMessageGroupInstance(message, ctx));
        threadPoolForServerProxy.execute(new CheckAndSubmitRunnable(message, MessageProto.Message.ActionType.ADD_STRONG, ctx, 
                messageForSubmitSyncCacheProxy, messageSyncCacheProxy, serverThreadLockCacheProxy, threadPoolForServerProxy));
    }
    
}