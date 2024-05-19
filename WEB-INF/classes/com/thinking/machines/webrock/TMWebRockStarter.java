package com.thinking.machines.webrock;
import com.thinking.machines.webrock.annotations.*;
import com.thinking.machines.webrock.model.*;
import com.thinking.machines.webrock.pojo.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

public class TMWebRockStarter extends HttpServlet
{
private List<String> fileList;
public void init()
{
System.out.println("----------------------------------------------------TMWebRock starts----------------------------------------------------");
ServletContext servletContext = getServletContext();
ServletConfig servletConfig = getServletConfig();
String packageName = servletConfig.getInitParameter("SERVICE_PACKAGE_PREFIX");
String path=servletContext.getRealPath("WEB-INF/classes");
fileList=new ArrayList<>();
WebRockModel webRockModel=new WebRockModel();
scanClassesDirectory(path+"/"+packageName);
for(String filePath : fileList)
{
String fileName=filePath;
char backSlash='\\';
int lastIndex=fileName.lastIndexOf(backSlash);
try
{
fileName=fileName.substring(lastIndex + 1);
int dot=fileName.indexOf(".");
String className=fileName.substring(0,dot);
String packagePath=filePath.substring(path.length()+1,filePath.length() - ".class".length()).replace('\\', '.');
Class c=Class.forName(packagePath);
Path pathAnnotationClass=(Path)c.getAnnotation(Path.class);
if(pathAnnotationClass!=null)
{
Get getAnnotationClass=(Get)c.getAnnotation(Get.class);
Post postAnnotationClass=(Post)c.getAnnotation(Post.class);
Forward forwardAnnotation=null;
InjectApplicationDirectory getApplicationDirectoryScope=(InjectApplicationDirectory)c.getAnnotation(InjectApplicationDirectory.class);
InjectApplicationScope getApplicationScope=(InjectApplicationScope)c.getAnnotation(InjectApplicationScope.class);
InjectRequestScope getRequestScope=(InjectRequestScope)c.getAnnotation(InjectRequestScope.class);
InjectSessionScope getSessionScope=(InjectSessionScope)c.getAnnotation(InjectSessionScope.class);
Field[] fields=c.getDeclaredFields();
AutowiredClass autowiredClass;
List<AutowiredClass> autowiredClassList=new ArrayList<>();
for(Field field : fields)
{
Autowired autowireProperty=(Autowired)field.getAnnotation(Autowired.class);
if(autowireProperty!=null)
{
autowiredClass=new AutowiredClass();
autowiredClass.setName(autowireProperty.name());
autowiredClass.setType(field.getType());
String fieldName=field.getType().toString();
int fieldLastIndex=fieldName.lastIndexOf('.')+1;
String methodName="set"+(fieldName.substring(fieldLastIndex));
autowiredClass.setMethodName(methodName);
autowiredClassList.add(autowiredClass);
}
}
if(autowiredClassList!=null)
{
for(AutowiredClass ac : autowiredClassList)
{
System.out.println("Name : "+ac.getName());
System.out.println("Type : "+ac.getType());
System.out.println("Method Name : "+ac.getMethodName());
}
}

Method[] methods = c.getDeclaredMethods();
for (Method method : methods)
{
Path pathAnnotationMethod=method.getAnnotation(Path.class);
if(pathAnnotationMethod != null)
{
String fullPath=pathAnnotationClass.value()+pathAnnotationMethod.value();
if(method.isAnnotationPresent(Get.class))
{
webRockModel.methodMap.put(method,method.getAnnotation(Get.class));
}
if(method.isAnnotationPresent(Post.class))
{
webRockModel.methodMap.put(method,method.getAnnotation(Post.class));
}
String forwardTo="";
if(method.isAnnotationPresent(Forward.class))
{
forwardAnnotation=(Forward)method.getAnnotation(Forward.class);
forwardTo=forwardAnnotation.value();
}


Service service = new Service();
service.setServiceClass(c);
service.setPath(fullPath);
service.setService(method);
if(getAnnotationClass!=null) service.setGetClassAnnotation(true);
else service.setGetClassAnnotation(false);
if(postAnnotationClass!=null) service.setPostClassAnnotation(true);
else service.setPostClassAnnotation(false);
service.setForwardTo(forwardTo);
service.setPathName(path+"/"+packageName);
if(getApplicationDirectoryScope!=null) service.setInjectApplicationDirectory(true);
if(getApplicationScope!=null) service.setInjectApplicationScope(true);
if(getRequestScope!=null) service.setInjectRequestScope(true);
if(getSessionScope!=null) service.setInjectSessionScope(true);
if(autowiredClassList.size()!=0)
{
service.setAutowiredClassList(autowiredClassList);
service.setIsAutowiredApplied(true);
}
else
{
service.setAutowiredClassList(null);
}
if(method.isAnnotationPresent(OnStartup.class))
{
OnStartup onStartup=method.getAnnotation(OnStartup.class);
int priority=onStartup.priority();
Class returnType=method.getReturnType();
if(priority<=-1)
{
System.out.println("Priority should not be less than zero");
}
else if(!(returnType.equals(void.class)))
{
System.out.println("Method return type is not void");
}
else
{
service.setRunOnStart(true);
service.setPriority(priority);
}
}
webRockModel.serviceMap.put(fullPath, service);
}// method loop ends

}
Map<Integer,Service> keyValueMap=new HashMap<>();
for(Map.Entry<String,Service> entry : webRockModel.serviceMap.entrySet())
{
String serviceName=entry.getKey();
Service serviceData=entry.getValue();
if(serviceData.getRunOnStart())
{
keyValueMap.put(serviceData.getPriority(),serviceData);
}
}
TreeMap<Integer,Service> sortedMap=new TreeMap<>(keyValueMap);
for(Map.Entry<Integer,Service> entry : sortedMap.entrySet())
{
int key=entry.getKey();
Service service=entry.getValue();
Object result=invokeMethod(service);
}

}

}catch(Exception e)
{
e.printStackTrace();
}
}
servletContext.setAttribute("webRockModel", webRockModel);
}

private Object invokeMethod(Service service) throws Exception
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
for(Method method: methods)
{
if(method.equals(service.getService()))
{
Object instance=c.getDeclaredConstructor().newInstance();
method.setAccessible(true);
Object result=method.invoke(instance);
return result;
}
}
return null;
}


private void scanClassesDirectory(String directoryPath)
{
try
{
File directory = new File(directoryPath);
File[] files = directory.listFiles();
if (files != null)
{
for(File file : files)
{
if(file.isDirectory())
{
scanClassesDirectory(file.getAbsolutePath());
} else if (file.isFile() && file.getName().endsWith(".class"))
{
fileList.add(file.getCanonicalPath());
}
}
}
}catch(Exception e)
{
e.printStackTrace();
}
}
}
