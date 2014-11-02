package module2.pechincha.manager;

import java.util.Collection;
import java.util.Hashtable;

import javax.websocket.Session;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.Messeger;
import module2.pechincha.util.MessegerFactory;
import module2.pechincha.util.UserSession;

public class ManagerLeilao extends Thread {
	private final int TIME_DELAY = 1 * 1000;
	
	private float lanceCorrente; 
	private Hashtable<Integer,UserSession> peers; 
	private Leilao leilao; 
	private int tempoCorrente; 
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
		//Inciando alguns atributos com os valores do Leilão passado por parâmetro.
		this.tempoCorrente = leilao.getTempoLimite(); 
		this.lanceCorrente = leilao.getLanceInicial();
		
		
		this.leilao = leilao; 
		this.done = false;
		this.maiorLance = new UserSession();
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
					chat(peers.get(messeger.getIdEmissor()), messeger); 
				break;
			case HANDSHAKE:
				 	generateCallback(peers.get(messeger.getIdEmissor()), messeger);
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
		if(!peers.containsKey(userSession.getIdUser()))return; 
		System.err.println("Clientes conectados removendo "+peers.size()+" Leilao " + leilao.getId()); 
		peers.remove(userSession.getIdUser());  
		this.feedOffline(peers.size());
	}; 
	
	public synchronized void addSession( UserSession userSession ){
		if(peers.containsKey(userSession.getIdUser())) return; 
		System.err.println("Clientes conectados adicionando "+peers.size()+" Leilao " + leilao.getId());
		feedOnline( peers.size()+1);
		peers.put(userSession.getIdUser(), userSession); 
		
		
	}; 
	 
	private void msgBroadcast( Messeger messeger){//Arrumar isso.
		//Enviar para todos os cliente, exceto para o emissor. 
		Collection<UserSession> c = peers.values();
		
		for(UserSession us : c){
			if( !us.getSession().isOpen())
				continue;
			Session session = us.getSession();
			session.getAsyncRemote().sendText(MessegerFactory.MessegerToJSONString(messeger)); 
		}
	}; 
	
	private void msgUnicast( UserSession userSession, Messeger messeger){
		//Criar um método na MessegerFactory para converter messeger em JsonString.
		userSession.getSession().getAsyncRemote().sendText(MessegerFactory.MessegerToJSONString(messeger));
	};
	
	private void chat ( UserSession userSession, Messeger messeger){
		msgBroadcast(messeger); 
	};
	
	private void generateCallback(UserSession userSession, Messeger messeger){
		//Lista de coisas que eu devo mandar no callback
		//1 TempoCorrente; 
		//2 Etiqueta; 
		//3 Quantidade de pessoas online; 
		//4 LanceCorrente; 
		//5 Nome do usuário com o maior lance; 
		//6 Nome do leiloeiro; 
		
		userSession.setNickName(messeger.getUserName());
		
		String msg = tempoCorrente+";";
		msg += leilao.getEtiqueta()+";";
		msg += peers.size()+";";
		msg += lanceCorrente +";";
		msg += (maiorLance.getNickName() != null) ? maiorLance.getNickName()+";" : "Ninguém ainda" +";";
		msg += "MacGayver"+";";
		
		msgUnicast(userSession, MessegerFactory.createMessegerCallback(msg)); 
	}; 
	
	private void feedOnline( int size ){
		Messeger messeger = MessegerFactory.createMessegerOnline(size);
		msgBroadcast(messeger);
	}; 
	
	private void feedOffline( int size){
		Messeger messeger = MessegerFactory.createMessegerOffline(size);
		msgBroadcast(messeger);
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
