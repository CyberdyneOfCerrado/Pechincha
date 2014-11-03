package module1.pechincha.viewUseCases;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.util.ActionDone;
import module1.pechincha.view.ViewController;

public class AmbienteLeilaoView extends ViewController {

	public AmbienteLeilaoView(String sevletContext, String useCase) {
		super(sevletContext, useCase);
	}; 
	

	@Override
	public String choose(ActionDone ad) {
		String resul=null;
		switch(ad.getAction()){
		case "ambiente":
			resul = ambiente(ad);
			break;
		}
		return resul;
	}; 

	private String ambiente(ActionDone ad) {
		String resul = "";
		if(ad.isProcessed()){
			MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));
			resul = temp.generateOutput();
		}else{
			MiniTemplator temp;
			
			if(ad.isStatus()){//Mensagem de 'tudo bem'.
				 temp = super.startMiniTemplator(super.getSevletContext()+"staff"+super.getSeparador()+"success.html");
				 temp.setVariable("log",ad.getMessage());
				 resul = temp.generateOutput();
			}else{
				 temp = super.startMiniTemplator(super.getSevletContext()+"staff"+super.getSeparador()+"erro.html");
				 temp.setVariable("log",ad.getMessage());
				 resul = temp.generateOutput();
			}
		}
		return resul;
	}; 

}
