package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import server.Server;
import shared.PacketListner;
import shared.PacketSender;

public class StartupDialog extends JDialog {
	private JTextField ipField;
	private JSlider latencyField;
	private JSlider packetLossField;
	private JSlider tppField;
	public class Result {
		public static final int CONNECT = 0;
		public static final int HOST = 1;
		public static final int EXIT = 2;
		public int type;
		public String ip;
		public int latency = 0;
		public int packetLossRate = 0;
		public int TPP = 2;
		
	}
	private Result result = null;
	private StartupDialog(String title) {
		super((JFrame) null, title, true);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		/*
		 * JPanel messagePane = new JPanel(); messagePane.add(new
		 * JLabel(message)); getContentPane().add(messagePane, 0);
		 */

		JPanel optionsPane = new JPanel();
		optionsPane.setLayout(new BoxLayout(optionsPane, BoxLayout.X_AXIS));
		
		JPanel titlePane = new JPanel();
		titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.Y_AXIS));
		
		JPanel sliderPane = new JPanel();
		sliderPane.setLayout(new BoxLayout(sliderPane, BoxLayout.Y_AXIS));
		
		JPanel valuePane = new JPanel();
		valuePane.setLayout(new BoxLayout(valuePane, BoxLayout.Y_AXIS));
		
		
		
		final JLabel latencyVal = new JLabel(PacketListner.DELAY + "ms");
		final JLabel pLossVal = new JLabel(PacketSender.LOSS_RATE + "%");
		final JLabel tppVal = new JLabel((Server.TPS / Server.TPP) + "p/sec");
		
		latencyField = new JSlider(0, 1000, PacketListner.DELAY);
		latencyField.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				latencyVal.setText(latencyField.getValue() + "ms");
			}
		});
		packetLossField = new JSlider(0, 100, PacketSender.LOSS_RATE);
		packetLossField.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				pLossVal.setText(packetLossField.getValue() + "%");
			}
		});
		tppField = new JSlider(1, 100, Server.TPP);
		tppField.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				int v = Server.TPS / tppField.getValue();
				if(v < 1) {
					float v2 = (float)Server.TPS / (float)tppField.getValue();
					String div;
					switch((int)(v2*8)) {
					case 8:
						div = "1";
						break;
					case 7:
						div = "7/8";
						break;
						//div = 8;
					case 6:
						div = "3/4";
						break;
					case 5:
						div = "5/8";
						break;
					case 4:
					default:
						div = "1/2";
					}
					tppVal.setText(div + "p/sec");
				} else {
					tppVal.setText(v + "p/sec");
				}
				
				
			}
		});
		titlePane.add(new JLabel("latency:"));
		titlePane.add(new JLabel("packet loss rate:"));
		titlePane.add(new JLabel("server packets/second:"));
		
		sliderPane.add(latencyField);
		sliderPane.add(packetLossField);
		sliderPane.add(tppField);
		
		valuePane.add(latencyVal);
		valuePane.add(pLossVal);
		valuePane.add(tppVal);
		
		optionsPane.add(titlePane);
		optionsPane.add(sliderPane);
		optionsPane.add(valuePane);
		getContentPane().add(optionsPane);
		
		
		
		JPanel portInputPane = new JPanel();
		ipField = new JTextField(16);
		portInputPane.add(new JLabel("IP:"));
		portInputPane.add(ipField);
		portInputPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		getContentPane().add(portInputPane);
		
		JPanel buttonPane = new JPanel();

		JButton button = new JButton("Join");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				result = new Result();
				result.type = Result.CONNECT;
				result.ip = ipField.getText();
				result.latency = latencyField.getValue();
				result.packetLossRate = packetLossField.getValue();
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
				result.latency = latencyField.getValue();
				result.packetLossRate = packetLossField.getValue();
				result.TPP = tppField.getValue();
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
		buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		getContentPane().add(buttonPane);
		//setMinimumSize(new Dimension(100, 800));

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static Result showDialog() {
		StartupDialog d = new StartupDialog("GBall Startup");
		while(d.result == null);
		return d.result;
	}
}
