package homework2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Node {
	/**
	 * �û�����
	 */
	String userName = "";
	Socket socket = null;
	ObjectInputStream inputStream = null;
	ObjectOutputStream outputStream = null;
	/**
	 * node �ṹ
	 */
	Node nextNode = null;
	
}
