package com.thinking.machines.webrock.scope;
import javax.servlet.*;
import javax.servlet.http.*;

public class ApplicationScope
{
private ServletContext servletContext;

public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
public void setAttribute(String name,Object object)
{
if (servletContext != null && name != null && object != null) {
servletContext.setAttribute(name, object);
} else {
System.out.println("ServletContext or attribute details are null. Cannot set attribute.");
}
}
public void setAttribute(ServletContext servletContext)
{
this.servletContext=servletContext;
}
public Object getAttribute(String name)
{
if (servletContext != null) {
            return servletContext.getAttribute(name);
        } else {
            System.out.println("ServletContext is null. Cannot get attribute.");
            return null;
        }
}

}