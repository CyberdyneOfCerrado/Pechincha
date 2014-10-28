package module2.pechincha.websocket;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import module2.pechincha.util.UserSession;


@ServerEndpoint("/server")
public class WebSocket {
	
   @OnMessage
   public void onMessage(Session session, String msg) {
     
   }; 
   
   @OnOpen
   public void open(Session session, EndpointConfig conf) {
	   
   };
   
   @OnError
   public void error(Session session, Throwable error) { 
	   
   };
   
   @OnClose
   public void close(Session session, CloseReason reason) { 
	   
   };
   
}