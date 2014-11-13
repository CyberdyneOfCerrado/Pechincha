package module1.pechincha.useCases;

import module1.pechincha.cruds.JDBCLeilaoDAO;
import module1.pechincha.model.Leilao;
import module1.pechincha.util.ActionDone;

public class ValidaLeilao {
	Leilao leilao;
	public ValidaLeilao(Leilao leilao){
		this.leilao=leilao;
	}
	public ActionDone validar(){
		ActionDone done=new ActionDone();
		done.setStatus(true);
		done.setMessage("OK");
		JDBCLeilaoDAO leilaoDao = new JDBCLeilaoDAO();
		if(leilao.getEtiqueta().length()<=20 && leilaoDao.searchEtiqueta(leilao.getEtiqueta())==null){
			done.setProcessed(true);
			done.setStatus(true);
			done.setMessage("OK");
		}else{
			done.setProcessed(true);
			done.setStatus(false);
			done.setMessage("Etiqueta inválida ou ja existente!");
		}
		if(leilao.getTempoLimite()<=86400){
			done.setProcessed(true);
			done.setStatus(true);
			done.setMessage("OK");
		}else{
			done.setProcessed(true);
			done.setStatus(false);
			done.setMessage("Tempo inválido!");
		}
		if(leilao.getDescricao().length()<=1000){
			done.setProcessed(true);
			done.setStatus(true);
			done.setMessage("OK");
		}else{
			done.setProcessed(true);
			done.setStatus(false);
			done.setMessage("Descrição inconsistente!");
		}
		if(leilao.getLanceInicial()<=1000 && leilao.getLanceInicial()<=0.50){
			done.setProcessed(true);
			done.setStatus(true);
			done.setMessage("OK");
		}else{
			done.setProcessed(true);
			done.setStatus(false);
			done.setMessage("Lance Inicial Invalido!");
		}
		if(leilao.getPrecolote()>0){
			done.setProcessed(true);
			done.setStatus(true);
			done.setMessage("OK");
		}else{
			done.setProcessed(true);
			done.setStatus(false);
			done.setMessage("O preço do lote tem que ser diferente de zero!");
		}
		return done;
	}
	
}
