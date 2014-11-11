package module1.pechincha.cruds;
import java.util.List;

import module1.pechincha.interf.DAOBehavior;
import module1.pechincha.model.LoteProduto;
import module1.pechincha.model.Produto;
public class JDBCLoteProdutoDAO extends DAOBehavior<LoteProduto> {

	@Override
	public void insert(LoteProduto arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int pk) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<LoteProduto> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoteProduto search(int pk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(LoteProduto arg) {
		// TODO Auto-generated method stub
		
	}
	
	public void produtosLeilao(int idleilao) {
		Produto tempProd;
	}

}
