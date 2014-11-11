package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.Leilao;
import module1.pechincha.model.Produto;
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
		return null;
	}

	@Override
	public void update(Leilao arg) {
			String sql = "Update " + arg.getTableName() + " set " +
					"etiqueta = ?, tempolimite = ?, descricao = ?, comprador = ?, ativo = ?,idleiloeiro = ?, lanceinicial = ?, nickname = ? " +
					"where idleilao = ?";
			try {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setString(1,arg.getEtiqueta());
				ps.setInt(2,arg.getTempoLimite());
				ps.setString(3,arg.getDescricao());
				ps.setInt(4,arg.getComprador());
				ps.setBoolean(5,arg.isAtivo());
				ps.setInt(6,arg.getIdLeiloeiro());
				ps.setFloat(7,arg.getLanceInicial());
				ps.setString(8,arg.getNickname());
				ps.execute();
				ps.close();
			
			} catch (SQLException e) {
				throw new RuntimeException("Erro ao atualizar dados. Classe JDBCLeilaoDAO", e); 
			}
	}

}
