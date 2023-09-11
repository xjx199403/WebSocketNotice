package com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation;

public enum NoticeType {
	Default("a"),
	CountyPaiDan("b"),
	ReAddr("c"),
	AddCustomer("d");
	
	private String sqlString;
	
	NoticeType(String sqlString){
		this.sqlString = sqlString;
	}
	
	private String getString() {
		return this.sqlString;
	}
}
