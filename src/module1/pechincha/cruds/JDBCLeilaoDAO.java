package module1.pechincha.cruds;

import java.sql.Connection;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.Leilao;
import module1.pechincha.util.ConnectionFactory;

public class JDBCLeilaoDAO extends DAOBehavior<Leilao>{
	private Connection c; 
	
	public JDBCLeilaoDAO(){
		c = ConnectionFactory.getConnection();
	};
	@Override
	public void insert(Leilao arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int pk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Leilao> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Leilao search(int pk) {
		String sql="";
		return null;
	}

	@Override
	public void update(Leilao arg) {
		// TODO Auto-generated method stub
		
	}

}
