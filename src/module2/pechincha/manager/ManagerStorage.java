package module2.pechincha.manager;

import java.util.Hashtable;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.UserSession;

public class ManagerStorage extends Thread{
	private Hashtable<Integer, ManagerLeilao > managers; 
	
	public void run(){
		
	}

	public ManagerStorage(){
		managers = new Hashtable<>();
	}; 
	
	public void addSession( UserSession userSession ){
		ManagerLeilao ml =  findLeilao( userSession.getIdLeilao()); 
		if(ml != null)
			ml.addSession(userSession);
	}; 
	
	public void removeSession( UserSession userSession ){
		ManagerLeilao ml =  findLeilao( userSession.getIdLeilao()); 
		if(ml != null)
			ml.removeSession(userSession);
	}; 
	
	public void createNewManagerLeilao( Leilao leilao){
		ManagerLeilao ml = new ManagerLeilao(leilao); 
		managers.put(leilao.getId(),ml); 
		ml.startManager();
	}; 
	
	private ManagerLeilao findLeilao ( int id ){
		return managers.get(id);
	}; 
}

