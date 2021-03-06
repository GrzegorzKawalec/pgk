package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import pl.gkawalec.pgk.infrastructure.constant.AspectOrder;

@Aspect
@ControllerAdvice
@RequiredArgsConstructor
@Order(AspectOrder.REQUEST_HANDLER)
public class RequestHandlerAspect {

    private final RequestAuditor auditor;

    @Pointcut("execution(public * pl.gkawalec.pgk.api.controller..*(..))")
    private void controllerPointcut(){}

    @Around("controllerPointcut()")
    private Object requestHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        return auditor.auditRequest(joinPoint);
    }

}
