package com.dtf.manager.service;

import com.dtf.common.protobuf.MessageProto;
import com.dtf.manager.utils.MessageSender;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Send short message.
 * 
 * @author wangguangyuan
 */
public final class SendShortMessageRunnable implements Runnable {
    
    /**
     * Logger of send short message.
     */
    private Logger logger = LoggerFactory.getLogger(SendShortMessageRunnable.class);
    
    /**
     * Group id.
     */
    private String groupId;

    /**
     * Action type.
     */
    private MessageProto.Message.ActionType actionType;

    /**
     * Channel handler context.
     */
    private ChannelHandlerContext ctx;
    
    public SendShortMessageRunnable(final String groupId, final MessageProto.Message.ActionType actionType, final ChannelHandlerContext ctx) {
        this.groupId = groupId;
        this.actionType = actionType;
        this.ctx = ctx;
    }

    /**
     * Send message.
     */
    @Override
    public void run() {
        MessageSender.sendMsg(groupId, actionType, ctx);
    }
    
}
