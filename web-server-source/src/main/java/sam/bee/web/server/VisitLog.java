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
		this.setTitle("历史记录");
		this.setLocationRelativeTo(null);
        
		this.setVisible(true);
	}

	public void init() {
		try {
			this.setLayout(new BorderLayout());
			String[] columnName={" 用户名","IP","访问时间"};
			String[][] data={{"客户名","IP地址","时间"}};
			table=new JTable(data,columnName);
			DataBase base = new DataBase();
			base.initial();
			table = base.getTable();
			JScrollPane scrollPane = new JScrollPane(table);
			

			Scanner scan = new Scanner(new File("文件/服务器访问历史记录.txt"));

			
			this.add(scrollPane, BorderLayout.CENTER);

			JButton printIt = new JButton("打 印 历 史 记 录");
			this.add(printIt, BorderLayout.NORTH);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(this, "出现异常");
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
