// Espero que isto aqui me ajude a upar arquivos http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm

package module1.pechincha.useCases;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemStream;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCCategoriaDAO;
import module1.pechincha.cruds.JDBCCategoriaProdutoDAO;
import module1.pechincha.cruds.JDBCImagemDAO;
import module1.pechincha.cruds.JDBCLoteProdutoDAO;
import module1.pechincha.cruds.JDBCProdutoDAO;
import module1.pechincha.model.Categoria;
import module1.pechincha.model.CategoriaProduto;
import module1.pechincha.model.Imagem;
import module1.pechincha.model.Produto;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module1.pechincha.util.GetFileUpload;

public class ManterProdutos  extends ModelController{
	public ActionDone novo ( DoAction da ){
		ActionDone ad = new ActionDone();
		HttpSession s = (HttpSession)da.getData("Session");
		ad.setData("Session", s);
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		List<Categoria> listcats = new JDBCCategoriaDAO().list();
		ad.setData("categorias", listcats);
		
		String confirm = (String)da.getData("confirm");
		if ( confirm == null){
			return ad;
		}
		String exception = (String)da.getData("exception");
		if ( exception != null){
			ad.setMessage(exception);
		}
		Produto prod = new Produto();
		prod.setTitulo((String)da.getData("titulo"));
		prod.setDescricao((String)da.getData("descricao"));		
		
		String cat = ((String)da.getData("categoria"));
		
		if ( cat == null ){
			ad.setMessage("Ao menos uma categoria deve ser selecionada!");
			return ad;
		}

		try{
			prod.setPreco(Float.valueOf((String)da.getData("preco")));
			prod.setQuantidade(Integer.valueOf((String)da.getData("quantidade")));
			prod.setFkUsuario(Integer.valueOf(((String)s.getAttribute("id"))));
					
			JDBCProdutoDAO manterproduto = new JDBCProdutoDAO();

			if ( manterproduto.validar(prod)){
				int pkprodinsert = manterproduto.insertReturningPk(prod);
				
				GetFileUpload fup = new GetFileUpload();
				String path = (String)da.getData("storageContext"),
						separador = (String)da.getData("pathSeparador");
				path += separador + "imagens";
				fup.setPath(path);
				fup.setSeparador(separador);
				int cont=0;
				
				ArrayList<DoAction> imagens = new ArrayList<DoAction>();
				for (int i = 1; i <= 5; i++){
					FileItemStream img = (FileItemStream)((DoAction)da.getData("imagem" + i)).getData("file");
					
					if (img != null && !img.getName().isEmpty()){
						imagens.add((DoAction)da.getData("imagem" + i));
					}
				}
				
				if ( imagens.size() == 0){
					manterproduto.delete(pkprodinsert);
					ad.setMessage("Ao menos uma imagem deve ser enviada, com dimens�es entre 480x360 e 1920x1080 nos formatos PNG ou JPG.");
					return ad;
				}
				
				for (DoAction img : imagens){
					ByteArrayOutputStream baos = (ByteArrayOutputStream)img.getData("data");
					
					if ( baos.size() > fup.getMaxSizeAllowed()){
						baos.close();
						continue;
					}
					
					FileItemStream fis = (FileItemStream)img.getData("file");
					String name = fis.getName();
					String formato = name.substring(name.lastIndexOf(".")+1);
					System.out.println(formato);
					Imagem imag = new Imagem(0, pkprodinsert, formato, baos.toByteArray());
					JDBCImagemDAO insimg = new JDBCImagemDAO();
					
					if ( !insimg.validar(imag)){
						ad.setMessage("Ao menos uma imagem deve ser enviada, com dimens�es entre 480x360 e 1920x1080 nos formatos PNG ou JPG e tamanho m�ximo de 5 Mb.");
						manterproduto.delete(pkprodinsert);
						return ad;
					}
					int pknewimg = insimg.insertReturningPk(imag);
					imag.setPk(pknewimg);
					
					long sizeread = fup.saveFile(baos, Integer.toString(pknewimg) + "." + formato);
					if ( sizeread == -1 || sizeread > fup.getMaxSizeAllowed()){
						insimg.delete(pknewimg);
					}else{
						cont++;
					}
					if ( cont == 5){
						break;
					}
				}
			
				if ( cont == 0 ){
					manterproduto.delete(pkprodinsert);
					ad.setMessage("Ao menos uma imagem deve ser enviada, com dimens�es entre 480x360 e 1920x1080 nos formatos PNG ou JPG e tamanho m�ximo de 5 Mb.");
					return ad;
				}
				JDBCCategoriaProdutoDAO daoprod = new JDBCCategoriaProdutoDAO(); 
				String[] cats = cat.split(",");
				for(String i : cats){
					daoprod.insert(new CategoriaProduto(0,Integer.valueOf(i),pkprodinsert));
				}
				
				return listar(da);
			}
			else{
				ad.setMessage("T�tulo do produto deve conter entre 5 e 50 caracteres; "
						+ "valor deve estar entre 0.1 e 1000000; "
						+ "quantidade deve ser entre 1 e 100.");
				return ad;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			ad.setMessage("Erro desconhecido. Por favor, tente novamente.");
			return ad;
		}
	};
	
	public ActionDone editar ( DoAction da ){
		ActionDone ad = new ActionDone();
		HttpSession s = (HttpSession)da.getData("Session");
		ad.setData("Session", s);
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		int idusuario, idproduto;
		
		try{
			idusuario = Integer.valueOf(((String)s.getAttribute("id")));
			idproduto = Integer.valueOf((String)da.getData("idproduto"));
		}catch(Exception e){
			e.printStackTrace();
			ad.setMessage("Erro desconhecido. Por favor, tente novamente.");
			return listar(da);
		}
		
		JDBCLoteProdutoDAO lp = new JDBCLoteProdutoDAO(); 
		if ( lp.existeLote(idproduto)){
			ad.setMessage("Voc� n�o pode editar produto que esteja relacionado a algum lote.");
			ad.setAction("listar");
			return ad;
		}
		
		List<Categoria> listcats = new JDBCCategoriaDAO().list();
		ad.setData("categorias", listcats);
		
		String confirm = (String)da.getData("confirm");
		
		if ( confirm == null){
			try{
				JDBCProdutoDAO daoprod = new JDBCProdutoDAO();
				Produto prod =  daoprod.select(idproduto);
				if ( prod != null){
					if ( prod.getFkUsuario() != idusuario){
						return ad;
					}
					ad.setData("titulo", prod.getTitulo());
					ad.setData("descricao", prod.getDescricao());
					ad.setData("preco", prod.getPreco());
					ad.setData("quantidade", prod.getQuantidade());
					JDBCImagemDAO img = new JDBCImagemDAO();
					List<Imagem> imagens = img.list(idproduto);
					
					ad.setData("imagens", imagens);
				
					List<CategoriaProduto> catprod = new JDBCCategoriaProdutoDAO().list(idproduto);
					ArrayList<Integer> catssel = new ArrayList<Integer>();

					for (CategoriaProduto cp : catprod){
						catssel.add(cp.getFkCategoria());
					}
					ad.setData("catsel", catssel);
				}
			}catch(Exception e){
				e.printStackTrace();
				ad.setMessage("Erro.");
			}
		}else{
			Produto prod = new Produto();
			prod.setTitulo((String)da.getData("titulo"));
			prod.setDescricao((String)da.getData("descricao"));		
			
			String cat = ((String)da.getData("categoria"));
			
			if ( cat == null ){
				ad.setMessage("Ao menos uma categoria deve ser selecionada!");
				return ad;
			}
			
			try{
				prod.setPreco(Float.valueOf((String)da.getData("preco")));
				prod.setQuantidade(Integer.valueOf((String)da.getData("quantidade")));
				prod.setFkUsuario(Integer.valueOf(((String)da.getData("idusuario")).split(",")[0]));
								
				List<String> imgs = null;
					
				String strimgrem = (String)da.getData("imgrem");
				
				if ( strimgrem != null){
					imgs = Arrays.asList(((String)da.getData("imgrem")).split(","));
				}

				JDBCProdutoDAO manterproduto = new JDBCProdutoDAO();
				if ( manterproduto.validar(prod)){
					int pkprodinsert = manterproduto.insertReturningPk(prod);
					
					GetFileUpload fup = new GetFileUpload();
					String path = (String)da.getData("storageContext"),
							separador = (String)da.getData("pathSeparador");
					path += separador + "imagens";
					fup.setPath(path);
					fup.setSeparador(separador);
					int cont=0;
					
					ArrayList<DoAction> imagens = new ArrayList<DoAction>();
					for (int i = 1; i <= 5; i++){
						FileItemStream img = (FileItemStream)((DoAction)da.getData("imagem" + i)).getData("file");
						
						if (img != null && !img.getName().isEmpty()){
							imagens.add((DoAction)da.getData("imagem" + i));
						}
					}
					
					if ( imagens.size() == 0){
						manterproduto.delete(pkprodinsert);
						ad.setMessage("Ao menos uma imagem deve ser enviada, com dimens�es entre 480x360 e 1920x1080 nos formatos PNG ou JPG e tamanho m�ximo de 5 Mb.");
						return ad;
					}
					
					for (DoAction img : imagens){
						ByteArrayOutputStream baos = (ByteArrayOutputStream)img.getData("data");
						
						if ( baos.size() > fup.getMaxSizeAllowed()){
							baos.close();
							continue;
						}
						
						FileItemStream fis = (FileItemStream)img.getData("file");
						String name = fis.getName();
						String formato = name.substring(name.lastIndexOf(".")+1);
						System.out.println(formato);
						Imagem imag = new Imagem(0, pkprodinsert, formato, baos.toByteArray());
						JDBCImagemDAO insimg = new JDBCImagemDAO();
						
						if ( !insimg.validar(imag)){
							ad.setMessage("Ao menos uma imagem deve ser enviada, com dimens�es entre 480x360 e 1920x1080 nos formatos PNG ou JPG e tamanho m�ximo de 5 Mb.");
							manterproduto.delete(pkprodinsert);
							return ad;
						}
						int pknewimg = insimg.insertReturningPk(imag);
						imag.setPk(pknewimg);
						
						long sizeread = fup.saveFile(baos, Integer.toString(pknewimg) + "." + formato);
						if ( sizeread == -1 || sizeread > fup.getMaxSizeAllowed()){
							insimg.delete(pknewimg);
						}else{
							cont++;
						}
						if ( cont == 5){
							break;
						}
					}
				
					if ( cont == 0 ){
						manterproduto.delete(pkprodinsert);
						ad.setMessage("Ao menos uma imagem deve ser enviada, com dimens�es entre 480x360 e 1920x1080 nos formatos PNG ou JPG e tamanho m�ximo de 5 Mb.");
						return ad;
					}
					JDBCCategoriaProdutoDAO daoprod = new JDBCCategoriaProdutoDAO(); 
					String[] cats = cat.split(",");
					for(String i : cats){
						daoprod.insert(new CategoriaProduto(0,Integer.valueOf(i),pkprodinsert));
					}
					
					return listar(da);
				}
				else{
					ad.setMessage("T�tulo do produto deve conter entre 5 e 50 caracteres; "
							+ "valor deve estar entre 0.1 e 1000000.00; "
							+ "quantidade deve ser entre 1 e 100.");
					return ad;
				}
			}
			catch(Exception e){
				e.printStackTrace();
				ad.setMessage("Erro desconhecido. Por favor, tente novamente.");
				return ad;
			}
		}
		
		return ad;
	};
	public ActionDone remover ( DoAction da ){
		ActionDone ad = new ActionDone();
		HttpSession s = (HttpSession)da.getData("Session");
		ad.setData("Session", s);
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		String confirm = (String)da.getData("confirm");
		boolean conf = Boolean.valueOf(confirm);		
		
		try{
			int idusuario = Integer.valueOf(((String)s.getAttribute("id"))),
				idproduto = Integer.valueOf((String)da.getData("idproduto"));
			
			JDBCProdutoDAO daoprod = new JDBCProdutoDAO();
			Produto prod =  daoprod.select(idproduto);
			
			if ( prod == null || prod.getFkUsuario() != idusuario){
				return ad;
			}
			JDBCLoteProdutoDAO lp = new JDBCLoteProdutoDAO(); 
			if ( lp.existeLote(idproduto)){
				ad.setMessage("Voc� n�o pode remover produto que esteja relacionado a algum lote.");
				ad.setAction("listar");
				return ad;
			}

			if ( conf ){
				JDBCImagemDAO imgs = new JDBCImagemDAO();
				List<Imagem> list = imgs.list(idproduto);
				
				String path = (String)da.getData("storageContext"),
						separador = (String)da.getData("pathSeparador");
				path += separador + "imagens";
				
				for (Imagem img : list){
					File file = new File(path + separador + img.getPk() + "." + img.getFormato());					 
		    		file.delete();
				}
				imgs.deleteFromFKProduto(idproduto);				
				new JDBCCategoriaProdutoDAO().deleteFromFKProduto(idproduto);
				daoprod.delete(idproduto);
				ad.setMessage("Produto removido com sucesso.");
				ad.setAction("listar");
				
				return listar(da);
			}else{
				ad.setData("idproduto", da.getData("idproduto"));
				ad.setData("titulo", prod.getTitulo());
				ad.setData("descricao", prod.getDescricao());
				ad.setData("preco", prod.getPreco());
				ad.setData("quantidade", prod.getQuantidade());
				JDBCImagemDAO img = new JDBCImagemDAO();
				List<Imagem> imagens = img.list(idproduto);
					
				for (int i = 0; i < imagens.size(); i++){
					ad.setData("img" + (i+1), imagens.get(i).getPk() + "." + imagens.get(i).getFormato());
				}
			}
		}catch(Exception e){
			
		}				
		return ad;
	};
	
	public ActionDone listar ( DoAction da ){
		ActionDone ad = new ActionDone();
		HttpSession s = (HttpSession)da.getData("Session");
		ad.setData("Session", s);
		//Identificando o pacote
		ad.setAction("listar");
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		try{
			int usuario = Integer.valueOf(((String)s.getAttribute("id")));			
			String[] filtro = (String[])da.getData("categoria_array");
			System.out.println("Categorias: " + filtro);
//			List<Produto> prods = new JDBCProdutoDAO().list(usuario);
			List<Produto> prods = new JDBCCategoriaProdutoDAO().getProdutosByCategorias(filtro,usuario);
			
			List<Categoria> listcats = new JDBCCategoriaDAO().list();
			ad.setData("categorias", listcats);
						
			ArrayList<DoAction> list = new ArrayList<DoAction>();
			for (Produto pr : prods){
				DoAction prod = new DoAction(null, null);
				System.out.println("pk prod :" + pr.getPk());
				prod.setData("idproduto", pr.getPk());
				prod.setData("titulo", pr.getTitulo());
				prod.setData("descricao", pr.getDescricao());
				prod.setData("preco", pr.getPreco());
				
				List<Imagem> imgs = new JDBCImagemDAO().list(pr.getPk());
				if ( imgs.size() > 0){
					Imagem img = imgs.get(0);
					prod.setData("img", img.getPk() + "." + img.getFormato());
					list.add(prod);
				}
			}
			ad.setData("produtos", list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ad;
	};
	
	public ActionDone visualizar ( DoAction da ){
		ActionDone ad = new ActionDone();
		HttpSession s = (HttpSession)da.getData("Session");
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		try{
			int idusuario = Integer.valueOf(((String)s.getAttribute("id"))),
				idproduto = Integer.valueOf((String)da.getData("idproduto"));
			
			JDBCProdutoDAO daoprod = new JDBCProdutoDAO();
			Produto prod =  daoprod.select(idproduto);
			if ( prod != null){
				if ( prod.getFkUsuario() != idusuario){
					return ad;
				}
				ad.setData("titulo", prod.getTitulo());
				ad.setData("descricao", prod.getDescricao());
				ad.setData("preco", prod.getPreco());
				ad.setData("quantidade", prod.getQuantidade());
				JDBCImagemDAO img = new JDBCImagemDAO();
				List<Imagem> imagens = img.list(idproduto);
				
				for (int i = 0; i < imagens.size(); i++){
					ad.setData("img" + (i+1), imagens.get(i).getPk() + "." + imagens.get(i).getFormato());
				}
				String categoria = "";
				List<CategoriaProduto> catprod = new JDBCCategoriaProdutoDAO().list(idproduto);
				
				JDBCCategoriaDAO cat = new JDBCCategoriaDAO();
				for (CategoriaProduto cp : catprod){
					categoria += cat.select(cp.getFkCategoria()).getDescricao() + ", ";
				}
				ad.setData("categorias", categoria.substring(0, categoria.lastIndexOf(",")));
				System.out.println("Processou.");
			}
		}catch(Exception e){
			e.printStackTrace();
			ad.setMessage("Erro.");
		}
		return ad;
	};
	
	@Override
	public String[] getActions() {
		String[] actions = {
			"novo",
			"editar",
			"remover",
			"listar",
			"visualizar"
		}; 
		return actions;
	}

	@Override
	public String getUserCase() {
		return "manterProdutos";
	}

}
