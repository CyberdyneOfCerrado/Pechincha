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
	public JDBCUsuarioDAO() {
		c = ConnectionFactory.getConnection();
	};
	@Override
	public void insert(Usuario arg) {
	}

	public int verifyLogin(Usuario us) {
		String sql = "select pk from usuario where  senha = ? and  emailprincipal = ?";

		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, us.getSenha());
			ps.setString(2, us.getEmailPrincipal());
			ResultSet result = ps.executeQuery();

			int pk = -1;
			while (result.next()) {
				pk = result.getInt("pk");
				break;
			}
			if (pk != -1)
				return pk;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	};

	public int insertReturningPk(Usuario arg) {
		String sql = "insert into usuario (nomecompleto,nickname,senha,datanascimento,emailprincipal,emailalternativo,skype,telcelular,telfixo) values(?,?,?,?,?,?,?,?,?) returning pk";
		try {
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, arg.getNomeCompleto());
			ps.setString(2, arg.getNickname());
			ps.setString(3, arg.getSenha());
			ps.setString(4, arg.getDataNascimento());
			ps.setString(5, arg.getEmailPrincipal());
			ps.setString(6, arg.getEmailAlternativo());
			ps.setString(7, arg.getSkype());
			ps.setString(8, arg.getTelCelular());
			ps.setString(9, arg.getTelFixo());
			ResultSet result = ps.executeQuery();
			int pk = 0;
			while (result.next()) {
				pk = result.getInt("pk");
				break;
			}
			result.close();
			ps.close();
			return pk;
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir dados. Classe JDBCUsuarioDAO", e);
		}
	}

	@Override
	public void delete(int pk) {
		String sql = "delete from usuario where pk=?";
		PreparedStatement ps;
		try {
			ps = c.prepareStatement(sql);
			ps.setInt(1,pk);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao deletar dados. Classe JDBCLoteUsuarioDAO", e); 
		}
		
	}

	@Override
	public List<Usuario> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario select(int pk) {
		Usuario temp = null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from usuario where pk = ?");
			ps.setInt(1, pk);
			ResultSet result = ps.executeQuery();
			temp = new Usuario();
			if (result.next()) {
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
			} else {
				System.err.println("O usuário pesquisado não existe");
				temp = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao procurar dados. Classe JDBCUsuarioDAO", e);
		}
		return temp;
	}

	@Override
	public void update(Usuario arg) {
		// TODO Auto-generated method stub

	}

}
