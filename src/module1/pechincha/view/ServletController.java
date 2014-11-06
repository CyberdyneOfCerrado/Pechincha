package module1.pechincha.view;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import module1.pechincha.controllers.UseCaseController;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module1.pechincha.viewUseCases.AmbienteLeilaoView;
import module1.pechincha.viewUseCases.ManterGalinhaView;
import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.TemplateSyntaxException;

//Cria o pacote geral de comunicação da aplicação 'DoAction' e gerencia as classes geradoreas de conteúdo.
public class ServletController {
	private String servletContext;
	private UseCaseController ucc;
	private Hashtable<String, ViewController> listViews;
	String separador;
	private int maxFileSize = 5 * 1024 * 1024;
	private int maxMemSize = 4 * 1024;

	public ServletController(String servletContext) {
		separador = System.getProperty("file.separator");
		this.servletContext = servletContext + separador + "templates" + separador;
		this.ucc = new UseCaseController();
		this.listViews = new Hashtable<>();
		initViews();
	};

	// Adicionar todas as classes de gerenciamento de conteúdo aqui.
	private void initViews() {
		listViews.put("ambienteLeilao", new AmbienteLeilaoView(servletContext,
				"ambienteLeilao"));
		listViews.put("manterGalinha", new ManterGalinhaView(servletContext,
				"manterGalinha"));
	};

	private String init() throws TemplateSyntaxException, IOException {
		MiniTemplator index = null;
		index = new MiniTemplator(servletContext + "index.html");
		index.setVariable("conteudo", " ");
		return index.generateOutput();
	};

	// Processa os dados da Servlet.
	public String process(HttpServletRequest request)
			throws TemplateSyntaxException, IOException {
		DoAction da = makeDoAction(request); // convertendo o Request em
												// DoAction
		ActionDone ad = null;
		if (da.getAction() == null) {
			return init();// cria uma tela vazia
		} else {
			// Se ação informada for do tipo 'redirect=true' os dados não devem
			// ser enviados p/ o UseCaseController
			if (da.getData("redirect").equals("false")) {
				ad = ucc.chooseUserCase(da);
				ad.setData("redirect", "false");
			} else {
				ad = new ActionDone(da.getUseCase(), da.getAction(),
						da.getHashtable());// copiando o DA p/ o AC
			}
		}
		return readActionDone(ad);// abre o pacote de ação concluída e o manda
									// p/ classe especialista
	};

	// Cria um pacote DoAction
	private DoAction makeDoAction(HttpServletRequest request) {
		// 1 pegando o nome do caso de uso e a respectiva ação.
		// useCase e Action são atributos estáticos em qualquer formulário
		String useCase = request.getParameter("useCase");
		String action = request.getParameter("action");
		DoAction da;

		da = new DoAction(useCase, action);

		// Pegando todos os parâmetros adicionados, exceto pelo useCase e
		// action;
		Enumeration<String> valuesName = request.getParameterNames();

		while (valuesName.hasMoreElements()) {
			String temp = valuesName.nextElement();
			if (!temp.equals("useCase") && !temp.equals("action")) {
				da.setData(temp, request.getParameter(temp));// setando o nome
																// do parametro
																// como chave na
																// hashtable
																// setando o
																// nome do
																// parametro
																// como valor
			}
		}
		// Pegando dados de Sessão
		da.setData("Session", request.getSession());

		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();

				factory.setSizeThreshold(this.maxMemSize);
				// Location to save data that is larger than maxMemSize.
				factory.setRepository(new File(this.servletContext + separador + "tmp"));

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);
				// maximum file size to be uploaded.
				upload.setSizeMax(this.maxFileSize);

				// Parse the request to get file items.
				List<FileItem> fileItems = upload.parseRequest(request);

				// Process the uploaded file items
				Iterator<FileItem> i = fileItems.iterator();

				da.setData("fileItem", i);
				da.setData("imageStorageContext", this.servletContext);
			} catch (Exception e) {

			}
		}
		return da;
	}

	// Interpreta os dados do pacote ActionDone.
	private String readActionDone(ActionDone ad) {

		String conteudo = null;
		// gerando o conteúdo.
		ViewController view = listViews.get(ad.getUseCase());

		conteudo = view.choose(ad);

		// Fixando conteúdo na index.
		MiniTemplator index = null;
		try {
			System.out.println(servletContext + "index.html");
			index = new MiniTemplator(servletContext + "index.html");
		} catch (TemplateSyntaxException | IOException e) {
			e.printStackTrace();
		}
		index.setVariable("conteudo", conteudo);
		return index.generateOutput();
	};
}
