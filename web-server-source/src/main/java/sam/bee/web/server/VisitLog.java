package sam.bee.web.server;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class VisitLog extends JFrame {

	private JTable table;

	public VisitLog() {
		init();
		this.setSize(new Dimension(500, 400));
		this.setTitle("��ʷ��¼");
		this.setLocationRelativeTo(null);
        
		this.setVisible(true);
	}

	public void init() {
		try {
			this.setLayout(new BorderLayout());
			String[] columnName={" �û���","IP","����ʱ��"};
			String[][] data={{"�ͻ���","IP��ַ","ʱ��"}};
			table=new JTable(data,columnName);
			DataBase base = new DataBase();
			base.initial();
			table = base.getTable();
			JScrollPane scrollPane = new JScrollPane(table);
			

			Scanner scan = new Scanner(new File("�ļ�/������������ʷ��¼.txt"));

			
			this.add(scrollPane, BorderLayout.CENTER);

			JButton printIt = new JButton("�� ӡ �� ʷ �� ¼");
			this.add(printIt, BorderLayout.NORTH);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(this, "�����쳣");
		}

	}

	class printListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			try {
				table.print();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
