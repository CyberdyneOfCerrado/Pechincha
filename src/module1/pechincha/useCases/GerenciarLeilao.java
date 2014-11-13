package module1.pechincha.useCases;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCLeilaoDAO;
import module1.pechincha.cruds.JDBCLoteProdutoDAO;
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
		String[] actions = { "criarLeilao", "reenviarEmail",
				"pesquisarLeilao", "finalizarLeilao", "criarLote" };
		return actions;
	}
	// os metodos vao abaixo
	@Override
	public String getUserCase() {
		return "gerenciarLeilao";
	}
	public ActionDone criarLeilao(DoAction action){
		ValidaLeilao valida = new ValidaLeilao();
		ActionDone done = new ActionDone();
		Leilao le=new Leilao();
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
		JDBCUsuarioDAO us = new JDBCUsuarioDAO();
		Usuario user;
		String etapa=desbuga(action,"etapa");
		switch(etapa){
		case "criarLeilao":
				le.setEtiqueta((desbuga(action,"etiqueta")));
				le.setDescricao(desbuga(action,"descricao"));
				String temp=desbuga(action,"tempolimite");
				int segundos=0;
				if(temp.length()==5){
					try{
					String s=temp.substring(0,2);
					int tempo=Integer.parseInt(s);
					segundos=tempo*60*60;
					s=temp.substring(2,5);
					segundos+=tempo*60;
					}catch(NumberFormatException e){
						done.setUseCase(action.getUseCase());
						done.setAction("leilaop0erro");
						done.setProcessed(true);
						done.setStatus(false);
						done.setData("idleiloeiro", desbuga(action,"idleiloeiro"));
						return done;
					}
				}
				le.setTempoLimite(segundos);
				le.setAtivo(false);
				le.setIdLeiloeiro(Integer.parseInt(desbuga(action,"idleiloeiro")));
				user=us.select(le.getIdLeiloeiro());
				le.setNickname(user.getNickname());
				if(valida.validar(le)){
					String pk=String.valueOf(leilao.insertReturningPk(le));
					done.setUseCase(action.getUseCase());
					done.setAction("leilaop1");
					done.setProcessed(true);
					done.setStatus(true);
					done.setData("idleiloeiro", desbuga(action,"idleiloeiro"));
					done.setData("idleilao", pk);
					return done;
				}else{
					done.setUseCase(action.getUseCase());
					done.setAction("leilaop0erro");
					done.setProcessed(true);
					done.setStatus(false);
					done.setData("idleiloeiro", Integer.parseInt(desbuga(action,"idleiloeiro")));
					return done;
				}
		case "leilaop0":
			done.setAction("leilaop0");
			done.setUseCase(action.getUseCase());
			done.setData("idleiloeiro",desbuga(action,"idleiloeiro"));
			done.setProcessed(true);
			done.setStatus(true);
			return done;
		}
		return done;
	}
	public String desbuga(DoAction action,String key){
		String[] filtro = (String[])action.getData(key+"_array");
		String saida=filtro[filtro.length-1];
		return saida;
	}
	
	public List<Leilao> getHistorico(DoAction action){
		ActionDone done=new ActionDone();
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
		List<Leilao> list = null;
		list=leilao.getHistorico((int) action.getData("idleilao"));
		return list;
	}
	
	public ActionDone reenviarEmail(DoAction action){
		ActionDone done = new ActionDone();
		Leilao leilao = null;
		JDBCLeilaoDAO leilaoDao = new JDBCLeilaoDAO();
		leilao=leilaoDao.select((int) action.getData("idleilao"));
		enviarEmail(leilao);
		done.setProcessed(true);
		done.setStatus(true);
		return done;
	}
	
	public boolean finalizarLeilao(Leilao leilao){
		JDBCLeilaoDAO update =new JDBCLeilaoDAO();
		leilao.setAtivo(false);
		update.update(leilao);
		enviarEmail(leilao);
		return true;
	}
	
	public void enviarEmail(Leilao leilao) {
		String nome="";
		String msg="";
		String destino="";
		JDBCUsuarioDAO search = new JDBCUsuarioDAO();
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
	
	public ActionDone criarLote(DoAction action){
		ValidaLeilao valida = new ValidaLeilao();
		ActionDone done = new ActionDone();
		JDBCProdutoDAO produto = new JDBCProdutoDAO();
		JDBCLoteProdutoDAO lt = new JDBCLoteProdutoDAO();
		Produto pr = new Produto();
		LoteProduto lote = new LoteProduto();
		lote.setFkleilao((int) action.getData("idleilao"));
		lote.setFkproduto((int) action.getData("idproduto"));
		pr=produto.select((int) action.getData("idproduto"));
		if(pr.getQuantidade()>(int) action.getData("quantidadelote")){
			lote.setUnidades((int) action.getData("quantidadelote"));
			lt.insert(lote);
			done.setAction(action.getAction());
			done.setUseCase(action.getUseCase());
			done.setProcessed(true);
			done.setStatus(true);
			done.setData("estado", "ok");
			return done;
		}else{
			done.setAction("criarLoteerro");
			done.setUseCase(action.getUseCase());
			done.setProcessed(true);
			done.setStatus(false);
			done.setData("estado", "Erro");
			return done;
		}
	}
}
