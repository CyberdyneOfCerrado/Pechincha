package module1.pechincha.cruds;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.LoteProduto;
import module1.pechincha.model.Produto;
import module1.pechincha.util.ConnectionFactory;
public class JDBCLoteProdutoDAO extends DAOBehavior<LoteProduto> {
	private Connection c;
	
	public JDBCLoteProdutoDAO(){
		  c = ConnectionFactory.getConnection();
		 };

	@Override
	public void insert(LoteProduto arg) {
		String sql = "Insert into "+arg.getTableName()+" (fkleilao,fkproduto,quantidade) values ( ?, ?, ? )"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1,arg.getFkleilao());
			ps.setInt(2, arg.getFkproduto());
			ps.setInt(3, arg.getUnidades());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCLoteProdutoDAO", e); 
		}
	}

	@Override
	public void delete(int pk){
		String sql = "delete from loteproduto where fkleilao= ?";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCLoteProdutoDAO", e); 
		}
	}

	@Override
	public List<LoteProduto> list() {
		return null;
	}

	@Override
	public LoteProduto select(int pk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(LoteProduto arg) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Produto> produtosLeilao(int idleilao) {
		List<Produto> list = new ArrayList<Produto>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select a.pk,a.descricao,a.preco,a.quantidade,a.fkusuario,a.titulo from produto a,loteproduto b,leilao c where b.fkleilao=? and b.fkproduto=a.pk");
			ps.setInt(1,idleilao);
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
			throw new RuntimeException("Erro ao listar dados. Classe JDBCLoteProdutoDAO", e); 
		}
		return list;
	}

}
