package module1.pechincha.useCases;

import java.util.ArrayList;
import java.util.Collection;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module2.pechincha.manager.ManagerLeilao;
import module2.pechincha.manager.StorageLeilaoEnvironments;

public class Home extends ModelController {
	
	public ActionDone getFeed(DoAction da){
		ActionDone ad = new ActionDone();		
		int pointStart = Integer.parseInt( (String) da.getData("point")); 
		
		Collection<ManagerLeilao> c = StorageLeilaoEnvironments.getMetadataEnvironments(); 
		ArrayList<ManagerLeilao> list = new ArrayList<>();
		
		int count = pointStart; 
		
		for(ManagerLeilao ml : c ){
			list.add(ml);
			if( ++count >= pointStart+12) break;
		}
		
		ad.setData("list",list); 
		
		//Por enquanto eu não vou fazer nada aqui. 
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setMessage("Cliente excluído com sucesso!");
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
