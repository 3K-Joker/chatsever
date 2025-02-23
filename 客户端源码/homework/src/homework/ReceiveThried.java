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
				if (type.equals("ϵͳ��Ϣ")) {
					String message = (String)inputStream.readObject();
					inputJTextArea.append("ϵͳ��Ϣ��"+message+"\n");
				}
				if (type.equals("������Ϣ")) {
					String message = (String)inputStream.readObject();
					inputJTextArea.append(message);
				}
				if (type.equals("����ر�")) {
					outputStream.close();
					inputStream.close();
					socket.close();
					inputJTextArea.append("     �������ر�\n");
					break;
				}
				if (type.equals("�û��б�")) {
					String message = (String)inputStream.readObject();
					String[] users = message.split("\n");
					comboBox.removeAllItems();
					comboBox.addItem("������");
					for (int i = 0; i < users.length; i++) {
						if (users[i].equals(userName)) {
							continue;
						}
						comboBox.addItem(users[i]);
					}
					comboBox.setSelectedIndex(0);
					inputJTextArea.append("�û��б��Ѹ���\n");
				}
			} catch (Exception e) {
				System.out.println("ReceiveThried_Exception");
			}
		}
	}
}
