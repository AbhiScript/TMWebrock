package com.thinking.machines.webrock.tokenManager;
import javax.servlet.http.*;
public class RequestContextHolder
{
private static final ThreadLocal<HttpServletRequest> requestHolder=new ThreadLocal<>();
private static final ThreadLocal<HttpServletResponse> responseHolder=new ThreadLocal<>();

public static void setRequest(HttpServletRequest request)
{
requestHolder.set(request);
}
public static HttpServletRequest getRequest()
{
return requestHolder.get();
}
public static void clearRequest()
{
requestHolder.remove();
}

public static void setResponse(HttpServletResponse response)
{
responseHolder.set(response);
}
public static HttpServletResponse getResponse()
{
return responseHolder.get();
}
public static void clearResponse()
{
requestHolder.remove();
}

public static void clear()
{
clearRequest();
clearResponse();
}

}