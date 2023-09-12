package com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
//import com.tongdatech.winterspring.zczx.config.Profiles;
import com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation.NoticeChooser;
import com.tongdatech.winterspring.zczx.webSocketConfig.SocketComponent.WebSocketServer;
import com.tongdatech.winterspringboot.core.service.UserInfoService;
import com.tongdatech.winterspringboot.weixin.domain.WxCpUserPO;
import com.tongdatech.winterspringboot.weixin.service.WeixinCpService;

/**
 * 在被注解的方法后 去通知用户
 * @author xjx
 *
 */
//@Profile(Profiles.hasWebSocket)
@Aspect
@Component
public class NoticeConfig {
	
	
	final ExpressionParser parser = new SpelExpressionParser();  
	
	final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
	
	@Autowired
	private WebSocketServer webSocketServer;
	// 策略选择接口
	@Autowired
	private NoticeChooser noticeChooser;
	
	@Autowired
	private WeixinCpService weixinCpService;
	    
	@Autowired
	private UserInfoService userInfoService;
	
	@Pointcut("@annotation(com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation.Notice)" )
	public void noticeConfig() {
   	}
    
	@AfterReturning(pointcut = "noticeConfig()", returning = "returnObject")
	public void doNotice(JoinPoint joinPoint,Object returnObject) throws IOException{
		Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        Notice noticeAnnotation = method.getAnnotation(Notice.class);
        /**
         * 判断是否开启发送通知
         */
        if(!noticeAnnotation.condition().equals("")) {
        	if(!generateKeyBySpEL(noticeAnnotation.condition(),joinPoint,Boolean.class)) {
        		return;
        	}
        }
        /**
         * 简单模式发送 	
         */
        if("simple".equals(noticeAnnotation.noticeType())) {
        	String receiver = generateKeyBySpEL(noticeAnnotation.receiver(),joinPoint,String.class);
        	String sender = "";
        	if(noticeAnnotation.isShowSender()) {
        		sender = "来自:"+getUserId();
        	}
        	NoticeMsg noticeMsg = new NoticeMsg(null,null,noticeAnnotation.noticeMsg(),sender,null,null);
        	webSocketServer.sendOneMessage(receiver,JSON.toJSONString(noticeMsg));
        	return;
        }
       	/**
         * 复杂模式发送
　　　　　　　根据不同的operateType 调用不同的实现类
　　　　　　　我们给IHandleNotice接口中，传入我们在@Notice注解的方法中获取到的参数和返回值，并获取它返回的 Map<接收人，消息>
         */
        IHandleNotice iHandleNotice = noticeChooser.choose(noticeAnnotation.operateType().toString());
	   	 Map<String, NoticeMsg> receiversAndMsgs = iHandleNotice.handleNotice(getUserId(), args, returnObject);
		for (Map.Entry<String, NoticeMsg> entry : receiversAndMsgs.entrySet()) {
			String jsonMsg = JSON.toJSONString(entry.getValue());
			webSocketServer.sendOneMessage(entry.getKey(),jsonMsg);
		}
	}
	
	
	/**
	 * 获取登录用户id
	 * @return
	 */
	protected String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }
        if (auth.getPrincipal() instanceof String) {
            String user = (String) auth.getPrincipal();
            return user;
        }
        return null;
    }
//	
//	/**
//	 * 获取用户信息，如果不是企业微信登录，则获取后台管理用户id
//	 * @return
//	 */
//	protected String getUserName() {
//		String userId = getUserId();
//		WxCpUserPO wxCpUserPO = weixinCpService.getWxCpUserByUserId(userId);
//		return  wxCpUserPO == null ? userInfoService.getUserInfoByUserId(userId).getNickName()
//				:wxCpUserPO.getUserName();
//	}
	
	/**
	 * 解析表达式工具类
	 * @param spELString
	 * @param joinPoint
	 * @param clazz
	 * @return
	 */
	private <T> T generateKeyBySpEL(String spELString, JoinPoint joinPoint, Class<T> clazz) {
	        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
	        Method method = methodSignature.getMethod();
	        String[] paramNames = nameDiscoverer.getParameterNames(method);
	        Expression expression = parser.parseExpression(spELString);
	        EvaluationContext context = new StandardEvaluationContext();
	        Object[] args = joinPoint.getArgs();
	        for(int i = 0 ; i < args.length ; i++) {
	            context.setVariable(paramNames[i], args[i]);
	        }
	        return expression.getValue(context,clazz);
	    }

	
}
