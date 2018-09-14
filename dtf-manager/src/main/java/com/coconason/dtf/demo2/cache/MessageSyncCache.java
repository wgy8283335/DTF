package com.coconason.dtf.demo2.cache;

import com.coconason.dtf.demo2.message.TransactionMessageGroup;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @Author: Jason
 * @date: 2018/8/24-16:34
 */
public class MessageSyncCache {

    private Cache<String,TransactionMessageGroup> cache;

    public MessageSyncCache() {
        cache = CacheBuilder.newBuilder().maximumSize(1000000L).build();
    }

    public synchronized TransactionMessageGroup get(String id){
        return cache.getIfPresent(id);
    }

    public synchronized void put(String id,TransactionMessageGroup msg){
        cache.put(id, msg);
    }

    public synchronized void putDependsOnCondition(TransactionMessageGroup group){
        Object element = cache.getIfPresent(group.getGroupId());
        if(element==null){
            cache.put(group.getGroupId(),group);
        }else{
            TransactionMessageGroup transactionMessageGroup = (TransactionMessageGroup) element;
            transactionMessageGroup.getMemberList().add(group.getMemberList().get(0));
            transactionMessageGroup.getMemberSet().add(group.getMemberList().get(0).getGroupMemberId());
            cache.put(transactionMessageGroup.getGroupId(),transactionMessageGroup);
        }
    }

    public long getSize(){
        return cache.size();
    }

    public void clear(String id){
        cache.invalidate(id);
    }

}