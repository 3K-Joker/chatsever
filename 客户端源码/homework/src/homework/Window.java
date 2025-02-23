package homework;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Window implements ActionListener {
	String clientHost,serverHost;
	String userName;
	Setting1 settingWindow;
	
	//private int flag = 0;	//�Ƿ����ӣ�
	private int port = 9999;	//�˿ں�
	
	ReceiveThried receiveThried;
	JFrame frame;
	JLabel label;
	JPanel baseJPanel,panel,panel1;
	JButton send_button;
	ButtonGroup group;
	JComboBox<String> comboBox;
	JScrollPane messageJScrollPane;
	JTextArea outputJTextArea,inputJTextArea;
	JMenuBar menuBar;
	JMenu jMenu1;
	JMenuItem menuItem1,menuItem2;
	Icon icon_send,icon_connect,icon_set;
	Thread thread;
	Socket socket;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;
	Window(){
		/*
		 *	 ��ƽ���
		 */
		frame = new JFrame("���Ŀͻ���");
		baseJPanel = new JPanel();
		panel = new JPanel();
		panel1 = new JPanel();
		menuBar = new JMenuBar();
		
		comboBox = new JComboBox<String>();
		comboBox.setEditable(false);
		comboBox.insertItemAt("������", 0);
		comboBox.setSelectedIndex(0);
		
		send_button = new JButton("����");
		send_button.setEnabled(false);
		jMenu1 = new JMenu("������");
		inputJTextArea = new JTextArea();
		messageJScrollPane = new JScrollPane(inputJTextArea);
		inputJTextArea.setEditable(false);
		inputJTextArea.setFont(new Font("����",Font.PLAIN,20));
		outputJTextArea = new JTextArea(2,30);
		outputJTextArea.setFont(new Font("����",Font.PLAIN,20));
		
		label = new JLabel("��������");
		menuItem1 = new JMenuItem("���±�������",new ImageIcon("res/��������.png"));
		menuItem2 = new JMenuItem("���ӷ�����",new ImageIcon("res/���ӷ�����.png"));
		
		send_button.addActionListener(this);
		menuItem1.addActionListener(this);
		menuItem2.addActionListener(this);
		
		jMenu1.add(menuItem1);
		jMenu1.addSeparator();
		jMenu1.add(menuItem2);
		menuBar.add(jMenu1);

		panel.setBorder(BorderFactory.createTitledBorder("sending"));
		panel.add(label);
		panel.add(comboBox);
		panel.add(outputJTextArea);
		panel.add(send_button);
		
		baseJPanel.setLayout(new BorderLayout());
		baseJPanel.add(messageJScrollPane,BorderLayout.CENTER);
		baseJPanel.add(new JLabel("  		 "),BorderLayout.WEST);
		baseJPanel.add(new JLabel("  		 "),BorderLayout.EAST);
		baseJPanel.add(panel,BorderLayout.SOUTH);
		frame.add(baseJPanel);
		
//		String string = "";
//		string = InetAddress.getLoopbackAddress().toString();
//		inputJTextArea.append("��ǰIP��ַΪ��"+string+"\n");
		try {
			String string = InetAddress.getLocalHost().toString();
			inputJTextArea.append("��ǰIP��ַΪ��"+string+"\n");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	
		frame.setJMenuBar(menuBar);
		frame.setLocation(600,300);
		frame.setSize(555,666);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuItem1) {
			settingWindow = new Setting1(this.frame);
			userName = settingWindow.userName;
			clientHost = settingWindow.clientHost;
			serverHost = settingWindow.serveHost;
			port = settingWindow.port;
				}
		if (e.getSource() == menuItem2) {
					connect();
				}
		if (e.getSource() == send_button) {
					send();
					outputJTextArea.setText("");
				}
	}	
	
	public void connect() {
		try {
			socket = new Socket(serverHost,port);
		} catch (Exception e) {
			System.out.println("�������ӵ���������\n ����±�������");
		}
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			out.writeObject(userName);
			out.flush();
			
			receiveThried = new ReceiveThried(socket,userName, in, out, comboBox, outputJTextArea, inputJTextArea);
			receiveThried.start();
			
			menuItem1.setEnabled(false);
			menuItem2.setEnabled(false);
			send_button.setEnabled(true);
			
			inputJTextArea.append("�û�����"+userName+"   IP:"+clientHost+"���ӷ������ɹ�!\n");
			//flag = 1;
			
		} catch (Exception e) {
			System.out.println("e2");
			return;
		}
	}
	
	public void send() {
		String user = comboBox.getSelectedItem().toString();
		String str = " ";
		String str1 = "�������ˡ�";
		String str2 = "��������";
		String message = outputJTextArea.getText();
		
		if(socket.isClosed()) {
			inputJTextArea.append("������������Ͽ�,���������ӻ������Ա��ϵ\n");
			menuItem2.setEnabled(true);
			return;
		}
		
		try {
			out.writeObject("������Ϣ");
			out.flush();
			if (comboBox.getSelectedIndex()==0) {
				out.writeObject(str1);
				out.flush();
			}
			else {
				out.writeObject(str2);
				out.flush();
			}
			out.writeObject(userName);
			out.flush();
			out.writeObject(str);
			out.flush();
			out.writeObject(user);
			out.flush();
			out.writeObject(str);
			out.flush();
			out.writeObject(message);
			out.flush();
			
		} catch (Exception e) {
			System.out.println("Send_Exception");
		}
		inputJTextArea.append(userName+":"+message+"\n");
	}
}
