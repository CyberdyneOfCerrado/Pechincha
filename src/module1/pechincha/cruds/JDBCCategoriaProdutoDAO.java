package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.CategoriaProduto;
import module1.pechincha.util.ConnectionFactory;

public class JDBCCategoriaProdutoDAO extends DAOBehavior<CategoriaProduto>{
	private Connection c; 
	
	public JDBCCategoriaProdutoDAO(){
		c = ConnectionFactory.getConnection();
	};
	
	@Override
	public void insert(CategoriaProduto arg) {
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ?, ? )"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1,arg.getFkCategoria());
			ps.setInt(2,arg.getFkProduto());
			ps.execute(); 
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCCategoriaProdutoDAO", e); 
		}
	};

	@Override
	public void delete(int pk) {
		try {
			PreparedStatement ps = c.prepareStatement("delete from categoriaproduto where pk = ?");
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCCategoriaProdutoDAO", e); 
		}
	};

	public List<CategoriaProduto> list(int fkcategoria, int fkusuario) {
		List<CategoriaProduto> list = new ArrayList<CategoriaProduto>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from categoriaproduto, produto where fkcategoria = ? " +
														"and fkproduto = produto.pk and produto.fkusuario = ?");
			ps.setInt(1,fkcategoria);
			ps.setInt(2,fkusuario);
			ResultSet result = ps.executeQuery();
			while(result.next()){
				CategoriaProduto temp = new CategoriaProduto();
				temp.setPk(result.getInt("pk"));
				temp.setFkProduto(result.getInt("fkproduto"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCCategoriaProdutoDAO", e); 
		}
		return list;
	};
	
	@Override
	public List<CategoriaProduto> list() {
		List<CategoriaProduto> list = new ArrayList<CategoriaProduto>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from CategoriaProduto");
			ResultSet result = ps.executeQuery();
			while(result.next()){
				CategoriaProduto temp = new CategoriaProduto();
				temp.setPk(result.getInt("pk"));
				temp.setFkProduto(result.getInt("fkproduto"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCCategoriaProdutoDAO", e); 
		}
		return list;
	};

	@Override
	public CategoriaProduto search(int pk) {
		CategoriaProduto temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from CategoriaProduto where pk = ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			temp = new CategoriaProduto(); 
			while(result.next()){
				temp.setPk(result.getInt("pk"));
				temp.setFkProduto(result.getInt("fkproduto"));
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCCategoriaProdutoDAO", e); 
		}
		return temp;
	};

	@Override
	public void update(CategoriaProduto arg) {
		
	};
}
