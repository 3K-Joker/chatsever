package homework2;

import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ReceiveThried extends Thread {
	JTextArea inputTextArea;
	JTextField messageField;
	JComboBox<String> comboBox;
	Node clientNode;
	Userlist userlist;
	
	boolean run;

	public ReceiveThried(JTextArea inputTextArea, JTextField messageField, JComboBox<String> comboBox, Node clientNode,
			Userlist userlist) {
		super();
		this.inputTextArea = inputTextArea;
		this.messageField = messageField;
		this.comboBox = comboBox;
		this.clientNode = clientNode;
		this.userlist = userlist;
		run = true;
	}
	
	public void run() {
		sendlist();
		while (run&&!clientNode.socket.isClosed()) {
			try {
				String type = (String)clientNode.inputStream.readObject();
				if (type.equals("聊天信息")) {
					String str0 = (String)clientNode.inputStream.readObject();
					String string;
					
					String userName = (String)clientNode.inputStream.readObject();
					clientNode.inputStream.readObject();
					String user = (String)clientNode.inputStream.readObject();
					clientNode.inputStream.readObject();
					String message = (String)clientNode.inputStream.readObject();
					if(str0.equals("【所有人】")){
						string = userName+"对"+str0+"说："+message+"\n";
						inputTextArea.append(string);
						string = userName+"说："+message+"\n";
						for (int i = 1; i <= userlist.getCount(); i++) {
							Node node = userlist.findIndex(i);
							if (node.userName.equals(userName)) {
								continue;
							}
							node.outputStream.writeObject("聊天消息");
							node.outputStream.flush();
							node.outputStream.writeObject(string);
							node.outputStream.flush();
						}
					}
					else {
						string = userName+"对"+user+"说："+message+"\n";
						inputTextArea.append(string);
						Node node = userlist.searchUser(user);
						node.outputStream.writeObject("聊天消息");
						node.outputStream.flush();
						node.outputStream.writeObject(userName+":"+message+"\n");
						node.outputStream.flush();
					}
				}
			} catch (Exception e) {
			}
		}
	}
	
	public void sendlist() {
		String users = "";
		int num = userlist.getCount();
		Node node;
		for (int i = 1; i <= num; i++) {
			node = userlist.findIndex(i);
			users += (node.userName+"\n");
		}
		for (int i = 1; i <= num; i++) {
			node = userlist.findIndex(i);
			try {
				node.outputStream.writeObject("用户列表");
				node.outputStream.flush();
				node.outputStream.writeObject(users);
				node.outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
