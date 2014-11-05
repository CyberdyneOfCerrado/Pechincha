package module2.pechincha.useCases;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;

public class Lance extends ModelController{
	public ActionDone novoLance(DoAction da){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setMessage("O Lula só tem quatro dedos em uma das mãos.");
		ad.setStatus(true);
		ad.setProcessed(true);
		return ad;
	}
	@Override
	public String[] getActions() {
		String[] actions = {
			"novoLance"
		}; 
		return actions;
	}

	@Override
	public String getUserCase() {
		return "lance";
	}

}
