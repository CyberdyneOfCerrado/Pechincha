package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.Produto;
import module1.pechincha.util.ConnectionFactory;

public class JDBCProdutoDAO extends DAOBehavior<Produto>{
	private Connection c; 
	
	public JDBCProdutoDAO(){
		c = ConnectionFactory.getConnection();
	};
	
	@Override
	public void insert(Produto arg) {
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ?, ?, ?, ?, ? )"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1,arg.getTitulo());
			ps.setString(2,arg.getDescricao());
			ps.setFloat(3, arg.getPreco());
			ps.setInt(4, arg.getQuantidade());
			ps.setInt(5, arg.getFkUsuario());
			ps.execute(); 
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCProdutoDAO", e); 
		}
	};
	
	public int insertReturningPk(Produto arg) {
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ?, ?, ?, ?, ? ) returning pk"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1,arg.getTitulo());
			ps.setString(2,arg.getDescricao());
			ps.setFloat(3, arg.getPreco());
			ps.setInt(4, arg.getQuantidade());
			ps.setInt(5, arg.getFkUsuario());
			
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
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCProdutoDAO", e); 
		}
	};
	
	@Override
	public void delete(int pk) {
		try {
			PreparedStatement ps = c.prepareStatement("delete from produto where pk = ?");
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCProdutoDAO", e); 
		}
	};

	public List<Produto> list(int fkusuario) {
		List<Produto> list = new ArrayList<Produto>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from produto where fkusuario = ?");
			ps.setInt(1,fkusuario);
			ResultSet result = ps.executeQuery();
			while(result.next()){
				Produto temp = new Produto();
				temp.setPk(result.getInt("pk"));
				temp.setTitulo(result.getString("titulo"));
				temp.setDescricao(result.getString("descricao"));
				temp.setPreco(result.getFloat("preco"));
				temp.setQuantidade(result.getInt("quantidade"));
				temp.setFkUsuario(result.getInt("fkusuario"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCLanceDAO", e); 
		}
		return list;
	};
	
	@Override
	public List<Produto> list() {
		List<Produto> list = new ArrayList<Produto>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from produto");
			ResultSet result = ps.executeQuery();
			while(result.next()){
				Produto temp = new Produto();
				temp.setPk(result.getInt("pk"));
				temp.setTitulo(result.getString("titulo"));
				temp.setDescricao(result.getString("descricao"));
				temp.setPreco(result.getFloat("preco"));
				temp.setQuantidade(result.getInt("quantidade"));
				temp.setFkUsuario(result.getInt("fkusuario"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCProdutoDAO", e); 
		}
		return list;
	};

	@Override
	public Produto select(int pk) {
		Produto temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from produto where pk = ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			while(result.next()){
				temp = new Produto(); 
				temp.setPk(result.getInt("pk"));
				temp.setTitulo(result.getString("titulo"));
				temp.setDescricao(result.getString("descricao"));
				temp.setPreco(result.getFloat("preco"));
				temp.setQuantidade(result.getInt("quantidade"));
				temp.setFkUsuario(result.getInt("fkusuario"));
				break;
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCProdutoDAO", e); 
		}
		return temp;
	};

	@Override
	public void update(Produto arg) {
		String sql = "Update " + arg.getTableName() + " set " +
				"titulo = ?, descricao = ?, preco = ?, quantidade = ?, fkusuario = ? " +
				"where pk = ?";
		
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1,arg.getTitulo());
			ps.setString(2,arg.getDescricao());
			ps.setFloat(3,arg.getPreco());
			ps.setInt(4,arg.getQuantidade());
			ps.setInt(5,arg.getFkUsuario());
			ps.setInt(6,arg.getPk());
			ps.execute();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCProdutoDAO", e); 
		}
	};
	
	public boolean validar(Produto produto){
		String test = produto.getDescricao();
		float preco = produto.getPreco();
		int quantidade = produto.getQuantidade();
		if ( test != null && test.length() > 2000){
			return false;
		}
		test = produto.getTitulo();
		if ( test != null && (test.length() < 5 || test.length() > 50)){
			return false;
		}
		if ( preco < 0 || preco > 1000000){
			return false;
		}
		if ( quantidade < 1 || quantidade > 100){
			return false;
		}
		return true;
	}
}
