package module1.pechincha.security;

import java.util.Hashtable;

public class Security {
	private Hashtable<String, String[]> acess;

	public Security() {
		acess = new Hashtable<>();
		// Adicionar ações que são permitidas sem o login; 

		String[] manterUsuario = {"login", "incluir"};

		acess.put("manterUsuario", manterUsuario);
	}

	// Com base na ação e caso de uso e o tipo de usuário, esse método diz se a
	// ação e permitida
	// ou não.
	public boolean permissao(String login, String useCase, String action) {
		if(login == null) return false;//não permitido, não está logado; 
		
		if(login.equals("false")  ){
			boolean isInList = false;
			if(useCase != null && action != null){
				
			}
			
		}else{
			return true; 
		}
		
		return false;//não permitida. 
	};
}