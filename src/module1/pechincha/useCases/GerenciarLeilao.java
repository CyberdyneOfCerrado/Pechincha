package module1.pechincha.useCases;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCLeilaoDAO;
import module1.pechincha.cruds.JDBCProdutoDAO;
import module1.pechincha.cruds.JDBCUsuarioDAO;
import module1.pechincha.model.Leilao;
import module1.pechincha.model.LoteProduto;
import module1.pechincha.model.Usuario;
import module1.pechincha.model.Produto;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module2.pechincha.manager.StorageLeilaoEnvironments;

public class GerenciarLeilao extends ModelController {

	@Override
	public String[] getActions() {
		String[] actions = { "criarLeilao", "reenviarEmail", "getHistorico",
				"getTodosLeiloes", "entrarLeilao", "enviarEmail",
				"pesquisarLeilao", "finalizarLeilao" };
		return actions;
	}
	// os metodos vao abaixo
	@Override
	public String getUserCase() {
		return "gerenciarLeilao";
	}
	public ActionDone criarLeilão(DoAction action){
		ValidaLeilao valida = new ValidaLeilao((Leilao) action.getData("leilao"));
		ActionDone done = new ActionDone();
		done=valida.validar();
		if(done.isStatus()==false){
			done.setProcessed(true);
			done.setStatus(false);
			done.setUseCase(action.getUseCase());
			done.setAction(action.getAction());
			return done;
		}
		Produto produto;
		float preco=0;
		Leilao le;
		LoteProduto lote=new LoteProduto();;
		JDBCProdutoDAO crudpr = new JDBCProdutoDAO();
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
		int idleilao= leilao.insertReturningPk((Leilao) action.getData("leilao"));
		Hashtable temp=(Hashtable) action.getData("produtos");
		while(temp.keys().hasMoreElements()){
			int pk=(int) temp.keys().nextElement();
			int unidade=(int) temp.get(pk);
			produto=crudpr.select(pk);
			int tempUnidade=produto.getQuantidade();
			produto.setQuantidade(tempUnidade-unidade);
			crudpr.update(produto);
			lote.setFkleilao(idleilao);
			lote.setFkproduto(pk);
			lote.setUnidades((int) temp.get(pk));
			preco+=(unidade*produto.getPreco());
		}
		if(action.getData("val")!=null){
			preco=(float) action.getData("val");
			le=leilao.select(idleilao);
			le.setPrecolote(preco);
			leilao.update(le);
		}else{
			le=leilao.select(idleilao);
			le.setPrecolote(preco);
			leilao.update(le);
		}
		StorageLeilaoEnvironments.iniciarAmbienteLeilao(le);
		done.setProcessed(true);
		done.setStatus(true);
		done.setUseCase(action.getUseCase());
		done.setAction(action.getAction());
		return done;
	}
	
	public ActionDone getHistorico(DoAction action){
		ActionDone done=new ActionDone();
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
		List<Leilao> list = null;
		list=leilao.getHistorico((int) action.getData("idleilao"));
		if(list==null){
			done.setProcessed(true);
			done.setStatus(false);
			done.setMessage("Houve algum erro!");
			return done;
		}else{
			if(list.isEmpty()){
				done.setProcessed(true);
				done.setStatus(true);
				done.setMessage("Nada encontrado!");
				return done;
			}else{
				done.setProcessed(true);
				done.setStatus(true);
				done.setData("getHistorico",list);
				return done;
			}
		}
	}
	
	public ActionDone getTodosLeiloes(){
		ActionDone done = new ActionDone();
		ArrayList<Leilao> array = new ArrayList<Leilao>();
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
		array=(ArrayList<Leilao>) leilao.list();
		done.setData("getTodosLeiloes", array);
		done.setStatus(true);
		done.setProcessed(true);
		done.setUseCase(this.getUserCase());
		done.setAction("getTodosLeiloes");
		return done;
	}
	
	public ActionDone reenviarEmail(DoAction action){
		ActionDone done = new ActionDone();
		Leilao leilao = null;
		JDBCLeilaoDAO leilaoDao = new JDBCLeilaoDAO();
		leilao=leilaoDao.select((int) action.getData("idleilao"));
		if(leilao==null){
			done.setProcessed(true);
			done.setStatus(false);
			done.setMessage("Houve um erro interno");
			return done;
		}
		DoAction act = new DoAction(this.getUserCase(),"enviarEmail");
		act.setData("leilao", leilao);
		enviarEmail(act);
		done.setProcessed(true);
		done.setStatus(true);
		return done;
	}
	
	public ActionDone finalizarLeilao(DoAction action){
		ActionDone done = new ActionDone();
		DoAction act = new DoAction(this.getUserCase(),"enviarEmail");
		done.setProcessed(true);
		done.setStatus(true);
		done.setUseCase(action.getUseCase());
		done.setAction(action.getAction());
		JDBCLeilaoDAO update =new JDBCLeilaoDAO();
		Leilao leilao=(Leilao) action.getData("leilao");
		leilao.setAtivo(false);
		update.update(leilao);
		act.setData("leilao", leilao);
		enviarEmail(act);
		return done;
	}
	
	public ActionDone pesquisarLeilao(DoAction action){
		ActionDone done = new ActionDone();
		done.setAction(action.getAction());
		done.setUseCase(action.getUseCase());
		done.setProcessed(true);
		JDBCLeilaoDAO leilaoDao = new JDBCLeilaoDAO();
		Leilao leilao = null;
		leilao=leilaoDao.searchEtiqueta((String) action.getData("etiqueta"));
		if(leilao==null){
			done.setStatus(false);
			done.setMessage("Nada encontrado");
			return done;
		}else{
			done.setStatus(true);
			done.setData("pesquisarLeilao", leilao);
			return done;
		}
	}
	
	public ActionDone enviarEmail(DoAction action) {
		ActionDone done = new ActionDone();
		String nome="";
		String msg="";
		String destino="";
		JDBCUsuarioDAO search = new JDBCUsuarioDAO();
		Leilao leilao=(Leilao) action.getData("leilao");
		Usuario leiloeiro = search.select(leilao.getIdLeiloeiro());
		Usuario comprador = search.select(leilao.getComprador());
		if(comprador==null){
		nome=leiloeiro.getNomeCompleto();
		msg="Informamos que seu leilão não obteve vendas, que tal anunciar novamente!";
		destino=leiloeiro.getEmailPrincipal();
		mail(nome,msg,destino);
		}else{
			nome=leiloeiro.getNomeCompleto();
			msg="Informamos que o senhor(a) "+comprador.getNomeCompleto()+" efetuou uma compra no seu leilão, </ br>Voce pode entrar em contato com o mesmo com os seguinte dados:</ br><ul><li>Skype: "+comprador.getSkype()+"</li><li>E-mail: "+comprador.getEmailPrincipal()+"</li><li>Telefone fixo: "+comprador.getTelFixo()+"</li><li>Telefone celular: "+comprador.getTelCelular()+"</li></ul></ br><h1>Agradecemos aos nossos clientes pela preferência</h1>";
			destino=leiloeiro.getEmailPrincipal();
			mail(nome,msg,destino);
			nome=comprador.getNomeCompleto();
			msg="É um prazer informar que o senhor(a) efetuou uma compra de "+leiloeiro.getNomeCompleto()+" no Pechincha.com, </ br>Voce pode entrar em contato com o mesmo para concluir sua compra pelos seguintes canais de comunicação:</ br><ul><li>Skype: "+leiloeiro.getSkype()+"</li><li>E-mail: "+leiloeiro.getEmailPrincipal()+"</li><li>Telefone fixo: "+leiloeiro.getTelFixo()+"</li><li>Telefone celular: "+leiloeiro.getTelCelular()+"</li></ul></ br><h1>Agradecemos aos nossos clientes pela preferência</h1>";
			destino=comprador.getEmailPrincipal();
			mail(nome,msg,destino);
		}
		done.setProcessed(true);
		done.setStatus(true);
		done.setUseCase(this.getUserCase());
		done.setAction("enviarEmail");
		return done;
	}
	private synchronized void  mail(String nome,String msg,String destino){
		HtmlEmail email = new HtmlEmail();
		email.setHostName("smtp.gmail.com");
		email.setSslSmtpPort("465");
		email.setStartTLSRequired(true);
		email.setSSLOnConnect(true);
		email.setAuthentication("pechinchaG4@gmail.com", "pechincha123");
		try {
			email.setFrom("pechincha@g4group.me", "G4group");// remetente
			email.setDebug(true);
			email.setSubject("Informe Pechincha");// assunto
			StringBuilder builder = new StringBuilder();
			builder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><title>Untitled Page</title><meta name=\"generator\" content=\"WYSIWYG Web Builder 9 - http://www.wysiwygwebbuilder.com\"><style type=\"text/css\">body{ background-color: #FFFFFF; color: #000000; font-family: Arial; font-size: 13px; margin: 0; padding: 0;}</style><style type=\"text/css\">a{ color: #0000FF; text-decoration: underline;}a:visited{ color: #800080;}a:active{ color: #FF0000;}a:hover{ color: #0000FF; text-decoration: underline;}</style><style type=\"text/css\">#Layer1{ background-color: transparent; background-image: url(http://g4group.me/convenience-store-background1.jpg); background-repeat: repeat; background-position: left top;}#wb_texto { background-color: transparent; border: 2px #FFFF00 solid; -moz-border-radius: 3px; -webkit-border-radius: 3px; border-radius: 3px; padding: 0; text-align: left; -moz-box-shadow: 3px 3px 3px #000000; -webkit-box-shadow: 3px 3px 3px #000000; box-shadow: 3px 3px 3px #000000;}#wb_texto div{ text-align: left;}</style></head><body><div id=\"Layer1\" style=\"position:absolute;text-align:left;left:0px;top:0px;width:898px;height:698px;z-index:2;\" title=\"Pechincha\"><div id=\"wb_TextArt1\" style=\"position:absolute;left:32px;top:17px;width:381px;height:127px;z-index:0;\"><img src=\"https://g4group.me/img0001.png\" id=\"TextArt1\" alt=\"Pechincha.com\" title=\"Pechincha.com\" style=\"border-width:0;width:381px;height:127px;\"></div><div id=\"wb_texto\" style=\"position:absolute;left:29px;top:175px;width:837px;height:46px;z-index:1;text-align:left;\"><span style=\"color:#000000;font-family:Arial;font-size:20px;\">Boa tarde senhor(a) "+nome+",<br>"+msg+"</span></div></div></body></html>");
			email.setHtmlMsg(builder.toString());
			email.addTo(destino);// destinatario
			email.send();
		} catch (EmailException e) {
			System.err.println("Houve um erro ao enviar o email!");
			e.printStackTrace();
		}
	}
}
