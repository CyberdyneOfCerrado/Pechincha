package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.Pessoa;
import module1.pechincha.util.ConnectionFactory;



public class JDBCPessoaDAO extends DAOBehavior<Pessoa>{
	private Connection c; 
	
	public JDBCPessoaDAO(){
		c = ConnectionFactory.getConnection(); 
	};
	
	@Override
	public void insert(Pessoa arg) {
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ? ,?, ?, ? )"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1,arg.getId());
			ps.setString(2,arg.getNome());
			ps.setInt(3, arg.getIdade());
			ps.setDouble(4,arg.getAltura());
			ps.execute(); 
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCPessoaDAO", e); 
		}
	};

	@Override
	public void delete(int pk) {
		try {
			PreparedStatement ps = c.prepareStatement("delete from pessoa where id =  ?");
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCPessoaDAO", e); 
		}
	};

	@Override
	public List<Pessoa> list() {
		List<Pessoa> list = new ArrayList<Pessoa>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from pessoa");
			ResultSet result = ps.executeQuery();
			while(result.next()){
				Pessoa temp = new Pessoa();
				temp.setAltura(result.getDouble("altura"));
				temp.setId(result.getInt("id"));
				temp.setIdade(result.getInt("idade"));
				temp.setNome(result.getString("nome"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCPessoaDAO", e); 
		}
		return list;
	};

	@Override
	public Pessoa search(int pk) {
		Pessoa temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from pessoa where id =  ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			temp = new Pessoa(); 
			while(result.next()){
				temp.setAltura(result.getDouble("altura"));
				temp.setId(result.getInt("id"));
				temp.setIdade(result.getInt("idade"));
				temp.setNome(result.getString("nome"));
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCPessoaDAO", e); 
		}
		return temp;
	};

	@Override
	public void update(Pessoa arg) {
		
	};
}
