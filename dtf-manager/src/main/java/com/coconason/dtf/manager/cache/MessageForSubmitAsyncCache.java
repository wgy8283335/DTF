package com.coconason.dtf.manager.cache;

import com.coconason.dtf.manager.message.TransactionMessageForSubmit;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Jason
 * @date: 2018/8/24-16:34
 */
public class MessageForSubmitAsyncCache {

    private Cache<String,TransactionMessageForSubmit> cache;

    public MessageForSubmitAsyncCache() {
        cache = CacheBuilder.newBuilder().expireAfterAccess(600, TimeUnit.SECONDS).maximumSize(1000000L).build();
    }

    public synchronized TransactionMessageForSubmit get(String id){
        return cache.getIfPresent(id);
    }

    public synchronized void put(TransactionMessageForSubmit msg){
        cache.put(msg.getGroupId(), msg);
    }

    public long getSize(){
        return cache.size();
    }

    public void clear(String id){
        cache.invalidate(id);
    }

}
