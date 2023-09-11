package com.tongdatech.winterspring.zczx.webSocketConfig.NoticeTypeImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongdatech.winterspring.zczx.mineuserinfo.MineUserInfoVO;
import com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation.IHandleNotice;
import com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation.NoticeMsg;
import com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation.NoticeType;
import com.tongdatech.winterspring.zczx.webSocketConfig.mapper.NoticeMapper;
import com.tongdatech.winterspringboot.core.service.UserInfoService;
import com.tongdatech.winterspringboot.weixin.domain.WxCpUserPO;
import com.tongdatech.winterspringboot.weixin.service.WeixinCpService;


/**
 * ===发送通知的业务实现类===
 * 县派单到网点 通知各个网点用户
 * @author xjx
 *
 */
@Service
public class CountyPaiDanNoticeImpl implements IHandleNotice{

	@Autowired
	private NoticeMapper noticeMapper;
	
    @Autowired
    private WeixinCpService weixinCpService;
    
    @Autowired
    private UserInfoService userInfoService;

	@Override
	public NoticeType noticeType() {
		// TODO Auto-generated method stub
		return NoticeType.CountyPaiDan;
	}

	@Override
	public Map<String, NoticeMsg> handleNotice(String userId, Object[] paramObject,
			Object returnObject) {
		HashMap<String, NoticeMsg> map = new HashMap<>();
		MineUserInfoVO mineUserInfoVO = (MineUserInfoVO) paramObject[0];
		int num = noticeMapper.getNoticePaiDanNumWithWD(mineUserInfoVO.getWd(),null);
		NoticeMsg noticeMsg = new NoticeMsg("1","您有"+num+"条待反馈用户信息需处理。","来自:"+getUserName(userId),"您有一条新的客户信息待处理。",null,null);					
		List<String> userIds = noticeMapper.getWdUserIds(mineUserInfoVO.getWd());
		for(String user_id:userIds) {
			map.put(user_id, noticeMsg);
		}
		return map;
	}
	
	/**
	 * 获取用户信息，如果不是企业微信登录，则获取后台管理用户id
	 * @return
	 */
	protected String getUserName(String userId) {
		WxCpUserPO wxCpUserPO = weixinCpService.getWxCpUserByUserId(userId);
		return  wxCpUserPO == null ? userInfoService.getUserInfoByUserId(userId).getNickName()
				:wxCpUserPO.getUserName();
	}

}
