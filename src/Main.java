
import module1.pechincha.security.VerificationCodeFactory;
import module2.pechincha.enumeration.MsgTypes;
import module2.pechincha.manager.StorageLeilaoEnvironments;
import module2.pechincha.util.Messeger;
import module2.pechincha.util.MessegerFactory;


public class Main {

	public static void main(String[] args) {
	 String test = "{'idEmissor':'1', 'idLeilao':'1', 'TipoMSG':'HANDSHAKE', 'Msg':'Jos�' }";
	 for(int a = 0 ; a < 30 ; a++ )
	 System.out.println("Codigo de verifica��o: "+ VerificationCodeFactory.verify(VerificationCodeFactory.create("Bsinet@hotmail.com"))); 
	 System.out.println("Codigo de verifica��o: "+ VerificationCodeFactory.verify("dfe")); 
	}
	
}
