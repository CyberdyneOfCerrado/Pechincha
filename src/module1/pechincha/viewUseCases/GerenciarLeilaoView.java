package module1.pechincha.viewUseCases;

import java.util.ArrayList;
import java.util.List;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.cruds.JDBCProdutoDAO;
import module1.pechincha.model.Leilao;
import module1.pechincha.model.Produto;
import module1.pechincha.util.ActionDone;
import module1.pechincha.view.ViewController;

public class GerenciarLeilaoView extends ViewController{

	public GerenciarLeilaoView(String sevletContext, String useCase) {
		super(sevletContext, useCase);
	}

	@Override
	public String choose(ActionDone ad) {
		String action = ad.getAction();
		String retorno = ""; 
		switch(action){
		case "leilaop0":
				retorno = leilaop0(ad); 
			break;
		case "leilaop1":
			retorno = leilaop1(ad); 
		break;
		case "leilaop0erro":
			retorno = leilaop0erro(ad); 
		break;
		case "historico":
			retorno = getHistorico(ad); 
		break;
		}
		return retorno;
	}
	
	public String leilaop0(ActionDone ad){
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));
		temp.setVariable("idleiloeiro",(String)ad.getData("idleiloeiro"));
		return temp.generateOutput();
	}
	
	public String leilaop0erro(ActionDone ad){
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));
		temp.setVariable("erro",(String)ad.getData("erro"));
		temp.setVariable("idleiloeiro",String.valueOf(ad.getData("idleiloeiro")));
		temp.setVariable("descricao",String.valueOf(ad.getData("descricao")));
		temp.setVariable("etiqueta",String.valueOf(ad.getData("etiqueta")));
		temp.setVariable("tempolimite",String.valueOf(ad.getData("tempolimite")));
		return temp.generateOutput();
	}
	public String leilaop1(ActionDone ad){
		String pathi = getSevletContext()+getUseCase()+"leilaop1"+".html";
		MiniTemplator index = super.startMiniTemplator(pathi);
		JDBCProdutoDAO pr = new JDBCProdutoDAO();
		List<Produto> list= pr.list(Integer.parseInt((String) ad.getData("idleiloeiro")));
		index.setVariable("etiqueta", String.valueOf(ad.getData("etiqueta")));
		index.setVariable("descricao", String.valueOf(ad.getData("descricao")));
		index.setVariable("tempolimite", String.valueOf(ad.getData("tempo")));
		index.setVariable("nickname", String.valueOf(ad.getData("nickname")));
		index.setVariable("idleiloeiro", String.valueOf(ad.getData("idleiloeiro")));
		if(String.valueOf(ad.getData("message")).equals(" ")){
			index.setVariable("message"," ");
		}else index.setVariable("message", "<div id=\"wb_Text5\" style=\"background:red;position:absolute;left:55px;top:440px;width:292px;height:19px;z-index:9;text-align:left;\"><span style=\"color:#FFFFFF;font-family:Arial;font-size:17px;\"><strong>"+String.valueOf(ad.getData("message"))+"</strong></span></div>");
		for (Produto produto:list){
			index.setVariable("produto", produto.getTitulo());
			index.setVariable("quantidade", produto.getQuantidade());
			index.setVariable("preco",String.valueOf(produto.getPreco()));
			index.setVariable("idproduto",String.valueOf(produto.getPk()));
			index.addBlock("produto");
		}
		return index.generateOutput();
		}

	public String getHistorico(ActionDone ad){
		String pathi = getSevletContext()+getUseCase()+"historicoLeilao"+".html";
		String pathok = getSevletContext()+getUseCase()+"okemail"+".html";
		String patherro = getSevletContext()+getUseCase()+"erroemail"+".html";
		MiniTemplator ok = super.startMiniTemplator(pathok);
		MiniTemplator erro = super.startMiniTemplator(patherro);
		MiniTemplator index = super.startMiniTemplator(pathi);
		JDBCProdutoDAO pr = new JDBCProdutoDAO();
		List<Leilao> list=(List<Leilao>) ad.getData("lista");
		if(ad.getData("message").equals("ok")){
			index.setVariable("message",ok.generateOutput());
		}
		if(ad.getData("message").equals("erro")){
			index.setVariable("message",erro.generateOutput());
		}
		if(ad.getData("message").equals(" ")){
			index.setVariable("message", String.valueOf(ad.getData("message")));
		}
		index.setVariable("idleiloeiro", String.valueOf(ad.getData("idleiloeiro")));
		for (Leilao le:list){
			index.setVariable("etiqueta", le.getEtiqueta());
			index.setVariable("termino", le.getTermino());
			index.setVariable("valor", String.valueOf(le.getPrecolote()));
			index.setVariable("idleilao",String.valueOf(le.getIdLeilao()));
			index.setVariable("idleiloeiro", String.valueOf(le.getIdLeiloeiro()));
			index.addBlock("historico");
		}
		return index.generateOutput();
	}
}
