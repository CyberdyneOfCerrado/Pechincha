package module1.pechincha.useCases;

import module1.pechincha.cruds.JDBCLeilaoDAO;
import module1.pechincha.model.Leilao;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;

public class ValidaLeilao {
	public ActionDone validar(Leilao leilao, DoAction action){
		ActionDone done = new ActionDone();
		boolean status=false;
		JDBCLeilaoDAO leilaoDao = new JDBCLeilaoDAO();
		Leilao le=null;
		String temp=String.valueOf(action.getData("tempolimite"));
		int segundos=0;
		if(temp.length()==5 && temp.substring(2,3).equals(":")){
			try{
			String s=temp.substring(0,2);
			int tempo=Integer.parseInt(s);
			if(tempo>23 || tempo<0){
				done.setUseCase("gerenciarLeilao");
				done.setAction("leilaop0erro");
				done.setProcessed(true);
				done.setStatus(false);
				done.setData("idleiloeiro", action.getData("idleiloeiro"));
				done.setData("etiqueta", action.getData("etiqueta"));
				done.setData("tempolimite", action.getData("tempolimite"));
				done.setData("descricao", action.getData("descricao"));
				done.setData("erro", "Houve um erro no campo tempo!");
				done.setData("valida",false);
				return done;
			}
			segundos=tempo*60*60;
			s=temp.substring(2,5);
			if(tempo>59 || tempo<0){
				done.setUseCase("gerenciarLeilao");
				done.setAction("leilaop0erro");
				done.setProcessed(true);
				done.setStatus(false);
				done.setData("idleiloeiro", action.getData("idleiloeiro"));
				done.setData("etiqueta", action.getData("etiqueta"));
				done.setData("tempolimite", action.getData("tempolimite"));
				done.setData("descricao", action.getData("descricao"));
				done.setData("erro", "Houve um erro no campo tempo!");
				done.setData("valida",false);
				return done;
			}
			segundos+=tempo*60;
			done.setData("tempo", segundos);
			done.setData("valida",true);
			}catch(NumberFormatException e){
				done.setUseCase("gerenciarLeilao");
				done.setAction("leilaop0erro");
				done.setProcessed(true);
				done.setStatus(false);
				done.setData("idleiloeiro", action.getData("idleiloeiro"));
				done.setData("etiqueta", action.getData("etiqueta"));
				done.setData("tempolimite", action.getData("tempolimite"));
				done.setData("descricao", action.getData("descricao"));
				done.setData("erro", "Houve um erro no campo tempo!");
				done.setData("valida",false);
				return done;
			}
		}else{
			done.setUseCase("gerenciarLeilao");
			done.setAction("leilaop0erro");
			done.setProcessed(true);
			done.setStatus(false);
			done.setData("idleiloeiro", action.getData("idleiloeiro"));
			done.setData("etiqueta", action.getData("etiqueta"));
			done.setData("tempolimite", action.getData("tempolimite"));
			done.setData("descricao", action.getData("descricao"));
			done.setData("erro", "Houve um erro no campo tempo!");
			done.setData("valida",false);
			return done;
		}
		le = leilaoDao.searchEtiqueta(leilao.getEtiqueta());
		if(leilao.getEtiqueta().length()<=20 && le==null)
			done.setData("valida",true); else {
				done.setUseCase("gerenciarLeilao");
				done.setAction("leilaop0erro");
				done.setProcessed(true);
				done.setStatus(false);
				done.setData("idleiloeiro", action.getData("idleiloeiro"));
				done.setData("etiqueta", action.getData("etiqueta"));
				done.setData("tempolimite", action.getData("tempolimite"));
				done.setData("descricao", action.getData("descricao"));
				done.setData("erro", "A etiqueta informada já existe!");
				done.setData("valida",false);
				return done;
			}
		if(leilao.getDescricao().length()<=1000)
			done.setData("valida",true); else{
				done.setUseCase("gerenciarLeilao");
				done.setAction("leilaop0erro");
				done.setProcessed(true);
				done.setStatus(false);
				done.setData("idleiloeiro", action.getData("idleiloeiro"));
				done.setData("etiqueta", action.getData("etiqueta"));
				done.setData("tempolimite", action.getData("tempolimite"));
				done.setData("descricao", action.getData("descricao"));
				done.setData("erro", "Erro no campo descrição!");
				done.setData("valida",false);
				return done;
			}
		return done;
}
}