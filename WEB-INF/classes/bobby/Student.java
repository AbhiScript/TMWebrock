package bobby;
import com.thinking.machines.webrock.annotations.*;

@CreatePOJO
public class Student implements java.io.Serializable
{
private int rollNo;
private String name;
private String gender;

public void setRollNo(int rollNo)
{
this.rollNo=rollNo;
}
public int getRollNo()
{
return this.rollNo;
}

public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}

public void setGender(String gender)
{
this.gender=gender;
}
public String getGender()
{
return this.gender;
}

}