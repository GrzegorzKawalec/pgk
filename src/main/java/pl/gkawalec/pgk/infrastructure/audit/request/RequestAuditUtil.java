package pl.gkawalec.pgk.infrastructure.audit.request;

import lombok.experimental.UtilityClass;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.gkawalec.pgk.common.annotation.request.AuditedRequest;
import pl.gkawalec.pgk.common.annotation.request.NotAuditedRequest;
import pl.gkawalec.pgk.common.util.ArrayUtil;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.common.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@UtilityClass
class RequestAuditUtil {

    HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest();
    }

    boolean canAuditRequestMethod(HttpServletRequest request, JoinPoint joinPoint) {
        if (Objects.isNull(request) || Objects.isNull(joinPoint)) {
            return false;
        }

        Signature signature = joinPoint.getSignature();
        if (isNotRestController(signature)) {
            return false;
        }

        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        Boolean valueFromAuditedRequestAnnotation = getValueFromAuditedRequestAnnotation(method);
        if (Objects.nonNull(valueFromAuditedRequestAnnotation)) {
            return valueFromAuditedRequestAnnotation;
        }

        if (hasMappingAnnotation(method)) {
            return true;
        }
        return checkRequestMapping(method);
    }

    private boolean isNotRestController(Signature signature) {
        Class<?> controllerClass = signature.getDeclaringType();
        RestController restControllerAnnotation = controllerClass.getAnnotation(RestController.class);
        return Objects.isNull(restControllerAnnotation);
    }

    private Boolean getValueFromAuditedRequestAnnotation(Method method) {
        NotAuditedRequest notAuditedRequestAnnotation = method.getAnnotation(NotAuditedRequest.class);
        if (Objects.nonNull(notAuditedRequestAnnotation)) {
            return false;
        }

        AuditedRequest auditedRequestAnnotation = method.getAnnotation(AuditedRequest.class);
        return Objects.isNull(auditedRequestAnnotation) ? null :
                auditedRequestAnnotation.value();
    }

    private boolean hasMappingAnnotation(Method method) {
        return AuditedRequestMethods.AUDIT_MAPPING_ANNOTATION.stream()
                .anyMatch(method::isAnnotationPresent);
    }

    private boolean checkRequestMapping(Method method) {
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        if (Objects.isNull(requestMappingAnnotation)) {
            return false;
        }

        RequestMethod[] requestMethods = requestMappingAnnotation.method();
        if (ArrayUtil.isEmpty(requestMethods)) {
            return false;
        }

        for (RequestMethod requestMethod : requestMethods) {
            if (AuditedRequestMethods.AUDIT_REQUEST_METHODS.contains(requestMethod)) {
                return true;
            }
        }
        return false;
    }

    String getRequestParam(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (CollectionUtil.isEmpty(parameterMap)) {
            return null;
        }
        StringJoiner result = new StringJoiner(",");
        parameterMap.forEach((paramName, params) -> {
            for (String paramVal : params) {
                result.add("[" + paramName + "=" + paramVal + "]");
            }
        });
        return result.toString();
    }

    String getRequestBody(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (ArrayUtil.isEmpty(args)) {
            return null;
        }
        List<Object> rawRequestBodyList = getRawRequestBodyList(joinPoint, args);
        return convertRawRequestBodyList(rawRequestBodyList);
    }

    private List<Object> getRawRequestBodyList(JoinPoint joinPoint, Object[] argsFromJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (ArrayUtil.isEmpty(parameterAnnotations)) {
            return Collections.emptyList();
        }
        return getRawRequestBodyList(argsFromJoinPoint, parameterAnnotations);
    }

    private List<Object> getRawRequestBodyList(Object[] argsFromJoinPoint, Annotation[][] parameterAnnotations) {
        List<Object> result = new ArrayList<>();
        for (int argIndex = 0; argIndex < argsFromJoinPoint.length; argIndex++) {
            for (Annotation annotation : parameterAnnotations[argIndex]) {
                if (annotation instanceof RequestBody) {
                    Object singleArgFromJoinPoint = argsFromJoinPoint[argIndex];
                    if (Objects.nonNull(singleArgFromJoinPoint)) {
                        result.add(singleArgFromJoinPoint);
                    }
                }
            }
        }
        return result;
    }

    private String convertRawRequestBodyList(List<Object> rawRequestBodyList) {
        if (CollectionUtil.isEmpty(rawRequestBodyList)) {
            return null;
        }
        StringJoiner result = new StringJoiner(" | ");
        rawRequestBodyList.forEach(rawRequestBody -> {
            String json = JSONConverter.toJSON(rawRequestBody);
            result.add(json);
        });
        return StringUtil.trim(
                result.toString()
        );
    }

}
