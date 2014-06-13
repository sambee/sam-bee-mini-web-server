package sam.bee.web.server;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import java.io.*;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ToolOfServer {
	ServerSocket serverSock;
	int port = 8000;
    boolean canWaite;
	boolean canAccept;
	JTextArea tArea;
	Accept acceptor;

	List<Runnable> unComplete;

	public ToolOfServer() {

	}

	/**
	 * 带文本域的构造方法
	 */
	public ToolOfServer(JTextArea tArea) {
		this.tArea = tArea;
	}

	/**
	 * 
	 * 设置文本域
	 */
	public void settArea(JTextArea tArea) {
		this.tArea = tArea;
	}

	/**
	 * 开启服务器服务功能
	 * 
	 * @return
	 */
	public boolean startServer() {
		try {
			serverSock = new ServerSocket(port);

			acceptor = new Accept(serverSock);
			canWaite = true;
			canAccept = true;
			acceptor.start();
			tArea.append("开启服务器,       时间:" + getTime() + "\n");

			return true;
		} catch (IOException ee) {
			ee.printStackTrace();
			JOptionPane.showMessageDialog(tArea, "发生错误");
			return false;
		}
	}

	/**
	 * 关闭服务器
	 */
	public void stopServer() {
		canAccept = false;
		try {
			serverSock.close();
			tArea.append("服务器关闭,       时间:" + getTime() + "\n");
		} catch (IOException e) {
			tArea.append("服务器未能成功关闭,请重启       时间:" + getTime() + "\n");
			e.printStackTrace();
		}

	}

	/**
	 * 得到时间后，转换格式返回
	 * */
	private String getTime() {
		Date date = new Date();
		// 转换格式后的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		return time;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private class Accept extends Thread {
		ServerSocket serverSock;
		Socket socket;

		ExecutorService executor = Executors.newCachedThreadPool();

		public Accept(ServerSocket serverSock) {
			this.serverSock = serverSock;
		}

		public void run() {

			String message = "";
			while (canAccept) {
				try {
					socket = serverSock.accept();
					executor.execute(new handleAClient(socket));
				} catch (Exception ex) {

				}
			}
			unComplete = executor.shutdownNow();
			try {

				serverSock.close(); // 关闭服务器接口
			} catch (Exception ex) {
				// System.out.println("dsfsd");
				JOptionPane.showMessageDialog(tArea, "服务器关闭出现异常，未能成功关闭");
			}

		}
	}

	/**
	 * 处理用户发来信息的类
	 */
	class handleAClient implements Runnable {
		ServerSocket serverSock;
		Socket socket;
		DataOutputStream out;
		DataInputStream in;

		public handleAClient(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				out = new DataOutputStream(socket.getOutputStream());
				in = new DataInputStream(socket.getInputStream());
				String fileName = in.readUTF();
			   // JOptionPane.showMessageDialog(tArea, fileName);
				
				 File file = new File(fileName);
				
				fileDirector(fileName);

			} catch (Exception e) {
				JOptionPane.showMessageDialog(tArea, "传输出现异常！！！");
			}
		}

		private void fileDirector(String fileName) {
			DataBase base = new DataBase();
		
			InetAddress address = socket.getInetAddress();
			String clientName = address.getHostName();
			String IP = address.getHostAddress();
			tArea.append("客户:  " + clientName + "  IP: " + IP + "   时间：" + getTime() + "\n");
			base.update(clientName, IP, getTime());
			try {

				File file = new File(fileName);
				RandomAccessFile raf = new RandomAccessFile(file, "rw");

				byte[] buffer = new byte[1024];
				int num = raf.read(buffer);
				while (num != -1) {
					out.write(buffer, 0, num);
					out.flush();
					num = raf.read(buffer);
				}
				raf.close();
				in.close();
				out.close();
				socket.close();
				tArea.append("文件传送成功！   " + "客户:  " + clientName + "  IP: " + IP + "   时间：" + getTime() + "\n");

			} catch (Exception e) {
				//e.printStackTrace();
				tArea.append("文件传送失败！！！   " + "客户:  " + clientName + "  IP: "
						+ IP + "   时间：" + getTime() + "\n");
				try {
					in.close();

					out.close();
					socket.close();
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}

		}

	}

}
