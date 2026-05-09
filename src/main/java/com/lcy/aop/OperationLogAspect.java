package com.lcy.aop;

import com.lcy.anno.Log;
import com.lcy.pojo.OperateLog;
import com.lcy.utils.CurrentHolder;
import com.lcy.utils.MQUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private MQUtils mqUtils;

    @Around("@annotation(com.lcy.anno.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        OperateLog olog = new OperateLog();
        olog.setOperateEmpId(getCurrentUserId());
        olog.setOperateTime(LocalDateTime.now());
        olog.setClassName(joinPoint.getTarget().getClass().getName());
        olog.setMethodName(joinPoint.getSignature().getName());
        olog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
        olog.setReturnValue(result != null ? result.toString() : "null");
        olog.setCostTime(costTime);

        log.info("发送操作日志到MQ: {}", olog);
        mqUtils.sendOperateLog(olog);
        return result;
    }

    private int getCurrentUserId() {
        return CurrentHolder.getCurrentId();
    }
}