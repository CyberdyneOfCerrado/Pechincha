package module2.pechincha.manager;

import java.util.Collection;

import module1.pechincha.model.Leilao;
import module2.pechincha.util.Messeger;
import module2.pechincha.util.UserSession;

public class StorageLeilaoEnvironments {
	private static StorageLeilaoEnvironments sle;
	private static ManagerStorage ms;

	public StorageLeilaoEnvironments() {
		ms = new ManagerStorage();
	}
	public static void initialize() {
		if (sle == null)
			sle = new StorageLeilaoEnvironments();
	};

	public static void resolverMsg(Messeger messeger) {
		ms.resolverMsg(messeger);// Encaminhando para o ManagerStorege
	};

	public static void addSession(UserSession userSession) {
		ms.addSession(userSession);
	};

	public static void removeSession(UserSession userSession) {
		ms.removeSession(userSession);
	};

	/**
	 * Este m�todo inicializa um ambiente de leil�o a partir de um modelo de
	 * leil�o. O par�metro leil�o deve estar devidamente inicializado.
	 * 
	 * @author Allyson Maciel Guimar�es
	 * @param Uma
	 *            vari�vel Leil�o
	 * @return void
	 */
	public static void iniciarAmbienteLeilao(Leilao leilao) {
		ms.createNewManagerLeilao(leilao);
	};

	public static Collection<ManagerLeilao> getMetadataEnvironments() {
		return ms.getMetadata();
	};
}
