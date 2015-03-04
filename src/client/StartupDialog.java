package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartupDialog extends JDialog {
	private JTextField field;
	
	public class Result {
		public static final int CONNECT = 0;
		public static final int HOST = 1;
		public static final int EXIT = 2;
		public int type;
		public String value;
	}
	private Result result = null;
	private StartupDialog(String title, String message) {
		super((JFrame) null, title, true);

		/*
		 * JPanel messagePane = new JPanel(); messagePane.add(new
		 * JLabel(message)); getContentPane().add(messagePane, 0);
		 */

		JPanel portInputPane = new JPanel();
		field = new JTextField(16);
		portInputPane.add(field);

		getContentPane().add(portInputPane);

		JPanel buttonPane = new JPanel();

		JButton button = new JButton("Join");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				result = new Result();
				result.type = Result.CONNECT;
				result.value = field.getText();
				dispose();
			}
		});
		buttonPane.add(button);

		JButton button2 = new JButton("Host");
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				result = new Result();
				result.type = Result.HOST;
				dispose();
			}
		});
		buttonPane.add(button2);
		
		JButton button3 = new JButton("Exit");
		button3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				result = new Result();
				result.type = Result.EXIT;
				dispose();
			}
		});
		buttonPane.add(button3);

		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();

		setVisible(true);
	}
	
	public static Result showDialog() {
		StartupDialog d = new StartupDialog("title", "message?");
		while(d.result == null);
		return d.result;
	}
}
