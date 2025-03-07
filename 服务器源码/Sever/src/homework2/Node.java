package homework2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Node {
	/**
	 * 用户数据
	 */
	String userName = "";
	Socket socket = null;
	ObjectInputStream inputStream = null;
	ObjectOutputStream outputStream = null;
	/**
	 * node 结构
	 */
	Node nextNode = null;
	
}
