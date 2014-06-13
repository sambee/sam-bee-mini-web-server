package sam.bee.web.server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JTable;

public class DataBase { 
	
	PreparedStatement ps;
	Connection connection;
	String getTable = "select * from visitlog";
	private final static String createTable= "";
	private final static String updateTable = "insert into visitlog (Name,IP,Time)" + "values(?,?,?)";
	JTable table;
	
	
	public DataBase(String jdbc, String username, String password){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbc,username,password);
		}catch(Exception ex){
			 ex.printStackTrace();
		}
	}
	
	public void update(String Host,String IP,String date){
		try{
			ps = connection.prepareStatement(updateTable);
			ps.setString(1, Host);
			ps.setString(2, IP);
			ps.setString(3, date);
			ps.executeUpdate();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public JTable getTable(){
		String[][] str = new String[300][3];
		String[] Name = {"Name","IP","Time"};
		int row = 0;
		   try{
			   Statement statement = connection.createStatement();
			   ResultSet resultSet = statement.executeQuery(getTable);
			   while(resultSet.next()){
					   for(int j=0;j<3;j++){
						   str[row][j] = resultSet.getString(j+1);
					   }
					   row++;
			   }
			   table = new JTable(str,Name);
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		   return table;
	}
}
