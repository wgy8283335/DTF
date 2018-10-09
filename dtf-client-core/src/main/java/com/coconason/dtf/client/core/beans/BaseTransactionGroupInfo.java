package com.coconason.dtf.client.core.beans;

import java.util.Set;

/**
 * @Author: Jason
 * @date: 2018/8/21-13:31
 */
public abstract class BaseTransactionGroupInfo {

    private final static ThreadLocal<BaseTransactionGroupInfo> current = new ThreadLocal<>();

    public static BaseTransactionGroupInfo getCurrent(){
        return current.get();
    }

    public static void setCurrent(BaseTransactionGroupInfo transactionGroupInfo){
        current.set(transactionGroupInfo);
    }

    public abstract void addNewMemeber();

    public abstract void addMemebers(Set<Long> tempSet);

    public abstract Long getMemberId();

    public abstract String getGroupId();

    public abstract Set<Long> getGroupMembers();

}