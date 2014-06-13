package sam.bee.web.server;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class OpenHTML extends JFrame {
	/**
	 * 
	 */
	
	static JMenuItem open;
	File file;
	private JTextArea tArea = new JTextArea();
	JButton save;
	JButton anotherSave;

	public OpenHTML() {
		init();

	}

	public void init() {

		// 初始化打开html文件对话框及文件，显示到文本域上
		this.setLayout(new BorderLayout());
        
		//从文件对话框中选择文件，然后见文件内容写入文本域
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(".//文件"));
		int result = chooser.showOpenDialog(open);
		if (result == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while (br.ready()) {
					String str = br.readLine();
					
					tArea.append(str+"\n");
				}
				br.close();
			} catch (Exception e) {

				JOptionPane.showMessageDialog(chooser, "出现异常");
			}

		}
		JPanel jpane = new JPanel();
		JButton change = new JButton("修改");
		save = new JButton("保存");
		anotherSave = new JButton("另存为");
		jpane.add(change);
		jpane.add(save);
		jpane.add(anotherSave);
		this.add(jpane, BorderLayout.NORTH);
		tArea.setEditable(false);
		this.add(tArea, BorderLayout.CENTER);

		// 设置修改按钮监听器
		change.addActionListener(new changeListener());

		// 设置保存按钮监听器
		save.addActionListener(new saveListener());

		// 设置另存为按钮监听器
		anotherSave.addActionListener(new anotherSaveListener());
		
		this.setSize(new Dimension(500, 400));
		this.setTitle("创 造 新 的 H T M L");
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	//点击修改按钮后，将使文本域变得可编辑
	class changeListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			tArea.setEditable(true);

		}
	}

	// 保存监听器遇到事件，将会将文本域内的内容写入原文件
	class saveListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			String fileName = file.getName();
			file.delete();
			file = new File(fileName);
			try {
				file.createNewFile();
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file)));

				tArea.append("\0");
				for (int i = 0; i < tArea.getLineCount(); i++) {
					try {
						bw.write(tArea.getText(
								tArea // getText(int offset,int length)
								.getLineStartOffset(i),
								tArea.getLineEndOffset(i)
										- tArea.getLineStartOffset(i) - 1));
						bw.newLine();
					} catch (BadLocationException ex) {
						JOptionPane.showMessageDialog(tArea, "出现异常");
					}
				}

				bw.close();
				JOptionPane.showMessageDialog(tArea, "保存成功");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(tArea, "出现异常");
			}

		}
	}

	// 将文本域内的内容存入一个新的文件内
	class anotherSaveListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {

			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(".//文件"));

			chooser.setDialogTitle("保 存 H T M L 文 件");
			File file;
			String fileName;

			int result = chooser.showSaveDialog(anotherSave);

			// 如果按下确定按钮，则获得该文件。
			try {
				if (result == JFileChooser.APPROVE_OPTION) {
					// 获得该文件
					file = chooser.getSelectedFile();
					// 如果文件名格式不正确，则重新保存；
					if (file.exists()) {
						int overwriteSelect = JOptionPane.showConfirmDialog(
								chooser, file.getPath() + " 是否覆盖原文件", "另存为",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
						if (overwriteSelect != JOptionPane.YES_OPTION) {
							JOptionPane.showMessageDialog(chooser, "请重新输入文件名");
							file.delete();
							anotherSaveListener resetFileName = new anotherSaveListener();
							resetFileName.actionPerformed(ae);
							return;
						} else {
							file = new File(chooser.getCurrentDirectory()
									+ "\\" + file.getName());
						}
					} else {

						fileName = file.getName();
						if (fileName.length() < 6) {
							JOptionPane.showMessageDialog(chooser,
									"请重新输入文件类型,文件类型为html");
							file.delete();
							anotherSaveListener resetFileName = new anotherSaveListener();
							resetFileName.actionPerformed(ae);
							return;
						} else {

							if (!fileName.substring(fileName.length() - 5,
									fileName.length()).equals(".html")) {
								JOptionPane.showMessageDialog(chooser,
										"请重新输入文件类型，文件类型为html");
								file.delete();
								anotherSaveListener resetFileName = new anotherSaveListener();
								resetFileName.actionPerformed(ae);
								return;
							} else {
								file = new File(chooser.getCurrentDirectory()
										+ "\\" + fileName);

							}
						}
						file.createNewFile();

						BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(
										file)));
						int n = tArea.getRows();
						tArea.append("\0");
						for (int i = 0; i < tArea.getLineCount(); i++) {
							try {
								bw.write(tArea.getText(
										tArea // getText(int offset,int length)
										.getLineStartOffset(i),
										tArea.getLineEndOffset(i)
												- tArea.getLineStartOffset(i)
												- 1));
								bw.newLine();
							} catch (BadLocationException ex) {
								JOptionPane.showMessageDialog(chooser, "出现异常");
							}
						}

						bw.close();
						JOptionPane.showMessageDialog(tArea, "保存成功");
					}

				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(chooser, "出现异常");
			}

		}

	}

}
