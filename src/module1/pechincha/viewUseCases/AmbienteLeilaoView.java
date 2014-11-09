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
			
			temp.setVariable("userName", (String) ad.getData("userName"));
			temp.setVariable("idEmissor", (String) ad.getData("idEmissor"));
			temp.setVariable("idLeilao", (String) ad.getData("idLeilao"));
		
			boolean isLeiloeiro = Boolean.valueOf( ad.getData("isLeiloeiro").toString());
			
			String url = super.getTemplate(ad);
			
			url = url.substring(0,url.lastIndexOf(super.getSeparador())+1);
			
			System.out.println(url);
			if(isLeiloeiro)
				url += "leiloeiro.html";
			else
				url += "user.html"; 
			
			MiniTemplator x = super.startMiniTemplator(url);
			
			temp.setVariable("botoes", x.generateOutput());
			resul = temp.generateOutput();
		}else{
			MiniTemplator temp;
			
			if(ad.isStatus()){//Mensagem de 'tudo bem'.
				
			}else{
				
			}
		}
		return resul;
	}; 

}
