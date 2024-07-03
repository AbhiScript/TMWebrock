package com.thinking.machines.webrock;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.pojo.*;
import com.thinking.machines.webrock.scope.*;
import com.thinking.machines.webrock.tokenManager.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;
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
RequestContextHolder.setRequest(request);
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
while(true) // getLoop
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
c=service.getServiceClass();
SecuredAccess securedAccessClass=(SecuredAccess)c.getAnnotation(SecuredAccess.class);
Token token=Token.getInstance();
String URLString="";
Service serviceObject=null;
if(securedAccessClass!=null)
{
String checkPost=securedAccessClass.checkPost();
String guard=securedAccessClass.guard();
for(Map.Entry<String,Service> map : webRockModel.serviceMap.entrySet())
{
URLString=map.getKey();
serviceObject=map.getValue();
if(guard.equals(serviceObject.getService().getName()))
{
break;
}
}
if(!token.isTokenApplied()) throw new ServiceException("Invalid Access : token is not set");
try
{
HttpSession httpSession=request.getSession();
SecurityModel securityModel=(SecurityModel)httpSession.getAttribute(guard);
if(securityModel.securityMap.containsKey(serviceObject.getService()))
{
Class<?> checkPostClass=serviceObject.getServiceClass();
Object instance=checkPostClass.getDeclaredConstructor().newInstance();
Method guardMethod=serviceObject.getService();
Class<?>[] parameterTypes=guardMethod.getParameterTypes();
if(parameterTypes.length>0)
{
Object[] parameters = new Object[parameterTypes.length];
for (int i=0;i<parameterTypes.length;i++)
{
if(parameterTypes[i].equals(RequestScope.class))
{
parameters[i]=new RequestScope();
}else if(parameterTypes[i].equals(SessionScope.class))
{
parameters[i]=new SessionScope();
}else if(parameterTypes[i].equals(ApplicationScope.class))
{
parameters[i]=new ApplicationScope();
}else
{
throw new ServiceException("Unsupported parameter type: "+parameterTypes[i].getName());
}
}
guardMethod.setAccessible(true);
guardMethod.invoke(instance,parameters);
}
else
{
guardMethod.setAccessible(true);
guardMethod.invoke(instance);
}
}
}catch(Exception e)
{
e.printStackTrace();
throw new ServiceException("Invalid Access : The guard is incorrect, hence the class/service is not accessible");
}
}// securedAccessClass!=null
else
{
method=service.getService();
SecuredAccess securedAccessMethod=(SecuredAccess)method.getAnnotation(SecuredAccess.class);
if(securedAccessMethod!=null)
{
String checkPost=securedAccessMethod.checkPost();
String guard=securedAccessMethod.guard();
for(Map.Entry<String,Service> map : webRockModel.serviceMap.entrySet())
{
URLString=map.getKey();
serviceObject=map.getValue();
if(guard.equals(serviceObject.getService().getName()))
{
break;
}
}
if(!token.isTokenApplied()) throw new ServiceException("Invalid Access : token is not set");
try
{
HttpSession httpSession=request.getSession();
SecurityModel securityModel=(SecurityModel)httpSession.getAttribute(guard);
if(securityModel.securityMap.containsKey(serviceObject.getService()))
{
Class<?> checkPostClass=serviceObject.getServiceClass();
Object instance=checkPostClass.getDeclaredConstructor().newInstance();
Method guardMethod=serviceObject.getService();
guardMethod.setAccessible(true);
guardMethod.invoke(instance);
}

}catch(Exception e)
{
throw new ServiceException("Invalid Access : The guard is incorrect, hence the class/service is not accessible");
}

}// securedAccessMethod!=null
else if(token.isTokenApplied())
{
throw new ServiceException("Invalid Access : secured service");
}

} // else block of securedAccessClass
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
RequestContextHolder.setRequest(request);

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
} // done
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
c=service.getServiceClass();
SecuredAccess securedAccessClass=(SecuredAccess)c.getAnnotation(SecuredAccess.class);
Token token=Token.getInstance();
String URLString="";
Service serviceObject=null;
if(securedAccessClass!=null)
{
String checkPost=securedAccessClass.checkPost();
String guard=securedAccessClass.guard();
for(Map.Entry<String,Service> map : webRockModel.serviceMap.entrySet())
{
URLString=map.getKey();
serviceObject=map.getValue();
if(guard.equals(serviceObject.getService().getName()))
{
break;
}
}
if(!token.isTokenApplied()) throw new ServiceException("Invalid Access : token is not set");
try
{
HttpSession httpSession=request.getSession();
SecurityModel securityModel=(SecurityModel)httpSession.getAttribute(guard);
if(securityModel.securityMap.containsKey(serviceObject.getService()))
{
Class<?> checkPostClass=serviceObject.getServiceClass();
Object instance=checkPostClass.getDeclaredConstructor().newInstance();
Method guardMethod=serviceObject.getService();
Class<?>[] parameterTypes=guardMethod.getParameterTypes();
if(parameterTypes.length>0)
{
Object[] parameters = new Object[parameterTypes.length];
for (int i=0;i<parameterTypes.length;i++)
{
if(parameterTypes[i].equals(RequestScope.class))
{
parameters[i]=new RequestScope();
}else if(parameterTypes[i].equals(SessionScope.class))
{
parameters[i]=new SessionScope();
}else if(parameterTypes[i].equals(ApplicationScope.class))
{
parameters[i]=new ApplicationScope();
}else
{
throw new ServiceException("Unsupported parameter type: "+parameterTypes[i].getName());
}
}
guardMethod.setAccessible(true);
guardMethod.invoke(instance,parameters);
}
else
{
guardMethod.setAccessible(true);
guardMethod.invoke(instance);
}
}
}catch(Exception e)
{
e.printStackTrace();
throw new ServiceException("Invalid Access : The guard is incorrect, hence the class/service is not accessible");
}
}// securedAccessClass!=null
else
{
method=service.getService();
SecuredAccess securedAccessMethod=(SecuredAccess)method.getAnnotation(SecuredAccess.class);
if(securedAccessMethod!=null)
{
String checkPost=securedAccessMethod.checkPost();
String guard=securedAccessMethod.guard();
for(Map.Entry<String,Service> map : webRockModel.serviceMap.entrySet())
{
URLString=map.getKey();
serviceObject=map.getValue();
if(guard.equals(serviceObject.getService().getName()))
{
break;
}
}
if(!token.isTokenApplied()) throw new ServiceException("Invalid Access : token is not set");
try
{
HttpSession httpSession=request.getSession();
SecurityModel securityModel=(SecurityModel)httpSession.getAttribute(guard);
if(securityModel.securityMap.containsKey(serviceObject.getService()))
{
Class<?> checkPostClass=serviceObject.getServiceClass();
Object instance=checkPostClass.getDeclaredConstructor().newInstance();
Method guardMethod=serviceObject.getService();
guardMethod.setAccessible(true);
guardMethod.invoke(instance);
}

}catch(Exception e)
{
throw new ServiceException("Invalid Access : The guard is incorrect, hence the class/service is not accessible");
}

}// securedAccessMethod!=null
else if(token.isTokenApplied())
{
throw new ServiceException("Invalid Access : secured service");
}

} // else block of securedAccessClass

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
Map<String,String[]> parameterMap=request.getParameterMap();
if(!parameterMap.isEmpty())
{
for(Map.Entry<String,String[]> entry : parameterMap.entrySet())
{
String key=entry.getKey();
Object[] values=entry.getValue();
for(Object value : values)
{
Object getValue=convertStringToType(value.toString());
request.setAttribute(key,getValue);
}
}
}
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
parameter=parameters[i];
Class<?> parameterType=parameter.getType();
Object scopeData=null;

BufferedReader reader=request.getReader();
if(reader!=null)
{
StringBuilder sb=new StringBuilder();
String line;
while((line=reader.readLine())!=null)
{
sb.append(line);
}
if(!sb.toString().trim().isEmpty()) 
{
scopeData=(Object)sb;
result=scopeData;
}
}

if(!isPrimitive(parameterType))
{
if(parameterType.getAnnotations().length>0)
{
Annotation[] annotations = parameter.getAnnotations();
if (annotations.length > 0)
{
System.out.println("Annotations applied to parameter " + parameter.getName() + ":");
for (Annotation annotation : annotations) {
System.out.println(" - " + annotation.annotationType().getSimpleName());
}
throw new ServiceException("Error: Annotations should not be applied parameter in the ("+method.getName()+") method.");
}
}
if(result!=null)
{
Gson gson=new Gson();
Object jsonObject=gson.fromJson(result.toString(),parameterType);
if(!parameterType.isAssignableFrom(jsonObject.getClass()))
{
parametersMatch=false;
break;
}
parameterValues[i]=jsonObject;
}
else
{
parameterValues[i]=null;
}
}
else
{
parameterValues[i]=convertPrimitive(result,parameterType);
if(result!=null && !parameterType.isAssignableFrom(parameterValues[i].getClass()))
{
parametersMatch=false;
break;
}
}
} // for loop
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

private Object getDefaultValue(Class<?> type)
{
if(type==int.class)
{
return 0;
}
else if(type==long.class)
{
return 0L;
}
else if(type==short.class)
{
return (short) 0;
}
else if(type == byte.class)
{
return (byte) 0;
}
else if(type==float.class)
{
return 0.0f;
}
else if(type==double.class)
{
return 0.0d;
}
else if(type==boolean.class)
{
return false;
}
else if(type==char.class)
{
return '\u0000';
}
else
{
return null; 
}
}

private Object convertPrimitive(Object result, Class<?> type)
{
if (result == null || result.toString().isEmpty())
{
return getDefaultValue(type);
}
String resultStr = result.toString();
if(type == String.class)
{
return resultStr;
}
if(type == int.class || type == Integer.class)
{
return Integer.parseInt(resultStr);
}
else if (type == long.class || type == Long.class)
{
return Long.parseLong(resultStr);
}
else if (type == short.class || type == Short.class)
{
return Short.parseShort(resultStr);
}
else if(type==byte.class || type==Byte.class)
{
return Byte.parseByte(resultStr);
}
else if(type==float.class || type==Float.class)
{
return Float.parseFloat(resultStr);
}
else if(type==double.class || type==Double.class)
{
return Double.parseDouble(resultStr);
}
else if (type == boolean.class || type == Boolean.class)
{
return Boolean.parseBoolean(resultStr);
}
else if(type==char.class || type==Character.class)
{
return resultStr.charAt(0);
}
else
{
return resultStr;
}
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

private boolean isPrimitive(Class<?> c)
{
return c.isPrimitive() || c.equals(String.class);
}

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
}else
{
if(requestScopeData instanceof Number)
{
return ((Number) requestScopeData).shortValue();
}
try
{
short x;
x=(short)requestScopeData;
return x;
}catch(Exception e)
{
System.out.println(e.getMessage());
return null;
}

}

}else if(parameterType==int.class || parameterType==Integer.class)
{
if(requestScopeData instanceof Integer)
{
return requestScopeData;
}else  // here
{
if(requestScopeData instanceof Number)
{
return ((Number) requestScopeData).intValue();
}
try
{
int x;
x=(int)requestScopeData;
return x;
}catch(Exception e)
{
System.out.println(e.getMessage());
return null;
}

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
}else
{
if(requestScopeData instanceof Number)
{
return ((Number) requestScopeData).floatValue();
}
try
{
long x;
x=(long)requestScopeData;
return x;
}catch(Exception e)
{
System.out.println(e.getMessage());
return null;
}

}

}else if(parameterType==double.class || parameterType==Double.class)
{
if(requestScopeData instanceof Double)
{
return requestScopeData;
}else
{
if(requestScopeData instanceof Number)
{
return ((Number) requestScopeData).doubleValue();
}
try
{
double x;
x=(double)requestScopeData;
return x;
}catch(Exception e)
{
System.out.println(e.getMessage());
return null;
}

}

}else if(parameterType==char.class || parameterType==Character.class)
{
if(requestScopeData instanceof Character)
{
return requestScopeData;
}
else
{
if(requestScopeData instanceof String && ((String)requestScopeData).length()==1)
{
return ((String)requestScopeData).charAt(0);
}
try
{
char x;
x=(char)requestScopeData;
return x;
}catch(Exception e)
{
System.out.println(e.getMessage());
return null;
}

}
}
return null;
} // type convertion

private Object convertStringToType(String value)
{
try
{
return Integer.parseInt(value);
}catch(NumberFormatException e)
{
}
try
{
return Long.parseLong(value);
}
catch(NumberFormatException e)
{
}
try
{
return Double.parseDouble(value);
}catch(NumberFormatException e)
{
}
if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
{
return Boolean.parseBoolean(value);
}
try
{
return Float.parseFloat(value);
}catch(NumberFormatException e)
{
}
try
{
return Byte.parseByte(value);
} catch (NumberFormatException e)
{
}
try
{
return Short.parseShort(value);
}catch(NumberFormatException e)
{
}
if(value.length() == 1)
{
return value.charAt(0);
}
return value;
}
}