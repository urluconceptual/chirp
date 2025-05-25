package org.unibuc.chirp.impl.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {
    @Before("within(@org.springframework.stereotype.Controller *)")
    public void logBefore(JoinPoint joinPoint) {
        log.info("[CONTROLLER] Calling method: {} with args: {}",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs());
    }

    @AfterReturning(
        pointcut = "within(@org.springframework.stereotype.Controller *)",
        returning = "result"
    )
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info("[CONTROLLER] Method {} returned: {}", joinPoint.getSignature().toShortString(), result);
    }
}
