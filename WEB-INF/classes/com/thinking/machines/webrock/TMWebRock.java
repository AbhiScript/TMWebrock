package com.thinking.machines.webrock;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.scope.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import java.io.*;
import java.net.*;
public class TMWebRock extends HttpServlet
{

private class Scope
{
public boolean isScopeApplied;
public Object result;
}

public Scope scope=new Scope();

public void doGet(HttpServletRequest request,HttpServletResponse response)
{
try
{
ServletContext servletContext=request.getServletContext();
WebRockModel webRockModel=(WebRockModel)servletContext.getAttribute("webRockModel");
String url=request.getRequestURI();
PrintWriter pw=response.getWriter();
boolean urlFound=false;
Service service=null;
String serviceURL="";
for(Map.Entry<String,Service> map: webRockModel.serviceMap.entrySet())
{
serviceURL=map.getKey();
service=map.getValue();
if(url.contains(serviceURL))
{
urlFound=true;
break;
}
} // for loop for map ends here
if(urlFound)
{
if(service.getPostClassAnnotation())
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"HTTP method POST is not supported for this resource");
return;
}
Class c=null;
Method method=null;
Annotation annotation=null;
for(Map.Entry<Method,Annotation> map: webRockModel.methodMap.entrySet())
{
method=map.getKey();
annotation=map.getValue();
if(service.getService()==method)
{
if(annotation.annotationType()==Post.class)
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"HTTP method POST is not supported for this resource");
return;
}
}
}// method loop ends
Object result=null;
c=service.getServiceClass();
String forwardTo="";
if(service.getIsAutowiredApplied())
{
List<AutowiredClass> list=service.getAutowiredClassList();
for(AutowiredClass ac : list) 
{
String name=ac.getName();
String methodName="set"+Character.toUpperCase(name.charAt(0))+name.substring(1);

Object data=request.getAttribute(name);
if(data==null)
{
HttpSession autowiredHttpSession=request.getSession();
if(autowiredHttpSession!=null)
{
data=autowiredHttpSession.getAttribute(name);
}
else
{
ServletContext autowiredServletContext=request.getServletContext();
if(autowiredServletContext!=null)
{
data=autowiredServletContext.getAttribute(name);
}
}

}
Class<?> attributeType=getClassFromType(ac.getType());
if(data!=null && isCompatible(data,attributeType))
{
Class<?> serviceClass=service.getServiceClass();
Class<?>[] parameterTypes = new Class<?>[] { attributeType };
Method setterMethod=serviceClass.getMethod(methodName,parameterTypes);
Object serviceInstance=serviceClass.newInstance();
setterMethod.invoke(serviceInstance,data);
}
}
}
while(true)
{
if(!scopeExcecution(service,request,response))
{
try
{
result=invokeMethod(service,result,request,response);
}catch(Exception e)
{
response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,"Cannot access a member of class with unspecified modifiers");
e.printStackTrace();
}
}
else
{
result=scope.result;
}
response.setContentType("text/plain");
if(result==null) pw.println("");
else pw.println(result);
forwardTo=service.getForwardTo();
String path="";
if(forwardTo!="")
{
if(forwardTo.endsWith(".jsp") || forwardTo.endsWith(".html"))
{
RequestDispatcher requestDispatcher=request.getRequestDispatcher(forwardTo);
requestDispatcher.forward(request,response);
return;
}
service=webRockModel.serviceMap.get(forwardTo);
if(service==null)
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Service not found for forwardTo: " + forwardTo);
return;
}
else
{
continue;
}
}
break; // will be removed , for testing purposes
}// infinite loop

} // url found
else
{
response.setStatus(HttpServletResponse.SC_NOT_FOUND);
}

}catch(Exception e)
{
e.printStackTrace();
}

} // get method ends here

public void doPost(HttpServletRequest request,HttpServletResponse response)
{
try
{
ServletContext servletContext=request.getServletContext();
ServletConfig servletConfig=getServletConfig();
WebRockModel webRockModel=(WebRockModel)servletContext.getAttribute("webRockModel");
String url=request.getRequestURI();
PrintWriter pw=response.getWriter();
boolean urlFound=false;
Service service=null;
String serviceURL="";
for(Map.Entry<String,Service> map: webRockModel.serviceMap.entrySet())
{
serviceURL=map.getKey();
service=map.getValue();
if(url.contains(serviceURL))
{
urlFound=true;
break;
}
} // for loop for map ends here
if(urlFound)
{
if(service.getGetClassAnnotation())
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"HTTP method GET is not supported for this resource");
return;
}
Class c=null;
Method method=null;
Annotation annotation=null;

for(Map.Entry<Method,Annotation> map: webRockModel.methodMap.entrySet())
{
method=map.getKey();
annotation=map.getValue();
if(service.getService()==method)
{
if(annotation.annotationType()==Get.class)
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"HTTP method GET is not supported for this resource");
return;
}
}
}// method loop ends
Object result=null;
c=service.getServiceClass();
String forwardTo="";

if(service.getIsAutowiredApplied())
{
List<AutowiredClass> list=service.getAutowiredClassList();
for(AutowiredClass ac : list) 
{
String name=ac.getName();
String methodName="set"+Character.toUpperCase(name.charAt(0))+name.substring(1);

Object data=request.getAttribute(name);
if(data==null)
{
HttpSession autowiredHttpSession=request.getSession();
if(autowiredHttpSession!=null)
{
data=autowiredHttpSession.getAttribute(name);
}
else
{
ServletContext autowiredServletContext=request.getServletContext();
if(autowiredServletContext!=null)
{
data=autowiredServletContext.getAttribute(name);
}
}

}
Class<?> attributeType=getClassFromType(ac.getType());
if(data!=null && isCompatible(data,attributeType))
{
Class<?> serviceClass=service.getServiceClass();
Class<?>[] parameterTypes = new Class<?>[] { attributeType };
Method setterMethod=serviceClass.getMethod(methodName,parameterTypes);
Object serviceInstance=serviceClass.newInstance();
setterMethod.invoke(serviceInstance,data);
}

}
}


while(true) // post
{
if(!scopeExcecution(service,request,response))
{
try
{
result=invokeMethod(service,result,request,response);
}catch(Exception e)
{
response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,"Cannot access a member of class with unspecified modifiers");
e.printStackTrace();
}
}
if(method.getReturnType()==void.class)
{
response.setContentType("text/plain");
pw.println(serviceURL+" found in bobby");
}
else
{
response.setContentType("text/plain");
pw.println(result);
}
forwardTo=service.getForwardTo();
String path="";
if(forwardTo!="")
{
if(forwardTo.endsWith(".jsp") || forwardTo.endsWith(".html"))
{
RequestDispatcher requestDispatcher=request.getRequestDispatcher(forwardTo);
requestDispatcher.forward(request,response);
return;
}
service=webRockModel.serviceMap.get(forwardTo);
if(service==null)
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Service not found for forwardTo: " + forwardTo);
return;
}
else
{
continue;
}
}
break;
}// infinite loop

} // Url found
else
{
response.setStatus(HttpServletResponse.SC_NOT_FOUND);
}

}catch(Exception e)
{
e.printStackTrace();
}

} // post method ends here

private Class<?> getClassFromType(Type type)
{
if(type instanceof Class)
{
return (Class<?>)type;
}
else
{
return null;
}

}

private boolean isCompatible(Object data,Class<?> type)
{
return type.isInstance(data);
}

private Object invokeMethod(Service service,Object result,HttpServletRequest request,HttpServletResponse response) throws Exception
{
try
{
String pathName=service.getPathName();
File directory=new File(pathName);
Class<?> packageClass=service.getServiceClass();
String packageName=packageClass.toString();
packageName=packageName.substring(6);
if(!directory.isDirectory())
{
return null;
}
File [] files=directory.listFiles();
if(files==null)
{
System.out.println("No file found");
return null;
}
for(File file : files)
{
String fileName=file.getName();
if(fileName.endsWith(".class"))
{
char backSlash='\\';
int dot=fileName.indexOf(".");
String className=fileName.substring(0, dot);
}
}
Class<?> c=Class.forName(packageName);
Method[] methods=c.getDeclaredMethods();
int modifiers=c.getModifiers();
int i=0;
for(Method method: methods)
{
if(method.equals(service.getService()))
{
Parameter[] parameters=method.getParameters();
Object[] parameterValues=new Object[parameters.length];
boolean parametersMatch=true;

boolean isAnnotationApplied=false;
Parameter parameter=null;
for(i=0;i<parameters.length;i++)
{
parameter=parameters[i];
RequestParameter requestParameter=parameter.getAnnotation(RequestParameter.class);
getRequestScope getRequestScope=parameter.getAnnotation(getRequestScope.class);
getSessionScope getSessionScope=parameter.getAnnotation(getSessionScope.class);
getApplicationScope getApplicationScope=parameter.getAnnotation(getApplicationScope.class);
Object scopeData=null;
if(requestParameter!=null)
{
String requestValue=requestParameter.value();
scopeData=request.getAttribute(requestValue);
Class<?> parameterType=parameter.getType();
Object convertedData=convertToType(scopeData,parameterType);
if(parameterType==String.class)
{
if(scopeData!=null && !parameterType.isAssignableFrom(convertedData.getClass()))
{
parametersMatch=false;
break;
}
}
else
{
if(scopeData!=null && parameterType.isAssignableFrom(convertedData.getClass()))
{
parametersMatch=false;
break;
}
}
isAnnotationApplied=true;
} // request parameter applied
else if(getRequestScope!=null)
{
String requestValue=getRequestScope.value();
scopeData=request.getAttribute(requestValue);
Class<?> parameterType=parameter.getType();
Object convertedData=convertToType(scopeData,parameterType);
if(parameterType==String.class)
{
if(scopeData!=null && !parameterType.isAssignableFrom(convertedData.getClass()))
{
parametersMatch=false;
break;
}
}
else
{
if(scopeData!=null && parameterType.isAssignableFrom(convertedData.getClass()))
{
parametersMatch=false;
break;
}
}
isAnnotationApplied=true;
}
else if(getSessionScope!=null)
{
String requestValue=getSessionScope.value();
HttpSession session=request.getSession();
scopeData=session.getAttribute(requestValue);
Class<?> parameterType=parameter.getType();
Object convertedData=convertToType(scopeData,parameterType);
if(parameterType==String.class)
{
if(scopeData!=null && !parameterType.isAssignableFrom(convertedData.getClass()))
{
parametersMatch=false;
break;
}
}
else
{
if(scopeData!=null && parameterType.isAssignableFrom(convertedData.getClass()))
{
parametersMatch=false;
break;
}
}
isAnnotationApplied=true;
}
else if(getApplicationScope!=null)
{
String requestValue=getApplicationScope.value();
ServletContext context=getServletContext();
scopeData=context.getAttribute(requestValue);
Class<?> parameterType=parameter.getType();
Object convertedData=convertToType(scopeData,parameterType);
if(parameterType==String.class)
{
if(scopeData!=null && !parameterType.isAssignableFrom(convertedData.getClass()))
{
parametersMatch=false;
break;
}
}
else
{
if(scopeData!=null && parameterType.isAssignableFrom(convertedData.getClass()))
{
parametersMatch=false;
break;
}
}
isAnnotationApplied=true;
}
parameterValues[i]=scopeData;
}// for loop for parameters

if(parametersMatch && isAnnotationApplied)
{
Object instance=c.getDeclaredConstructor().newInstance();
method.setAccessible(true);
Object returnedValue=method.invoke(instance,parameterValues);
System.out.println(returnedValue);
return returnedValue;
}
else if(parameters.length==0)
{
Object instance=c.getDeclaredConstructor().newInstance();
method.setAccessible(true);
Object returnedValue=method.invoke(instance);
return returnedValue;
}
else if(result!=null && isAnnotationApplied)
{
throw new ServiceException("Error : Type mismatch for parameter ");
}


for(i=0;i<parameters.length;i++)
{
Class<?> parameterType=parameters[i].getType();
if(result!=null && !parameterType.isAssignableFrom(result.getClass()))
{
parametersMatch=false;
break;
}
parameterValues[i]=result;
}
if(parametersMatch && isAnnotationApplied==false)
{
Object instance=c.getDeclaredConstructor().newInstance();
method.setAccessible(true);
Object returnedValue=method.invoke(instance,parameterValues);
return returnedValue;
}
else if(parameters.length==0)
{
Object instance=c.getDeclaredConstructor().newInstance();
method.setAccessible(true);
Object returnedValue=method.invoke(instance);
return returnedValue;
}
else if(result!=null && isAnnotationApplied==false)
{
throw new ServiceException("Error : Type mismatch for parameter ");
}
}
}
}catch(Exception e)
{
e.printStackTrace();
}
return null;
}


private boolean scopeExcecution(Service service,HttpServletRequest request,HttpServletResponse response)
{
Object result=null;
Method method=null;
scope.isScopeApplied=false;
Class<?> c=null;
try
{
PrintWriter pw=response.getWriter();
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
result=m.invoke(object);
pw.println(result);
scope.isScopeApplied=true;
scope.result=result;
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
result=m.invoke(object);
pw.println(result);
scope.isScopeApplied=true;
scope.result=result;
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
result=m.invoke(object);
pw.println(result);
scope.isScopeApplied=true;
scope.result=result;
}// request scope
}catch(Exception e)
{
e.printStackTrace();
}
return scope.isScopeApplied;
} // scope execution function ends

private Object convertToType(Object requestScopeData,Class<?> parameterType)
{
if(requestScopeData==null) return null;

if(parameterType==String.class)
{
return requestScopeData.toString();
}else if(parameterType==boolean.class || parameterType==Boolean.class)
{
if(requestScopeData instanceof Boolean)
{
return requestScopeData;
}else if(requestScopeData instanceof String)
{
String stringValue=(String)requestScopeData;
return Boolean.parseBoolean(stringValue);
}

}else if(parameterType==byte.class || parameterType==Boolean.class)
{
if(requestScopeData instanceof Byte)
{
return requestScopeData;
}else if(requestScopeData instanceof Number)
{
return ((Number)requestScopeData).byteValue();
}

}else if(parameterType==short.class || parameterType==Short.class)
{
if(requestScopeData instanceof Short)
{
return requestScopeData;
}else if(requestScopeData instanceof Number)
{
return ((Number) requestScopeData).shortValue();
}

}else if(parameterType==int.class || parameterType==Integer.class)
{
if(requestScopeData instanceof Integer)
{
return requestScopeData;
}else if(requestScopeData instanceof Number) // here
{
return ((Number) requestScopeData).intValue();
}

}else if(parameterType==long.class || parameterType==Long.class)
{
if(requestScopeData instanceof Long)
{
return requestScopeData;
}else if(requestScopeData instanceof Number)
{
return ((Number) requestScopeData).longValue();
}

}else if(parameterType==float.class || parameterType==Float.class)
{
if(requestScopeData instanceof Float)
{
return requestScopeData;
}else if(requestScopeData instanceof Number)
{
return ((Number) requestScopeData).floatValue();
}

}else if(parameterType==double.class || parameterType==Double.class)
{
if(requestScopeData instanceof Double)
{
return requestScopeData;
}else if(requestScopeData instanceof Number)
{
return ((Number) requestScopeData).doubleValue();
}

}else if(parameterType==char.class || parameterType==Character.class)
{
if(requestScopeData instanceof Character)
{
return requestScopeData;
}else if(requestScopeData instanceof String && ((String)requestScopeData).length()==1)
{
return ((String)requestScopeData).charAt(0);
}
}
return null;
} // type convertion

}