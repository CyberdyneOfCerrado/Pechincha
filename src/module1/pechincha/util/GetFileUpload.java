package module1.pechincha.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GetFileUpload {
	private String path = null, separador = null;
	private long maxSizeAllowed = 5 * 1024 * 1024;
	
	public long saveFile(ByteArrayOutputStream baos, String name) {
		long tam = baos.size();
		
		if ( tam > this.maxSizeAllowed){
			return tam;
		}
			try {
				FileOutputStream out = new FileOutputStream(new File(path + separador + name));
	            out.write(baos.toByteArray(), 0, baos.size());
		        out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}			

		return tam;
	}
	public void setPath(String path){
		this.path = path;
	}
	public void setSeparador(String separador){
		this.separador = separador;
	}
	public void setMaxSizeAllowed(long maxsize){
		this.maxSizeAllowed = maxsize;
	}
	public String getPath(){
		return this.path;
	}
	public String getSeparador(){
		return this.separador;
	}
	public long getMaxSizeAllowed(){
		return this.maxSizeAllowed;
	}
}
