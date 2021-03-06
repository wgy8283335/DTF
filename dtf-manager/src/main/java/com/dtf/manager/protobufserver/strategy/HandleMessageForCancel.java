package com.dtf.manager.protobufserver.strategy;

import com.dtf.common.protobuf.MessageProto;
import com.dtf.manager.cache.MessageCacheInterface;
import com.dtf.manager.protobufserver.ServerTransactionHandler;
import com.dtf.manager.service.CancelRunnable;
import com.dtf.manager.thread.ServerThreadLockCacheProxy;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * Handle message for cancel action.
 * 
 * @author wangguangyuan
 */
public class HandleMessageForCancel implements HandleMessageStrategy {
    
    /**
     * Handle message for cancel action.
     * 
     * @param serverTransactionHandler server transaction handler
     * @param ctx channel handler context
     * @param msg message
     */
    @Override
    public void handleMessage(final ServerTransactionHandler serverTransactionHandler, final ChannelHandlerContext ctx, final Object msg) {
        MessageCacheInterface messageSyncCacheProxy = serverTransactionHandler.getMessageSyncCacheProxy();
        ExecutorService threadPoolForServerProxy = serverTransactionHandler.getThreadPoolForServerProxy();
        ServerThreadLockCacheProxy serverThreadLockCacheProxy = serverTransactionHandler.getServerThreadLockCacheProxy();
        MessageProto.Message message = (MessageProto.Message) msg;
        threadPoolForServerProxy.execute(new CancelRunnable(message, MessageProto.Message.ActionType.CANCEL, ctx, 
                messageSyncCacheProxy, serverThreadLockCacheProxy, threadPoolForServerProxy));
    }
    
}
