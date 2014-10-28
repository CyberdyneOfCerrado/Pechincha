package module2.pechincha.manager;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.UserSession;

public class StorageLeilaoEnvironments {
	private static StorageLeilaoEnvironments sle;
	private static ManagerStorage ms; 
	
	public StorageLeilaoEnvironments(){
		ms = new ManagerStorage();
	}
	public static void initialize(){
		if( sle == null ) sle = new StorageLeilaoEnvironments();
	}; 
	
	public static void addSession( UserSession userSession){
		ms.addSession(userSession);
	}; 
	
	public static void removeSession( UserSession userSession){
		ms.removeSession(userSession);
	}; 
	
	public static void iniciarAmbienteLeilao(int idLeilao){
		//Apenas simulando..
		
		ms.createNewManagerLeilao(new Leilao(idLeilao));
	}
}
