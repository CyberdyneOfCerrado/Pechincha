package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.Categoria;
import module1.pechincha.util.ConnectionFactory;

public class JDBCCategoriaDAO extends DAOBehavior<Categoria> {
private Connection c; 
	
	public JDBCCategoriaDAO(){
		c = ConnectionFactory.getConnection();
	};
	@Override
	public void insert(Categoria arg) {
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ? )"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1,arg.getDescricao());
				ps.execute(); 
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCCategoriaDAO", e); 
		}		
	}

	@Override
	public void delete(int pk) {
		try {
			PreparedStatement ps = c.prepareStatement("delete from categoria where pk = ?");
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCCategoriaDAO", e); 
		}
	}

	@Override
	public List<Categoria> list() {
		List<Categoria> list = new ArrayList<Categoria>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from categoria");
			ResultSet result = ps.executeQuery();
			while(result.next()){
				Categoria temp = new Categoria();
				temp.setPk(result.getInt("pk"));
				temp.setDescricao(result.getString("descricao"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCCategoriaDAO", e); 
		}
		return list;
	}

	@Override
	public Categoria search(int pk) {
		Categoria temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from categoria where pk = ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			temp = new Categoria(); 
			while(result.next()){
				temp.setPk(result.getInt("pk"));
				temp.setDescricao(result.getString("descricao"));
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCCategoriaDAO", e); 
		}
		return temp;
	}

	@Override
	public void update(Categoria arg) {
		// TODO Auto-generated method stub
		
	}

}
