package module2.pechincha.manager;

import java.util.Collection;
import java.util.Hashtable;

import javax.websocket.Session;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.Messeger;
import module2.pechincha.util.UserSession;

public class ManagerLeilao extends Thread {
	private final int TIME_DELAY = 4 * 1000;
	
	private double lanceCorrente; 
	private Hashtable<Integer,UserSession> peers; 
	private Leilao leilao; 
	private int tempoCorrente = 10000; 
	private UserSession maiorLance; 
	private boolean done; 
	
	@Override
	public void run(){
		while(tempoCorrente >= 0){
			sleep(TIME_DELAY);
			 tempoCorrente--;
			System.out.println("Manager verificando se o tempo já acabou: " + tempoCorrente);
		}
		done = true;
		//Provavelmente terei que chamar o método de finalizar aqui. 
	}; 
	
	public ManagerLeilao( Leilao leilao){
		this.leilao = leilao; 
		this.done = false;
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
			case HANDSHAKE:
				 	msgUnicast(peers.get(messeger.getIdEmissor()), messeger);
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
	
	private void msgUnicast( UserSession userSession, Messeger messeger){
		//Criar um método na MessegerFactory para converter messeger em JsonString.
		userSession.getSession().getAsyncRemote().sendText("Você tá conectado Bah. Leilão: "+ this.leilao.getId()+ "Cliente conectados: "+ this.peers.size());
	};
	
	public boolean isDone(){
		return done;
	}; 
	
	private void sleep( int time ){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	};
}
