package sam.bee.web.server;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Server extends JFrame {

	private JButton start;
	private JButton stop;
	JTextArea tArea;
	JEditorPane jedit;
	JScrollPane jscroll;
	JMenuItem mi1 = new JMenuItem("�� �� �ļ�");
	JMenuItem mi2 = new JMenuItem("�� �� �� ��");

	JMenuItem mh1 = new JMenuItem("�� �� �� �� h t m l");
	JMenuItem mh2 = new JMenuItem("�� ��  h t m l");
	boolean isStart = false;
	public static int client_num = 0;
	private int frame_xlocate = 400;
	private int frame_ylocate = 200;
	ToolOfServer serveTool;
	VisitLog visitlog;

	public Server() {
		try {
			init();// ��ʼ������ʾ����
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLocation(frame_xlocate, frame_ylocate);
		this.setSize(new Dimension(500, 400));
		this.setTitle("�� �� S e r v e r");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void init() {
		// ���÷��������
		this.setLayout(new BorderLayout());

		// ���ò˵���
		JPanel top_pane = new JPanel();
		top_pane.setLayout(new BorderLayout());
		JMenuBar MenuBar = new JMenuBar();
		JMenu mFile = new JMenu("�� ��");

		JMenuItem mi3 = new JMenuItem("Exit");
		mFile.add(mi1);
		mFile.add(mi2);
		mFile.add(mi3);
		MenuBar.add(mFile);
		JMenu mHtml = new JMenu("H t m l");
		mHtml.add(mh1);
		mHtml.add(mh2);
		MenuBar.add(mHtml);
		JMenu visitlogm = new JMenu("�� ʷ �� ¼");
		JMenuItem visitlog = new JMenuItem("�� �� �� ʷ �� ¼");
		JMenu set = new JMenu("����");
		JMenuItem setPort = new JMenuItem("�� �� �� ��");
		set.add(setPort);
		visitlogm.add(visitlog);
		MenuBar.add(visitlogm);
		MenuBar.add(set);
		top_pane.add(MenuBar, BorderLayout.NORTH);

		// ���ñ����ļ���ť������
		mi1.addActionListener(new saveFileListener());

		// ���ô��ļ���ť������
		mi2.addActionListener(new openFileListener());

		// ����Exit������
		mi3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});

		// ���ô����µ�HTML�ļ���ť������
		mh1.addActionListener(new makeNewHTMLListener());

		// ���ô�HTML�ļ���ť�ļ�����
		mh2.addActionListener(new openHTMLListener());

		// ���ö˿����ð�ť�ļ�����
		setPort.addActionListener(new portListener());

		// ���ÿ�����������ť������
		start = new JButton("�� �� �� �� ��");
		startListener startl = new startListener();
		start.addActionListener(startl);

		// ���ùرշ�������ť������
		stop = new JButton("�� �� �� �� ��");
		stop.setEnabled(false);
		stopListener stopl = new stopListener();
		stop.addActionListener(stopl);

		// ������ʷ��¼�ļ�����
		visitlog.addActionListener(new visitlogListener());
		JPanel hold_s = new JPanel();
		hold_s.add(start);
		hold_s.add(stop);
		top_pane.add(hold_s, BorderLayout.SOUTH);
		add(top_pane, BorderLayout.NORTH);

		// ���÷�����������
		tArea = new JTextArea();
		jscroll = new JScrollPane(tArea);
		add(jscroll, BorderLayout.CENTER);

		// ���÷�����ǩ��
		final JLabel lblServerStatus = new JLabel("M i n i  �� �� ��");
		add(lblServerStatus, BorderLayout.SOUTH);

		// ���÷�����ͷ��
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		setSize(width / 2, height / 2);
		setLocation(width / 4, height / 4);
		Image img = kit.getImage("image/��.png");
		setIconImage(img);

		// �����ı��򲻿ɱ༭
		tArea.setEditable(false);
		serveTool = new ToolOfServer(tArea);
	}

	/**
	 * ������������ť��������
	 * 
	 * @author lenovo
	 * 
	 */
	class startListener implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			// start the server

			if (serveTool.startServer()) {
				// success
				start.setEnabled(false);
				stop.setEnabled(true);
				tArea.append("�����ɹ�   +\n");
			} else {
				// failed
				tArea.append("����ʧ��   +\n");
			}
		}
	}

	/**
	 * �رշ�������ť��������
	 * 
	 * @author lenovo
	 * 
	 */
	class stopListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {

			// stop the server
			serveTool.stopServer();
			stop.setEnabled(false);
			start.setEnabled(true);

		}
	}

	/**
	 * �鿴��ʷ��¼��������
	 * 
	 * @author lenovo
	 * 
	 */
	class visitlogListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			visitlog = new VisitLog();
		}
	}

	/**
	 * �����ļ���������
	 * 
	 * @author lenovo
	 * 
	 */
	class saveFileListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));

			int result = chooser.showSaveDialog(mi1);
		}
	}

	/**
	 * ���ļ���������
	 * 
	 * @author lenovo
	 * 
	 */
	class openFileListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("."));

			int result = chooser.showOpenDialog(mi2);
		}
	}

	/**
	 * �����µ�HTML��������
	 * 
	 * @author lenovo
	 * 
	 */
	class makeNewHTMLListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			CreatNewHTML creat = new CreatNewHTML();
		}
	}

	/**
	 * html�ļ�����
	 * 
	 * @author lenovo
	 * 
	 */
	class openHTMLListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			OpenHTML.open = mh2;
			OpenHTML openhtml = new OpenHTML();
		}
	}

	/**
	 * �˿ڰ�ť�ļ�����
	 * 
	 * @author lenovo
	 * 
	 */
	class portListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			try {
				int port = Integer.parseInt(JOptionPane.showInputDialog(tArea, "�������µĶ˿ں�", "8000"));
				if (port > 1024) {
					int charge = JOptionPane.showConfirmDialog(tArea, "ȷ��Ҫ�޸ķ������˿ں���");
					if (charge == JOptionPane.YES_OPTION)
						serveTool.setPort(port);
				} else {
					JOptionPane.showMessageDialog(tArea, "�ö˿���Ϊ��Ȩ��������������");
					new portListener().actionPerformed(ae);
				}
			} catch (Exception ex) {

			}

		}
	}

	public static void main(String[] args) {
		Server server = new Server();
	}
}
