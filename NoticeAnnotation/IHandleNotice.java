package com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation;

import java.util.Map;

public interface IHandleNotice {
	//发送通知
	Map<String, NoticeMsg> handleNotice(String userId, Object[] paramObject, Object returnObject);
	
	public NoticeType noticeType();
}
