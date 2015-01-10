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
			int hora=Integer.parseInt(s);
			if(hora>23 || hora<0){
				done.setUseCase("gerenciarLeilao");
				done.setAction("leilaop0erro");
				done.setProcessed(true);
				done.setStatus(false);
				done.setData("idleiloeiro", action.getData("idleiloeiro"));
				done.setData("etiqueta", action.getData("etiqueta"));
				done.setData("tempolimite", action.getData("tempolimite"));
				done.setData("descricao", action.getData("descricao"));
				done.setData("erro", "Houve um erro no campo tempo!");
				done.setData("tipo", "3");
				done.setData("valida",false);
				return done;
			}
			segundos=hora*60*60;
			s=temp.substring(3,5);
			int minutos=Integer.parseInt(s);
			if(minutos>59 || minutos<0){
				done.setUseCase("gerenciarLeilao");
				done.setAction("leilaop0erro");
				done.setProcessed(true);
				done.setStatus(false);
				done.setData("idleiloeiro", action.getData("idleiloeiro"));
				done.setData("etiqueta", action.getData("etiqueta"));
				done.setData("tempolimite", action.getData("tempolimite"));
				done.setData("descricao", action.getData("descricao"));
				done.setData("erro", "Houve um erro no campo tempo!");
				done.setData("tipo", "3");
				done.setData("valida",false);
				return done;
			}
			segundos+=minutos*60;
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
				done.setData("tipo", "3");
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
			done.setData("tipo", "3");
			done.setData("valida",false);
			return done;
		}
		le = leilaoDao.searchEtiqueta(leilao.getEtiqueta());
		if(leilao.getEtiqueta().length()<=20 && le==null && !leilao.getEtiqueta().equals(""))
			done.setData("valida",true); else {
				done.setUseCase("gerenciarLeilao");
				done.setAction("leilaop0erro");
				done.setProcessed(true);
				done.setStatus(false);
				done.setData("idleiloeiro", action.getData("idleiloeiro"));
				done.setData("etiqueta", action.getData("etiqueta"));
				done.setData("tempolimite", action.getData("tempolimite"));
				done.setData("descricao", action.getData("descricao"));
				done.setData("erro", "Nenhuma etiqueta informada ou a etiqueta informada já existe!");
				done.setData("tipo", "1");
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
				done.setData("tipo", "2");
				done.setData("valida",false);
				return done;
			}
		return done;
}
}