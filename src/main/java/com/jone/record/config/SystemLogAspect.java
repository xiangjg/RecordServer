package com.jone.record.config;

import com.alibaba.fastjson.JSONObject;
import com.jone.record.controller.common.SystemControllerLog;
import com.jone.record.dao.RedisDao;
import com.jone.record.entity.system.SystemLog;
import com.jone.record.entity.vo.UserInfo;
import com.jone.record.service.SystemLogService;
import com.jone.record.util.IpUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
@SuppressWarnings("all")
public class SystemLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);
    @Resource
    private SystemLogService systemLogService;
    @Resource
    private RedisDao redisDao;

    @Pointcut("@annotation(com.jone.record.controller.common.SystemControllerLog)")
    public void controllerAspect(){
    }

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //读取session中的用户
        String key = request.getHeader("token") == null ? "" : request.getHeader("token");
        String value = redisDao.getValue(key);
        UserInfo userInfo = new UserInfo();
        if (!StringUtils.isEmpty(value))
            userInfo = JSONObject.parseObject(value, UserInfo.class);

        String ip = IpUtils.getIpAddr(request);

        try {
            //*========控制台输出=========*//
            System.out.println("==============前置通知开始==============");
            System.out.println("请求方法" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()));
            System.out.println("方法描述：" + getControllerMethodDescription(joinPoint));
            System.out.println("请求人："+userInfo.getUserName());
            System.out.println("请求ip："+ip);

            //*========数据库日志=========*//
            SystemLog systemLog = new SystemLog();
            systemLog.setRemark(getControllerMethodDescription(joinPoint));
            systemLog.setType(0);
            systemLog.setIp(ip);
            systemLog.setUserId(userInfo.getUserId());
            systemLog.setTm(new Date());
            //保存数据库
            systemLogService.save(systemLog);

        }catch (Exception e){
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息：{}",e.getMessage());
        }
    }

    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}
