package module2.pechincha.manager;

import java.util.Collection;
import java.util.Hashtable;

import javax.websocket.Session;

import module1.pechincha.model.Leilao;
import module1.pechincha.useCases.GerenciarLeilao;
import module2.pechincha.useCases.Chat;
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
	private Chat chat;
	private GerenciarLeilao gl;
	@Override
	public void run(){
		while(tempoCorrente >= 0){
			sleep(TIME_DELAY);
			 tempoCorrente--;
			System.out.println("Manager verificando se o tempo já acabou: " + tempoCorrente);
		}
		done = true;
		//Provavelmente terei que chamar o método de finalizar aqui. 
		finalizar();
	}; 
	
	public ManagerLeilao( Leilao leilao){
		//Inciando alguns atributos com os valores do Leilão passado por parâmetro.
		this.tempoCorrente = leilao.getTempoLimite(); 
		this.lanceCorrente = leilao.getLanceInicial();
		
		
		this.leilao = leilao; 
		this.done = false;
		this.maiorLance = new UserSession();
		peers = new Hashtable<>(); 
		chat = new Chat();
		gl   = new GerenciarLeilao(); 
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
					lance(peers.get(messeger.getIdEmissor()), messeger);
				break;
			case FINALIZAR: 
					finalizar(peers.get(messeger.getIdEmissor()));
				break;
			default:
				break;
		}
	}; 
	
	public synchronized void removeSession( UserSession userSession ){
		if(!peers.containsKey(userSession.getIdUser()))return; 
		System.err.println("Clientes conectados removendo "+peers.size()+" Leilao " + leilao.getIdLeilao()); 
		peers.remove(userSession.getIdUser());  
		this.feedOffline(peers.size());
	}; 
	
	public synchronized void addSession( UserSession userSession ){
		if(peers.containsKey(userSession.getIdUser())) return; 
		System.err.println("Clientes conectados adicionando "+peers.size()+" Leilao " + leilao.getIdLeilao());
		feedOnline( peers.size()+1);
		peers.put(userSession.getIdUser(), userSession); 
	}; 
	 
	private void msgBroadcast( Messeger messeger){
		Collection<UserSession> c = peers.values();
		
		for(UserSession us : c){
			if( !us.getSession().isOpen())
				continue;
			Session session = us.getSession();
			session.getAsyncRemote().sendText(MessegerFactory.MessegerToJSONString(messeger)); 
		}
	}; 
	
	private void msgUnicast( UserSession userSession, Messeger messeger){
		userSession.getSession().getAsyncRemote().sendText(MessegerFactory.MessegerToJSONString(messeger));
	};
	
	private void chat ( UserSession userSession, Messeger messeger){
		boolean valida = chat.validarMensagem(messeger); 
		if(valida){
			messeger = chat.diferenciarUsuario(messeger, userSession, maiorLance, leilao);
			msgBroadcast(messeger);
		}
	};

	private void lance( UserSession userSession, Messeger messeger){
		float novoLance = Float.parseFloat(messeger.getMsg());
		if( novoLance > this.lanceCorrente){
			lanceCorrente = novoLance;
			this.maiorLance = userSession;
			//Essa parte é vital para o caso o método finalizar do Erick funcionar 
			leilao.setComprador(maiorLance.getIdUser());
			
			msgBroadcast( MessegerFactory.createMessegerLance(String.valueOf(lanceCorrente),userSession.getNickname()));
		}else{
			msgUnicast(userSession,MessegerFactory.createMessegerLanceInvalido());
		}	
	};
	
	private void finalizar(UserSession userSession){
		boolean result = gl.finalizarLeilao(this.leilao);
		
		if( result ){
			//Pegar a sessão do maior lance e do leiloeiro; 
			//Preparar mensagem para ambos. 
			//Enviar mensagem de acordo de compra unicast. 
			//Replicar anúncio de termino de leilão para os participantes conectados.
			
			String msg     ="Os dados de acordo de comprar foram eviados para o seu email.";
			String msgDone = "O leilão foi encerrado."; 
			
			if(maiorLance.getSession().isOpen()){
				msgUnicast(maiorLance, MessegerFactory.createMessegerFinalizar(msg));
				peers.remove(maiorLance.getIdUser());
			}
			
			if(userSession.getSession().isOpen()){
				msgUnicast(userSession, MessegerFactory.createMessegerFinalizar(msg)); 
				peers.remove(userSession.getIdUser()); 
			}
			
			msgBroadcast(MessegerFactory.createMessegerFinalizar(msgDone)); 
			this.done = true;
			
		}else{
			msgUnicast(userSession, MessegerFactory.createMessegerFinalizar("Você não pode finalizar o leilão neste momento.")); 
		}
	};
	
	private void finalizar(){
			gl.finalizarLeilao(this.leilao);
		
			UserSession leiloeiro = peers.get( this.leilao.getIdLeilao()); 
			
			String msg     ="Os dados de acordo de comprar foram eviados para o seu email.";
			String msgDone = "O leilão foi encerrado."; 
			
			if(maiorLance.getSession().isOpen()){
				msgUnicast(maiorLance, MessegerFactory.createMessegerFinalizar(msg));
				peers.remove(maiorLance.getIdUser());
			}
			
			if(leiloeiro != null && leiloeiro.getSession().isOpen()){
				msgUnicast(leiloeiro, MessegerFactory.createMessegerFinalizar(msg)); 
				peers.remove(leiloeiro.getIdUser()); 
			}
			
			msgBroadcast(MessegerFactory.createMessegerFinalizar(msgDone)); 
			this.done = true;
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
		msg += (maiorLance.getNickname() != null) ? maiorLance.getNickname()+";" : "Ninguém ainda" +";";
		msg += leilao.getNickname()+";";
		
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
