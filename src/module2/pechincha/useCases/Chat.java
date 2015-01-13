package module2.pechincha.useCases;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.Messeger;
import module2.pechincha.util.MessegerFactory;
import module2.pechincha.util.UserSession;

public class Chat {
	private final int MAX_LENGTH = 250; 
	private final int MIN_LENGTH =   0; 
	
	public Chat(){
		
	}; 
	
	public boolean validarMensagem( Messeger messeger ){
		if (messeger.getMsg().length()          > MAX_LENGTH )
			return false;
		if( messeger.getMsg().trim().length()  <= MIN_LENGTH )
			return false;
		
		return true;
	};
	
	public Messeger diferenciarUsuario( Messeger messager, UserSession userSession, UserSession maiorLance, Leilao leilao){
		//Difirenciar:
		//1 Usu�rio normal; 
		//2 Usu�rio com o maior lance; 
		//3 Usu�rio que � o Dono do leil�o. 	
		//4 Retirar qualquer tipo de html que possa ser indevidamente incorporado � p�gina. 
		
		messager.setMsg(messager.getMsg().replaceAll("<[^>]*>", ""));
	
		if( userSession.getIdUser() == maiorLance.getIdUser())
			return MessegerFactory.createMessegerMaiorLance(messager.getUserName(),messager.getMsg());
		if( userSession.getIdUser() == leilao.getIdLeiloeiro())
			return MessegerFactory.createMessegerLeiloeiro(messager.getUserName(),messager.getMsg());		
		return messager;
	}
	
}
