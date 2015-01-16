package module1.pechincha.useCases;

import java.math.BigInteger;
import java.security.MessageDigest;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCUsuarioDAO;
import module1.pechincha.model.Leilao;
import module1.pechincha.model.Usuario;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;

public class ManterUsuario extends ModelController {

	@Override
	public String[] getActions() {
		String[] actions = {"incluirUsuario"};
		return actions;
	}

	@Override
	public String getUserCase() {
		return "manterUsuario";
	}
	
	public ActionDone incluirUsuario(DoAction action){
		String etapa=String.valueOf(action.getData("etapa"));
		Usuario user=new Usuario();
		ActionDone done = null;
		switch(etapa){
		case "check":
			user.setNomeCompleto(String.valueOf(action.getData("nomeCompleto")));
			user.setDataNascimento(String.valueOf(action.getData("dataDeNascimento")));
			user.setSenha(String.valueOf(action.getData("senha")));
			user.setNickname(String.valueOf(action.getData("nickname")));
			user.setEmailPrincipal(String.valueOf(action.getData("email")));
			user.setEmailAlternativo(String.valueOf(action.getData("emailAlternativo")));
			user.setSkype(String.valueOf(action.getData("skype")));
			user.setTelCelular(String.valueOf(action.getData("telefoneCelular")));
			user.setTelFixo(String.valueOf(action.getData("telefoneFixo")));
			String confsenha=String.valueOf(action.getData("confirmarSenha"));
			action.setData("user", user);
			action.setData("confsenha",confsenha);
			done= validar(action);
			boolean status=(boolean) done.getData("status");
			if(status){
				done=retorno("false",null,true);
			}else{
				done=retorno(String.valueOf(done.getData("erro")),String.valueOf(done.getData("tipo")),(boolean)done.getData("status"));
			}
			return done;
			
		case "cadastrar":
			user.setNomeCompleto(String.valueOf(action.getData("nomeCompleto")));
			user.setSenha(String.valueOf(action.getData("senha")));
			user.setNickname(String.valueOf(action.getData("nickname")));
			user.setDataNascimento(String.valueOf(action.getData("dataDeNascimento")));
			user.setEmailPrincipal(String.valueOf(action.getData("email")));
			user.setEmailAlternativo(String.valueOf(action.getData("emailAlternativo")));
			user.setSkype(String.valueOf(action.getData("skype")));
			user.setTelCelular(String.valueOf(action.getData("telefoneCelular")));
			user.setTelFixo(String.valueOf(action.getData("telefoneFixo")));
			String senha=encrypt(String.valueOf(action.getData("senha")));
			user.setSenha(senha);
			System.out.println(senha);
			JDBCUsuarioDAO userDao=new JDBCUsuarioDAO();
			int pk=userDao.insertReturningPk(user);
			System.out.println("O usuario foi cadastrado, id:"+pk);
			//provisorio ate que incorpore a tela home
			done=new ActionDone();
			done.setAction("cadastroerro");
			done.setUseCase("manterUsuario");
			done.setProcessed(true);
			done.setStatus(true);
			String temp= "O usuario foi cadastrado";
			done.setData("message",temp);
			done.setData("index","false");
			return done;
			//provisorio ate que incorpore a tela home
		case "cadastro":
			done=new ActionDone();
			done.setAction("cadastro");
			done.setUseCase(action.getUseCase());
			done.setProcessed(true);
			done.setStatus(true);
			return done;
		}
		return null;
	}
	
	public ActionDone validar(DoAction action){
		Usuario user=(Usuario) action.getData("user");
		String confsenha=(String) action.getData("confsenha");
		if(user.getNomeCompleto().length()<10 || user.getNomeCompleto().length()>250 || user.getNomeCompleto().equals("")){
			return check("Houve um erro no campo Nome! O campo deve conter entre 10 a 250 caracteres.","1",false);
		}
		if(user.getSenha().length()<6 || user.getSenha().length()>16 || user.getSenha().equals("")){
			return check("A senha informada deverá conter entre 6 a 16 dígitos!","2",false);
		}
		if(!user.getSenha().equals(confsenha) || user.getSenha().equals("")){
			return check("As senhas não conferem!","3",false);
		}
		if(user.getNickname().length()<5 || user.getNickname().length()>10 || user.getNickname().equals("")){
			return check("Erro no campo nickname! O campo deve conter entre 5 a 10 caracteres.","4",false);
		}
		String temp=user.getDataNascimento();
		int d=0;
		int m=0;
		int a=0;
		try{
		d=Integer.parseInt(temp.substring(0,2));
		m=Integer.parseInt(temp.substring(3,5));
		a=Integer.parseInt(temp.substring(6,10));
		}catch(Exception e){
			return check("Erro no campo Data de Nascimento! O campo deve conter a mascara DD-MM-AAAA","5",false);
		}
		if(d>31 || d<1 || m>12 || m<1 || a<1900 || temp==null){
			return check("Erro no campo Data de Nascimento! O campo deve conter a mascara DD-MM-AA","5",false);
		}
		if(user.getEmailPrincipal().length()<10 || user.getEmailPrincipal().length()>100 || user.getEmailPrincipal().equals("")){
			return check("Erro no campo E-mail principal! O campo deve conter entre 10 a 100 caracteres.","6",false);
		}
		if(user.getEmailAlternativo().length()>100){
			return check("Erro no campo E-mail alternativo! O campo deve conter entre 10 a 100 caracteres.","7",false);
		}
		if(user.getSkype().length()>100){
			return check("Erro no campo Skype! O campo deve conter ate 100 caracteres.","8",false);
		}
		if(user.getTelCelular().length()<11 || user.getTelCelular().length()>13 || user.getTelCelular().equals("")){
			return check("Erro no campo Telefone Celular, este campo deverá ter a seguinte máscara: '0xx55558888'","9",false);
		}
		if(user.getTelFixo().length()>13){
			return check("Erro no campo Telefone Fixo, este campo deverá ter a seguinte máscara: '0xx55558888'","10",false);
		}
		return check("false","null",true);
	}
	
public ActionDone check(String erro, String tipo,boolean valida){
	ActionDone done = new ActionDone();
	done.setData("erro", erro);
	done.setData("tipo", tipo);
	done.setData("status", valida);
	return done;
}

public static String encrypt(String source) {
String md5 = null;
try {
   MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
   mdEnc.update(source.getBytes(), 0, source.length());
   md5 = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted string
} catch (Exception ex) {
   return null;
}
return md5;
}

public ActionDone retorno(String erro, String tipo,boolean valida){
	ActionDone done = new ActionDone();
	done.setAction("cadastroerro");
	done.setUseCase("manterUsuario");
	done.setProcessed(true);
	done.setStatus(valida);
	String temp= "{ \"erro\" : \""+erro+"\", \"tipo\" : \""+tipo+"\", \"estado\" : "+valida+" }";
	done.setData("message",temp);
	done.setData("index","false");
	return done;
}
}
