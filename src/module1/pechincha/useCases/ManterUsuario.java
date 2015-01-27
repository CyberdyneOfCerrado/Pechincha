package module1.pechincha.useCases;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.servlet.http.HttpSession;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCUsuarioDAO;
import module1.pechincha.model.Usuario;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;

public class ManterUsuario extends ModelController {

	@Override
	public String[] getActions() {
		String[] actions = {"incluirUsuario", "login", "meusDados", "excluirConta", "alterarDados", "sair"};
		return actions;
	}

	@Override
	public String getUserCase() {
		return "manterUsuario";
	}

	public ActionDone excluirConta(DoAction action) {
		String etapa = String.valueOf(action.getData("etapa"));
		ActionDone done = null;
		HttpSession s = null;
		int pk = 0;
		JDBCUsuarioDAO userDao = new JDBCUsuarioDAO();
		switch (etapa) {
			case "telaExcluir" :
				done = new ActionDone();
				done.setAction("excluirConta");
				s = (HttpSession) action.getData("Session");
				done.setData("id", String.valueOf(s.getAttribute("id")));
				done.setUseCase(action.getUseCase());
				done.setProcessed(true);
				done.setStatus(true);
				return done;
			case "check" :
				s = (HttpSession) action.getData("Session");
				pk = Integer.parseInt((String) s.getAttribute("id"));
				Usuario user = userDao.select(pk);
				String senha = String.valueOf(action.getData("senha"));
				if (user.getSenha().equals(senha)) {
					done = retorno("Conta excluída com sucesso!", "0", true, "manterUsuario", "excluir");
					return done;
				} else {
					done = retorno("true", "0", false, "manterUsuario", "excluir");
					return done;
				}
			case "excluir" :
				s = (HttpSession) action.getData("Session");
				pk = Integer.parseInt((String) s.getAttribute("id"));
				userDao.delete(pk);
				s = (HttpSession) action.getData("Session");
				s.setAttribute("id", null);
				s.setAttribute("login", "false");
				s.setAttribute("nickname", null);
				done = new ActionDone();
				done.setAction("login");
				done.setUseCase(action.getUseCase());
				done.setData("index", "false");
				done.setProcessed(false);
				done.setStatus(true);
				return done;
		}
		return done;
	}

	public ActionDone alterarDados(DoAction action) {
		String etapa = String.valueOf(action.getData("etapa"));
		Usuario user = null;
		ActionDone done = null;
		JDBCUsuarioDAO userDao = null;
		HttpSession s = null;
		switch (etapa) {
			case "check" :
				user = new Usuario();
				user.setNomeCompleto(String.valueOf(action.getData("nomeCompleto")));
				user.setDataNascimento(String.valueOf(action.getData("dataDeNascimento")));
				user.setNickname(String.valueOf(action.getData("nickname")));
				user.setEmailPrincipal(String.valueOf(action.getData("email")));
				user.setEmailAlternativo(String.valueOf(action.getData("emailAlternativo")));
				user.setSkype(String.valueOf(action.getData("skype")));
				user.setTelCelular(String.valueOf(action.getData("telefoneCelular")));
				user.setTelFixo(String.valueOf(action.getData("telefoneFixo")));
				String confsenha = String.valueOf(action.getData("confirmarSenha"));
				action.setData("user", user);
				action.setData("confsenha", confsenha);
				action.setData("tipo", "2");
				done = validar(action);
				boolean status = (boolean) done.getData("status");
				if (status) {
					done = retorno("O seu cadastro foi alterado com exito!", null, true, "manterUsuario", "respostaAlterar");
				} else {
					done = retorno(String.valueOf(done.getData("erro")), String.valueOf(done.getData("tipo")), (boolean) done.getData("status"),
							"manterUsuario", "respostaAlterar");
				}
				return done;
			case "alterar" :
				user = new Usuario();
				user.setNomeCompleto(String.valueOf(action.getData("nomeCompleto")));
				user.setSenha(String.valueOf(action.getData("senha")));
				user.setNickname(String.valueOf(action.getData("nickname")));
				user.setDataNascimento(String.valueOf(action.getData("dataDeNascimento")));
				user.setEmailPrincipal(String.valueOf(action.getData("email")));
				user.setEmailAlternativo(String.valueOf(action.getData("emailAlternativo")));
				user.setSkype(String.valueOf(action.getData("skype")));
				user.setTelCelular(String.valueOf(action.getData("telefoneCelular")));
				user.setTelFixo(String.valueOf(action.getData("telefoneFixo")));
				s = (HttpSession) action.getData("Session");
				user.setPk(Integer.parseInt((String) s.getAttribute("id")));
				userDao = new JDBCUsuarioDAO();
				userDao.update(user);
				done = new ActionDone();
				done = retorno("O seu cadastro foi alterado com exito!", null, true, "manterUsuario", "respostaAlterar");
				return done;
			case "telaAlterar" :
				done = new ActionDone();
				userDao = new JDBCUsuarioDAO();
				s = (HttpSession) action.getData("Session");
				user = userDao.select(Integer.parseInt((String) s.getAttribute("id")));
				done.setData("user", user);
				done.setAction("alterarDados");
				done.setUseCase(action.getUseCase());
				done.setProcessed(true);
				done.setStatus(true);
				return done;
		}
		return done;
	}

	public ActionDone incluirUsuario(DoAction action) {
		String etapa = String.valueOf(action.getData("etapa"));
		Usuario user = new Usuario();
		ActionDone done = null;
		switch (etapa) {
			case "check" :
				user.setNomeCompleto(String.valueOf(action.getData("nomeCompleto")));
				user.setDataNascimento(String.valueOf(action.getData("dataDeNascimento")));
				user.setSenha(String.valueOf(action.getData("senha")));
				user.setNickname(String.valueOf(action.getData("nickname")));
				user.setEmailPrincipal(String.valueOf(action.getData("email")));
				user.setEmailAlternativo(String.valueOf(action.getData("emailAlternativo")));
				user.setSkype(String.valueOf(action.getData("skype")));
				user.setTelCelular(String.valueOf(action.getData("telefoneCelular")));
				user.setTelFixo(String.valueOf(action.getData("telefoneFixo")));
				String confsenha = String.valueOf(action.getData("confirmarSenha"));
				action.setData("user", user);
				action.setData("confsenha", confsenha);
				done = validar(action);
				boolean status = (boolean) done.getData("status");
				if (status) {
					done = retorno("Cadastro efetuado com sucesso!", null, true, "manterUsuario", "cadastroerro");
				} else {
					done = retorno(String.valueOf(done.getData("erro")), String.valueOf(done.getData("tipo")), (boolean) done.getData("status"),
							"manterUsuario", "cadastroerro");
				}
				return done;

			case "cadastrar" :
				user.setNomeCompleto(String.valueOf(action.getData("nomeCompleto")));
				user.setSenha(String.valueOf(action.getData("senha")));
				user.setNickname(String.valueOf(action.getData("nickname")));
				user.setDataNascimento(String.valueOf(action.getData("dataDeNascimento")));
				user.setEmailPrincipal(String.valueOf(action.getData("email")));
				user.setEmailAlternativo(String.valueOf(action.getData("emailAlternativo")));
				user.setSkype(String.valueOf(action.getData("skype")));
				user.setTelCelular(String.valueOf(action.getData("telefoneCelular")));
				user.setTelFixo(String.valueOf(action.getData("telefoneFixo")));
				String senha = encrypt(String.valueOf(action.getData("senha")));
				user.setSenha(senha);
				System.out.println(senha);
				JDBCUsuarioDAO userDao = new JDBCUsuarioDAO();
				int pk = userDao.insertReturningPk(user);
				System.out.println("O usuario foi cadastrado, id:" + pk);
				HttpSession s = (HttpSession) action.getData("Session");
				s.setAttribute("id", String.valueOf(pk));
				s.setAttribute("login", "true");
				s.setAttribute("nickName", user.getNickname());
				done = new ActionDone();
				done.setData("Session", action.getData("Session"));
				done.setAction("home");
				done.setUseCase("home");
				done.setProcessed(true);
				done.setData("redirect", "true");
				done.setStatus(true);
				return done;
			case "cadastro" :
				done = new ActionDone();
				done.setAction("cadastro");
				done.setUseCase(action.getUseCase());
				done.setData("index", "false");
				done.setProcessed(true);
				done.setStatus(true);
				return done;
		}
		return null;
	}

	public ActionDone meusDados(DoAction action) {
		HttpSession s = (HttpSession) action.getData("Session");
		String id = String.valueOf(s.getAttribute("id"));
		JDBCUsuarioDAO userDao = new JDBCUsuarioDAO();
		Usuario user = userDao.select(Integer.parseInt(id));
		ActionDone done = new ActionDone();
		done.setData("usuario", user);
		done.setAction("meusDados");
		done.setUseCase("manterUsuario");
		done.setProcessed(true);
		done.setStatus(true);
		return done;
	}

	public ActionDone sair(DoAction action) {
		ActionDone done = new ActionDone();
		HttpSession s = (HttpSession) action.getData("Session");
		// Adicionar dados a Sessão:
		// id = pk;
		// login: informando se o usuário foi logado ou não (true ou false);
		// nickName: para uso interno de outros casos de uso.
		s.setAttribute("login", "false");

		done.setAction("login");
		done.setUseCase("manterUsuario");
		done.setProcessed(false);
		done.setStatus(true);
		return done;
	};

	public ActionDone validar(DoAction action) {
		Usuario user = (Usuario) action.getData("user");
		JDBCUsuarioDAO userDao = new JDBCUsuarioDAO();
		String confsenha = (String) action.getData("confsenha");
		if (user.getNomeCompleto().length() < 10 || user.getNomeCompleto().length() > 250 || user.getNomeCompleto().equals("")) {
			return check("Houve um erro no campo Nome! O campo deve conter entre 10 a 250 caracteres.", "1", false);
		}
		if (action.getData("tipo") == null) {
			if (user.getSenha().length() < 6 || user.getSenha().length() > 16 || user.getSenha().equals("")) {
				return check("A senha informada deverá conter entre 6 a 16 dígitos!", "2", false);
			}
		}
		if (action.getData("tipo") == null) {
			if (!user.getSenha().equals(confsenha) || user.getSenha().equals("")) {
				return check("As senhas não conferem!", "3", false);
			}
		}
		if (user.getNickname().length() < 5 || user.getNickname().length() > 10 || user.getNickname().equals("")) {
			return check("Erro no campo nickname! O campo deve conter entre 5 a 10 caracteres.", "4", false);
		}
		String temp = user.getDataNascimento();
		int d = 0;
		int m = 0;
		int a = 0;
		try {
			d = Integer.parseInt(temp.substring(0, 2));
			m = Integer.parseInt(temp.substring(3, 5));
			a = Integer.parseInt(temp.substring(6));
		} catch (Exception e) {
			return check("Erro no campo Data de Nascimento! O campo deve conter a mascara DD-MM-AAAA", "5", false);
		}
		if (d > 31 || d < 1 || m > 12 || m < 1 || a<=1900 || temp == null) {
			return check("Erro no campo Data de Nascimento! O campo deve conter a mascara DD-MM-AA", "5", false);
		}
		if (user.getEmailPrincipal().length() < 10 || user.getEmailPrincipal().length() > 100 || user.getEmailPrincipal().equals("")) {
			return check("Erro no campo E-mail principal! O campo deve conter entre 10 a 100 caracteres.", "6", false);
		}
		if (userDao.emailExiste(user.getEmailPrincipal()) && action.getData("tipo") != "2") {
			return check("Erro no campo E-mail principal! O e-mail principal deve ser único no sistema.", "6", false);
		}
		if (user.getEmailAlternativo().length() > 100) {
			return check("Erro no campo E-mail alternativo! O campo deve conter entre 10 a 100 caracteres.", "7", false);
		}
		if (user.getSkype().length() > 100) {
			return check("Erro no campo Skype! O campo deve conter ate 100 caracteres.", "8", false);
		}
		if (user.getTelCelular().length() < 11 || user.getTelCelular().length() > 12 || user.getTelCelular().equals("")) {
			return check("Erro no campo Telefone Celular, este campo deverá ter a seguinte máscara: '0xx55558888'", "9", false);
		}
		if (user.getTelFixo().length() > 11) {
			return check("Erro no campo Telefone Fixo, este campo deverá ter a seguinte máscara: '0xx55558888'", "10", false);
		}
		return check("false", "null", true);
	}

	public ActionDone check(String erro, String tipo, boolean valida) {
		ActionDone done = new ActionDone();
		done.setData("erro", erro);
		done.setData("tipo", tipo);
		done.setData("status", valida);
		return done;
	}

	public static String encrypt(String source) {
		String md5 = null;
		try {
			MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption
																	// algorithm
			mdEnc.update(source.getBytes(), 0, source.length());
			md5 = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted
																	// string
		} catch (Exception ex) {
			return null;
		}
		return md5;
	}

	public ActionDone retorno(String erro, String tipo, boolean valida, String useCase, String action) {
		ActionDone done = new ActionDone();
		done.setAction(action);
		done.setUseCase(useCase);
		done.setProcessed(true);
		done.setStatus(valida);
		String temp = "{ \"erro\" : \"" + erro + "\", \"tipo\" : \"" + tipo + "\", \"estado\" : " + valida + " }";
		done.setData("message", temp);
		done.setData("index", "false");
		return done;
	}

	public ActionDone login(DoAction da) {
		ActionDone ad = new ActionDone();
		String email = (String) da.getData("email");
		String senha = (String) da.getData("senha");

		JDBCUsuarioDAO daoUsuario = new JDBCUsuarioDAO();
		Usuario us = new Usuario();
		us.setSenha(senha);
		us.setEmailPrincipal(email);
		int result = daoUsuario.verifyLogin(us);

		if (result != -1) { // -1 significa que não há um registro no banco de
							// dados;
			HttpSession s = (HttpSession) da.getData("Session");
			// Adicionar dados a Sessão:
			// id = pk;
			// login: informando se o usuário foi logado ou não (true ou false);
			// nickName: para uso interno de outros casos de uso.

			s.setAttribute("id", String.valueOf(result));
			s.setAttribute("login", "true");

			us = daoUsuario.select(result);
			s.setAttribute("nickName", us.getNickname());

			ad.setData("loginStatus", "true");
		} else {
			ad.setData("loginStatus", "false");
		}

		ad.setData("index", "false");
		// Por enquanto eu não vou fazer nada aqui.
		// Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		return ad;
	}
}
