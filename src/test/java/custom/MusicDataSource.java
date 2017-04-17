package custom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class MusicDataSource implements DataSource {
	
	private File file = null;
	
	public MusicDataSource( File file ) {
	    this.file = file;
	}//cons

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return "audio/mp3";
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return new java.io.FileInputStream(file);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return file.getName();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		throw new IOException();
	}

}
