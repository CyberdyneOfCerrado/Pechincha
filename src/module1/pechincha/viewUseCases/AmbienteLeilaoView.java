package module1.pechincha.viewUseCases;

import java.util.ArrayList;

import biz.source_code.miniTemplator.MiniTemplator;
import module1.pechincha.model.Imagem;
import module1.pechincha.util.ActionDone;
import module1.pechincha.view.ViewController;

public class AmbienteLeilaoView extends ViewController {

	public AmbienteLeilaoView(String sevletContext, String useCase) {
		super(sevletContext, useCase);
	};

	@Override
	public String choose(ActionDone ad) {
		String resul = null;
		switch (ad.getAction()) {
			case "ambiente" :
				resul = ambiente(ad);
				break;
		}
		return resul;
	};

	private String ambiente(ActionDone ad) {
		String resul = "";
		if (ad.isProcessed()) {
			MiniTemplator temp = super.startMiniTemplator(super.getTemplate(ad));

			temp.setVariable("userName", (String) ad.getData("userName"));
			temp.setVariable("idEmissor", (String) ad.getData("idEmissor"));
			temp.setVariable("idLeilao", (String) ad.getData("idLeilao"));

			boolean isLeiloeiro = Boolean.valueOf(ad.getData("isLeiloeiro").toString());
			// Adicionando imagens ao ambiente:

			ArrayList<Object> imagens = (ArrayList<Object>) ad.getData("imagens");

			for (Object a : imagens) {
				if (a.getClass().getName().contains("Imagem")) {
					Imagem b = (Imagem) a;
					temp.setVariable("img", b.getPk() + "." + b.getFormato());
				} else {
					temp.setVariable("img", (String) a);
				}
				temp.addBlock("ImagemProduto");
			}

			String url = super.getTemplate(ad);

			url = url.substring(0, url.lastIndexOf(super.getSeparador()) + 1);

			System.out.println(url);
			if (isLeiloeiro)
				url += "leiloeiro.html";
			else
				url += "user.html";

			MiniTemplator x = super.startMiniTemplator(url);

			temp.setVariable("botoes", x.generateOutput());
			resul = temp.generateOutput();
		}

		return resul;
	};

}
