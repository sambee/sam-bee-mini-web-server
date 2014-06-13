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
	 * ���ı���Ĺ��췽��
	 */
	public ToolOfServer(JTextArea tArea) {
		this.tArea = tArea;
	}

	/**
	 * 
	 * �����ı���
	 */
	public void settArea(JTextArea tArea) {
		this.tArea = tArea;
	}

	/**
	 * ����������������
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
			tArea.append("����������,       ʱ��:" + getTime() + "\n");

			return true;
		} catch (IOException ee) {
			ee.printStackTrace();
			JOptionPane.showMessageDialog(tArea, "��������");
			return false;
		}
	}

	/**
	 * �رշ�����
	 */
	public void stopServer() {
		canAccept = false;
		try {
			serverSock.close();
			tArea.append("�������ر�,       ʱ��:" + getTime() + "\n");
		} catch (IOException e) {
			tArea.append("������δ�ܳɹ��ر�,������       ʱ��:" + getTime() + "\n");
			e.printStackTrace();
		}

	}

	/**
	 * �õ�ʱ���ת����ʽ����
	 * */
	private String getTime() {
		Date date = new Date();
		// ת����ʽ���ʱ��
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

				serverSock.close(); // �رշ������ӿ�
			} catch (Exception ex) {
				// System.out.println("dsfsd");
				JOptionPane.showMessageDialog(tArea, "�������رճ����쳣��δ�ܳɹ��ر�");
			}

		}
	}

	/**
	 * �����û�������Ϣ����
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
				JOptionPane.showMessageDialog(tArea, "��������쳣������");
			}
		}

		private void fileDirector(String fileName) {
			DataBase base = new DataBase();
		
			InetAddress address = socket.getInetAddress();
			String clientName = address.getHostName();
			String IP = address.getHostAddress();
			tArea.append("�ͻ�:  " + clientName + "  IP: " + IP + "   ʱ�䣺" + getTime() + "\n");
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
				tArea.append("�ļ����ͳɹ���   " + "�ͻ�:  " + clientName + "  IP: " + IP + "   ʱ�䣺" + getTime() + "\n");

			} catch (Exception e) {
				//e.printStackTrace();
				tArea.append("�ļ�����ʧ�ܣ�����   " + "�ͻ�:  " + clientName + "  IP: "
						+ IP + "   ʱ�䣺" + getTime() + "\n");
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
