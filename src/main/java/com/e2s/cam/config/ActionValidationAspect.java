package com.e2s.cam.config;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.e2s.cam.service.AuthenticationService;

@Aspect
@Component
@Order(1) // Set the desired order for your aspect
public class ActionValidationAspect {
    @Autowired
    private AuthenticationService authenticationService;

    @Around("@annotation(com.e2s.cam.annotation.Authentication)")
    public Object validateActionRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Aspect is executed!"); 
    	authenticationService.validateActionRequest(joinPoint);
        return joinPoint.proceed(); 
    }
}
