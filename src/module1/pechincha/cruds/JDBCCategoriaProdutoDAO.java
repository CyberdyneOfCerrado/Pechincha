package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.CategoriaProduto;
import module1.pechincha.model.Produto;
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
	public void deleteFromFKProduto(int pk) {
		try {
			PreparedStatement ps = c.prepareStatement("delete from categoriaproduto where fkproduto = ?");
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCCategoriaProdutoDAO", e); 
		}
	};

	public List<CategoriaProduto> list(int fkproduto) {
		List<CategoriaProduto> list = new ArrayList<CategoriaProduto>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from categoriaproduto where fkproduto = ?");
			ps.setInt(1,fkproduto);
			ResultSet result = ps.executeQuery();
			while(result.next()){
				CategoriaProduto temp = new CategoriaProduto();
				temp.setPk(result.getInt("pk"));
				temp.setFkProduto(result.getInt("fkproduto"));
				temp.setFkCategoria(result.getInt("fkcategoria"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCCategoriaProdutoDAO", e); 
		}
		return list;
	};
	public List<Produto> getProdutosByCategorias(String[] pks, int fkusuario){
		List<Produto> prod = new ArrayList<Produto>();
		JDBCProdutoDAO daoprod = new JDBCProdutoDAO();
		if ( pks == null ){
			return daoprod.list(fkusuario);
		}
		if ( pks.length == 0){
			return daoprod.list(fkusuario);
		}
		String where = "";
		for (String i : pks){
			if ( i.equals("0")){
				return daoprod.list(fkusuario);
			}
			where += "categoriaproduto.fkcategoria = " + i + " OR ";
		}
		where = where.substring(0, where.length()-4);
		String sql = "select DISTINCT categoriaproduto.fkproduto AS \"fkproduto\" from categoriaproduto, produto, categoria where (" + where + ") and produto.pk = categoriaproduto.fkproduto AND categoriaproduto.fkcategoria = categoria.pk AND produto.fkusuario = " + fkusuario;
		System.out.println(sql);
		
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet result = ps.executeQuery();
			while(result.next()){
				prod.add(daoprod.select(result.getInt("fkproduto")));
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCCategoriaProdutoDAO", e); 
		}
		System.out.println(prod.size() + " produtos");
		return prod;
	}
	@Override
	public List<CategoriaProduto> list() {
		List<CategoriaProduto> list = new ArrayList<CategoriaProduto>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from categoriaproduto");
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
	public CategoriaProduto select(int pk) {
		CategoriaProduto temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from categoriaproduto where pk = ?");
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
