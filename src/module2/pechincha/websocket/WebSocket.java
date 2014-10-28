package module2.pechincha.websocket;



import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import module2.pechincha.controllers.WebSocketController;



@ServerEndpoint("/server")
public class WebSocket {
   private static WebSocketController wsc; 
   public WebSocket(){
	   if( wsc == null){
		   wsc = new WebSocketController();
		   System.out.println("Toasty, Tô tendo da webSocket"); 
	   }
   }; 
   
   @OnMessage
   public void onMessage(Session session, String JsonObjectString) {
	   System.out.println(JsonObjectString);
	   wsc.onMessage(session, JsonObjectString);
   }; 
   
   @OnOpen
   public void open(Session session, EndpointConfig conf) {
	   wsc.open(session);
   };
   
   @OnError
   public void error(Session session, Throwable error) { 
	   wsc.error(session);
   };
   
   @OnClose
   public void close(Session session, CloseReason reason) { 
	   wsc.close(session);
   };
   
}