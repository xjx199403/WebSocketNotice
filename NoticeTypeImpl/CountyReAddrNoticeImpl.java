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
 * 改址 或 退单通知用户
 * @author xjx
 *
 */
@Service
public class CountyReAddrNoticeImpl implements IHandleNotice{

	@Autowired
	private NoticeMapper noticeMapper;
	
    @Autowired
    private WeixinCpService weixinCpService;
    
    @Autowired
    private UserInfoService userInfoService;
    
	//实现类的 一个标识
	@Override
	public NoticeType noticeType() {
		//return your type
		return NoticeType.ReAddr;
	}

	@Override
	public Map<String, NoticeMsg> handleNotice(String userId, Object[] paramObject,
			Object returnObject) {
		// TODO Auto-generated method stub
		HashMap<String, NoticeMsg> map = new HashMap<>();
		MineUserInfoVO mineUserInfoVO = (MineUserInfoVO) paramObject[0];
		System.out.println("获得的参数"+mineUserInfoVO.toString());
		//取得所有需要发送的对象
		List<String> userIds = noticeMapper.getCountyUserIds(mineUserInfoVO.getCounty());
		System.out.println("所有要发送的对象"+userIds.toString());
		int imgCount = noticeMapper.getNoticeImgNumWithCounty(mineUserInfoVO.getCounty(),null);
		int validCount = noticeMapper.getNoticeValidNumWithCounty(mineUserInfoVO.getCounty(),null);
		NoticeMsg noticeMsgImg = new NoticeMsg("1","您有"+imgCount+"条待补全图片信息。","来自:"+getUserName(userId),"您有一条退回或来自其他区县的图片信息待处理。",null,null);
		NoticeMsg noticeMsgValid = new NoticeMsg("2", "您有"+validCount+"条待验证信息。","来自:"+getUserName(userId),"您有一条退回或来自其他区县的文本信息待处理。",null,null);
		NoticeMsg msg = "1".equals(mineUserInfoVO.getIsImageUpload())? noticeMsgImg:noticeMsgValid;
		for(String user_id:userIds) {
			map.put(user_id, msg);
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
