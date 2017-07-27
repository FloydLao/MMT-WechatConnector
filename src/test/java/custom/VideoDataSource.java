/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package custom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class VideoDataSource implements DataSource {
	
	private File file = null;
	
	public VideoDataSource( File file ) {
	    this.file = file;
	}//cons

	@Override
	public String getContentType() {
		return "video/mp4";
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new java.io.FileInputStream(file);
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new IOException();
	}

}
