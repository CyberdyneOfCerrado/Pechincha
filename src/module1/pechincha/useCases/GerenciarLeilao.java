package module1.pechincha.useCases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.TemplateSyntaxException;
import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCImagemDAO;
import module1.pechincha.cruds.JDBCLeilaoDAO;
import module1.pechincha.cruds.JDBCLoteProdutoDAO;
import module1.pechincha.cruds.JDBCProdutoDAO;
import module1.pechincha.cruds.JDBCUsuarioDAO;
import module1.pechincha.model.Imagem;
import module1.pechincha.model.Leilao;
import module1.pechincha.model.LoteProduto;
import module1.pechincha.model.Usuario;
import module1.pechincha.model.Produto;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module1.pechincha.view.ServletController;
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
		String[] quantidadeLote;
		String[] precoLote;
		String[] idproduto;
		String[] quantidade;
		String[] precoProd;
		String[] adicionado;
		float valorPerson;
		float valorTrue;
		LoteProduto loteProduto;
		ValidaLeilao valida = new ValidaLeilao();
		ValidaLote validaLote = new ValidaLote();
		ActionDone done = new ActionDone();
		Leilao le=new Leilao();
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
		JDBCLoteProdutoDAO loteProdutoDao = new JDBCLoteProdutoDAO();
		JDBCUsuarioDAO us = new JDBCUsuarioDAO();
		HttpSession s = (HttpSession) action.getData("Session");
		Usuario user;
		String etapa=String.valueOf(action.getData("etapa"));
		switch(etapa){
		case "criarLeilao":
				le.setEtiqueta(String.valueOf(action.getData("etiqueta")));
				le.setDescricao(String.valueOf(action.getData("descricao")));
				le.setIdLeiloeiro(Integer.parseInt((String)s.getAttribute("id")));
				user=us.select(le.getIdLeiloeiro());
				le.setNickname(user.getNickname());
				done=valida.validar(le,action);
				if(done.getData("valida").equals(true)){
					done.setUseCase(action.getUseCase());
					done.setData("etiqueta",String.valueOf(action.getData("etiqueta")));
					done.setData("descricao",String.valueOf(action.getData("descricao")));
					done.setData("nickname",le.getNickname());
					done.setAction("leilaop1");
					done.setProcessed(true);
					done.setStatus(true);
					done.setData("idleiloeiro", String.valueOf(s.getAttribute("id")));
					return done;
				}else{
					done.setProcessed(true);
					done.setStatus(true);
					String temp= "{ \"erro\":\""+(String)done.getData("erro")+"\", \"tipo\" : \""+done.getData("tipo")+"\"}";
					done.setData("message",temp);
					done.setData("index","false");
					return done;
				}
		case "leilaop0":
			done.setAction("leilaop0");
			done.setUseCase(action.getUseCase());
			done.setData("idleiloeiro",String.valueOf(s.getAttribute("id")));
			done.setProcessed(true);
			done.setStatus(true);
			return done;
		case "check":
			quantidadeLote=(String[])action.getData("quantidadeLote_array");
			precoLote=(String[])action.getData("precoLote_array");
			idproduto=(String[])action.getData("idproduto_array");
			quantidade=(String[])action.getData("quantidade_array");
			precoProd=(String[])action.getData("precoProd_array");
			adicionado=(String[])action.getData("adicionado_array");
			valorPerson=Float.parseFloat((String) action.getData("valorBox"));
			valorTrue=0;
			boolean valido=validaLote.validar(quantidadeLote, precoLote, idproduto, quantidade, precoProd,valorPerson,adicionado);
			if(valido){
				done.setUseCase(action.getUseCase());
				done.setAction("leilaop1erro");
				done.setProcessed(true);
				done.setStatus(true);
				String temp= "{ \"erro\" : false }";
				done.setData("message",temp);
				done.setData("index","false");
				return done;
			}
			else{
				done.setUseCase(action.getUseCase());
				done.setAction("leilaop1erro");
				done.setProcessed(true);
				done.setStatus(true);
				String temp= "{ \"erro\" : \"Cheque novamente o cadastro do lote de produto, deve existir pelomenos um produto adicionado!\"}";
				done.setData("message",temp);
				done.setData("index","false");
				return done;
			}
		case "concluir":
			quantidadeLote=(String[])action.getData("quantidadeLote_array");
			precoLote=(String[])action.getData("precoLote_array");
			idproduto=(String[])action.getData("idproduto_array");
			quantidade=(String[])action.getData("quantidade_array");
			precoProd=(String[])action.getData("precoProd_array");
			adicionado=(String[])action.getData("adicionado_array");
			valorPerson=Float.parseFloat((String) action.getData("valorBox"));
			valorTrue=0;
				le.setEtiqueta(String.valueOf(action.getData("etiqueta")));
				le.setDescricao(String.valueOf(action.getData("descricao")));
				le.setIdLeiloeiro(Integer.parseInt((String)s.getAttribute("id")));
				le.setAtivo(true);
				user=us.select(le.getIdLeiloeiro());
				le.setNickname(user.getNickname());
				le.setTempoLimite(Integer.parseInt((String) action.getData("tempolimite")));
				if(action.getData("valorPersonalizado").equals("true")){
					le.setLanceInicial(valorPerson);
					le.setPrecolote(valorPerson);
				}
				else{
					ArrayList<Integer> adicionados=new ArrayList<Integer>();
					adicionado=(String[])action.getData("adicionado_array");
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
				adicionado=(String[])action.getData("adicionado_array");
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
				JDBCProdutoDAO pr = new JDBCProdutoDAO();
				List<Produto> list= pr.list(Integer.parseInt((String)s.getAttribute("id")));
				ArrayList <String> imgAdd = new ArrayList<>();
				for (Produto produto:list){
					List<Imagem> imgs = new JDBCImagemDAO().list(produto.getPk());
					if ( imgs.size() > 0){
						int temp=0;
						while(temp<imgs.size()){
							Imagem img = imgs.get(temp);
							imgAdd.add(img.getPk() + "." + img.getFormato());
							temp++;
						}
					}
				}
				done.setData("arrayImg",imgAdd);
				for (String image:imgAdd){
					System.out.println(image);
				}
				StorageLeilaoEnvironments.iniciarAmbienteLeilao(le);
				return done;
			}
		return done;
	}
	
	public ActionDone historicoLeilao(DoAction action){
		ActionDone done=new ActionDone();
		HttpSession s = (HttpSession) action.getData("Session");
		List<Leilao> list;
		JDBCLeilaoDAO leilao = new JDBCLeilaoDAO();
			list=leilao.getHistorico(Integer.parseInt((String) s.getAttribute("id")));
			done.setAction("historico");
			done.setData("termino",false);
			done.setUseCase(action.getUseCase());
			done.setData("idleiloeiro",String.valueOf((String) s.getAttribute("id")));
			done.setData("lista", list);
			done.setProcessed(true);
			done.setStatus(true);
			done.setData("message", " ");
			return done;
	}
	
	
	public ActionDone processaEmail(DoAction action) throws IOException{
		ActionDone done = new ActionDone();
		HttpSession s = (HttpSession) action.getData("Session");
		Leilao leilao = null;
		JDBCLeilaoDAO leilaoDao = new JDBCLeilaoDAO();
		leilao=leilaoDao.select(Integer.parseInt((String) action.getData("idleilao")));
		boolean statusEmail=enviarEmail(leilao);
		List<Leilao> list=leilaoDao.getHistorico(Integer.parseInt((String) s.getAttribute("id")));
		done.setAction("historico");
		done.setUseCase(action.getUseCase());
		done.setData("idleiloeiro",String.valueOf(s.getAttribute("id")));
		done.setData("lista", list);
		if(statusEmail){
			done.setData("message","ok");
		}else done.setData("message","erro");
		done.setProcessed(true);
		done.setStatus(true);
		done.setData("index","false");
		return done;
	}
	
	public void finalizarLeilao(Leilao leilao){
		JDBCLeilaoDAO update =new JDBCLeilaoDAO();
		JDBCLoteProdutoDAO lt =new JDBCLoteProdutoDAO();
		JDBCProdutoDAO daoprod = new JDBCProdutoDAO();
		Date dataCal = new Date();  
		GregorianCalendar data = new GregorianCalendar();  
		data.setTime(dataCal);  
		String tempo = String.valueOf(data.get(Calendar.DAY_OF_MONTH))+"/"+ String.valueOf(data.get(Calendar.MONTH)+1)+"/"+String.valueOf(data.get(Calendar.YEAR));
		leilao.setTermino(tempo);
		JDBCUsuarioDAO search = new JDBCUsuarioDAO();
		Usuario comprador = search.select(leilao.getComprador());
		if(comprador!=null){
		List<Produto> temp=lt.produtosVendidos(leilao.getIdLeilao());
			for(Produto prodTemp:temp){
				Produto prod =  daoprod.select(prodTemp.getPk());
				prod.setQuantidade(prod.getQuantidade()-prodTemp.getQuantidade());
				daoprod.update(prod);
			}
		}
		lt.delete(leilao.getIdLeilao());
		leilao.setAtivo(false);
		update.update(leilao);
		Mail mail=new Mail();
		mail.setLeilao(leilao);
		Thread vai=new Thread(mail);
		vai.start();
	}
	
	public boolean enviarEmail(Leilao leilao){
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
			if(!mail(nome,msg,destino)){
				return false;
			}
		}
		return true;
		}else{
			nome=leiloeiro.getNomeCompleto();
			msg="<span>Informamos que o senhor(a) "+comprador.getNomeCompleto()+" efetuou uma compra no seu leilão. <br />Voce pode entrar em contato com o mesmo com os seguinte dados:<br /><ul><li>Skype: "+comprador.getSkype()+"</li><li>E-mail: "+comprador.getEmailPrincipal()+"</li><li>Telefone fixo: "+comprador.getTelFixo()+"</li><li>Telefone celular: "+comprador.getTelCelular()+"</li></ul><br /><h1>Agradecemos aos nossos clientes pela preferência</h1></span>";
			destino=leiloeiro.getEmailPrincipal();
			if(!mail(nome,msg,destino)){
				destino=leiloeiro.getEmailAlternativo();
				if(!mail(nome,msg,destino)){
					return false;
				}
			}
			nome=comprador.getNomeCompleto();
			msg="<span>É um prazer informar que o senhor(a) efetuou uma compra de "+leiloeiro.getNomeCompleto()+" no Pechincha.com. <br />Voce pode entrar em contato com o mesmo para concluir sua compra pelos seguintes canais de comunicação:<br /><ul><li>Skype: "+leiloeiro.getSkype()+"</li><li>E-mail: "+leiloeiro.getEmailPrincipal()+"</li><li>Telefone fixo: "+leiloeiro.getTelFixo()+"</li><li>Telefone celular: "+leiloeiro.getTelCelular()+"</li></ul><br /><h1>Agradecemos aos nossos clientes pela preferência</h1></span>";
			destino=comprador.getEmailPrincipal();
			if(!mail(nome,msg,destino)){
				destino=comprador.getEmailAlternativo();
				if(!mail(nome,msg,destino)){
					return false;
				}
			}
			return true;
		}
	}
	private synchronized boolean  mail(String nome,String msg,String destino){
		final String username = "pechincha@g4group.me";
	    final String password = "pechincha123";
	    String separador=System.getProperty("file.separator");
	    MiniTemplator mail=null;
		try {
			mail = new MiniTemplator(ServletController.getServletContext()+separador+"gerenciarLeilao"+separador+"mail.html");
		} catch (TemplateSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    mail.setVariable("mensagem", msg);
	    String html = mail.generateOutput();
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.host", "g4group.me");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.starttls.enable","true"); 
	    props.put("mail.smtp.ssl.trust", "g4group.me");
	    props.put("mail.transport.protocol","smtp"); 
	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(username, password);
	                }
	            });

	    try {

	    	MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("pechincha@g4group.me"));
	        message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(destino));
	        message.setSubject("Informe Pechincha");
	        String builder =html;
	        message.setText(builder, "utf-8", "html");
	        Transport.send(message);
	        System.out.println("Done");

	    } catch (MessagingException e) {
			System.err.println("Houve um erro ao enviar o email!");
			e.printStackTrace();
			return false;
	    }
	    return true;
	}
	
	public ActionDone criarLote(DoAction action){
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
