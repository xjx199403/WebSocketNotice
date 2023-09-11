package com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation;

import java.io.Serializable;

public class NoticeMsg implements Serializable{
	private static final long serialVersionUID = 1L;
	private String msgNo;//消息编号 对应前台消息的位置
	private String msg;//消息
	private String noticeMsg;
	private String noticeDescription;
	private String url;
	private Object object;//预留对象
	
	


	
	public NoticeMsg(String msg, String url) {
		super();
		this.msg = msg;
		this.url = url;
	}
	public NoticeMsg(String msg, String url,Object object) {
		super();
		this.msg = msg;
		this.url = url;
		this.object = object;
	}
	public NoticeMsg(String msgNo, String msg, String noticeMsg, String noticeDescription, String url,
			Object object) {
		super();
		this.msgNo = msgNo;
		this.msg = msg;
		this.noticeMsg = noticeMsg;
		this.noticeDescription = noticeDescription;
		this.url = url;
		this.object = object;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getNoticeMsg() {
		return noticeMsg;
	}


	public void setNoticeMsg(String noticeMsg) {
		this.noticeMsg = noticeMsg;
	}


	public String getNoticeDescription() {
		return noticeDescription;
	}


	public void setNoticeDescription(String noticeDescription) {
		this.noticeDescription = noticeDescription;
	}


	public Object getObject() {
		return object;
	}


	public void setObject(Object object) {
		this.object = object;
	}


	public String getMsgNo() {
		return msgNo;
	}
	
	public void setMsgNo(String msgNo) {
		this.msgNo = msgNo;
	}

	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return "NoticeMsg [msgNo=" + msgNo + ", msg=" + msg + ", noticeMsg=" + noticeMsg + ", noticeDescription="
				+ noticeDescription + ", url=" + url + ", object=" + object + "]";
	}	
	
	
}
