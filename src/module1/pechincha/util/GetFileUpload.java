package module1.pechincha.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItemStream;

public class GetFileUpload {
	private String path = null, separador = null;
	private long maxSizeAllowed = 5 * 1024 * 1024;

	public long saveFile(FileItemStream fis, String name) {
		long lido = 0;
		if (!fis.isFormField()) {
			// Get the uploaded file parameters
			InputStream fs;
			try {
				fs = fis.openStream();
				
				FileOutputStream out = new FileOutputStream(new File(path + separador + name));
				int read = 0;
		        final byte[] bytes = new byte[1024];

		        while ((read = fs.read(bytes)) != -1) {
		            out.write(bytes, 0, read);
		            lido += read;
		        }
		        fs.close();
		        out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}			
		}
		else{
			return -1;
		}
		return lido;
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
