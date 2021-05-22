package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Aspect
@ControllerAdvice
@RequiredArgsConstructor
public class RequestHandler {

    private final RequestAuditor auditor;

    @Pointcut("execution(public * pl.gkawalec.pgk.api.controller..*(..))")
    private void controllerPointcut(){}

    @Around("controllerPointcut()")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return auditor.auditRequest(joinPoint);
    }

}