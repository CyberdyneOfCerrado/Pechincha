package module1.pechincha.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import module1.pechincha.controllers.UseCaseController;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module1.pechincha.viewUseCases.AmbienteLeilaoView;
import module1.pechincha.viewUseCases.GerenciarLeilaoView;
import module1.pechincha.viewUseCases.HomeView;
import module1.pechincha.viewUseCases.ManterGalinhaView;
import module1.pechincha.viewUseCases.ManterProdutosView;
import module1.pechincha.viewUseCases.ManterUsuarioView;
import biz.source_code.miniTemplator.MiniTemplator;
import biz.source_code.miniTemplator.MiniTemplator.TemplateSyntaxException;

//Cria o pacote geral de comunicação da aplicação 'DoAction' e gerencia as classes geradoreas de conteúdo.
public class ServletController {
	private static String servletContext;
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
		listViews.put("ambienteLeilao", new AmbienteLeilaoView(servletContext, "ambienteLeilao"));
		listViews.put("manterUsuario", new ManterUsuarioView(servletContext, "manterUsuario"));
		listViews.put("gerenciarLeilao", new GerenciarLeilaoView(servletContext, "gerenciarLeilao"));
		listViews.put("manterGalinha", new ManterGalinhaView(servletContext, "manterGalinha"));
		listViews.put("manterProdutos", new ManterProdutosView(servletContext, "manterProdutos"));
		listViews.put("home", new HomeView(servletContext, "home"));
	};

	private String init() throws TemplateSyntaxException, IOException {
		MiniTemplator index = null;
		index = new MiniTemplator(servletContext + "index.html");
		index.setVariable("conteudo", " ");
		return index.generateOutput();
	};

	// Processa os dados da Servlet.
	public String process(HttpServletRequest request) throws TemplateSyntaxException, IOException {
		DoAction da = makeDoAction(request); // convertendo o Request em
												// DoAction
		ActionDone ad = null;
		if (da.getAction() == null) {
			return init();// cria uma tela vazia
		} else {
			// Se ação informada for do tipo 'redirect=true' os dados não devem
			// ser enviados p/ o UseCaseController
			if (!Boolean.valueOf((String) da.getData("redirect"))) {
				ad = ucc.chooseUserCase(da);
				ad.setData("redirect", "false");
			} else {
				ad = new ActionDone(da.getUseCase(), da.getAction(), da.getHashtable());// copiando
																						// o
																						// DAp/o
																						// AC
			}
		}
		return readActionDone(ad);// abre o pacote de ação concluída e o manda
									// p/ classe especialista
	};

	// Cria um pacote DoAction
	private DoAction makeDoAction(HttpServletRequest request) {
		// 1 pegando o nome do caso de uso e a respectiva ação.
		// useCase e Action são atributos estáticos em qualquer formulário
		DoAction da;

		// Verificação de segurança;
		String security = (String) request.getAttribute("security");
		String useCase, action;

		if (security.equals("true")) {
			useCase = "manterUsuario";
			action = "login";
			da = new DoAction(useCase, action);
			da.setData("redirect", "true");
			return da;
		} else {
			useCase = request.getParameter("useCase");
			action = request.getParameter("action");
		}

		da = new DoAction(useCase, action);

		// Pegando todos os parâmetros adicionados, exceto pelo useCase e
		// action;
		Enumeration<String> valuesName = request.getParameterNames();

		while (valuesName.hasMoreElements()) {
			String temp = valuesName.nextElement();
			if (!temp.equals("useCase") && !temp.equals("action")) {
				// setando o nome do parametro como chave na hashtable setando o
				// nome do parametro como valor
				da.setData(temp, request.getParameter(temp));
				da.setData(temp + "_array", request.getParameterValues(temp));
			}
		}
		// Pegando dados de Sessão
		da.setData("Session", request.getSession());

		// Check that we have a file upload request
		if (ServletFileUpload.isMultipartContent(request)) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();

				factory.setSizeThreshold(this.maxMemSize);
				// Location to temporary save data that is larger than
				// maxMemSize.
				factory.setRepository(new File(this.servletContext + separador + "tmp"));

				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);
				// maximum file size to be uploaded.
				upload.setSizeMax(this.maxFileSize);

				// Parse the request to get file items.
				// List<FileItem> fileItems = upload.parseRequest(request);

				// Process the uploaded file items
				FileItemIterator iter = upload.getItemIterator(request);

				while (iter.hasNext()) {
					FileItemStream fi = iter.next();
					InputStream is = fi.openStream();
					if (fi.isFormField()) {
						System.out.println(fi.getContentType());

						if (da.getData(fi.getFieldName()) != null) {
							da.setData(fi.getFieldName(), ((String) da.getData(fi.getFieldName())) + "," + Streams.asString(is));
						} else {
							da.setData(fi.getFieldName(), Streams.asString(is));
						}
					} else {
						ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
						int len;
						byte[] buffer = new byte[8192];
						while ((len = is.read(buffer, 0, buffer.length)) != -1) {
							byteOut.write(buffer, 0, len);
						}
						DoAction file = new DoAction(null, null);
						file.setData("file", fi);
						file.setData("data", byteOut);
						da.setData(fi.getFieldName(), file);
					}
				}
			} catch (SizeLimitExceededException e) {
				da.setData("exception", "Tamanho máximo excedido");
				return da;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		da.setData("storageContext", this.servletContext);
		da.setData("pathSeparador", separador);

		return da;
	}

	// Interpreta os dados do pacote ActionDone.
	private String readActionDone(ActionDone ad) {

		String conteudo = null;
		// gerando o conteúdo.
		ViewController view = listViews.get(ad.getUseCase());

		conteudo = view.choose(ad);
		// Fixando conteúdo na index.

		if (ad.getData("index") == null) {
			MiniTemplator index = null;
			try {
				System.out.println(servletContext + "index.html");
				index = new MiniTemplator(servletContext + "index.html");
			} catch (TemplateSyntaxException | IOException e) {
				e.printStackTrace();
			}
			index.setVariable("conteudo", conteudo);
			return index.generateOutput();
		} else
			return conteudo;
	};

	public static String getServletContext() {
		return servletContext;
	}
}
