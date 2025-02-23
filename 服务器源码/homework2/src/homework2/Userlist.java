package homework2;

public class Userlist {
	
	private Node rootNode;	//begin
	private Node pointerNode;//temp_pointer
	private int count;
	
	public Userlist() {
		rootNode = new Node();
		rootNode.nextNode = null;
		pointerNode = null;
		count = 0;
	}
	
	public void addUser(Node node) {
		pointerNode = rootNode;
		while (pointerNode.nextNode != null) {
			pointerNode = pointerNode.nextNode;
		}//找到最后一个node
		pointerNode.nextNode = node;
		node.nextNode = null;
		count++;
	}
	
	public void deleteUser(Node node) {
		pointerNode = rootNode;
		while (pointerNode.nextNode != node) {
			pointerNode = pointerNode.nextNode;
		}//找到node的前节点
		pointerNode.nextNode = node.nextNode;
		count--;
	}

	public int getCount() {
		return count;
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	/**
	 * 查找用户节点
	 */
	public Node searchUser(String userName) {
		pointerNode = rootNode;
		while (pointerNode.nextNode != null) {
			if (pointerNode.nextNode.userName.equals(userName)) {
				break;
			}
			pointerNode = pointerNode.nextNode;
		}
		if (pointerNode.nextNode == null) {
			System.out.println("该消息未被接收！");
			return null;
		}
		return pointerNode.nextNode;
	}
	
	public Node findIndex(int num) {
		pointerNode = rootNode;
		for (int i = 0; i < num; i++) {
			pointerNode = pointerNode.nextNode;
		}
		return pointerNode;
	}
}
