package bobby;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;

@InjectApplicationScope
@Path("/UserProduct")
public class UserProduct
{
private ApplicationScope applicationScope;
public void setApplicationScope(ApplicationScope applicationScope)
{
this.applicationScope=applicationScope;
}
@Path("/add")
@Get
//@Forward("/item/addItem")
public void addProduct()
{
int x=10;
applicationScope.setAttribute("pqr",x);
applicationScope.setAttribute("xyz","Some data");
}


@Path("/list")
@Post
public void listProduct()
{

}

}

class Student
{
public void add()
{
}
public void get()
{
}
public void edit()
{
}
public void delete()
{
}
}

class Teacher
{
public void add()
{
}
public void get()
{
}
public void edit()
{
}
public void delete()
{
}

}