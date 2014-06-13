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

		// ��ʼ����html�ļ��Ի����ļ�����ʾ���ı�����
		this.setLayout(new BorderLayout());
        
		//���ļ��Ի�����ѡ���ļ���Ȼ����ļ�����д���ı���
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(".//�ļ�"));
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

				JOptionPane.showMessageDialog(chooser, "�����쳣");
			}

		}
		JPanel jpane = new JPanel();
		JButton change = new JButton("�޸�");
		save = new JButton("����");
		anotherSave = new JButton("���Ϊ");
		jpane.add(change);
		jpane.add(save);
		jpane.add(anotherSave);
		this.add(jpane, BorderLayout.NORTH);
		tArea.setEditable(false);
		this.add(tArea, BorderLayout.CENTER);

		// �����޸İ�ť������
		change.addActionListener(new changeListener());

		// ���ñ��水ť������
		save.addActionListener(new saveListener());

		// �������Ϊ��ť������
		anotherSave.addActionListener(new anotherSaveListener());
		
		this.setSize(new Dimension(500, 400));
		this.setTitle("�� �� �� �� H T M L");
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	//����޸İ�ť�󣬽�ʹ�ı����ÿɱ༭
	class changeListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			tArea.setEditable(true);

		}
	}

	// ��������������¼������Ὣ�ı����ڵ�����д��ԭ�ļ�
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
						JOptionPane.showMessageDialog(tArea, "�����쳣");
					}
				}

				bw.close();
				JOptionPane.showMessageDialog(tArea, "����ɹ�");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(tArea, "�����쳣");
			}

		}
	}

	// ���ı����ڵ����ݴ���һ���µ��ļ���
	class anotherSaveListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {

			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(".//�ļ�"));

			chooser.setDialogTitle("�� �� H T M L �� ��");
			File file;
			String fileName;

			int result = chooser.showSaveDialog(anotherSave);

			// �������ȷ����ť�����ø��ļ���
			try {
				if (result == JFileChooser.APPROVE_OPTION) {
					// ��ø��ļ�
					file = chooser.getSelectedFile();
					// ����ļ�����ʽ����ȷ�������±��棻
					if (file.exists()) {
						int overwriteSelect = JOptionPane.showConfirmDialog(
								chooser, file.getPath() + " �Ƿ񸲸�ԭ�ļ�", "���Ϊ",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
						if (overwriteSelect != JOptionPane.YES_OPTION) {
							JOptionPane.showMessageDialog(chooser, "�����������ļ���");
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
									"�����������ļ�����,�ļ�����Ϊhtml");
							file.delete();
							anotherSaveListener resetFileName = new anotherSaveListener();
							resetFileName.actionPerformed(ae);
							return;
						} else {

							if (!fileName.substring(fileName.length() - 5,
									fileName.length()).equals(".html")) {
								JOptionPane.showMessageDialog(chooser,
										"�����������ļ����ͣ��ļ�����Ϊhtml");
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
								JOptionPane.showMessageDialog(chooser, "�����쳣");
							}
						}

						bw.close();
						JOptionPane.showMessageDialog(tArea, "����ɹ�");
					}

				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(chooser, "�����쳣");
			}

		}

	}

}
