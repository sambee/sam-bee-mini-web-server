package sam.bee.web.server;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.*;

import javax.swing.text.BadLocationException;

public class CreatNewHTML extends JFrame {
	/**
	 * 
	 */


	private JTextArea tArea = new JTextArea();

	JButton save = new JButton("保 存 ");
	JButton clear = new JButton("清 空");

	public CreatNewHTML() {
		init();
		
	}

	public void init() {
		this.setLayout(new BorderLayout());

		JPanel jPane = new JPanel();
		jPane.add(save);
		jPane.add(clear);
		this.add(jPane, BorderLayout.NORTH);
		this.add(tArea, BorderLayout.CENTER);

		// 设置保存按钮的监听器
		save.addActionListener(new saveHTMLListener());

		// 设置清空按钮的监听器
		clear.addActionListener(new clearListener());
		
		this.setSize(new Dimension(500, 400));
		this.setTitle("创 造 新 的 H T M L");
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	// 保存文件监听器类
	class saveHTMLListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(".//文件"));

			chooser.setDialogTitle("保 存 H T M L 文 件");
			File file;
			String fileName;
			try {
				int result = chooser.showSaveDialog(save);

				// 如果按下确定按钮，则获得该文件。
				if (result == JFileChooser.APPROVE_OPTION) {
					// 获得该文件
					file = chooser.getSelectedFile();
					// 如果文件名格式不正确，则重新保存；
					if (file.exists()) {
						int overwriteSelect = JOptionPane.showConfirmDialog(
								chooser, file.getPath() + " 已存在。\n要替换它吗？",
								"另存为", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
						if (overwriteSelect != JOptionPane.YES_OPTION) {
							JOptionPane.showMessageDialog(chooser, "请重新输入文件名");
							file.delete();
							saveHTMLListener resetFileName = new saveHTMLListener();
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
							saveHTMLListener resetFileName = new saveHTMLListener();
							resetFileName.actionPerformed(ae);
							return;
						} else {

							if (!fileName.substring(fileName.length() - 5,
									fileName.length()).equals(".html")) {
								JOptionPane.showMessageDialog(chooser,
										"请重新输入文件类型，文件类型为html");
								file.delete();
								saveHTMLListener resetFileName = new saveHTMLListener();
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
			} catch (HeadlessException head) {
				JOptionPane.showMessageDialog(chooser, "出现异常");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(chooser, "出现异常");
			}

		}
	}

	class clearListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			tArea.setText(null);
		}
	}
}
