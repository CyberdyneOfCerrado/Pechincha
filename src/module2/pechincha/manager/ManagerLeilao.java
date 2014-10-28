package module2.pechincha.manager;

import java.util.Collection;
import java.util.Hashtable;

import javax.websocket.Session;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.UserSession;

public class ManagerLeilao extends Thread {
	private double lanceCorrente; 
	private Hashtable<Integer,UserSession> peers; 
	private Leilao leilao; 
	private int tempoCorrente; 
	private UserSession maiorLance; 
	
	@Override
	public void run(){
		while(true){
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			MsgBroadcast();
		}
	}; 
	
	public ManagerLeilao( Leilao leilao){
		this.leilao = leilao; 
		peers = new Hashtable<>(); 
	};
	
	public void startManager(){
		this.start();
	}; 
	
	public synchronized void removeSession( UserSession userSession ){
		if(!peers.containsKey(userSession.getIdUser())) return; 
		System.err.println("Clientes conectados removendo "+peers.size()+" Leilao " + leilao.getId()); 
		peers.remove(userSession.getIdUser());  
		
	}; 
	
	public synchronized void addSession( UserSession userSession ){
		if(peers.containsKey(userSession.getIdUser())) return; 
		System.err.println("Clientes conectados adicionando "+peers.size()+" Leilao " + leilao.getId()); 
		peers.put(userSession.getIdUser(), userSession); 
		
	}; 
	 
	public void MsgBroadcast(){
		Collection<UserSession> c = peers.values();
		
		for(UserSession us : c){
			Session session = us.getSession(); 
			session.getAsyncRemote().sendText("Seus putos"); 
		}
		
	}
}
