package com.thinking.machines.webrock.tokenManager;
import javax.servlet.*;
import javax.servlet.http.*;
import java.security.SecureRandom;
import com.thinking.machines.webrock.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.annotations.*;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class Token
{
private static Token instance;
private boolean isAllowed=false;
private Token() {}

public static Token getInstance()
{
if(instance==null) instance=new Token();
return instance;
}

public double generateToken()
{
double randomNumber=Math.random();
randomNumber=randomNumber*1000000;
return randomNumber;
}

public boolean isTokenApplied()
{
return this.isAllowed;
}

public void setToken(String methodName,Double token)
{
Service service=null;
this.isAllowed=true;
HttpSession httpSession=null;
HttpServletRequest request=RequestContextHolder.getRequest();
SecurityModel securityModel=new SecurityModel();
if(request!=null)
{
ServletContext servletContext=request.getServletContext();
WebRockModel webRockModel=(WebRockModel)servletContext.getAttribute("webRockModel");
String serviceURL="";
for(Map.Entry<String,Service> map : webRockModel.serviceMap.entrySet())
{
serviceURL=map.getKey();
service=map.getValue();
if(methodName.equals(service.getService().getName()))
{
break;
}
}
try
{
Class<?> c=service.getServiceClass();
String scopeApplied="";
scopeApplied=scopeExcecution(service,request);
Object instance=c.getDeclaredConstructor().newInstance();
Method method=null;

Method[] methods=c.getDeclaredMethods();
Class<?>[] requestScopeParameterType={RequestScope.class};
Class<?>[] sessionScopeParameterType={SessionScope.class};
Class<?>[] applicationScopeParameterType={ApplicationScope.class};

boolean methodExecuted=false;

for(Method methodWithParameter : methods)
{
if(methodName.equals(methodWithParameter.getName()))
{
try
{
Method requestScopeParameter=c.getDeclaredMethod(methodName,requestScopeParameterType);
requestScopeParameter.setAccessible(true);
requestScopeParameter.invoke(instance,new RequestScope());
securityModel.securityMap.put(requestScopeParameter,token);
request.setAttribute(methodName,securityModel);
methodExecuted=true;
}catch(NoSuchMethodException e)
{
try
{
Method sessionScopeParameter=c.getDeclaredMethod(methodName,sessionScopeParameterType);
sessionScopeParameter.setAccessible(true);
sessionScopeParameter.invoke(instance,new SessionScope());
securityModel.securityMap.put(sessionScopeParameter,token);
httpSession=request.getSession();
httpSession.setAttribute(methodName,securityModel);
methodExecuted=true;
}catch(NoSuchMethodException N)
{
try
{
Method applicationScopeParameter=c.getDeclaredMethod(methodName,applicationScopeParameterType);
applicationScopeParameter.setAccessible(true);
applicationScopeParameter.invoke(instance,new ApplicationScope());
securityModel.securityMap.put(method,token);
ServletContext applicationServletContext=request.getServletContext();
applicationServletContext.setAttribute(methodName,securityModel);
methodExecuted=true;
}catch(NoSuchMethodException S)
{
throw new ServiceException("Method : "+methodName+" with specified parameters not found");
}// application parameter
}// session parameter
}// request parameter

}// if block

} // for loop

if(methodExecuted)
{
method=null;
}
else
{
method=c.getDeclaredMethod(methodName);
}

if(method!=null)
{
if("session".equals(scopeApplied))
{
method.setAccessible(true);
method.invoke(instance);
securityModel.securityMap.put(method,token);
httpSession=request.getSession();
httpSession.setAttribute(methodName,securityModel);
}else if("application".equals(scopeApplied))
{
method.setAccessible(true);
method.invoke(instance);
securityModel.securityMap.put(method,token);
ServletContext applicationServletContext=request.getServletContext();
applicationServletContext.setAttribute(methodName,securityModel);
}else if("request".equals(scopeApplied))
{
method.setAccessible(true);
method.invoke(instance);
securityModel.securityMap.put(method,token);
request.setAttribute(methodName,securityModel);
}else
{
method.setAccessible(true);
method.invoke(instance);
securityModel.securityMap.put(method,token);
httpSession=request.getSession();
httpSession.setAttribute(methodName,securityModel);
}


}// if method(without parameter) is not null

}catch(Exception e)
{
e.printStackTrace();
}
}

} // setToken

private String scopeExcecution(Service service,HttpServletRequest request)
{
Class<?> c=null;
Method method=null;
try
{
if(service.getInjectSessionScope())
{
SessionScope sessionScope=new SessionScope();
HttpSession httpSession=request.getSession();
sessionScope.setHttpSession(httpSession);
c=service.getServiceClass();
method=c.getMethod("setSessionScope",SessionScope.class);
Object object=c.newInstance();
method.invoke(object,sessionScope);
Method m=service.getService();
m.invoke(object);
return "session";
} // session scope
if(service.getInjectApplicationScope())
{
ApplicationScope applicationScope=new ApplicationScope();
ServletContext applicationServletContext=request.getServletContext();
applicationScope.setAttribute(applicationServletContext);
c=service.getServiceClass();
method=c.getMethod("setApplicationScope",ApplicationScope.class);
Object object=c.newInstance();
method.invoke(object,applicationScope);
Method m=service.getService();
m.invoke(object);
return "application";
}// application scope
if(service.getInjectRequestScope())
{
RequestScope requestScope=new RequestScope();
requestScope.setAttribute(request);
c=service.getServiceClass();
method=c.getMethod("setRequestScope",RequestScope.class);
Object object=c.newInstance();
method.invoke(object,requestScope);
Method m=service.getService();
m.invoke(object);
return "request";
}// request scope
}catch(Exception e)
{
e.printStackTrace();
return "";
}
return "";
} // scope execution function ends

}