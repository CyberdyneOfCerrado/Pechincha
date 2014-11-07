package module2.pechincha.util;

import java.io.File;
import org.apache.commons.fileupload.FileItem;

public class GetFileUpload {
	private String path = null, separador = null;
	private long maxSizeAllowed = 5 * 1024 * 1024;

	public long saveFile(FileItem fi, String name) {
		if (!fi.isFormField()) {
			// Get the uploaded file parameters
			long sizeInBytes = fi.getSize();
			if (sizeInBytes > this.maxSizeAllowed) {
				return sizeInBytes;
			}
			try {
				File file = new File(this.path + this.separador + name);
				fi.write(file);
				return sizeInBytes;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
		}
		return -1;
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
