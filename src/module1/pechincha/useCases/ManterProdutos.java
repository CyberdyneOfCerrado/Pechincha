// Espero que isto aqui me ajude a upar arquivos http://www.tutorialspoint.com/servlets/servlets-file-uploading.htm

package module1.pechincha.useCases;

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
				
				GetFileUpload fup = new GetFileUpload();
				String path = (String)da.getData("storageContext"),
						separador = (String)da.getData("pathSeparador");
				path += separador + "imagens";
				fup.setPath(path);
				fup.setSeparador(separador);
				int cont=0;
				
				ArrayList<FileItemStream> imagens = new ArrayList<FileItemStream>();
				for (int i = 1; i <= 5; i++){
					FileItemStream img = (FileItemStream)da.getData("imagem" + i);
					
					if (img != null && !img.isFormField() && !img.getName().isEmpty()){
						imagens.add(img);
					}
				}
				
				if ( imagens.size() == 0){
					manterproduto.delete(pkprodinsert);
					ad.setMessage("Nenhuma imagem.");
					return ad;
				}
				
				for (FileItemStream img : imagens){
					System.out.println(img.getName());
					String formato = img.getName().substring(img.getName().lastIndexOf(".")+1);
					System.out.println(formato);
					Imagem imag = new Imagem(0, pkprodinsert, formato);
					JDBCImagemDAO insimg = new JDBCImagemDAO();
					int pknewimg = insimg.insertReturningPk(imag);
					imag.setPk(pknewimg);
					
					System.out.println("PK da imagem: " + pknewimg);
					
					long sizeread = fup.saveFile(img, Integer.toString(pknewimg) + "." + formato);
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
					ad.setMessage("Nenhuma imagem.");
					return ad;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			ad.setMessage("Erro desconhecido ou erro ao converter valores.");
			return ad;
		}
		
		ad.setMessage("Ocorreu tudo bem.");
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
