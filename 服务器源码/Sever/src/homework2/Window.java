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
	private int port = 9999;	//端口号
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
		setTitle("畅聊服务器端");
		
		panel1 = new JPanel();
		panel2 = new JPanel();
		basePanel = new JPanel();
		inpuTextArea = new JTextArea();
		inpuTextArea.setEditable(false);
		scrollPane = new JScrollPane(inpuTextArea);
		messageField = new JTextField(20);
		inpuTextArea.setFont(new Font("宋体",Font.PLAIN,20));
		messageField.setFont(new Font("宋体",Font.PLAIN,20));
		
		menuBar = new JMenuBar();
		menu = new JMenu("启动项");
		menuItem1 = new JMenuItem("服务器设置");
		menuItem2 = new JMenuItem("服务器上线");
		label1 = new JLabel("用户：");
		label2 = new JLabel("操作：");
		label3 = new JLabel(" 消息列表：    ");
		label3.setFont(new Font("宋体",Font.PLAIN,25));
		
		sendButton = new JButton("发送");
		sendButton.setEnabled(false);
		logoutButton = new JButton("强制下线");
		logoutButton.setEnabled(false);
		
		comboBox = new JComboBox<String>();
		comboBox.setEditable(false);
		comboBox.insertItemAt("所有人", 0);
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
			inpuTextArea.append("当前IP地址为："+string+"\n");
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
				inpuTextArea.append("您的服务器已启动......    端口为："+port+"\n");
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
			inpuTextArea.append("服务器不支持全部用户下线\n");
			return;
		}
		
		Node node = userlist.searchUser(comboBox.getSelectedItem().toString());
		
		try {
			node.outputStream.writeObject("服务关闭");
			node.inputStream.close();
			node.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		userlist.deleteUser(node);
		/**
		 * 重建comboBox
		 */
		String[] users = new String[userlist.getCount()];
		for (int i = 1; i <= userlist.getCount(); i++) {
			users[i-1] = userlist.findIndex(i).userName;
			
		}
		comboBox.removeAllItems();
		comboBox.addItem("所有人");
		for (int i = 1; i <= users.length; i++) {
			comboBox.addItem(users[i-1]);
		}
		comboBox.setSelectedIndex(0);
		inpuTextArea.append("用户"+node.userName+"已被强制下线   当前在线"+userlist.getCount()+"人\n");
		listenThried.receiveThried.sendlist();////////////////////////
	}

	public void send() {
		int num = userlist.getCount();
		String type = "系统消息";
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
