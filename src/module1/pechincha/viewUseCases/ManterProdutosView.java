package module1.pechincha.viewUseCases;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.util.ActionDone;
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
		}
		return retorno;
	}

	private String novo(ActionDone ad) {
		
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad)); 
		temp.setVariable("resultado",ad.getMessage());
		return temp.generateOutput();
	}

}
