package module2.pechincha.manager;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.Messeger;
import module2.pechincha.util.UserSession;

public class ManagerStorage extends Thread{
	private Hashtable<Integer, ManagerLeilao> managers; 
	private final int TIME_DELAY = 5 * 1000;
	
	//Retirará ManagerLeilao que já foram concluídos da memória da máquina. 
	public void run(){
		while(true){
			sleep(TIME_DELAY); 
			Enumeration<Integer> keys = managers.keys(); 
			while(keys.hasMoreElements()){
				int key = keys.nextElement(); 
				if(managers.get(key).isDone()){
					managers.remove(key);
					System.out.println("Limpando leilão "+key+" da memória. "); 
				}
			}
//			System.out.println("Storege fazendo a limpeza."); 
		}
	}; 

	public ManagerStorage(){
		managers = new Hashtable<>();
		this.start(); 
	}; 
	
	public void addSession( UserSession userSession ){
		ManagerLeilao ml =  findLeilao( userSession.getIdLeilao()); 
		if(ml != null)
			ml.addSession(userSession);
	}; 
	
	public void resolverMsg( Messeger messeger ){
		ManagerLeilao ml = findLeilao( messeger.getIdLeilao()); 
		ml.resolverMsg(messeger);//Encaminhando para o Manager específico.
	}; 
	
	public void removeSession( UserSession userSession ){
		ManagerLeilao ml =  findLeilao( userSession.getIdLeilao()); 
		if(ml != null)
			ml.removeSession(userSession);
	}; 
	
	public void createNewManagerLeilao( Leilao leilao){
		ManagerLeilao ml = new ManagerLeilao(leilao); 
		managers.put(leilao.getIdLeilao(),ml); 
		ml.startManager();
	}; 
	
	private ManagerLeilao findLeilao ( int id ){
		return managers.get(id);
	}; 
	
	public Collection<ManagerLeilao> getMetadata(){
		return managers.values(); 
	}; 
	
	private void sleep( int time ){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	};
}

