package homework2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ListenThried extends Thread {
	ServerSocket serverSocket;
	JComboBox<String> comboBox;
	JTextArea inputtextArea;
	JTextField messageField;
	Userlist userlist;
	
	Node clientNode;
	
	ReceiveThried receiveThried;
	
	public boolean run;

	public ListenThried(ServerSocket serverSocket, JComboBox<String> comboBox, JTextArea inputtextArea,
			JTextField messageField, Userlist userlist) {
		super();
		this.serverSocket = serverSocket;
		this.comboBox = comboBox;
		this.inputtextArea = inputtextArea;
		this.messageField = messageField;
		this.userlist = userlist;
		
		run = true;
	}
	
	public void run() {
		while (run&&!serverSocket.isClosed()) {
			try {
				clientNode = new Node();
				clientNode.socket = serverSocket.accept();
				clientNode.inputStream = new ObjectInputStream(clientNode.socket.getInputStream());
				clientNode.outputStream = new ObjectOutputStream(clientNode.socket.getOutputStream());
				clientNode.outputStream.flush();
				clientNode.userName = (String)clientNode.inputStream.readObject();
				
				comboBox.addItem(clientNode.userName);
				userlist.addUser(clientNode);
				inputtextArea.append("新用户："+clientNode.userName+"上线了！   当前在线："+userlist.getCount()+"人\n");
				
				receiveThried = new ReceiveThried(inputtextArea, messageField, comboBox, clientNode, userlist);
				receiveThried.start();
			} catch (Exception e) {
			}
		}
	}
	
	
	
}
