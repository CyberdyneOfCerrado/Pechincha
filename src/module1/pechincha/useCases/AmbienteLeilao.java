package module1.pechincha.useCases;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;

public class AmbienteLeilao extends ModelController {
	
	public ActionDone ambiente( DoAction da ){
	ActionDone ad = new ActionDone();
	//Verificar aqui se o usuário que requisita a o ambiente é o leiloeiro da mesma; 
	
	System.out.println(da.getData("userName"));
	ad.setData("userName", da.getData("userName")); 
	ad.setData("idEmissor", da.getData("idEmissor")); 
	ad.setData("idLeilao", da.getData("idLeilao"));			
			
	ad.setData("isLeiloeiro", true);
	
	//Por enquanto eu não vou fazer nada aqui. 
	//Identificando o pacote
	ad.setAction(da.getAction());
	ad.setUseCase(da.getUseCase());
	ad.setMessage("Cliente excluído com sucesso!");
	ad.setStatus(true);
	ad.setProcessed(true);
	return ad;
	};
	
	@Override
	public String[] getActions() {
	String[] actions ={"ambiente"};
		return actions;
	};

	@Override
	public String getUserCase() {
		return "ambienteLeilao";
	};

}
