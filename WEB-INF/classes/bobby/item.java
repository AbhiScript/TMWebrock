package bobby;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.*;

@Path("/item")
public class item
{


@Get
@Path("/addItem")
public String addItem(@getSessionScope("abc")String e,@getApplicationScope("pqr")int f)
{
System.out.println("Something");
System.out.println("request parameter value : "+e);
System.out.println("request parameter value : "+f);
return e;
}

@Post
@Path("/removeItem")
public String removeItem()
{
return "class : (ITEM), method : remove Item";
}


}