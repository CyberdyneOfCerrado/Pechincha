package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.Imagem;
import module1.pechincha.util.ConnectionFactory;

public class JDBCImagemDAO extends DAOBehavior<Imagem>{
	private Connection c; 
	
	public JDBCImagemDAO(){
		c = ConnectionFactory.getConnection();
	};
	
	@Override
	public void insert(Imagem arg) {
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ? ) returning pk"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1,arg.getFkProduto());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCImagemDAO", e); 
		}
	};
	
	public int insertReturningPk(Imagem arg) {
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ?, ? ) returning pk"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1,arg.getFkProduto());
			ps.setString(2, arg.getFormato());
			ResultSet result = ps.executeQuery();
			int pk = 0;
			while(result.next()){
				pk = result.getInt("pk");
				break;
			}
			result.close();
			ps.close();
			return pk;			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCImagemDAO", e); 
		}
	};

	@Override
	public void delete(int pk) {
		try {
			PreparedStatement ps = c.prepareStatement("delete from imagem where pk = ?");
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCImagemDAO", e); 
		}
	};

	public List<Imagem> list(int fkproduto) {
		List<Imagem> list = new ArrayList<Imagem>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from imagem where fkproduto = ?");
			ps.setInt(1,fkproduto);
			ResultSet result = ps.executeQuery();
			while(result.next()){
				Imagem temp = new Imagem();
				temp.setPk(result.getInt("pk"));
				temp.setFkProduto(result.getInt("fkproduto"));
				temp.setFormato(result.getString("formato"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCImagemDAO", e); 
		}
		return list;
	};
	
	@Override
	public List<Imagem> list() {
		List<Imagem> list = new ArrayList<Imagem>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from imagem");
			ResultSet result = ps.executeQuery();
			while(result.next()){
				Imagem temp = new Imagem();
				temp.setPk(result.getInt("pk"));
				temp.setFkProduto(result.getInt("fkproduto"));
				temp.setFormato(result.getString("formato"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCImagemDAO", e); 
		}
		return list;
	};

	@Override
	public Imagem search(int pk) {
		Imagem temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from imagem where pk = ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			temp = new Imagem(); 
			while(result.next()){
				temp.setPk(result.getInt("pk"));
				temp.setFkProduto(result.getInt("fkproduto"));
				temp.setFormato(result.getString("formato"));
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCImagemDAO", e); 
		}
		return temp;
	};

	@Override
	public void update(Imagem arg) {
		
	};
}
