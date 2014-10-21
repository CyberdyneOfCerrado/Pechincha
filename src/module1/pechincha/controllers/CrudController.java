package module1.pechincha.controllers;
import java.sql.ResultSet;

import pedidos.util.GenericDAO;


//Classe que gerencia os aspectos básicos das classes crud especialistas.
public class CrudController {
	private GenericDAO dao;
	
	public CrudController(){
		dao = GenericDAO.getInstace();
	};
	
	//Sql 'sem retorno'.
	public boolean run( String sql){
		return dao.run(sql);
	};
	//Sql com retorno de um resultSet
	public ResultSet runWithResult(String sql ){
		return dao.runWithResult(sql);
	};
	
}
