package homework2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

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
import javax.swing.JTextField;


public class Window extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5194662922765339870L;
	private int port = 9999;	//�˿ں�
	Userlist userlist;
	
	ServerSocket serverSocket;
	String serverHost;
	
	JScrollPane scrollPane;
	JPanel basePanel,panel1,panel2;
	JTextArea inpuTextArea;
	JTextField messageField;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem1,menuItem2;
	
	JLabel label1,label2,label3;
	JComboBox<String> comboBox;
	JButton sendButton,logoutButton;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;
	
	ListenThried listenThried;
	
	public Window() throws HeadlessException {
		super();
		setTitle("���ķ�������");
		
		panel1 = new JPanel();
		panel2 = new JPanel();
		basePanel = new JPanel();
		inpuTextArea = new JTextArea();
		inpuTextArea.setEditable(false);
		scrollPane = new JScrollPane(inpuTextArea);
		messageField = new JTextField(20);
		inpuTextArea.setFont(new Font("����",Font.PLAIN,20));
		messageField.setFont(new Font("����",Font.PLAIN,20));
		
		menuBar = new JMenuBar();
		menu = new JMenu("������");
		menuItem1 = new JMenuItem("����������");
		menuItem2 = new JMenuItem("����������");
		label1 = new JLabel("�û���");
		label2 = new JLabel("������");
		label3 = new JLabel(" ��Ϣ�б�    ");
		label3.setFont(new Font("����",Font.PLAIN,25));
		
		sendButton = new JButton("����");
		sendButton.setEnabled(false);
		logoutButton = new JButton("ǿ������");
		logoutButton.setEnabled(false);
		
		comboBox = new JComboBox<String>();
		comboBox.setEditable(false);
		comboBox.insertItemAt("������", 0);
		comboBox.setSelectedIndex(0);
		
		menu.add(menuItem1);
		menu.addSeparator();
		menu.add(menuItem2);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		
		basePanel.add(label1);
		basePanel.add(comboBox);
		basePanel.add(logoutButton);
		basePanel.add(messageField);
		basePanel.add(sendButton);
		
		add(new JPanel().add(label3),BorderLayout.NORTH);
		add(scrollPane,BorderLayout.CENTER);
		add(new JPanel(),BorderLayout.WEST);
		add(new JPanel(),BorderLayout.EAST);
		add(basePanel,BorderLayout.SOUTH);
		
		menuItem1.addActionListener(this);
		menuItem2.addActionListener(this);
		logoutButton.addActionListener(this);
		sendButton.addActionListener(this);
		
		try {
			String string = InetAddress.getLocalHost().getHostAddress();
			inpuTextArea.append("��ǰIP��ַΪ��"+string+"\n");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		setLocation(300,200);
		setSize(555,666);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == menuItem1) {
			Server_Setting setting = new Server_Setting(this);
			port = setting.getPort();
		}
		if (obj == menuItem2) {
			try {
				serverSocket = new ServerSocket(port,10);
				inpuTextArea.append("���ķ�����������......    �˿�Ϊ��"+port+"\n");
				menuItem1.setEnabled(false);
				menuItem2.setEnabled(false);
				logoutButton.setEnabled(true);
				sendButton.setEnabled(true);
				comboBox.setEnabled(true);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			userlist = new Userlist();
			listenThried = new ListenThried(serverSocket, comboBox, inpuTextArea, messageField, userlist);
			listenThried.start();
		}
		if (obj == logoutButton) {
			logout();
		}
		if (obj == sendButton) {
			send();
			messageField.setText("");
		}
	}
	
	private void logout() {
		if (comboBox.getSelectedIndex()==0) {
			inpuTextArea.append("��������֧��ȫ���û�����\n");
			return;
		}
		
		Node node = userlist.searchUser(comboBox.getSelectedItem().toString());
		
		try {
			node.outputStream.writeObject("����ر�");
			node.inputStream.close();
			node.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		userlist.deleteUser(node);
		/**
		 * �ؽ�comboBox
		 */
		String[] users = new String[userlist.getCount()];
		for (int i = 1; i <= userlist.getCount(); i++) {
			users[i-1] = userlist.findIndex(i).userName;
			
		}
		comboBox.removeAllItems();
		comboBox.addItem("������");
		for (int i = 1; i <= users.length; i++) {
			comboBox.addItem(users[i-1]);
		}
		comboBox.setSelectedIndex(0);
		inpuTextArea.append("�û�"+node.userName+"�ѱ�ǿ������   ��ǰ����"+userlist.getCount()+"��\n");
		listenThried.receiveThried.sendlist();////////////////////////
	}

	public void send() {
		int num = userlist.getCount();
		String type = "ϵͳ��Ϣ";
		Node node;
		for (int i = 1; i <= num; i++) {
			node = userlist.findIndex(i);
			try {
				node.outputStream.writeObject(type);
				node.outputStream.flush();
				node.outputStream.writeObject(messageField.getText());
				node.outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		inpuTextArea.append(type+":"+messageField.getText()+"\n");
	}

}
