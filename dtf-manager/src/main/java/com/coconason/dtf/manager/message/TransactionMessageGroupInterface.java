package com.coconason.dtf.manager.message;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Set;

/**
 * @Author: Jason
 * @date: 2018/10/3-10:13
 */
public interface TransactionMessageGroupInterface<T> {
    String getGroupId();

    void setGroupId(String groupId);

    List<TransactionMessageForAdding> getMemberList();

    Set<T> getMemberSet();

    void addMember(String memberId,String url,Object obj);

    void addMemberToGroup(TransactionMessageForAdding e);

    void setCtxForSubmitting(ChannelHandlerContext ctxForSubmitting);

    ChannelHandlerContext getCtx();

    String getGroupMemberId();

    Boolean isCommitted();

    void setCommitted(boolean commited);

    String getUrl();

    Object getObj();
}
