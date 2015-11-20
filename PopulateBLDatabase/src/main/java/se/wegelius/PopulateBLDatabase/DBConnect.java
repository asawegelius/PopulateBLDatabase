package se.wegelius.PopulateBLDatabase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	private static Connection con = null;
	
	private DBConnect(){		
	}
	
	public static Connection getConnection(){		
		//If instance has not been created yet, create it
		if(DBConnect.con == null){
			initConnection();
		}
		return DBConnect.con;
	}
	
	//Gets JDBC connection instance
	private static void initConnection(){			
		try{		
			Class.forName("com.mysql.jdbc.Driver");	
			//localhost
			//188.181.85.75
			String url = "jdbc:mysql://localhost/businesslocator";
			String user = "locatorAdmin";
			String pw = "!blDbpw1";
			DBConnect.con = DriverManager.getConnection(url, user, pw);		
		}
		catch (ClassNotFoundException e){		
			System.out.println("class not found " + e.getMessage());
			System.exit(0);
		}
		catch (SQLException e){			
			System.out.println("sql exception " + e.getMessage());
			System.exit(0);
		}
		catch (Exception e){	
			System.out.println("exception " +e.getMessage());
		}		
	}
}


