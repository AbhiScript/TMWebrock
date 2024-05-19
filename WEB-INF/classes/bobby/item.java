package bobby;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.*;

@Path("/item")
public class item
{


@Get
@Path("/addItem")
public String addItem(@RequestParameter("xyz")String e,@RequestParameter("pqr")int f)
{
System.out.println("Something");
System.out.println("request parameter value : "+e);
System.out.println("request parameter value : "+f);
return "Add item got called";
}

@Post
@Path("/removeItem")
public String removeItem()
{
return "class : (ITEM), method : remove Item";
}


}