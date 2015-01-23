package module1.pechincha.viewUseCases;


import java.util.List;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.cruds.JDBCImagemDAO;
import module1.pechincha.cruds.JDBCProdutoDAO;
import module1.pechincha.model.Imagem;
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
		case "leilaop1erro":
			retorno = leilaop1erro(ad); 
		break;
		case "historico":
			retorno = getHistorico(ad); 
		break;
		}
		return retorno;
	}
	
	public String leilaop0(ActionDone ad){
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));
		return temp.generateOutput();
	}
	
	public String leilaop0erro(ActionDone ad){
		return (String) ad.getData("message");
	}
	public String leilaop1erro(ActionDone ad){
		return (String) ad.getData("message");
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
		for (Produto produto:list){
			List<Imagem> imgs = new JDBCImagemDAO().list(produto.getPk());
			if ( imgs.size() > 0){
				Imagem img = imgs.get(0);
				index.setVariable("img", img.getPk() + "." + img.getFormato());
			}
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
		MiniTemplator index = super.startMiniTemplator(pathi);
		@SuppressWarnings("unchecked")
		List<Leilao> list=(List<Leilao>) ad.getData("lista");
		if(ad.getData("message").equals("ok")){
			return (String) ad.getData("message");
		}
		if(ad.getData("message").equals("erro")){
			return (String) ad.getData("message");
		}
		for (Leilao le:list){
			index.setVariable("etiqueta", le.getEtiqueta());
			index.setVariable("termino", le.getTermino());
			index.setVariable("valor", String.valueOf(le.getPrecolote()));
			index.setVariable("idleilao",String.valueOf(le.getIdLeilao()));
			index.addBlock("historico");
		}
		return index.generateOutput();
	}
}
