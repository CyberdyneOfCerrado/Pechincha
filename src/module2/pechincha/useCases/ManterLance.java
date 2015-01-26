package module2.pechincha.useCases;

import module1.pechincha.cruds.JDBCLanceDAO;
import module1.pechincha.model.Lance;


public class ManterLance {
	public boolean novoLance(int pkleilao, int pkusuario, float vallance) {
		
		try {
			Lance novo = new Lance(pkleilao, pkusuario, vallance);

			JDBCLanceDAO dao = new JDBCLanceDAO();

			if (dao.validar(novo)) {
				dao.insert(novo);
				return true;
			} 
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
