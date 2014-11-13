package module1.pechincha.viewUseCases;

import java.util.ArrayList;
import java.util.List;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.model.Categoria;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module1.pechincha.view.ViewController;

public class ManterProdutosView extends ViewController {

	public ManterProdutosView(String sevletContext, String useCase) {
		super(sevletContext, useCase);
	}

	@Override
	public String choose(ActionDone ad) {
		String action = ad.getAction();
		String retorno = ""; 
		
		switch(action){
			case "novo":
					retorno = novo(ad); 
				break;
			case "visualizar":
				retorno = visualizar(ad);
				break;
	
			case "listar":
				retorno = listar(ad);
				break;
			
			case "remover":
				retorno = remover(ad);
				break;
		}
		return retorno;
	}

	private String novo(ActionDone ad) {
		
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad)); 
		temp.setVariable("resultado",ad.getMessage());
		
		@SuppressWarnings("unchecked")
		List<Categoria> listcats = (List<Categoria>)ad.getData("categorias");
		for (Categoria cat : listcats){
			temp.setVariable("pk", cat.getPk());
			temp.setVariable("descricao", cat.getDescricao());
			temp.addBlock("Categoria");
		}
		return temp.generateOutput();
	}
	private String visualizar(ActionDone ad){
		
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad)); 
		temp.setVariable("titulo",(String)ad.getData("titulo"));
		for (int i = 1; i <= 5; i++){
			String img = (String)ad.getData("img" + i);
			if ( img == null){
				break;
			}
			temp.setVariable("img",img);
			temp.addBlock("Imagem");
		}
		temp.setVariable("idusuario",Integer.toString((int)ad.getData("idusuario")));
		temp.setVariable("descricao",(String)ad.getData("descricao"));
		temp.setVariable("preco",Float.toString((float)ad.getData("preco")));
		temp.setVariable("quantidade",Integer.toString((int)ad.getData("quantidade")));
		temp.setVariable("categorias",(String)ad.getData("categorias"));
		return temp.generateOutput();
	}
	private String listar(ActionDone ad){
		
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));
		
		@SuppressWarnings("unchecked")
		List<Categoria> listcats = (List<Categoria>)ad.getData("categorias");
		for (Categoria cat : listcats){
			temp.setVariable("pk", cat.getPk());
			temp.setVariable("descricao", cat.getDescricao());
			temp.addBlock("Categoria");
			System.out.println("Entrou");
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<DoAction> listprods = (ArrayList<DoAction>)ad.getData("produtos");
		for (DoAction p : listprods){
			temp.setVariable("titulo",(String)p.getData("titulo"));
			temp.setVariable("img",(String)p.getData("img"));
			temp.setVariable("preco",Float.toString((float)p.getData("preco")));
			temp.setVariable("idusuario",Integer.toString((int)p.getData("idusuario")));
			temp.setVariable("idproduto",Integer.toString((int)p.getData("idproduto")));
			temp.addBlock("Produto");
		}
		return temp.generateOutput();
	}
	private String remover(ActionDone ad){
		
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));
		temp.setVariable("idproduto",(String)ad.getData("idproduto"));
		temp.setVariable("idusuario",(String)ad.getData("idusuario"));
		temp.setVariable("titulo",(String)ad.getData("titulo"));
		for (int i = 1; i <= 5; i++){
			String img = (String)ad.getData("img" + i);
			if ( img == null){
				break;
			}
			temp.setVariable("img",img);
			temp.addBlock("Imagem");
		}
		temp.setVariable("descricao",(String)ad.getData("descricao"));
		temp.setVariable("preco",Float.toString((float)ad.getData("preco")));
		temp.setVariable("quantidade",Integer.toString((int)ad.getData("quantidade")));
		return temp.generateOutput();
	}
}
