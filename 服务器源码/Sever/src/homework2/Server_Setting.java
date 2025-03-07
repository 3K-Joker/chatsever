package homework2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Server_Setting extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3163267032470999352L;
	int port;
	JTextField textField;
	JButton button;
	JLabel label;
	JPanel jPanel;
	
	public Server_Setting(JFrame frame) {
		super(frame,true);
		textField = new JTextField(10);
		button = new JButton("完成");
		label = new JLabel("端口号: ");
		jPanel = new JPanel();
		jPanel.add(label);
		jPanel.add(textField);
		jPanel.add(button);
		add(jPanel);
		
		button.addActionListener(this);
		
		setLocation(500, 500);
		setSize(300,80);
		setVisible(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			port = Integer.parseInt(textField.getText());
			if(port<1024||port>65535){
				JOptionPane.showMessageDialog(jPanel, "端口号应在【1024,65535】之间","警告",JOptionPane.WARNING_MESSAGE);
			}
			dispose();
		}
	}

	public int getPort() {
		return port;
	}

}
