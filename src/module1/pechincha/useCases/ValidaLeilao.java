package module1.pechincha.useCases;

import module1.pechincha.cruds.JDBCLeilaoDAO;
import module1.pechincha.model.Leilao;
import module1.pechincha.util.ActionDone;

public class ValidaLeilao {
	public boolean validar(Leilao leilao){
		boolean status=false;
		JDBCLeilaoDAO leilaoDao = new JDBCLeilaoDAO();
		Leilao le=null;
		le = leilaoDao.searchEtiqueta(leilao.getEtiqueta());
		if(leilao.getEtiqueta().length()<=20 && le==null)
			status=true; else return false;
		if(leilao.getTempoLimite()<=86400 && leilao.getTempoLimite()!=0)
			status=true; else return false;
		if(leilao.getDescricao().length()<=1000)
			status=true; else return false;
//		if(leilao.getLanceInicial()<=1000 && leilao.getLanceInicial()<=0.50){
//			done.setProcessed(true);
//			done.setStatus(true);
//			done.setMessage("OK");
//		}else{
//			done.setProcessed(true);
//			done.setStatus(false);
//			done.setMessage("Lance Inicial Invalido!");
//		}
//		if(leilao.getPrecolote()>0){
//			done.setProcessed(true);
//			done.setStatus(true);
//			done.setMessage("OK");
//		}else{
//			done.setProcessed(true);
//			done.setStatus(false);
//			done.setMessage("O preço do lote tem que ser diferente de zero!");
//		}
		return status;
	}
	
}
