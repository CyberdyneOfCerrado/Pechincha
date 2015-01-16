package module1.pechincha.viewUseCases;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.util.ActionDone;
import module1.pechincha.view.ViewController;

public class ManterUsuarioView extends ViewController {
	public ManterUsuarioView(String sevletContext, String useCase) {
		super(sevletContext, useCase);
	}
	@Override
	public String choose(ActionDone ad) {
		String action = ad.getAction();
		String retorno = "";
		switch(action){
		case "cadastroerro":
			retorno=cadastroerro(ad);
		break;
		case "cadastro":
			retorno=cadastro(ad);
		break;
		}
		return retorno;
	}
	public String cadastroerro(ActionDone ad){
		return (String) ad.getData("message");
	}
	public String cadastro(ActionDone ad){
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));
		return temp.generateOutput();
	}

}
