import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ClientGui {
	private Client client;
	private JFrame frame;
	private JTextField textIP;
	private JTextField textPort;
	private JTextField textFileName;
	private JEditorPane textResult;
	private JTextArea textContent;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGui window = new ClientGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 715, 633);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("IP");
		lblNewLabel.setBounds(31, 19, 47, 23);
		frame.getContentPane().add(lblNewLabel);
		
		textIP = new JTextField();
		textIP.setText("13.229.49.95");
		textIP.setBounds(88, 16, 175, 29);
		frame.getContentPane().add(textIP);
		textIP.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Port");
		lblNewLabel_1.setBounds(285, 19, 47, 23);
		frame.getContentPane().add(lblNewLabel_1);
		
		textPort = new JTextField();
		textPort.setText("9999");
		textPort.setBounds(326, 16, 175, 29);
		frame.getContentPane().add(textPort);
		textPort.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					if(btnConnect.getText()=="Connect") {
						client = new Client(textIP.getText(), Integer.parseInt(textPort.getText()));
						btnConnect.setText("disconnect");
					}else {
						btnConnect.setText("Connect");
						client.closeConnection();
					}
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnConnect.setBounds(212, 54, 144, 31);
		frame.getContentPane().add(btnConnect);
		
		JButton btnGetKey = new JButton("Get Key");
		btnGetKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				client.requestKey();
				
			}
		});
		btnGetKey.setBounds(212, 104, 144, 31);
		frame.getContentPane().add(btnGetKey);
		
		JLabel lblNewLabel_2 = new JLabel("File Name:");
		lblNewLabel_2.setBounds(88, 158, 103, 23);
		frame.getContentPane().add(lblNewLabel_2);
		
		textFileName = new JTextField();
		textFileName.setBounds(167, 154, 219, 29);
		frame.getContentPane().add(textFileName);
		textFileName.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Content");
		lblNewLabel_3.setBounds(88, 200, 47, 23);
		frame.getContentPane().add(lblNewLabel_3);
		
		textContent = new JTextArea();
		textContent.setBounds(167, 194, 501, 149);
		frame.getContentPane().add(textContent);
		
		textResult = new JEditorPane();
		textResult.setBounds(167, 351, 501, 191);
		frame.getContentPane().add(textResult);
		
		JLabel lblNewLabel_4 = new JLabel("Result:");
		lblNewLabel_4.setBounds(88, 406, 47, 23);
		frame.getContentPane().add(lblNewLabel_4);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.createFile(textFileName.getText(), textContent.getText());	
				client.readResponseFromServer();
				textResult.setText(client.getResult());
				
			//	client.createFile(, null);
			}
		});
		btnSend.setBounds(581, 544, 87, 31);
		frame.getContentPane().add(btnSend);
	}
}
