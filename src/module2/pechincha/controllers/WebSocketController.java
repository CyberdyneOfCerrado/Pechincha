package module2.pechincha.controllers;

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

import module2.pechincha.util.UserSession;

public class WebSocketController {
	   private static Map <UserSession, Session > peers = Collections.synchronizedMap( new Hashtable<UserSession,Session>());
	   
	   //Em caso de uma mensagem do tipo HandShake, este método deve registrar a Session a variável peers. 
	   public void onMessage(Session session, String msg) {
	     
	   }; 
	   
	   //Não fazer nada por enquanto. 
	   public void open(Session session, EndpointConfig conf) {
		   
	   };
	
	   //Remover UserSession da variável peers. 
	   public void error(Session session, Throwable error) { 
		   
	   };
	   
	   //Remover UserSession da variável peers. 
	   public void close(Session session, CloseReason reason) { 
		   
	   };
}
