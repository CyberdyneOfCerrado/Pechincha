package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ?, ?, ?, ?, ?, ?, ?, ?, ? )"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1,arg.getEtiqueta());
			ps.setInt(2,arg.getTempoLimite());
			ps.setString(3, arg.getDescricao());
			ps.setInt(4, arg.getComprador());
			ps.setBoolean(5, arg.isAtivo());
			ps.setInt(6, arg.getIdLeiloeiro());
			ps.setFloat(7, arg.getLanceInicial());
			ps.setString(8, arg.getNickname());
			ps.setFloat(9, arg.getPrecolote());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCLeilaoDAO", e); 
		}
	}
	
	public int insertReturningPk(Leilao arg){
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning idleilao"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1,arg.getEtiqueta());
			ps.setInt(2,arg.getTempoLimite());
			ps.setString(3, arg.getDescricao());
			ps.setInt(4, arg.getComprador());
			ps.setBoolean(5, arg.isAtivo());
			ps.setInt(6, arg.getIdLeiloeiro());
			ps.setFloat(7, arg.getLanceInicial());
			ps.setString(8, arg.getNickname());
			ps.setFloat(9, arg.getPrecolote());
			ResultSet result=ps.executeQuery();
			int idleilao = 0;
			while(result.next()){
				idleilao = result.getInt("idleilao");
				break;
			}
			result.close();
			ps.close();
			return idleilao;
			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCLeilaoDAO", e); 
		}
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
		Leilao temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from leilao where pk = ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			while(result.next()){
				temp = new Leilao(); 
				temp.setIdLeilao(result.getInt("idleilao"));
				temp.setEtiqueta(result.getString("etiqueta"));
				temp.setTempoLimite(result.getInt("tempolimite"));
				temp.setDescricao(result.getString("descricao"));
				temp.setComprador(result.getInt("comprador"));
				temp.setAtivo(result.getBoolean("ativo"));
				temp.setIdLeiloeiro(result.getInt("idleiloeiro"));
				temp.setLanceInicial(result.getFloat("lanceinicial"));
				temp.setNickname(result.getString("nickname"));
				temp.setPrecolote(result.getFloat("pecolote"));
				break;
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCLeilaoDAO", e); 
		}
		return temp;
	}

	@Override
	public void update(Leilao arg) {
			String sql = "Update " + arg.getTableName() + " set " +
					"etiqueta = ?, tempolimite = ?, descricao = ?, comprador = ?, ativo = ?,idleiloeiro = ?, lanceinicial = ?, nickname = ?, precolote = ? " +
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
				ps.setFloat(9, arg.getPrecolote());
				ps.execute();
				ps.close();
			
			} catch (SQLException e) {
				throw new RuntimeException("Erro ao atualizar dados. Classe JDBCLeilaoDAO", e); 
			}
	}

}
