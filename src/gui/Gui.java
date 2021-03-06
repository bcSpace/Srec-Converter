package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import error.CustomError;
import main.Controller;

public class Gui {
	
	private final JFrame frame;
	private Controller controller;
	
	private JTextField sourcePathField, writePathField;
	private JButton go;
	private JTextArea decodedOutput, fileOutput;
	
	public Gui() {
		frame = new JFrame();
		controller = new Controller();
		init();
	}
	
	private void init() {
		createWindow(); //create the frame
		comps(); //put the stuff on the frames and set frame visible
		listeners(); //add the listeners for the components 
	}
	
	private void createWindow() {
		frame.setTitle("Coding Challenge");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}

	private void comps() {
		sourcePathField = new JTextField(15);
		writePathField = new JTextField(15);
		
		go = new JButton("Run");
		
		decodedOutput = new JTextArea(12,35);
		decodedOutput.setEditable(false);
		
		fileOutput = new JTextArea(12,35);
		fileOutput.setEditable(false);
		
		JLabel labels[] = {new JLabel("Source"), new JLabel("Write")};
		
		frame.setLayout(new FlowLayout());
		frame.add(labels[0]);
		frame.add(sourcePathField);
		frame.add(labels[1]);
		frame.add(writePathField);
		frame.add(go);
		
		JTabbedPane tab = new JTabbedPane();
		
		JScrollPane scroll = new JScrollPane(decodedOutput), scroll1 = new JScrollPane(fileOutput);
		
		tab.add("Decoded", scroll);
		tab.add("File output", scroll1);
		
		frame.add(tab); //used for displaying decoded srec
		
		frame.pack();
		frame.setSize(600, 300);
		frame.setVisible(true);
	}
	
	private void listeners() {
		sourcePathField.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  run();
			  } 
		});
		
		writePathField.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  run();
			  } 
		});
		
		go.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  run();
			  } 
		});
	}
	
	private void run() {
		String sourcePath = sourcePathField.getText();
		String writePath = writePathField.getText();
		
		//very, very simple safety check
		if(sourcePath.length() == 0 || writePath.length() == 0) { 
			errorMessage("Make sure paths are not blank");
			return;
		}
		
		if(!writePath.endsWith(".binary")) if(!warningMessage()) return;
		
		try {
			String output[] = controller.run(sourcePath, writePath);
			this.decodedOutput.setText(output[0]); //show the user something pretty
			this.fileOutput.setText(output[1]);
		} catch (CustomError e) {
			errorMessage(e.getMessage());
			return;
		} catch(Exception e) {
			e.printStackTrace(); 
			errorMessage("UNEXPECTED: " + e.getMessage() + " | " + e);
			return;
		}
		
		successMessage(writePath);
		
	}
	
	private void errorMessage(String message) {
		JOptionPane.showMessageDialog(frame,
			    message,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	private boolean warningMessage() {
		int n = JOptionPane.showConfirmDialog(
			    frame,
			    "Are your sure that you want to use a non .binary file extension",
			    "?",
			    JOptionPane.YES_NO_OPTION);
		
		return n == 0;
	}
	
	private void successMessage(String path) {
		JOptionPane.showMessageDialog(frame,
			    "File writen to: " + path,
			    "Success",
			    JOptionPane.INFORMATION_MESSAGE);
	}
	
}
