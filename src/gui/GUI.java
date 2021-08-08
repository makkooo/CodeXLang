package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import interpreter.Interpreter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;

import lexer.Lexer;
import lexer.Token;
import parser.CodeXRuntimeException;
import parser.Parser;

import java.awt.Cursor;

@SuppressWarnings("serial")
public class GUI extends JFrame{

	private JPanel contentPane;
	private JFileChooser TXTFileChooser;
	private String inputString;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		
		TXTFileChooser = new JFileChooser() {
			@Override
			public void approveSelection() {
				File selectedFile = getSelectedFile();
			    if(selectedFile.exists() && getDialogType() == JFileChooser.SAVE_DIALOG) {
			    	int result = JOptionPane.showConfirmDialog(this,
			    			getSelectedFile().getName() + 
			    			" already exists.\nDo you want to replace it?",
			    			"Confirm Save As",
			    			JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			    			if(result != JOptionPane.YES_OPTION) {
			    				cancelSelection();
			    			return;
			    			}
			    	}
			    super.approveSelection();
			}
		};
        TXTFileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Desktop"));
        TXTFileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
		
		setResizable(false);
		
		setTitle("CodeX Lexer Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextPane inputTextPane = new JTextPane();
		inputTextPane.setFont(new Font("Consolas", Font.BOLD, 17));
		inputTextPane.setBackground(new Color(240, 241, 242));
		inputTextPane.setEditable(true);
		
		JScrollPane inputScrollPane = new JScrollPane(inputTextPane);
		inputScrollPane.setBounds(90, 30, 1050, 375);
		inputScrollPane.setBorder(null);
		contentPane.add(inputScrollPane);
		
		JTextArea outputTextArea = new JTextArea();
		outputTextArea.setFont(new Font("Consolas", Font.PLAIN, 15));
		outputTextArea.setBackground(new Color(240, 241, 242));
		outputTextArea.setEditable(false);
		
		JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
		outputScrollPane.setBounds(90, 450, 1050, 280);
		outputScrollPane.setBorder(null);
		contentPane.add(outputScrollPane);
		
		PrintStream printStream = new PrintStream(new CustomOutputStream(outputTextArea));
		System.setOut(printStream);
		System.setErr(printStream);
		
		JLabel outputLbl = new JLabel("Console");
		outputLbl.setHorizontalAlignment(SwingConstants.LEFT);
		outputLbl.setFont(new Font("Consolas", Font.BOLD, 20));
		outputLbl.setBounds(90, 426, 90, 23);
		contentPane.add(outputLbl);
		
		JButton openFileBtn = new JButton("");
		openFileBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		openFileBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = TXTFileChooser.showOpenDialog(null);
				Scanner sc = null;
		        inputString = "";
				
		        if(returnVal == JFileChooser.APPROVE_OPTION) {
		            try {
		            	setTitle("CodeX Lexer Test - " + TXTFileChooser.getSelectedFile().getCanonicalPath());
		            	sc = new Scanner(TXTFileChooser.getSelectedFile());
		            	while(sc.hasNext()) {
		            		inputString += sc.nextLine() + '\n';
		            	}	 
		            	inputTextPane.setText(inputString);
		            } catch(IOException ioe) {
		                JOptionPane.showMessageDialog(null, "Failed to load file!", "Error", JOptionPane.WARNING_MESSAGE);
		            } finally {
		            	sc.close();
		            }
		        }
			}
		});
		openFileBtn.setIcon(new ImageIcon(GUI.class.getResource("/res/open-icon.jpg")));
		openFileBtn.setPressedIcon(new ImageIcon(GUI.class.getResource("/res/open-icon_pressed.jpg")));
		openFileBtn.setToolTipText("Open file");
		openFileBtn.setBorder(null);
		openFileBtn.setBackground(Color.WHITE);
		openFileBtn.setBounds(10, 30, 81, 78);
		openFileBtn.setFocusable(false);
		contentPane.add(openFileBtn);
		
		JButton saveBtn = new JButton("");
		saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = TXTFileChooser.showSaveDialog(null);
				FileWriter writer = null;
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try{
						if(TXTFileChooser.getSelectedFile().getName().contains(".txt"))
							writer = new FileWriter(TXTFileChooser.getSelectedFile());
						else
							writer = new FileWriter(TXTFileChooser.getSelectedFile() +  ".txt");
						writer.write(outputTextArea.getText());
						JOptionPane.showMessageDialog(null, "File saved successfully!");
					 } catch(IOException ioe) {
			            	JOptionPane.showMessageDialog(null, "Failed to save file!", "Error", JOptionPane.WARNING_MESSAGE);
			         } finally {
						try {
							if(writer != null)
								writer.close();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
			         }
				}
			}
		});	
		saveBtn.setPressedIcon(new ImageIcon(GUI.class.getResource("/res/save-icon_pressed.jpg")));
		saveBtn.setIcon(new ImageIcon(GUI.class.getResource("/res/save-icon.jpg")));
		saveBtn.setToolTipText("Save output file");
		saveBtn.setBorder(null);
		saveBtn.setBackground(Color.WHITE);
		saveBtn.setBounds(10, 124, 81, 78);
		saveBtn.setFocusable(false);
		contentPane.add(saveBtn);
		
		JButton saveSrcBtn = new JButton("");
		saveSrcBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveSrcBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = TXTFileChooser.showSaveDialog(null);
				FileWriter writer = null;
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try{
						if(TXTFileChooser.getSelectedFile().getName().contains(".txt"))
							writer = new FileWriter(TXTFileChooser.getSelectedFile());
						else
							writer = new FileWriter(TXTFileChooser.getSelectedFile() +  ".txt");
						writer.write(inputTextPane.getText());
						JOptionPane.showMessageDialog(null, "File saved successfully!");
			        } catch(IOException ioe) {
			            	JOptionPane.showMessageDialog(null, "Failed to save file!", "Error", JOptionPane.WARNING_MESSAGE);
			        } finally {
						try {
							if(writer != null)
								writer.close();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
			        }
				}
			}
		});	
		saveSrcBtn.setPressedIcon(new ImageIcon(GUI.class.getResource("/res/saveSRC-icon_pressed.jpg")));
		saveSrcBtn.setIcon(new ImageIcon(GUI.class.getResource("/res/saveSRC-icon.jpg")));
		saveSrcBtn.setToolTipText("Save source file");
		saveSrcBtn.setFocusable(false);
		saveSrcBtn.setBorder(null);
		saveSrcBtn.setBackground(Color.WHITE);
		saveSrcBtn.setBounds(10, 213, 81, 78);
		contentPane.add(saveSrcBtn);
		
		JButton runBtn = new JButton("");
		runBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		runBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputTextArea.setText(null);
				
				inputString = inputTextPane.getText();
				//try {
					Lexer lexer = new Lexer(inputString);
					List<Token> tokenList = lexer.getToken();
					Parser parser = new Parser(tokenList);
					Interpreter interpreter = new Interpreter();
					interpreter.interpret(parser.parseStatementList());
					//parser.parseProgram();
				//} catch (CodeXRuntimeException cxre) {
					
				//}
				
				for(Token t : tokenList) 
					outputTextArea.append(String.format("%-5d %-15s %-20s \r\n", t.line, t.type, t.lexeme));
			}
		});
		runBtn.setPressedIcon(new ImageIcon(GUI.class.getResource("/res/run-icon_pressed.jpg")));
		runBtn.setIcon(new ImageIcon(GUI.class.getResource("/res/run-icon.jpg")));
		runBtn.setToolTipText("Run");
		runBtn.setFocusable(false);
		runBtn.setBorder(null);
		runBtn.setBackground(Color.WHITE);
		runBtn.setBounds(10, 302, 81, 78);
		contentPane.add(runBtn);
		
		JTextLineNumber tln = new JTextLineNumber(inputTextPane);
		tln.setBackground(new Color(234, 234, 234));
		inputScrollPane.setRowHeaderView(tln);
	}
}