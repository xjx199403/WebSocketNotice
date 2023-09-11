package com.tongdatech.winterspring.zczx.webSocketConfig.SocketComponent;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tongdatech.winterspring.zczx.config.Profiles;
import com.tongdatech.winterspring.zczx.webSocketConfig.mapper.NoticeMapper;
import com.tongdatech.winterspringboot.core.web.AbstractApiController;




@CrossOrigin
@RestController
@RequestMapping("/api/socket")
public class SocketApiController extends AbstractApiController{
	
	
	@Value("${websockt.localaddress:}")
	private String localAddress;
	
	@Autowired
	private NoticeMapper noticeMapper;
	
	@GetMapping("/getWsAddress")
	public String getWsAddress() {
		System.out.println("localAddress"+localAddress);
		if("".equals(localAddress)) return "";
		String user_id = getUserId();
		String role = noticeMapper.getRoleById(user_id);
		//只有营销员，网点 和 县开启websocket通知
		if("ROLE_YXY".equals(role) || "ROLE_WD".equals(role) || "ROLE_COUNTY".equals(role)) {
		    String wsUrl = "ws://" + localAddress + "/websocket/" +user_id ;
			return wsUrl;
		}else {
			return "";
		}
        //String hostAddress;
		//hostAddress = InetAddress.getLocalHost().getHostAddress();
	}
}

