// Espero que isto aqui me ajude a upar arquivos http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm

package module1.pechincha.useCases;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.commons.fileupload.FileItemStream;

import module1.pechincha.controllers.ModelController;
import module1.pechincha.cruds.JDBCImagemDAO;
import module1.pechincha.cruds.JDBCProdutoDAO;
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
