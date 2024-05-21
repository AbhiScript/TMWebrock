package bobby;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.*;

@InjectSessionScope
@Path("/Employee")
public class Employee
{
public SessionScope sessionScope;
public void setSessionScope(SessionScope sessionScope)
{
this.sessionScope=sessionScope;
}

@Path("/addEmployee")
public void addEmployee()
{
String e="Employee Added";
sessionScope.setAttribute("abc",e);

}

}