package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		String sql = "Insert into Leilao (etiqueta,descricao,comprador,ativo,idLeiloeiro,lanceInicial,tempoLimite,nickname,precolote,termino) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) "; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1,arg.getEtiqueta());
			ps.setString(2, arg.getDescricao());
			ps.setInt(3, arg.getComprador());
			ps.setBoolean(4, arg.isAtivo());
			ps.setInt(5, arg.getIdLeiloeiro());
			ps.setFloat(6, arg.getLanceInicial());
			ps.setInt(7,arg.getTempoLimite());
			ps.setString(8, arg.getNickname());
			ps.setFloat(9, arg.getPrecolote());
			ps.setString(10, arg.getTermino());
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCLeilaoDAO", e); 
		}
	}
	
	public boolean verificarProprietarioLeilao(int pkUsuario, int pkLeilao){
		try {
		PreparedStatement ps = c.prepareStatement("select idleiloeiro from leilao where leilao.idleiloeiro=? and leilao.idleilao=?"); 
		ps.setInt(1,pkUsuario);
		ps.setInt(2,pkLeilao);
		ResultSet result = ps.executeQuery();
		while(result.next()){
			if(result.getInt("idleiloeiro")==pkUsuario){
				result.close();
				ps.close();
				return true;
			}
			else {
				result.close();
				ps.close();
				return false;
			}
		}
		}catch (SQLException e) {
		throw new RuntimeException("Erro ao listar dados. Classe JDBCLoteProdutoDAO", e); 
	}
		return false;
}
	
	public int insertReturningPk(Leilao arg){
		String sql = "Insert into Leilao (etiqueta,descricao,comprador,ativo,idLeiloeiro,lanceInicial,tempoLimite,nickname,precolote,termino) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning idleilao"; 
		System.out.println(sql);
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1,arg.getEtiqueta());
			ps.setString(2, arg.getDescricao());
			ps.setInt(3, arg.getComprador());
			ps.setBoolean(4, arg.isAtivo());
			ps.setInt(5, arg.getIdLeiloeiro());
			ps.setFloat(6, arg.getLanceInicial());
			ps.setInt(7,arg.getTempoLimite());
			ps.setString(8, arg.getNickname());
			ps.setFloat(9, arg.getPrecolote());
			ps.setString(10, arg.getTermino());			
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
		List<Leilao> list = new ArrayList<Leilao>();
		Leilao temp;
		try {
			PreparedStatement ps = c.prepareStatement("select * from leilao");
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
				temp.setPrecolote(result.getFloat("precolote"));
				temp.setTermino(result.getString("termino"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCLeilaoDAO", e); 
		}
		return list;
	}

	@Override
	public Leilao select(int pk) {
		Leilao temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from leilao where idleilao = ?");
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
				temp.setPrecolote(result.getFloat("precolote"));
				temp.setTermino(result.getString("termino"));
				break;
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCLeilaoDAO", e); 
		}
		return temp;
	}

	public List<Leilao> getHistorico(int pk) {
		List<Leilao> list = new ArrayList<Leilao>();
		Leilao temp;
		try {
			PreparedStatement ps = c.prepareStatement("select * from leilao where idleiloeiro = ? and ativo=false");
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
				temp.setPrecolote(result.getFloat("precolote"));
				temp.setTermino(result.getString("termino"));
				list.add(temp);
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao listar dados. Classe JDBCLeilaoDAO", e); 
		}
		return list;
	}
	
	public Leilao searchEtiqueta(String etiqueta) {
		Leilao temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from leilao where etiqueta= ? ");
			ps.setString(1,etiqueta);
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
				temp.setPrecolote(result.getFloat("precolote"));
				temp.setTermino(result.getString("termino"));
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
					"etiqueta = ?,descricao = ?, comprador = ?, ativo = ?, idleiloeiro = ?, lanceinicial = ?, tempolimite = ?, nickname = ?, precolote = ?, termino = ? " +
					"where idleilao = ?";
			try {
				PreparedStatement ps = c.prepareStatement(sql);
				ps.setString(1,arg.getEtiqueta());
				ps.setString(2, arg.getDescricao());
				ps.setInt(3, arg.getComprador());
				ps.setBoolean(4, arg.isAtivo());
				ps.setInt(5, arg.getIdLeiloeiro());
				ps.setFloat(6, arg.getLanceInicial());
				ps.setInt(7,arg.getTempoLimite());
				ps.setString(8, arg.getNickname());
				ps.setFloat(9, arg.getPrecolote());
				ps.setString(10, arg.getTermino());
				ps.setInt(11, arg.getIdLeilao());
				ps.execute();
				ps.close();
			
			} catch (SQLException e) {
				throw new RuntimeException("Erro ao atualizar dados. Classe JDBCLeilaoDAO", e); 
			}
	}

}
