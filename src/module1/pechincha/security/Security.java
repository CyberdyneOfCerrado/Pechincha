package module1.pechincha.security;

import java.util.Hashtable;

public class Security {
	private Hashtable<String, String[]> acess;

	public Security() {
		acess = new Hashtable<>();
		// Adicionar ações que são permitidas sem o login;

		String[] manterUsuario = {"login", "incluirUsuario"};

		acess.put("manterUsuario", manterUsuario);
	}

	// Com base na ação e caso de uso e o tipo de usuário, esse método diz se a
	// ação e permitida
	// ou não.
	public boolean permissao(String login, String useCase, String action) {
		if (login == null || login.equals("false")) {
			// Verificando se há caso de uso ou Ação:
			if ((useCase == null || action == null))
				return false;
			String[] temp = acess.get(useCase);

			for (int a = 0; a < temp.length; a++)
				if (temp[a].equals(action))
					return true;
		} else {
			return true;
		}
		return false; // Usuário sem login não está permitido.
	};
}