package module1.pechincha.useCases;

import module1.pechincha.model.Leilao;

public class Mail implements Runnable {
	private Leilao leilao;
	public Leilao getLeilao() {
		return leilao;
	}

	public void setLeilao(Leilao leilao) {
		this.leilao = leilao;
	}

	@Override
	public void run() {
	    GerenciarLeilao instancia= new GerenciarLeilao();
	    instancia.enviarEmail(leilao);
	}
}
