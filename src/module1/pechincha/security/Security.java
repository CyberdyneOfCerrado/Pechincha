package module1.pechincha.security;

import java.util.Hashtable;

public class Security {
	private Hashtable<String, String[]> acess;

	public Security() {
		acess = new Hashtable<>();
		// Adicionar a��es que s�o permitidas sem o login; 

		String[] manterUsuario = {"login", "incluir"};

		acess.put("manterUsuario", manterUsuario);
	}

	// Com base na a��o e caso de uso e o tipo de usu�rio, esse m�todo diz se a
	// a��o e permitida
	// ou n�o.
	public boolean permissao(String login, String useCase, String action) {
		if(login == null) return false;//n�o permitido, n�o est� logado; 
		
		if(login.equals("false")  ){
			boolean isInList = false;
			if(useCase != null && action != null){
				
			}
			
		}else{
			return true; 
		}
		
		return false;//n�o permitida. 
	};
}