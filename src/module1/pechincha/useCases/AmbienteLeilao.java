package module1.pechincha.useCases;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCImagemDAO;
import module1.pechincha.cruds.JDBCLeilaoDAO;
import module1.pechincha.cruds.JDBCLoteProdutoDAO;
import module1.pechincha.model.Imagem;
import module1.pechincha.model.Produto;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;

public class AmbienteLeilao extends ModelController {

	public ActionDone ambiente(DoAction da) {

		ActionDone ad = new ActionDone();
		JDBCLeilaoDAO daoLeilao = new JDBCLeilaoDAO();
		// Verificar aqui se o usuário que requisita a o ambiente é o leiloeiro
		// da mesma;
		// Nota: só haverá id na sessão se o mesmo já estiver passado pelo
		// processo de login;

		HttpSession s = (HttpSession) da.getData("Session");

		ad.setData("userName", s.getAttribute("nickName"));
		ad.setData("idEmissor", s.getAttribute("id"));
		ad.setData("idLeilao", da.getData("idLeilao"));

		int idEmissor = Integer.parseInt(ad.getData("idEmissor").toString());
		int idLeilao = Integer.parseInt(ad.getData("idLeilao").toString());

		ad.setData("isLeiloeiro", daoLeilao.verificarProprietarioLeilao(idEmissor, idLeilao));

		// Pegar imagens dos produtos:
		// 1 Pegar produtos na JDBCLoteProdutoDAO.
		// 2 Pegar as imagens a partir do JDBCImagemDAO via id produto;

		JDBCLoteProdutoDAO loteProdutoDao = new JDBCLoteProdutoDAO();
		JDBCImagemDAO imageDao = new JDBCImagemDAO();
		ArrayList<Imagem> imagens = new ArrayList<>();

		List<Produto> listaProduto = loteProdutoDao.produtosLeilao(Integer.parseInt((String) da.getData("idLeilao")));

		for (Produto p : listaProduto) {
			List<Imagem> listaImagem = imageDao.list(p.getPk());
			for (Imagem a : listaImagem) {
				imagens.add(a);
			}
		}

		ad.setData("imagens", imagens);

		// Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		return ad;
	};

	@Override
	public String[] getActions() {
		String[] actions = {"ambiente"};
		return actions;
	};

	@Override
	public String getUserCase() {
		return "ambienteLeilao";
	};

}
