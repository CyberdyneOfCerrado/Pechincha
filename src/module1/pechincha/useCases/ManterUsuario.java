package module1.pechincha.useCases;

import module1.pechincha.controllers.ModelController;
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
		ActionDone done;
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
			user.setEmailPrincipal(String.valueOf(action.getData("email")));
			user.setEmailAlternativo(String.valueOf(action.getData("emailAlternativo")));
			user.setSkype(String.valueOf(action.getData("skype")));
			user.setTelCelular(String.valueOf(action.getData("telefoneCelular")));
			user.setTelFixo(String.valueOf(action.getData("telefoneFixo")));
			break;
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
			return check("Houve um erro no campo Nome!","1",false);
		}
		if(user.getSenha().length()<6 || user.getSenha().length()>16 || user.getSenha().equals("")){
			return check("A senha informada deverá conter entre 6 a 16 dígitos!","2",false);
		}
		if(!user.getSenha().equals(confsenha) || user.getSenha().equals("")){
			return check("As senhas não conferem!","3",false);
		}
		if(user.getNickname().length()<5 || user.getNickname().length()>10 || user.getNickname().equals("")){
			return check("Erro no campo nickname!","4",false);
		}
		String temp=user.getDataNascimento();
		int d=Integer.parseInt(temp.substring(0,2));
		int m=Integer.parseInt(temp.substring(3,5));
		int a=Integer.parseInt(temp.substring(6,10));
		if(d>31 || d<1 || m>12 || m<1 || a<1900 || temp==null){
			return check("Erro no campo Data de Nascimento!","5",false);
		}
		if(user.getEmailPrincipal().length()<10 || user.getEmailPrincipal().length()>100 || user.getEmailPrincipal().equals("")){
			return check("Erro no campo E-mail principal!","6",false);
		}
		if(user.getEmailAlternativo().length()>100 || user.getEmailAlternativo().equals("")){
			return check("Erro no campo E-mail alternativo!","7",false);
		}
		if(user.getSkype().length()>100 || user.getSkype().equals("")){
			return check("Erro no campo Skype!","8",false);
		}
		if(user.getTelCelular().length()<11 || user.getTelCelular().length()>13 || user.getTelCelular().equals("")){
			return check("Erro no campo Telefone Celular, este campo deverá ter a seguinte máscara: '0xx55558888'","9",false);
		}
		if(user.getTelFixo().length()>13 || user.getTelFixo().equals("")){
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
