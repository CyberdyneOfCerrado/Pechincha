// Espero que isto aqui me ajude a upar arquivos http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm

package module1.pechincha.useCases;

import java.util.Iterator;

import org.apache.commons.fileupload.FileItem;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCCategoriaProdutoDAO;
import module1.pechincha.cruds.JDBCImagemDAO;
import module1.pechincha.cruds.JDBCProdutoDAO;
import module1.pechincha.model.CategoriaProduto;
import module1.pechincha.model.Imagem;
import module1.pechincha.model.Produto;
import module1.pechincha.util.ActionDone;
import module1.pechincha.util.DoAction;
import module2.pechincha.util.GetFileUpload;

public class ManterProdutos  extends ModelController{
	public ActionDone novo ( DoAction da ){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction("resultado");
		ad.setUseCase(da.getUseCase());
		
		Produto prod = new Produto();
		prod.setTitulo((String)da.getData("titulo"));
		prod.setDescricao((String)da.getData("descricao"));
		try{
			prod.setPreco(Float.valueOf((String)da.getData("preco")));
			prod.setQuantidade(Integer.valueOf((String)da.getData("quantidade")));
			prod.setFkUsuario(Integer.valueOf((String)da.getData("idusuario")));
		}
		catch(Exception e){
		}
		JDBCProdutoDAO manterproduto = new JDBCProdutoDAO();
		if ( manterproduto.validar(prod)){
			int pkprodinsert = manterproduto.insertReturningPk(prod);
			
			@SuppressWarnings("unchecked")
			Iterator<FileItem> files = (Iterator<FileItem>)da.getData("fileItem");
			GetFileUpload fup = new GetFileUpload();
			String path = (String)da.getData("storageContext"),
					separador = (String)da.getData("pathSeparador");
			path += separador + "imagens";
			fup.setPath(path);
			fup.setSeparador(separador);
			int cont=0;
			while ( files.hasNext () ){
				FileItem fi = files.next();
				if ( fi.getSize() > fup.getMaxSizeAllowed()){
					//erro
					break;
				}
				String formato = fi.getName().substring(fi.getName().lastIndexOf("."));
				Imagem img = new Imagem(0, pkprodinsert, formato);
				JDBCImagemDAO insimg = new JDBCImagemDAO();
				int pknewimg = insimg.insertReturningPk(img);
				img.setPk(pknewimg);
				long sizeread = fup.saveFile(files.next(), Integer.toString(pknewimg));
				if ( sizeread == -1 || sizeread > fup.getMaxSizeAllowed()){
					insimg.delete(pknewimg);
					break;
				}
				cont++;
				if ( cont == 5){
					break;
				}
			}
			if ( cont == 0 ){
				manterproduto.delete(pkprodinsert);
				//erro
			}
		}
		ad.setMessage("Processado");
		ad.setStatus(true);
		ad.setProcessed(true);

		return ad;
	};
	
	public ActionDone editar ( DoAction da ){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setStatus(true);
		ad.setProcessed(true);
		
		Produto prod = new Produto();
		prod.setTitulo((String)da.getData("titulo"));
		prod.setDescricao((String)da.getData("descricao"));
		try{
			prod.setPreco(Float.valueOf((String)da.getData("preco")));
			prod.setQuantidade(Integer.valueOf((String)da.getData("quantidade")));
			prod.setFkUsuario(Integer.valueOf((String)da.getData("idusuario")));
			
			JDBCProdutoDAO manterproduto = new JDBCProdutoDAO();
			if ( manterproduto.validar(prod)){
				int pkprodinsert = manterproduto.insertReturningPk(prod);
				
				String[] cats = (String[])da.getData("categorias");
				
				if ( cats.length == 0 ){
					ad.setMessage("Nenhuma categoria.");
					return ad;
				}
				JDBCCategoriaProdutoDAO catprod = new JDBCCategoriaProdutoDAO();
				for (String str : cats){
					catprod.insert(new CategoriaProduto(0, Integer.valueOf(str), pkprodinsert));
				}
				
				@SuppressWarnings("unchecked")
				Iterator<FileItem> files = (Iterator<FileItem>)da.getData("fileItem");
				GetFileUpload fup = new GetFileUpload();
				String path = (String)da.getData("storageContext"),
						separador = (String)da.getData("pathSeparador");
				path += separador + "imagens";
				fup.setPath(path);
				fup.setSeparador(separador);
				int cont=0;
				while ( files.hasNext () ){
					FileItem fi = files.next();
					if ( fi.getSize() > fup.getMaxSizeAllowed()){
						ad.setMessage("Arquivo maior do que o tamanho permitido.");
						return ad;
					}
					String formato = fi.getName().substring(fi.getName().lastIndexOf("."));
					Imagem img = new Imagem(0, pkprodinsert, formato);
					JDBCImagemDAO insimg = new JDBCImagemDAO();
					int pknewimg = insimg.insertReturningPk(img);
					img.setPk(pknewimg);
					long sizeread = fup.saveFile(files.next(), Integer.toString(pknewimg) + "." + formato);
					if ( sizeread == -1 || sizeread > fup.getMaxSizeAllowed()){
						insimg.delete(pknewimg);
						ad.setMessage("Erro ao realizar upload.");
						return ad;
					}
					cont++;
					if ( cont == 5){
						break;
					}
				}
				if ( cont == 0 ){
					manterproduto.delete(pkprodinsert);
					ad.setMessage("Erro (nenhuma imagem adicionada)");
					return ad;
					//erro
				}
			}
		}
		catch(Exception e){
			ad.setMessage("Falha ao converter valores.");
			return ad;
		}
		
		ad.setMessage("Foi.");
		return ad;
	};
	
	public ActionDone remover ( DoAction da ){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setMessage("O Lula só tem quatro dedos em uma das mãos.");
		ad.setStatus(true);
		ad.setProcessed(true);
		return ad;
	};
	
	public ActionDone buscar ( DoAction da ){
		ActionDone ad = new ActionDone();
		
		//Identificando o pacote
		ad.setAction(da.getAction());
		ad.setUseCase(da.getUseCase());
		ad.setMessage("O Lula só tem quatro dedos em uma das mãos.");
		ad.setStatus(true);
		ad.setProcessed(true);
		return ad;
	};
	
	@Override
	public String[] getActions() {
		String[] actions = {
			"novo",
			"editar",
			"remover",
			"buscar"
		}; 
		return actions;
	}

	@Override
	public String getUserCase() {
		return "manterProdutos";
	}

}
