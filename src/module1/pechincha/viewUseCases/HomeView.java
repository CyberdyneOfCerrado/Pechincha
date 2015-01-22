package module1.pechincha.viewUseCases;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.util.ActionDone;
import module1.pechincha.view.ViewController;
import module2.pechincha.manager.ManagerLeilao;

public class HomeView extends ViewController {

	public HomeView(String sevletContext, String useCase) {
		super(sevletContext, useCase);
	}

	@Override
	public String choose(ActionDone ad) {
		String action = ad.getAction();
		String retorno = ""; 
		
		switch(action){
		case "home":
				retorno = home(ad); 
			break;
		case "getFeed": 
			retorno = getFeed(ad);
			break;
		}
		return retorno;
	}

	private String getFeed(ActionDone ad) {
		//fixar variáveis: 
		//tipo: boxAlertaTempoLimite e boxAlertaSemLance
		//online: quantidade de pessoas online; 
		//tempo: tempo restante para o termino de leilão. 
		//etiqueta; 
		//lanceInciial; 
		//lanceCorrente; 
		//leiloeiro; 
		ArrayList<ManagerLeilao> array = (ArrayList<ManagerLeilao>) ad.getData("list");
	    String result = ""; 
	    
		for(ManagerLeilao ml : array){
			MiniTemplator m = super.startMiniTemplator(getSevletContext()+"home"+super.getSeparador()+"box.html"); 
			m.setVariable("idL", ml.getLeilao().getIdLeilao());
			String tipo = ""; 
				if( ml.getLanceCorrente() == ml.getLeilao().getLanceInicial())
					tipo = "boxAlertaSemLance"; 
				else if( ml.getTempoCorrente() <= 1500 )
					tipo = "boxAlertaTempoLimite"; 
				
			m.setVariable("tipo", tipo);
			m.setVariable("online",ml.getOnline());
			m.setVariable("tempo", ml.getTempoCorrente());
			m.setVariable("etiqueta",ml.getLeilao().getEtiqueta());
			m.setVariable("lanceInicial", String.valueOf(ml.getLeilao().getLanceInicial()));
			m.setVariable("lanceCorrente", String.valueOf(ml.getLanceCorrente()));
			m.setVariable("leiloeiro", ml.getLeilao().getNickname());
			result += m.generateOutput();
		}
		
		return result;
	}

	private String home(ActionDone ad) {
		MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));
		HttpSession s = (HttpSession) ad.getData("Session");
		temp.setVariable("idEmissor",(String) s.getAttribute("id"));
		return temp.generateOutput();
	}

}
