package com.coconason.dtf.demo2.threadpools;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Jason
 * @date: 2018/9/17-11:36
 */
public class ThreadPoolForServer {
    private ExecutorService pool;

    public ThreadPoolForServer() {
        pool = new ThreadPoolExecutor(100,1000,0L, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(1000),new DefaultThreadFactory("defaultThreadFactory"),new ThreadPoolExecutor.AbortPolicy());
    }

    public void addTask(Runnable runnable){
        pool.execute(runnable);
    }
}