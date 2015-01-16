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
	 * Este método inicializa um ambiente de leilão a partir de um modelo de
	 * leilão. O parêmetro leilão deve estar devidamente inicializado.
	 * 
	 * @author Allyson Maciel Guimarães
	 * @param Uma
	 *            variável Leilão
	 * @return void
	 */
	public static void iniciarAmbienteLeilao(Leilao leilao) {
		ms.createNewManagerLeilao(leilao);
	};

	public static Collection<ManagerLeilao> getMetadataEnvironments() {
		return ms.getMetadata();
	};
}
