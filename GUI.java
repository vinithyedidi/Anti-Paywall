package antiPaywall;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;
import javax.swing.*;

/**
 * @author Vinith Yedidi
 * 
 *	Date created: 5/30/20
 *	Version 1.1
 *
 */

class OSException extends Exception {}

public class GUI implements ActionListener {

	// used to make the program wait until the JButton is pressed.
	static Semaphore semaphore = new Semaphore(0);
	
	/**
	 * @return url, the complete URL that the user inputs
	 * @throws InterruptedException
	 */
	public String createJFrame() throws InterruptedException {
		
		JFrame frame = new JFrame("Anti-Paywall Machine 1.0");
        
		JTextField textField = new JTextField("Enter URL: ");
		textField.setEditable(true);
		textField.setBounds(50, 40, 250, 40);
		frame.add(textField);
		
		JButton button = new JButton("Submit");
		button.setBounds(350, 40, 100, 40);
		button.addActionListener(this);
		frame.add(button);
		
		frame.setSize(500 , 160); 
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		semaphore.acquire();
		
		String url = textField.getText();
		frame.dispose();
		return url;
		
	}
	
	public static void createErrorFrame(String message) throws InterruptedException {
		
		JFrame frame = new JFrame("Error");
		
		JTextArea displayMessage = new JTextArea(message);
		displayMessage.setBounds(50, 50, 400, 25);
		frame.add(displayMessage);
		
		JTextArea closing = new JTextArea("Closing in 5");
		closing.setBounds(50, 100, 400, 25);
		frame.add(closing);
		
		frame.setSize(500, 200);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread.sleep(1000);
		closing.setText("Closing in 4");
		Thread.sleep(1000);
		closing.setText("Closing in 3");
		Thread.sleep(1000);
		closing.setText("Closing in 2");
		Thread.sleep(1000);
		closing.setText("Closing in 1");
		Thread.sleep(1000);
		closing.setText("Closing in 0");
		frame.dispose();
		
	}
	
	public static void main(String[] args) throws Exception {
		
		
		String url = new GUI().createJFrame();
		AntiPaywall antiPaywall = null;
		
		try {
			
			antiPaywall = new AntiPaywall(new URL(url), null);
		} catch (MalformedURLException e) {
			createErrorFrame("Invalid URL. Please try again. (MalformedURLException)");
			System.exit(1);
		}
		
	 	try {
	 		antiPaywall.createFile();
	 		
	 	} catch (IOException e) {
	 		
	 		createErrorFrame("File not found. Please try again. (IOException)");
	 		System.exit(1);
	 	} catch (OSException e) {
	 		createErrorFrame("Unsupported operating system.");
	 		System.exit(1);
	 	}
	 	
	 	try {
	 		antiPaywall.openFile();
	 	} catch (IOException e) {
	 		createErrorFrame("An error occurred. Please try again.");
	 		System.exit(1);
	 	}

	 		Thread.sleep(20000);
	 		antiPaywall.file.delete();
	 		System.exit(0);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		semaphore.release();
	}
	
}
