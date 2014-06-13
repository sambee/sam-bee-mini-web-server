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
	JMenuItem mi1 = new JMenuItem("保 存 文件");
	JMenuItem mi2 = new JMenuItem("打 开 文 件");

	JMenuItem mh1 = new JMenuItem("创 造 新 的 h t m l");
	JMenuItem mh2 = new JMenuItem("打 开  h t m l");
	boolean isStart = false;
	public static int client_num = 0;
	private int frame_xlocate = 400;
	private int frame_ylocate = 200;
	ToolOfServer serveTool;
	VisitLog visitlog;

	public Server() {
		try {
			init();// 初始化并显示界面
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLocation(frame_xlocate, frame_ylocate);
		this.setSize(new Dimension(500, 400));
		this.setTitle("迷 你 S e r v e r");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void init() {
		// 设置服务器面板
		this.setLayout(new BorderLayout());

		// 设置菜单栏
		JPanel top_pane = new JPanel();
		top_pane.setLayout(new BorderLayout());
		JMenuBar MenuBar = new JMenuBar();
		JMenu mFile = new JMenu("文 件");

		JMenuItem mi3 = new JMenuItem("Exit");
		mFile.add(mi1);
		mFile.add(mi2);
		mFile.add(mi3);
		MenuBar.add(mFile);
		JMenu mHtml = new JMenu("H t m l");
		mHtml.add(mh1);
		mHtml.add(mh2);
		MenuBar.add(mHtml);
		JMenu visitlogm = new JMenu("历 史 记 录");
		JMenuItem visitlog = new JMenuItem("访 问 历 史 记 录");
		JMenu set = new JMenu("设置");
		JMenuItem setPort = new JMenuItem("端 口 设 置");
		set.add(setPort);
		visitlogm.add(visitlog);
		MenuBar.add(visitlogm);
		MenuBar.add(set);
		top_pane.add(MenuBar, BorderLayout.NORTH);

		// 设置保存文件按钮监听器
		mi1.addActionListener(new saveFileListener());

		// 设置打开文件按钮监听器
		mi2.addActionListener(new openFileListener());

		// 设置Exit监听器
		mi3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});

		// 设置创造新的HTML文件按钮监听器
		mh1.addActionListener(new makeNewHTMLListener());

		// 设置打开HTML文件按钮的监听器
		mh2.addActionListener(new openHTMLListener());

		// 设置端口设置按钮的监听器
		setPort.addActionListener(new portListener());

		// 设置开启服务器按钮监听器
		start = new JButton("开 启 服 务 器");
		startListener startl = new startListener();
		start.addActionListener(startl);

		// 设置关闭服务器按钮监听器
		stop = new JButton("关 闭 服 务 器");
		stop.setEnabled(false);
		stopListener stopl = new stopListener();
		stop.addActionListener(stopl);

		// 设置历史记录的监听器
		visitlog.addActionListener(new visitlogListener());
		JPanel hold_s = new JPanel();
		hold_s.add(start);
		hold_s.add(stop);
		top_pane.add(hold_s, BorderLayout.SOUTH);
		add(top_pane, BorderLayout.NORTH);

		// 设置服务器文字域
		tArea = new JTextArea();
		jscroll = new JScrollPane(tArea);
		add(jscroll, BorderLayout.CENTER);

		// 设置服务器签名
		final JLabel lblServerStatus = new JLabel("M i n i  服 务 器");
		add(lblServerStatus, BorderLayout.SOUTH);

		// 设置服务器头像
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		setSize(width / 2, height / 2);
		setLocation(width / 4, height / 4);
		Image img = kit.getImage("image/龙.png");
		setIconImage(img);

		// 设置文本域不可编辑
		tArea.setEditable(false);
		serveTool = new ToolOfServer(tArea);
	}

	/**
	 * 开启服务器按钮监听器类
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
				tArea.append("开启成功   +\n");
			} else {
				// failed
				tArea.append("开启失败   +\n");
			}
		}
	}

	/**
	 * 关闭服务器按钮监听器类
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
	 * 查看历史记录监听器类
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
	 * 保存文件监听器类
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
	 * 打开文件监听器类
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
	 * 创造新的HTML监听器类
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
	 * html的监听器
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
	 * 端口按钮的监听器
	 * 
	 * @author lenovo
	 * 
	 */
	class portListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			try {
				int port = Integer.parseInt(JOptionPane.showInputDialog(tArea, "请输入新的端口号", "8000"));
				if (port > 1024) {
					int charge = JOptionPane.showConfirmDialog(tArea, "确认要修改服务器端口号吗？");
					if (charge == JOptionPane.YES_OPTION)
						serveTool.setPort(port);
				} else {
					JOptionPane.showMessageDialog(tArea, "该端口已为特权服务，请重新输入");
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
