package module1.pechincha.useCases;

import java.util.ArrayList;
import java.util.Calendar;
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
		String[] actions = { "criarLeilao", "processaEmail",
				"pesquisarLeilao", "finalizarLeilao", "criarLote", "historicoLeilao"};
		return actions;
	}
	// os metodos vao abaixo
	@Override
	public String getUserCase() {
		return "gerenciarLeilao";
	}
	public ActionDone criarLeilao(DoAction action){
		LoteProduto loteProduto;
		ValidaLeilao valida = new ValidaLeilao();
		ValidaLote validaLote = new ValidaLote();
		ActionDone done = new ActionDone();
		Leilao le=new Leilao();
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
		JDBCLoteProdutoDAO loteProdutoDao = new JDBCLoteProdutoDAO();
		JDBCUsuarioDAO us = new JDBCUsuarioDAO();
		Usuario user;
		String etapa=check(action,"etapa");
		switch(etapa){
		case "criarLeilao":
				le.setEtiqueta((check(action,"etiqueta")));
				le.setDescricao(check(action,"descricao"));
				le.setIdLeiloeiro(Integer.parseInt(check(action,"idleiloeiro")));
				user=us.select(le.getIdLeiloeiro());
				le.setNickname(user.getNickname());
				done=valida.validar(le,action);
				if(done.getData("valida").equals(true)){
					done.setUseCase(action.getUseCase());
					done.setData("etiqueta",check(action,"etiqueta"));
					done.setData("descricao",check(action,"descricao"));
					done.setData("nickname",le.getNickname());
					done.setAction("leilaop1");
					done.setProcessed(true);
					done.setStatus(true);
					done.setData("idleiloeiro", check(action,"idleiloeiro"));
					return done;
				}else{
					return done;
				}
		case "leilaop0":
			done.setAction("leilaop0");
			done.setUseCase(action.getUseCase());
			done.setData("idleiloeiro",check(action,"idleiloeiro"));
			done.setProcessed(true);
			done.setStatus(true);
			return done;
		case "concluir":
			String[] quantidadeLote=(String[])action.getData("quantidadeLote_array");
			String[] precoLote=(String[])action.getData("precoLote_array");
			String[] idproduto=(String[])action.getData("idproduto_array");
			String[] quantidade=(String[])action.getData("quantidade_array");
			String[] precoProd=(String[])action.getData("precoProd_array");
			float valorPerson=Float.parseFloat((String) action.getData("valorBox"));
			float valorTrue=0;
			boolean valido=validaLote.validar(quantidadeLote, precoLote, idproduto, quantidade, precoProd,valorPerson);
			if(valido){
				le.setEtiqueta((check(action,"etiqueta")));
				le.setDescricao(check(action,"descricao"));
				le.setIdLeiloeiro(Integer.parseInt(check(action,"idleiloeiro")));
				le.setAtivo(true);
				user=us.select(le.getIdLeiloeiro());
				le.setNickname(user.getNickname());
				le.setTempoLimite(Integer.parseInt(check(action,"tempolimite")));
				if(action.getData("valorPersonalizado").equals("true")){
					le.setLanceInicial(valorPerson);
					le.setPrecolote(valorPerson);
				}
				else{
					ArrayList<Integer> adicionados=new ArrayList<Integer>();
					String[] adicionado=(String[])action.getData("adicionado_array");
					int x=0;
					for(String temp:adicionado){
						if(temp.equals("true"))adicionados.add(x);
						x++;
					}
					for(int indice:adicionados){
						valorTrue+=Integer.parseInt(quantidadeLote[indice])*Float.parseFloat(precoProd[indice]);
					}
					le.setLanceInicial(valorTrue);
					le.setPrecolote(valorTrue);
				}
				int pk=leilao.insertReturningPk(le);
				le.setIdLeilao(pk);
				ArrayList<Integer> adicionados=new ArrayList<Integer>();
				String[] adicionado=(String[])action.getData("adicionado_array");
				int x=0;
				for(String temp:adicionado){
					if(temp.equals("true"))adicionados.add(x);
					x++;
				}
				for(int indice:adicionados){
					loteProduto = new LoteProduto();
					loteProduto.setFkleilao(pk);
					loteProduto.setFkproduto(Integer.parseInt(idproduto[indice]));
					loteProduto.setUnidades(Integer.parseInt(quantidadeLote[indice]));
					loteProdutoDao.insert(loteProduto);
				}
				done.setData("idEmissor", String.valueOf(le.getIdLeiloeiro()));
				done.setData("idLeilao", String.valueOf(pk));
				done.setData("idleiloeiro",String.valueOf(le.getIdLeiloeiro()));
				done.setData("isLeiloeiro", true);
				done.setAction("ambiente");
				done.setData("userName",le.getNickname());
				done.setUseCase("ambienteLeilao");
				done.setProcessed(true);
				done.setStatus(true);
				StorageLeilaoEnvironments.iniciarAmbienteLeilao(le);
				return done;
			}else{
				done.setUseCase(action.getUseCase());
				done.setData("etiqueta",check(action,"etiqueta"));
				done.setData("descricao",check(action,"descricao"));
				done.setData("idleiloeiro", check(action,"idleiloeiro"));
				done.setData("tempolimite", check(action,"tempolimite"));
				done.setData("nickname",le.getNickname());
				done.setAction("leilaop1");
				done.setData("message", "Houve um erro no cadastro do lote!");
				done.setProcessed(true);
				done.setStatus(true);
				return done;
			}
		}
		return done;
	}
	public String check(DoAction action,String key){
		String[] filtro = (String[])action.getData(key+"_array");
		String saida=filtro[filtro.length-1];
		return saida;
	}
	
	public ActionDone historicoLeilao(DoAction action){
		ActionDone done=new ActionDone();
		List<Leilao> list;
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
			list=leilao.getHistorico(Integer.parseInt((String) action.getData("idleiloeiro")));
			done.setAction("historico");
			done.setData("termino",false);
			done.setUseCase(action.getUseCase());
			done.setData("idleiloeiro",check(action,"idleiloeiro"));
			done.setData("lista", list);
			done.setProcessed(true);
			done.setStatus(true);
			done.setData("message", " ");
			return done;
	}
	
	
	public ActionDone processaEmail(DoAction action){
		ActionDone done = new ActionDone();
		Leilao leilao = null;
		JDBCLeilaoDAO leilaoDao = new JDBCLeilaoDAO();
		leilao=leilaoDao.select(Integer.parseInt((String) action.getData("idleilao")));
		boolean statusEmail=enviarEmail(leilao);
		List<Leilao> list=leilaoDao.getHistorico(Integer.parseInt((String) action.getData("idleiloeiro")));
		done.setAction("historico");
		done.setUseCase(action.getUseCase());
		done.setData("idleiloeiro",check(action,"idleiloeiro"));
		done.setData("lista", list);
		if(statusEmail){
			done.setData("message","ok");
		}else done.setData("message","erro");
		done.setProcessed(true);
		done.setStatus(true);
		return done;
	}
	
	public boolean finalizarLeilao(Leilao leilao){
		JDBCLeilaoDAO update =new JDBCLeilaoDAO();
		JDBCLoteProdutoDAO lt =new JDBCLoteProdutoDAO();
		Calendar data = Calendar.getInstance(); 
		String tempo = String.valueOf(data.get(Calendar.DAY_OF_MONTH))+"/"+ String.valueOf(data.get(Calendar.MONTH))+"/"+String.valueOf(data.get(Calendar.YEAR));
		leilao.setTermino(tempo);
		lt.delete(leilao.getIdLeilao());
		leilao.setAtivo(false);
		update.update(leilao);
		return enviarEmail(leilao);
	}
	
	public boolean enviarEmail(Leilao leilao) {
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
		if(!mail(nome,msg,destino)){
			destino=leiloeiro.getEmailAlternativo();
			return mail(nome,msg,destino);
		}
		return mail(nome,msg,destino);
		}else{
			nome=leiloeiro.getNomeCompleto();
			msg="Informamos que o senhor(a) "+comprador.getNomeCompleto()+" efetuou uma compra no seu leilão, </ br>Voce pode entrar em contato com o mesmo com os seguinte dados:</ br><ul><li>Skype: "+comprador.getSkype()+"</li><li>E-mail: "+comprador.getEmailPrincipal()+"</li><li>Telefone fixo: "+comprador.getTelFixo()+"</li><li>Telefone celular: "+comprador.getTelCelular()+"</li></ul></ br><h1>Agradecemos aos nossos clientes pela preferência</h1>";
			destino=leiloeiro.getEmailPrincipal();
			mail(nome,msg,destino);
			nome=comprador.getNomeCompleto();
			msg="É um prazer informar que o senhor(a) efetuou uma compra de "+leiloeiro.getNomeCompleto()+" no Pechincha.com, </ br>Voce pode entrar em contato com o mesmo para concluir sua compra pelos seguintes canais de comunicação:</ br><ul><li>Skype: "+leiloeiro.getSkype()+"</li><li>E-mail: "+leiloeiro.getEmailPrincipal()+"</li><li>Telefone fixo: "+leiloeiro.getTelFixo()+"</li><li>Telefone celular: "+leiloeiro.getTelCelular()+"</li></ul></ br><h1>Agradecemos aos nossos clientes pela preferência</h1>";
			destino=comprador.getEmailPrincipal();
			if(!mail(nome,msg,destino)){
				destino=leiloeiro.getEmailAlternativo();
				return mail(nome,msg,destino);
			}
			return mail(nome,msg,destino);
		}
	}
	private synchronized boolean  mail(String nome,String msg,String destino){
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
			return true;
		} catch (EmailException e) {
			System.err.println("Houve um erro ao enviar o email!");
			e.printStackTrace();
			return false;
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
