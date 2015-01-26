package module1.pechincha.security;

import java.util.Hashtable;

public class Security {
	private Hashtable<String, String[]> acess;

	public Security() {
		acess = new Hashtable<>();
		// Adicionar a��es que s�o permitidas sem o login;

		String[] manterUsuario = {"login", "incluirUsuario"};

		acess.put("manterUsuario", manterUsuario);
	}

	// Com base na a��o e caso de uso e o tipo de usu�rio, esse m�todo diz se a
	// a��o e permitida
	// ou n�o.
	public boolean permissao(String login, String useCase, String action) {
		if (login == null || login.equals("false")) {
			// Verificando se h� caso de uso ou A��o:
			if ((useCase == null || action == null))
				return false;
			String[] temp = acess.get(useCase);

			for (int a = 0; a < temp.length; a++)
				if (temp[a].equals(action))
					return true;
		} else {
			return true;
		}
		return false; // Usu�rio sem login n�o est� permitido.
	};
}