// Espero que isto aqui me ajude a upar arquivos http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm

package module1.pechincha.useCases;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItemStream;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCCategoriaDAO;
import module1.pechincha.cruds.JDBCCategoriaProdutoDAO;
import module1.pechincha.cruds.JDBCImagemDAO;
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
			ad.setMessage("Nenhuma categoria.");
			return ad;
		}
		String resp = "";
		
		try{
			prod.setPreco(Float.valueOf((String)da.getData("preco")));
			prod.setQuantidade(Integer.valueOf((String)da.getData("quantidade")));
			prod.setFkUsuario(Integer.valueOf((String)da.getData("idusuario")));
					
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
					ad.setMessage("Nenhuma imagem.");
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
						ad.setMessage("Dados inconsistentes.");
						manterproduto.delete(pkprodinsert);
						return ad;
					}
					int pknewimg = insimg.insertReturningPk(imag);
					imag.setPk(pknewimg);
					
					System.out.println("PK da imagem: " + pknewimg);
					
					long sizeread = fup.saveFile(baos, Integer.toString(pknewimg) + "." + formato);
					if ( sizeread == -1 || sizeread > fup.getMaxSizeAllowed()){
						insimg.delete(pknewimg);
					}else{
						
						resp += "<br/><a href=\"templates/imagens/" + pknewimg + "." + formato + "\" target=\"_blank\">Clique aqui para visualizar a imagem.</a>";
						cont++;
					}
					if ( cont == 5){
						break;
					}
				}
			
				if ( cont == 0 ){
					manterproduto.delete(pkprodinsert);
					ad.setMessage("Nenhuma imagem.");
					return ad;
				}
				JDBCCategoriaProdutoDAO daoprod = new JDBCCategoriaProdutoDAO(); 
				String[] cats = cat.split(",");
				for(String i : cats){
					daoprod.insert(new CategoriaProduto(0,Integer.valueOf(i),pkprodinsert));
				}
				ad.setMessage("Ocorreu tudo bem." + resp);
			}
			else{
				ad.setMessage("Dados inconsistentes.");
				return ad;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			ad.setMessage("Erro desconhecido ou erro ao converter valores.");
			return ad;
		}
		
		return ad;
	};
	
	public ActionDone editar ( DoAction da ){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);

		return ad;
	};
	
	public ActionDone remover ( DoAction da ){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		try{
			int idprod = Integer.valueOf((String)da.getData("idproduto"));
			JDBCImagemDAO delimgs = new JDBCImagemDAO();
			delimgs.deleteFromFKProduto(idprod);
			
			JDBCProdutoDAO manterproduto = new JDBCProdutoDAO();
			manterproduto.delete(idprod);
		}catch(Exception e){
			
		}				
		return ad;
	};
	
	public ActionDone listar ( DoAction da ){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		try{
			int usuario = Integer.valueOf((String)da.getData("idusuario"));
			List<Produto> prods = new JDBCProdutoDAO().list(usuario);
			
			List<Categoria> listcats = new JDBCCategoriaDAO().list();
			ad.setData("categorias", listcats);
			
			ArrayList<DoAction> list = new ArrayList<DoAction>();
			for (Produto pr : prods){
				DoAction prod = new DoAction(null, null);
				prod.setData("idusuario", usuario);
				prod.setData("idproduto", pr.getPk());
				prod.setData("titulo", pr.getTitulo());
				prod.setData("descricao", pr.getDescricao());
				prod.setData("preco", pr.getPreco());
				
				Imagem img = new JDBCImagemDAO().list(pr.getPk()).get(0);
				prod.setData("img", img.getPk() + "." + img.getFormato());
				list.add(prod);
			}
			ad.setData("produtos", list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return ad;
	};
	
	public ActionDone visualizar ( DoAction da ){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		try{
			int idusuario = Integer.valueOf((String)da.getData("idusuario")),
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
