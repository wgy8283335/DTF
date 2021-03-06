package com.dtf.client.core.transaction;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Aspect before the join point.
 * 
 * @author wangguangyuan
 */
public interface AspectInterface {

    /**
     * Aspect before the join point.
     * 
     * @param info group information
     * @param point join point
     * @return result of method of the join point
     * @throws Throwable throwable
     */
    Object before(String info, ProceedingJoinPoint point) throws Throwable;
    
}
