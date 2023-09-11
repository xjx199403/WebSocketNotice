package com.tongdatech.winterspring.zczx.webSocketConfig;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongdatech.winterspring.sys.config.Roles;
import com.tongdatech.winterspring.sys.param.ParamService;
import com.tongdatech.winterspring.sys.userinfo.UserInfoController;
import com.tongdatech.winterspring.sys.userinfo.WsUserInfoBO;
import com.tongdatech.winterspring.zczx.config.Profiles;
import com.tongdatech.winterspring.zczx.mineuserinfo.ZczxOrgVO;
import com.tongdatech.winterspring.zczx.mineuserinfo.mapper.MineUserInfoMapper;
import com.tongdatech.winterspring.zczx.webSocketConfig.NoticeAnnotation.NoticeMsg;
import com.tongdatech.winterspring.zczx.webSocketConfig.mapper.NoticeMapper;
import com.tongdatech.winterspringboot.pojo.RetMsg;

import io.swagger.annotations.ApiOperation;

/**
 * ===众创众享首页的一些初始化的提示信息===
 * @author xjx
 *
 */

@CrossOrigin
@RestController
@RequestMapping("/api/notice")
public class NoticeApiController extends UserInfoController{
	
	
	@Autowired
	private NoticeMapper noticeMapper;
	
	@Autowired
	private MineUserInfoMapper mineUserInfoMapper;
	
	@Autowired
	private ParamService paramService;
	
	
	
	
	@ApiOperation("首页超时警告")
	@GetMapping("/getHomePageWarning")
	public RetMsg<ArrayList<HashMap<String, String>>> getHomePageWarning() {
		WsUserInfoBO userinfo = getWsUserInfo();
		List<String> roles = getRoles();
		userinfo.setRoles(roles);
		ZczxOrgVO org = new ZczxOrgVO();
		String hours = paramService.getValueByText("warn_hours", "警告间隔");
		int countAll = 0;
		HashMap<String, String> map1 = new HashMap<>();
		HashMap<String, String> map2 = new HashMap<>();
		HashMap<String, String> map3 = new HashMap<>();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		// 县级
		if (userinfo.getRoles().get(0).equals(Roles.ROLE_COUNTY.getValue() ) ) {		
			org = mineUserInfoMapper.queryOrgByOrgId(userinfo.getOrgId());
			int i = noticeMapper.getNoticeImgNumWithCounty(org.getCoRegion(),Integer.valueOf(hours));
			int j = noticeMapper.getNoticeValidNumWithCounty(org.getCoRegion(),Integer.valueOf(hours));
			countAll = i+j;
			if(i == 0) {
				map1.put("show", "false");
			}else {
				map1.put("show", "true");
			}
			map1.put("msg", "您有" + i + "条待补全图片信息，"+"已超过"+hours+"小时未处理。");
			map1.put("title", "图片信息处理");
			map1.put("short", "IMG");
			map1.put("gotoDetail", "/mine/imageInfo");
			
			if(j == 0) {
				map2.put("show", "false");
			}else {
				map2.put("show", "true");
			}
			map2.put("msg", "您有" + j + "条待验证信息，"+"已超过"+hours+"小时未处理。");
			map2.put("title", "文字信息验证");
			map2.put("short", "WORD");
			map2.put("gotoDetail", "/mine/validate");
		}
		// 网点
		if (userinfo.getRoles().get(0).equals(Roles.ROLE_WD.getValue() ) ) {	
			int i = noticeMapper.getNoticePaiDanNumWithWD(String.valueOf(userinfo.getOrgId() ),Integer.valueOf(hours));
			if(i == 0) {
				map1.put("show", "false");
			}else {
				map1.put("show", "true");
			}
			countAll = i;
			map1.put("msg", "您有"+i+"条待派单用户信息需处理，"+"已超过"+hours+"小时未处理。");
			map1.put("title", "用户信息派单");
			map1.put("short", "PD");
			map1.put("gotoDetail", "/mine/userinfo-list2");
		}
		//营销员
		if (userinfo.getRoles().get(0).equals(Roles.ROLE_YXY.getValue() ) ) {	
			int i = noticeMapper.getNoticePaiDanNumWithYXY(userinfo.getUserId(),Integer.valueOf(hours));
			if(i == 0) {
				map1.put("show", "false");
			}else {
				map1.put("show", "true");
			}
			countAll = i;
			map1.put("msg", "您有"+i+"条待派单用户信息需处理，"+"已超过"+hours+"小时未处理。");
			map1.put("title", "用户信息开发");
			map1.put("short", "KF");
			map1.put("gotoDetail", "/mine/developResult");
		}
		RetMsg<ArrayList<HashMap<String, String>>> result = new RetMsg<>();
		result.setMsg(String.valueOf(countAll));
		list.add(map1);
		list.add(map2);
		list.add(map3);
		result.setResult(list);
		return result;		
	}
	
	@Profile(Profiles.hasWebSocket)
	@ApiOperation("notice初始化提示信息")
	@GetMapping("/getNotice")
	public RetMsg<ArrayList<NoticeMsg>> getNotice() {
		WsUserInfoBO userinfo = getWsUserInfo();
		List<String> roles = getRoles();
		userinfo.setRoles(roles);
		ZczxOrgVO org = new ZczxOrgVO();
		ArrayList<NoticeMsg> result = new ArrayList<>();
		// 县级
		if (userinfo.getRoles().get(0).equals(Roles.ROLE_COUNTY.getValue() ) ) {		
			org = mineUserInfoMapper.queryOrgByOrgId(userinfo.getOrgId());
			int i = noticeMapper.getNoticeImgNumWithCounty(org.getCoRegion(),null);
			int j = noticeMapper.getNoticeValidNumWithCounty(org.getCoRegion(),null);
			NoticeMsg noticeMsg1 = new NoticeMsg("您有" + i + "条待补全图片信息。", "/mine/imageInfo",i);
			NoticeMsg noticeMsg2 = new NoticeMsg("您有" + j + "条待验证信息。", "/mine/validate",j);
			result.add(noticeMsg1);
			result.add(noticeMsg2);
		}
		// 网点
		if (userinfo.getRoles().get(0).equals(Roles.ROLE_WD.getValue() ) ) {	
			int i = noticeMapper.getNoticePaiDanNumWithWD(String.valueOf(userinfo.getOrgId() ),null);
			NoticeMsg noticeMsg = new NoticeMsg("您有"+i+"条待派单用户信息需处理。", "/mine/userinfo-list2",i);
			result.add(noticeMsg);
		}
		//营销员
		if (userinfo.getRoles().get(0).equals(Roles.ROLE_YXY.getValue() ) ) {	
			int i = noticeMapper.getNoticePaiDanNumWithYXY(userinfo.getUserId(),null);
			NoticeMsg noticeMsg = new NoticeMsg("您有"+i+"条待派单用户信息需处理。", "/mine/developResult",i);
			result.add(noticeMsg);
		}
		RetMsg<ArrayList<NoticeMsg>> retMsg = new RetMsg<>();
		retMsg.setResult(result);
		return retMsg;		
	}
	
}

