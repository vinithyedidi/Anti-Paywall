package antiPaywall;

import java.awt.Desktop;
import java.io.*;
import java.net.*;

/**
 * @author Vinith Yedidi
 * 
 *	Date created: 5/30/20
 *	Version 1.0
 *
 */

public class AntiPaywall {

	URL url;
	File file;
	public AntiPaywall(URL url, File file) {
		this.url = url;
		this.file = file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public String getOS() {
		return System.getProperty("os.name").toLowerCase();
	}
	
	/**
	 * @return htmlCode, the complete String containing the HTML code of the website.
	 * @throws IOException
	 */
	public String getHTML() throws IOException {
		String htmlCode = null;
		InputStream stream = url.openConnection().getInputStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String nextLine = null;
		while((nextLine = reader.readLine()) != null)  {
		   htmlCode += nextLine + "\n";
		}
		reader.close();
        return htmlCode;
	}
	
	/**
	 * @throws OSException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void createFile() throws OSException, IOException, InterruptedException {
		
		if (getOS().indexOf("win") >= 0) {
			String tempPath = "C:\\Windows\\Temp\\tempFile.html";
			File file = new File(tempPath);
			file.createNewFile();
			setFile(file);
		} else if (getOS().indexOf("mac") >= 0) {
			Process process = Runtime.getRuntime().exec("pwd");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String tempPath = reader.readLine() + "/tempFile.html";
			System.out.println(tempPath);
			File file = new File(tempPath);
			file.createNewFile();
			setFile(file);
		} else {
			throw new OSException();
		}

	    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	    writer.write(this.getHTML());
	    writer.close();
	}
	
	/**
	 * @throws IOException
	 */
	public void openFile() throws IOException {
		Desktop.getDesktop().open(file);
	}
}
