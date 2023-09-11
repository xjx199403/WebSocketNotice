package com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

import com.tongdatech.winterspring.zczx.config.Profiles;

@Profile(Profiles.hasWebSocket)
@Documented 
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface Notice {
	/**
	 * 消息类型 (必填)  
	 * 	1.simple(default):使用表达式配置 接收者和接收内容
	 * 	2.complex:使用策略进行复杂配置
	 */
	String noticeType() default "simple";
	/**
	 * 接收对象  simple模式配置 （可输入表达式）
	 */
	String receiver() default "";
	/**
	 * 消息内容  simple模式配置
	 */
	String noticeMsg() default "";
	/**
	 * 是否显示发送方  simple模式配置
	 */
	boolean isShowSender() default true;
	
	
	
	/**
	 * 通知类型，在枚举类中添加 对应complex模式的策略
	 */
	NoticeType operateType() default NoticeType.Default;
	
	
	
	/**
	 * 是否开启通知（可输入表达式） 
	 * eq.  condition = "#userInfo.userId != null"
	 */
	String condition() default "";
}
