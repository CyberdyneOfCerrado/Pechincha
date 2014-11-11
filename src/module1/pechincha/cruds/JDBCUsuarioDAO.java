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

/**
 * @author ErIcK
 *
 */
public class JDBCUsuarioDAO extends DAOBehavior<Usuario> {
	private Connection c;
	@Override
	public void insert(Usuario arg) {
		// TODO Auto-generated method stub
		
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
	public Usuario search(int pk) {
		Usuario temp=null;
		try {
			PreparedStatement ps = c.prepareStatement("select * from usuario where pk = ?");
			ps.setInt(1,pk);
			ResultSet result = ps.executeQuery();
			temp = new Usuario();
			if(result != null){
				temp.setPk(result.getInt("pk"));
				temp.setNomeCompleto(result.getString("nomecompleto"));
				temp.setCpf(result.getString("cpf"));
				temp.setNomeApresentação(result.getString("nomeapresentacao"));
				temp.setNickname(result.getString("nickname"));
				temp.setSenha(result.getString("senha"));
				temp.setDataNascimento(result.getString("datanascimento"));
				temp.setEndereco(result.getString("endereco"));
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
