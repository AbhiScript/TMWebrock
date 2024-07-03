package com.thinking.machines.webrock.tokenManager;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class RequestContextFilter implements Filter
{
@Override
public void init(FilterConfig filterConfig) throws ServletException {}

@Override
public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException,ServletException
{
try
{
RequestContextHolder.setRequest((HttpServletRequest) request);
RequestContextHolder.setResponse((HttpServletResponse) response);
}finally
{
RequestContextHolder.clear();
}

}

@Override
public void destroy(){}
}