package module1.pechincha.useCases;

import java.util.ArrayList;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module2.pechincha.manager.ManagerLeilao;
import module2.pechincha.manager.StorageLeilaoEnvironments;

public class Home extends ModelController {
	
	public ActionDone getFeed(DoAction da){
		ActionDone ad = new ActionDone();		
		int pointStart;
		
		if(da.getData("point") != null)
			pointStart = Integer.parseInt( (String) da.getData("point") ); 
		else
			pointStart = 0;
		Object[] array = StorageLeilaoEnvironments.getMetadataEnvironments().toArray();
		
		ArrayList<ManagerLeilao> list = new ArrayList<>();
		//12 é quantidade limite de cada requisição ajax. Essa quantidade é a ideal para preencher a tela do usuários com os 
		//box contendo as informações sobre os leilões. 
		
		
		for(int count = pointStart ; count < pointStart+12 ; count++){
			if(array.length <= count ) break;
			list.add( (ManagerLeilao) array[count]); 
		}
		
		ad.setData("list",list); 
		ad.setData("concatToIndex","false");
		
		//Por enquanto eu não vou fazer nada aqui. 
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		return ad;
	}

	@Override
	public String[] getActions() {
		String[] actions = {"getFeed"}; 
		
		return actions;
	}

	@Override
	public String getUserCase() {
		return "home";
	}

}
