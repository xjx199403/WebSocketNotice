package com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
//import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//import com.tongdatech.winterspring.zczx.config.Profiles;
import com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation.IHandleNotice;
import com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation.NoticeType;

/**
 * ApplicationContextAware：实现该接口拿到容器
 * @author xjx
 *
 */
//@Profile(Profiles.hasWebSocket)
@Component
public class NoticeChooser implements ApplicationContextAware{
	
	private Map<NoticeType, IHandleNotice> noticeMap = new ConcurrentHashMap<>();
	
	
	//成员变量来接收容器
	private ApplicationContext applicationContext;
	
	
	public IHandleNotice choose(String sqlString) {
		return noticeMap.get(NoticeType.valueOf(sqlString));
	}
	
	@PostConstruct
	public void initNoticeTypes() {
		//根据类型拿到容器
		Map<String, IHandleNotice> noticeTypes = applicationContext.getBeansOfType(IHandleNotice.class);
		noticeTypes.forEach((String t, IHandleNotice handle) -> {
			noticeMap.put(handle.noticeType(), handle);
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}

}
