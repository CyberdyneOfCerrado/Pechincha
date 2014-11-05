package module1.pechincha.viewUseCases;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.util.ActionDone;
import module1.pechincha.view.ViewController;

public class ManterGalinhaView extends ViewController {

	public ManterGalinhaView(String sevletContext, String useCase) {
		super(sevletContext, useCase);
	}

	@Override
	public String choose(ActionDone ad) {
		String action = ad.getAction();
		String retorno = ""; 
		
		switch(action){
		case "cantar":
				retorno = cantar(ad); 
			break;
		}
		return retorno;
	}

	private String cantar(ActionDone ad) {
		
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad)); 
		temp.setVariable("lula",ad.getMessage());
		return temp.generateOutput();
	}

}
