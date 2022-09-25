package com.example.account.service;

import com.example.account.aop.AccountLockInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;

    @Around("@annotation(com.example.account.aop.AccountLock) && args(request)")
    public Object aroundMethod(
            ProceedingJoinPoint pjp,
            AccountLockInterface request
    ) throws Throwable {

        lockService.lock(request.getAccountNumber());
        try {
            return pjp.proceed();
        } finally {

            lockService.unlock(request.getAccountNumber());
        }
    }
}
