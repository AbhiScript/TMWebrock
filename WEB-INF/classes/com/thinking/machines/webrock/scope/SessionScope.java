package com.thinking.machines.webrock.scope;
import javax.servlet.*;
import javax.servlet.http.*;

public class SessionScope
{
private HttpSession httpSession;
public void setHttpSession(HttpSession httpSession)
{
this.httpSession=httpSession;
}
public HttpSession getHttpSession()
{
return this.httpSession;
}
public void setAttribute(String name,Object object)
{
if(httpSession!=null)
{
this.httpSession.setAttribute(name,object);
}
}
public Object getAttribute(String name)
{
return this.httpSession.getAttribute(name);
}
}