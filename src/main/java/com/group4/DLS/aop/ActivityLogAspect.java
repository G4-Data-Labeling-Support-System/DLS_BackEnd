package com.group4.DLS.aop;

import java.lang.reflect.Field;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.group4.DLS.domain.entity.User;
import com.group4.DLS.security.CurrentUserProvider;
import com.group4.DLS.services.ActivityLogService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogAspect {

    ActivityLogService activityLogService;
    HttpServletRequest httpServletRequest;
    CurrentUserProvider userProvider;

    @AfterReturning(pointcut = "@annotation(logActivity)", returning = "result")
    public void logAfterSuccess(
            JoinPoint joinPoint,
            LogActivity logActivity,
            Object result) {
        String entityId = extractEntityId(joinPoint, logActivity, result);
        handleLog(joinPoint, logActivity, "SUCCESS", null, entityId);
    }

    @AfterThrowing(pointcut = "@annotation(logActivity)", throwing = "ex")
    public void logAfterFail(
            JoinPoint joinPoint,
            LogActivity logActivity,
            Exception ex
        ) {
            String entityId = extractEntityId(joinPoint, logActivity, null);
            handleLog(joinPoint, logActivity, "FAILED", ex.getMessage(), entityId);
    }

    private void handleLog(
            JoinPoint joinPoint,
            LogActivity logActivity,
            String status,
            String errorMessage,
            String entityId
        ) {
        try {
            String ip = getClientIp();
            User user = userProvider.getCurrentUser();

            String description = logActivity.description();

            if ("FAILED".equals(status)) {
                description += " | Error: " + errorMessage;
            }

            activityLogService.log(
                    logActivity.action(),
                    logActivity.entity(),
                    entityId,
                    description,
                    ip,
                    user);
        } catch (Exception e) {
            System.out.println("Logging failed: " + e.getMessage());
        }
    }

    private String extractEntityId(
            JoinPoint joinPoint,
            LogActivity logActivity,
            Object result) {

        // 1. Try param first
        String paramName = logActivity.entityIdParam();

        if (!paramName.isEmpty()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            for (int i = 0; i < paramNames.length; i++) {
                if (paramNames[i].equals(paramName)) {
                    return String.valueOf(args[i]);
                }
            }
        }

        // 2. Try return object (for CREATE)
        if (result != null) {
            try {
                String fieldName = logActivity.entityIdField();

                Field field = result.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);

                return String.valueOf(field.get(result));

            } catch (Exception ignored) {
            }
        }

        return "UNKNOWN";
    }

    private String getClientIp() {
        String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");

        if (xfHeader == null) {
            return httpServletRequest.getRemoteAddr();
        }

        return xfHeader.split(",")[0];
    }
}