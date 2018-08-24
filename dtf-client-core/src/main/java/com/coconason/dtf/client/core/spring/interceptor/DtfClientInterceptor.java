package com.coconason.dtf.client.core.spring.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.coconason.dtf.client.core.transaction.AspectHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Jason
 * @date: 2018/7/24-9:07
 */
@Aspect
@Component
public class DtfClientInterceptor {

    @Autowired
    private AspectHandler aspectHandler;

    @Around("@annotation(com.coconason.dtf.client.core.annotation.DtfTransaction)")
    public Object dtfTransactionExecute(ProceedingJoinPoint joinPoint)throws Throwable{
//        System.out.println("Start to proceed Aspect");
//        new Client("localhost", 8848).start();
//        Object result = joinPoint.proceed();
//        System.out.println("End to proceed Aspect");
//        return result;

        //If the model is creator the request would be null.
        //If the model is follower the request would not be null.
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes == null ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
        String info = request == null ? null : request.getHeader("info");
        String groupId = info == null ? null : JSONObject.parseObject(info).get("groupId").toString();
        Integer groupMemberId = info == null ? null : (Integer)JSONObject.parseObject(info).get("groupMemberId");
        return aspectHandler.before(groupId,groupMemberId,joinPoint);
    }
}
