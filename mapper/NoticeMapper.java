package com.tongdatech.winterspring.zczx.webSocketConfig.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import me.chanjar.weixin.cp.bean.WxCpDepart;

@Mapper
public interface NoticeMapper {
	int getNoticeImgNumWithCounty(@Param("org_id")String org_id,@Param("warnHours")Integer warnHours);
	
	int getNoticeValidNumWithCounty(@Param("org_id")String org_id,@Param("warnHours")Integer warnHours);
	
	List<String> getCountyUserIds(String county);
	
	List<String> getWdUserIds(String orgId);
	
	int getNoticePaiDanNumWithWD(@Param("org_id")String org_id,@Param("warnHours")Integer warnHours);
	
	int getNoticePaiDanNumWithYXY(@Param("user_id")String user_id,@Param("warnHours")Integer warnHours);
	
	String getRoleById(@Param("userId")String userId);
	
	int insertOrg(@Param("WxCpDepart")WxCpDepart wxCpDepart);

}
