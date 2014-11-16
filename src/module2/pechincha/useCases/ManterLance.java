package module2.pechincha.useCases;

import module1.pechincha.cruds.JDBCLanceDAO;
import module1.pechincha.model.Lance;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;

public class ManterLance {
	public synchronized ActionDone novoLance(DoAction da){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);

		try{
			int pkleilao = Integer.valueOf((String)da.getData("pkleilao")),
					pkusuario = Integer.valueOf((String)da.getData("pkusuario"));
			float vallance = Float.valueOf((String)da.getData("lance"));			
			
			Lance novo = new Lance(pkleilao, pkusuario, vallance);
			
			JDBCLanceDAO dao = new JDBCLanceDAO();
			
			if ( dao.validar(novo)){
				dao.insert(novo);
				ad.setData("valid", true);
			}else{
				ad.setData("valid", false);
			}
			ad.setData("error", false);
			return ad;
		}catch(Exception e){
			ad.setData("error", true);
		}
		
		return ad;
	}
}
