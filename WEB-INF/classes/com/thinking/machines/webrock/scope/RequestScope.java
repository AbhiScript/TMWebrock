package com.thinking.machines.webrock.scope;
import javax.servlet.*;
import javax.servlet.http.*;
public class RequestScope
{
private HttpServletRequest httpServletRequest;
public void setAttribute(String name,Object object)
{
this.httpServletRequest.setAttribute(name,object);
}
public void setAttribute(HttpServletRequest httpServletRequest)
{
this.httpServletRequest=httpServletRequest;
}
public Object getAttribute(String name)
{
return this.httpServletRequest.getAttribute(name);
}

}