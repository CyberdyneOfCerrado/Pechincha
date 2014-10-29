package module2.pechincha.manager;

import java.util.Collection;
import java.util.Hashtable;

import javax.websocket.Session;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.Messeger;
import module2.pechincha.util.UserSession;

public class ManagerLeilao extends Thread {
	private double lanceCorrente; 
	private Hashtable<Integer,UserSession> peers; 
	private Leilao leilao; 
	private int tempoCorrente; 
	private UserSession maiorLance; 
	private boolean isDone; 
	
	@Override
	public void run(){
		while(true){
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}; 
	
	public ManagerLeilao( Leilao leilao){
		this.leilao = leilao; 
		this.isDone = false;
		peers = new Hashtable<>(); 
		
		
	};
	
	public void startManager(){
		this.start();
	}; 
	
	public void resolverMsg(Messeger messeger){
		//Ações: 
		//1: Resolver a mensagem conforme a ação do TipoMsg; 
		
		switch( messeger.getTipoMsg()){
			case MENSAGEM: 
					msgBroadcast(peers.get(messeger.getIdEmissor()), messeger); 
				break;
			case LANCE: 
				break;
			case FINALIZAR: 
				break;
			default:
				break;
		}
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
	 
	private void msgBroadcast( UserSession userSession, Messeger messeger){//Arrumar isso.
		//Enviar para todos os cliente, exceto para o emissor. 
		Collection<UserSession> c = peers.values();
		
		for(UserSession us : c){
			Session session = us.getSession(); 
			session.getAsyncRemote().sendText(messeger.getMsg()); 
		}
	}; 
}
