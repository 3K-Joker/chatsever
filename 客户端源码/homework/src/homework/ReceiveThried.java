package homework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class ReceiveThried extends Thread{
	private JComboBox<String> comboBox;
	private JTextArea inputJTextArea; 
	private String userName;
	Socket socket;
	ObjectInputStream inputStream;
	ObjectOutputStream outputStream;
	
	public ReceiveThried(Socket socket,String userName,ObjectInputStream inputStream,ObjectOutputStream outputStream,
			JComboBox<String> comboBox,JTextArea outputJTextArea,JTextArea inputJTextArea) {
		this.socket = socket;
		this.userName = userName;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.comboBox = comboBox;
		this.inputJTextArea = inputJTextArea;
	}
	
	public void run() {
		while(!socket.isClosed()) {
			try {
				String type = (String)inputStream.readObject();
				if (type.equals("系统消息")) {
					String message = (String)inputStream.readObject();
					inputJTextArea.append("系统消息："+message+"\n");
				}
				if (type.equals("聊天消息")) {
					String message = (String)inputStream.readObject();
					inputJTextArea.append(message);
				}
				if (type.equals("服务关闭")) {
					outputStream.close();
					inputStream.close();
					socket.close();
					inputJTextArea.append("     服务器关闭\n");
					break;
				}
				if (type.equals("用户列表")) {
					String message = (String)inputStream.readObject();
					String[] users = message.split("\n");
					comboBox.removeAllItems();
					comboBox.addItem("所有人");
					for (int i = 0; i < users.length; i++) {
						if (users[i].equals(userName)) {
							continue;
						}
						comboBox.addItem(users[i]);
					}
					comboBox.setSelectedIndex(0);
					inputJTextArea.append("用户列表已更新\n");
				}
			} catch (Exception e) {
				System.out.println("ReceiveThried_Exception");
			}
		}
	}
}
