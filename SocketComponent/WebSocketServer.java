package com.tongdatech.winterspring.zczx.webSocketConfig.SocketComponent;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.tongdatech.winterspring.zczx.config.Profiles;

/** 
 * 这里就相当于一个ws协议的controller
 */
@Profile(Profiles.hasWebSocket)
@Component
@ServerEndpoint("/websocket/{userName}")
public class WebSocketServer {
        
    //存放每个客户端建立的 链接
    private static ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();
    
    /**
     * 一个应用建立连接时调用的方法
     * @param session
     * @param userName
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userName")String userName) {
        sessionPool.put(userName, session);
        
    }
    
    /**
     * 关闭时调用，删掉一个session
     */
    @OnClose
    public void onClose(@PathParam(value = "userName") String userName) {
    	sessionPool.remove(userName);
    }
    
    @OnMessage
    public void onMessage(String message) {
            
    }
    
    //发送消息
    public void sendOneMessage(String userName, String message) throws IOException {
    	System.out.println(userName+":发送【websocket消息】:"+message);
        Session session = sessionPool.get(userName);
        if (session != null) {
        	synchronized(session) {
        		session.getBasicRemote().sendText(message);
        	}
        }
    }
}
    
