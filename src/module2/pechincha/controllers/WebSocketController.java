package module2.pechincha.controllers;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;


import javax.websocket.Session;

import module1.pechincha.model.Leilao;
import module2.pechincha.enumeration.MsgTypes;
import module2.pechincha.manager.StorageLeilaoEnvironments;
import module2.pechincha.util.Messeger;
import module2.pechincha.util.MessegerFactory;
import module2.pechincha.util.UserSession;

public class WebSocketController {
	   private static Map <Session,UserSession> peers = Collections.synchronizedMap( new Hashtable<Session,UserSession>());
	   
	   public WebSocketController(){
		   StorageLeilaoEnvironments.initialize();
		   //public Leilao(int id, String etiqueta, int tempoLimite, int idLeiloeiro, double valorIncial)
		   StorageLeilaoEnvironments.iniciarAmbienteLeilao( new Leilao(1,"Venda de Equipamentos Eletr�nicos" ,50000, 1 ,2000.00f) );
	   }; 
	   
	   //Em caso de uma mensagem do tipo HandShake, este m�todo deve registrar a Session a vari�vel peers. 
	   public void onMessage(Session session, String JsonObjectString) {
		 //Lista do que fazer: 
		 //1: inteceptar HANDSKAKE; 
	     Messeger m = MessegerFactory.createMesseger(JsonObjectString);
	     //� necess�rio inteceptar a mensagem de tipo HANDSHAKE, motivos: arranjo t�cnico. 
	     if(m.getTipoMsg() == MsgTypes.HANDSHAKE )
	    	 configureHandShake(m,session);
	    	 StorageLeilaoEnvironments.resolverMsg(m);
	   }; 
	   
	   //N�o fazer nada por enquanto. 
	   public void open(Session session) {
	   };
	
	   //Remover UserSession da vari�vel peers. 
	   public void error(Session session) { 
		   removeSession(session);
	   };
	   
	   //Remover UserSession da vari�vel peers. 
	   public void close(Session session) { 
		   removeSession(session);
	   };
	   
	   private synchronized void configureHandShake(Messeger m , Session session){
		   //A��es: 
		   //1 Criar UserSession; 
		   //2 Associar UserSession a reposit�rio de peers; 
		   //3 Associar UserSession ao ManagerLeilao espec�fico. 
		   
		   UserSession userSession = new UserSession(m.getMsg(),session,m.getIdEmissor(),m.getIdLeilao());
		   if(peers.containsKey(session)) return;
		   peers.put(session,userSession); 
		   StorageLeilaoEnvironments.addSession(userSession);
		   System.out.println("Quantidade de Sess�es: "+peers.size()); 
	   }; 
	   
	   private synchronized void removeSession(Session session){
		   //A��es: 
		   //1 Retirar do reposit�rio peers; 
		   //2 Retirar do ManagerLeil�o espec�fico. 
		   
		   UserSession userSession = peers.get(session); 
		   if(userSession == null) return;
		   StorageLeilaoEnvironments.removeSession(userSession);
		   peers.remove(session);
		   System.err.println("Quantidade de Sess�es: "+peers.size()); 
	   }
}
