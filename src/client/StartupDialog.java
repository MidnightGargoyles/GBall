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

	public StartupDialog(String title, String message) {
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
				System.out.println(field.getText());
			}
		});
		buttonPane.add(button);

		JButton button2 = new JButton("Host");
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		buttonPane.add(button2);
		
		JButton button3 = new JButton("Exit");
		button3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("bai");
				System.exit(0);
			}
		});
		buttonPane.add(button3);

		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();

		setVisible(true);
	}
}
