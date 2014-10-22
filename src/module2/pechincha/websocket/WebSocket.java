package module2.pechincha.websocket;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

import org.apache.catalina.Session;


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