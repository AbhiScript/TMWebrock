package bobby;
import com.thinking.machines.webrock.*;
import java.sql.*;
public class DAOConnection
{
private DAOConnection(){}
public static Connection getConnection() throws ServiceException
{
Connection connection=null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb","admin","admin");
}catch(Exception e)
{
throw new ServiceException(e.getMessage());
}
return connection;
}
 
}