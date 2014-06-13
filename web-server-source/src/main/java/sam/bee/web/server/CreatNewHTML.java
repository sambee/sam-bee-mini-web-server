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

	JButton save = new JButton("�� �� ");
	JButton clear = new JButton("�� ��");

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

		// ���ñ��水ť�ļ�����
		save.addActionListener(new saveHTMLListener());

		// ������հ�ť�ļ�����
		clear.addActionListener(new clearListener());
		
		this.setSize(new Dimension(500, 400));
		this.setTitle("�� �� �� �� H T M L");
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	// �����ļ���������
	class saveHTMLListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(".//�ļ�"));

			chooser.setDialogTitle("�� �� H T M L �� ��");
			File file;
			String fileName;
			try {
				int result = chooser.showSaveDialog(save);

				// �������ȷ����ť�����ø��ļ���
				if (result == JFileChooser.APPROVE_OPTION) {
					// ��ø��ļ�
					file = chooser.getSelectedFile();
					// ����ļ�����ʽ����ȷ�������±��棻
					if (file.exists()) {
						int overwriteSelect = JOptionPane.showConfirmDialog(
								chooser, file.getPath() + " �Ѵ��ڡ�\nҪ�滻����",
								"���Ϊ", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
						if (overwriteSelect != JOptionPane.YES_OPTION) {
							JOptionPane.showMessageDialog(chooser, "�����������ļ���");
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
									"�����������ļ�����,�ļ�����Ϊhtml");
							file.delete();
							saveHTMLListener resetFileName = new saveHTMLListener();
							resetFileName.actionPerformed(ae);
							return;
						} else {

							if (!fileName.substring(fileName.length() - 5,
									fileName.length()).equals(".html")) {
								JOptionPane.showMessageDialog(chooser,
										"�����������ļ����ͣ��ļ�����Ϊhtml");
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
								JOptionPane.showMessageDialog(chooser, "�����쳣");
							}
						}

						bw.close();
						JOptionPane.showMessageDialog(tArea, "����ɹ�");
					}

				}
			} catch (HeadlessException head) {
				JOptionPane.showMessageDialog(chooser, "�����쳣");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(chooser, "�����쳣");
			}

		}
	}

	class clearListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			tArea.setText(null);
		}
	}
}
