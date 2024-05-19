package bobby;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;

@InjectRequestScope
@Path("/UserProduct")
public class UserProduct
{
private RequestScope requestScope;
public void setRequestScope(RequestScope requestScope)
{
this.requestScope=requestScope;
}
@Path("/add")
@Get
@Forward("/item/addItem")
public void addProduct()
{
int x=10;
requestScope.setAttribute("pqr",x);
requestScope.setAttribute("xyz","Some data");
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