package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.Lance;
import module1.pechincha.util.ConnectionFactory;

public class JDBCLanceDAO extends DAOBehavior<Lance>{
	private Connection c; 
	
	public JDBCLanceDAO(){
		c = ConnectionFactory.getConnection();
	};
	
	@Override
	public void insert(Lance arg) {
		String sql = "Insert into "+arg.getTableName()+" ("+arg.getColumnName()+") values ( ?, ?, ? )"; 
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1,arg.getIdLeilao());
			ps.setInt(2,arg.getIdUsuario());
			ps.setFloat(3, arg.getLance());
			ps.execute(); 
			ps.close();
			
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCPessoaDAO", e); 
		}
	};

	@Override
	public void delete(int pk) {
		try {
			PreparedStatement ps = c.prepareStatement("delete from lance where pk = ?");
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCLanceDAO", e); 
		}
	};

	@Override
	public List<Lance> list() {
		List<Lance> list = new ArrayList<Lance>();
		
		try {
			PreparedStatement ps = c.prepareStatement("select * from lance");
			ResultSet result = ps.executeQuery();
			while(result.next()){
				Lance temp = new Lance();
				temp.setIdLeilao(result.getInt("idleilao"));
				temp.setIdusuario(result.getInt("idusuario"));
				temp.setLance(result.getFloat("lance"));
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
	public Lance search(int pk) {
		Lance temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from lance where idleilao = ? and lance = (select max(lance) from lance where idleilao = ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			temp = new Lance(); 
			while(result.next()){
				temp.setIdLeilao(result.getInt("idleilao"));
				temp.setIdusuario(result.getInt("idusuario"));
				temp.setLance(result.getFloat("lance"));
			}
			result.close();
			ps.close();
		
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCLanceDAO", e); 
		}
		return temp;
	};

	@Override
	public void update(Lance arg) {
		
	};
}
