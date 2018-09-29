package com.coconason.dtf.manager.cache;

import com.coconason.dtf.manager.message.MessageInfo;
import com.coconason.dtf.manager.message.TransactionMessageGroupAsync;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Jason
 * @date: 2018/8/24-16:34
 */
public class MessageAsyncCache {

    private Cache<String,TransactionMessageGroupAsync> cache;

    public MessageAsyncCache() {
        cache = CacheBuilder.newBuilder().expireAfterAccess(600, TimeUnit.SECONDS).maximumSize(1000000L).build();
    }

    public synchronized TransactionMessageGroupAsync get(String id){
        return cache.getIfPresent(id);
    }

    public synchronized void put(String id,TransactionMessageGroupAsync msg){
        cache.put(id, msg);
    }

    public synchronized void putDependsOnConditionAsync(TransactionMessageGroupAsync groupAsync){
        Object o = cache.getIfPresent(groupAsync.getGroupId());
        if(o==null){
            cache.put(groupAsync.getGroupId(),groupAsync);
        }else{
            TransactionMessageGroupAsync transactionMessageGroupAsync = (TransactionMessageGroupAsync)o;
            if(transactionMessageGroupAsync.equals(groupAsync)){
                return;
            }
            Set<MessageInfo> memberSet = groupAsync.getMemberSet();
            for(MessageInfo messageInfo:memberSet){
                transactionMessageGroupAsync.addMember(messageInfo.getMemberId(),messageInfo.getUrl(),messageInfo.getObj());
            }
            cache.put(transactionMessageGroupAsync.getGroupId(),transactionMessageGroupAsync);
        }
    }

    public long getSize(){
        return cache.size();
    }

    public void clear(Object id){
        cache.invalidate(id);
    }

}
