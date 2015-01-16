/**
 * 
 */
package module1.pechincha.cruds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.Usuario;
import module1.pechincha.util.ConnectionFactory;

/**
 * @author ErIcK
 *
 */
public class JDBCUsuarioDAO extends DAOBehavior<Usuario> {
	private Connection c;
	public JDBCUsuarioDAO(){
		  c = ConnectionFactory.getConnection();
		 };
	@Override
	public void insert(Usuario arg) {
		String sql = "Insert into usuario (etiqueta,descricao,comprador,ativo,idLeiloeiro,lanceInicial,tempoLimite,nickname,precolote,termino) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) "; 
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

	@Override
	public void delete(int pk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Usuario> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario select(int pk) {
		Usuario temp=null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from usuario where pk = ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			temp = new Usuario();
			if(result.next()){
				temp.setPk(result.getInt("pk"));
				temp.setNomeCompleto(result.getString("nomecompleto"));
				temp.setNickname(result.getString("nickname"));
				temp.setSenha(result.getString("senha"));
				temp.setDataNascimento(result.getString("datanascimento"));
				temp.setEmailPrincipal(result.getString("emailprincipal"));
				temp.setEmailAlternativo(result.getString("emailalternativo"));
				temp.setSkype(result.getString("skype"));
				temp.setTelCelular(result.getString("telcelular"));
				temp.setTelFixo(result.getString("telfixo"));
				result.close();
				ps.close();
			}else{
				System.err.println("O usuário pesquisado não existe");
				temp=null;
			}
		}catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCUsuarioDAO", e); 
		}
		return temp;
	}

	@Override
	public void update(Usuario arg) {
		// TODO Auto-generated method stub
		
	}

}
