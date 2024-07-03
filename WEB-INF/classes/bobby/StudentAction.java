package bobby;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.*;

import java.sql.*;
import com.google.gson.*;
import java.util.*;

@Path("/studentAction")
@CreateService(isJSON=true)
public class StudentAction
{

@Path("/add")
@Post
public String add(Student student)
{
if(student==null) return "{}";
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
String sql="insert into studenttable (name,gender) values (?,?)";
preparedStatement=connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1,student.getName());
String gender = student.getGender();
if(gender.length() > 1)
{
throw new SQLException("Gender field exceeds maximum allowed length.");
}
preparedStatement.setString(2,student.getGender());
Gson gson=new Gson();
System.out.println("Student data : "+gson.toJson(student));

System.out.println(sql);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();
}catch(Exception se)
{
System.out.println(se);
return "{}";
}
Gson gson=new Gson();
return gson.toJson(student);
}

@Path("/update")
@Post
public void update(Student student)
{
if(student==null) return;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
String sql="UPDATE studenttable SET name=?,gender=? WHERE rollNo=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setString(1,student.getName());
preparedStatement.setString(2,student.getGender());
preparedStatement.setInt(3,student.getRollNo());
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();

}catch(Exception se)
{
System.out.println(se);
}
}

@Path("/delete")
@Post
public void Delete(@RequestParameter("id")int rollNo)
{
if(rollNo==0) return;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
String sql="DELETE FROM studenttable WHERE rollNo=?";
preparedStatement=connection.prepareStatement(sql);
preparedStatement.setInt(1,rollNo);
preparedStatement.executeUpdate();
preparedStatement.close();
connection.close();

}catch(Exception se)
{
System.out.println(se);
}
}

@Path("/getById")
@Get
public String getById(@RequestParameter("id")int rollNo)
{
Student student=new Student();
try
{
Connection connection=DAOConnection.getConnection();
String sql="SELECT * FROM studenttable WHERE rollNo=?";
PreparedStatement preparedStatement=connection.prepareStatement(sql);
preparedStatement.setInt(1,rollNo);
ResultSet resultSet=preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
preparedStatement.close();
connection.close();
String s="Invalid id : "+rollNo+" not found";
Gson gson=new Gson();
return gson.toJson(s);
}
student.setRollNo(resultSet.getInt("rollNo"));
student.setName(resultSet.getString("name"));
student.setGender(resultSet.getString("gender"));
resultSet.close();
preparedStatement.close();
connection.close();
}catch(Exception e)
{
System.out.println(e);
}
Gson gson=new Gson();
return gson.toJson(student);
}

@Path("/getAll")
@Get
public String getAll()
{
List<Student> list=new ArrayList<>();
try
{
Connection connection=DAOConnection.getConnection();
Statement statement=connection.createStatement();
ResultSet resultSet=statement.executeQuery("select * from studenttable");
Student student;
while(resultSet.next())
{
student=new Student();
student.setRollNo(resultSet.getInt("rollNo"));
student.setName(resultSet.getString("name").trim());
student.setGender(resultSet.getString("gender").trim());
list.add(student);
}
resultSet.close();
statement.close();
connection.close();
}catch(Exception se)
{
System.out.println(se);
}
Gson gson=new Gson();
return gson.toJson(list);
}

}