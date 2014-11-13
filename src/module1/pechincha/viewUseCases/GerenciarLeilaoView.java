package module1.pechincha.viewUseCases;

import java.util.ArrayList;
import java.util.List;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.cruds.JDBCProdutoDAO;
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
//		case "getHistorico":
//			retorno = getHistorico(ad); 
//		break;
//		case "getTodosLeiloes":
//			
//			retorno = getTodosLeiloes(ad); 
//		break;
//		case "entrarLeilao":
//			retorno = entrarLeilao(ad); 
//		break;
//		case "enviarEmail":
//			retorno = enviarEmail(ad); 
//		break;
//		case "pesquisarLeilao":
//			retorno = pesquisarLeilao(ad); 
//		break;
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
		temp.setVariable("etiqueta",(String)ad.getData("etiqueta"));
		temp.setVariable("descricao",(String)ad.getData("descricao"));
		temp.setVariable("tempolimite",(String)ad.getData("tempolimite"));
		temp.setVariable("idleiloeiro",(String)ad.getData("idleiloeiro"));
		return temp.generateOutput();
	}
	public String leilaop1(ActionDone ad){
		String pathi = getSevletContext()+getUseCase()+"leilaop1"+".html";
		String pathp = getSevletContext()+getUseCase()+"produto"+".html";
		MiniTemplator temp = super.startMiniTemplator(pathp);
		MiniTemplator index = super.startMiniTemplator(pathi);
		JDBCProdutoDAO pr = new JDBCProdutoDAO();
		List<Produto> list= pr.list(Integer.parseInt((String) ad.getData("idleiloeiro")));
		index.setVariable("idleiloeiro", (String) ad.getData("idleiloeiro"));
		index.setVariable("idleilao",(String) ad.getData("idleilao"));
		for (Produto produto:list){
			temp.setVariable("produto", produto.getTitulo());
			temp.setVariable("quantidade", produto.getQuantidade());
			temp.setVariable("preco",String.valueOf(produto.getPreco()));
			temp.setVariable("idproduto",String.valueOf(produto.getPk()));
			temp.setVariable("idleilao", String.valueOf(ad.getData("idleiloeiro")));
			index.setVariable("produto",temp.generateOutput());
			index.addBlock("produto");
		}
		return index.generateOutput();
		}
}
