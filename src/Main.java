
import module2.pechincha.util.Messeger;
import module2.pechincha.util.MessegerFactory;

import org.json.JSONObject;


public class Main {

	public static void main(String[] args) {
	 String test = "{ 'idEmissor':'1', 'idLeilao':'1', 'TipoMSG':'MENSAGEM', 'Msg':'Olá mundo' }"; 
	 
	 Messeger m = MessegerFactory.createMesseger(test, null); 
	}

}
