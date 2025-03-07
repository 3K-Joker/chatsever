package homework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Setting1 extends JDialog implements ActionListener {
	private static final long serialVersionUID = -741005609012802224L;
	/**
	 * 更新本机配置
	 */
	int port;
	String clientHost, serveHost, userName;
	JLabel label1, label2, label3,label4;
	JTextField textField1, textField2, textField3,textField4;
	Box basebox, box1, box2;
	JPanel panel;
	JButton button;

	public Setting1(JFrame frame) {
		super(frame,true);
		int x, y;
		panel = new JPanel();
		button = new JButton("完成");
		label1 = new JLabel("用户名：");
		label2 = new JLabel("本机IP：");
		label3 = new JLabel("服务器IP：");
		label4 = new JLabel("端口：");
		textField1 = new JTextField(20);
		textField2 = new JTextField(20);
		textField3 = new JTextField(20);
		textField4 = new JTextField(20);
		box1 = Box.createVerticalBox();
		box1.add(label1);
		box1.add(Box.createVerticalStrut(10));
		box1.add(label2);
		box1.add(Box.createVerticalStrut(10));
		box1.add(label3);
		box1.add(Box.createVerticalStrut(10));
		box1.add(label4);
		box1.add(Box.createVerticalStrut(10));
		
		box2 = Box.createVerticalBox();
		box2.add(textField1);
		box2.add(Box.createVerticalStrut(10));
		box2.add(textField2);
		box2.add(Box.createVerticalStrut(10));
		box2.add(textField3);
		box2.add(Box.createVerticalStrut(10));
		box2.add(textField4);
		basebox = Box.createHorizontalBox();
		basebox.add(box1);
		basebox.add(Box.createHorizontalStrut(8));
		basebox.add(box2);
		basebox.add(Box.createHorizontalStrut(8));
		panel.add(basebox);
		panel.add(button);
		add(panel);

		button.addActionListener(this);

		x = frame.getBounds().x + frame.getBounds().y;
		y = frame.getBounds().y;
		setLocation(x, y);
		setSize(300, 210);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			define(textField1.getText(), textField2.getText(), textField3.getText(),textField4.getText());
			dispose();
		}
	}
	
	public synchronized void define(String s1,String s2, String s3,String s4) {
		userName = s1;
		clientHost = s2;
		serveHost = s3;
		port = Integer.parseInt(s4);
	}

}
