package module1.pechincha.viewUseCases;

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
		case "criarLeilao":
				retorno = criarLeilao(ad); 
			break;
		case "reenviarEmail":
			retorno = reenviarEmail(ad); 
		break;
		case "getHistorico":
			retorno = getHistorico(ad); 
		break;
		case "getTodosLeiloes":
			
			retorno = getTodosLeiloes(ad); 
		break;
		case "entrarLeilao":
			retorno = entrarLeilao(ad); 
		break;
		case "enviarEmail":
			retorno = enviarEmail(ad); 
		break;
		case "pesquisarLeilao":
			retorno = pesquisarLeilao(ad); 
		break;
		}
		return retorno;
	}

}
