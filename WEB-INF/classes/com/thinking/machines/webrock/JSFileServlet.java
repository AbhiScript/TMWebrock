package com.thinking.machines.webrock;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import com.thinking.machines.webrock.annotations.*;

public class JSFileServlet extends HttpServlet
{
private List<String> fileList;
public void doGet(HttpServletRequest request,HttpServletResponse response)
{
String JSfileName=request.getParameter("name");
ServletConfig servletConfig = getServletConfig();
String packageName = servletConfig.getInitParameter("SERVICE_PACKAGE_PREFIX");
ServletContext servletContext=getServletContext();
String path=servletContext.getRealPath("WEB-INF/classes");
String pathToCreateFile=servletContext.getRealPath("WEB-INF");
String pathToScan=path+File.separator+"js";

File file=new File(pathToCreateFile);
String originalPath="";
fileList=new ArrayList<>();
scanClassesDirectory(path+File.separator+packageName);
if(file.exists())
{
originalPath=pathToCreateFile;
}
else
{
File directory=new File(pathToCreateFile);
if(directory.mkdirs())
{
String absolutePath=directory.getAbsolutePath();
originalPath=absolutePath;
}
else
{
return;
}
}
String jsDirectoryPath=originalPath+File.separator+"js";
File jsDirectory=new File(jsDirectoryPath);
if(!jsDirectory.exists())
{
if(jsDirectory.mkdir())
{
}
else
{
return;
}

}


String filePath=jsDirectoryPath+File.separator+JSfileName;
List<String> list=new ArrayList<String>();
String line;
StringBuffer requestURL=request.getRequestURL();
for(String classPath : fileList)
{
String fileName=classPath;
char backSlash='\\';
int lastIndex=fileName.lastIndexOf(backSlash);
try
{
fileName=fileName.substring(lastIndex + 1);
int dot=fileName.indexOf(".");
String className=fileName.substring(0,dot);
String packagePath=classPath.substring(path.length()+1,classPath.length()-".class".length()).replace('\\','.');
Class c=Class.forName(packagePath);
CreatePOJO pojoAnnotation=(CreatePOJO)c.getAnnotation(CreatePOJO.class);
if(pojoAnnotation!=null)
{
Field fields[]=c.getDeclaredFields();
line="class "+c.getSimpleName();
list.add(line);
list.add("{");
line="constructor(";
for(int i=0;i<fields.length;i++)
{
line+=fields[i].getName();
if(i<fields.length-1)
{
line+=", ";
}
}
line+=")";
list.add(line);
list.add("{");
for(Field field : fields)
{
list.add("this."+field.getName()+"="+field.getName()+";");
}
list.add("}");
list.add("}");
list.add("");
} // if POJO annotation is applied
CreateService serviceAnnotation=(CreateService)c.getAnnotation(CreateService.class);
if(serviceAnnotation!=null)
{
list.add("class "+className+"{");
Method[] methods=c.getDeclaredMethods();
for(Method method : methods)
{
addMethodToJs(list,serviceAnnotation,method,className);
}
list.add("}");
}// service Annotation is applied on class
else
{
Method[] methods=c.getDeclaredMethods();
boolean isMethodServiceAnnotated = false;
for(Method method : methods)
{
CreateService methodServiceAnnotation=method.getAnnotation(CreateService.class);
if(methodServiceAnnotation != null)
{
if(!isMethodServiceAnnotated)
{
list.add("class "+className+"{");
isMethodServiceAnnotated=true;
}
addMethodToJs(list,methodServiceAnnotation,method,className);
}
}
if(isMethodServiceAnnotated)
{
list.add("}");
}
}// else
}catch(Exception e)
{
e.printStackTrace();
}
}// for loop
try
{
File JSFile=new File(filePath);
if(JSFile.exists())
{
if(!JSFile.delete())
{
return;
}
}
if(!JSFile.createNewFile())
{
System.out.println("Failed to create file : "+filePath);
return;
}
try(RandomAccessFile randomAccessFile = new RandomAccessFile(JSFile, "rw"))
{
for (String listItem : list) {
randomAccessFile.writeBytes(listItem + "\r\n");
}
}
}
catch (Exception e)
{
e.printStackTrace();
}
}

private void addMethodToJs(List<String> list,CreateService serviceAnnotation,Method method,String className)
{
Path pathAnnotation=method.getAnnotation(Path.class);
Get getAnnotation=method.getAnnotation(Get.class);
Post postAnnotation=method.getAnnotation(Post.class);
if(serviceAnnotation==null || pathAnnotation==null) return;
String returnType=method.getReturnType().getSimpleName();
String methodName=method.getName();
Class<?>[] parameterTypes=method.getParameterTypes();
Annotation[][] parameterAnnotations=method.getParameterAnnotations();
String urlPath=pathAnnotation.value();
String httpMethod=postAnnotation!=null ? "POST" : "GET";
StringBuilder paramList=new StringBuilder();
StringBuilder bodyParams=new StringBuilder();
StringBuilder queryParams=new StringBuilder();
boolean hasBodyParams=false;
boolean hasQueryParams=false;
for(int i=0;i<parameterTypes.length;i++)
{
String paramName="";
boolean paramAnnotated=false;
for(Annotation annotation : parameterAnnotations[i])
{
if(annotation instanceof RequestParameter)
{
paramName=((RequestParameter) annotation).value();
hasQueryParams=true;
paramAnnotated=true;
break;
}
}

if(!paramAnnotated)
{
paramName=parameterTypes[i].getSimpleName();
hasBodyParams=true;
}

if(!paramName.isEmpty())
{
if(hasBodyParams)
{
bodyParams.append(paramName).append(", ");
}
else if(hasQueryParams)
{
queryParams.append(paramName).append("=${").append(paramName).append("}&");
}
}

}

if(bodyParams.length()>0)
{
bodyParams.setLength(bodyParams.length()-2);
}
if(queryParams.length()>0)
{
queryParams.setLength(queryParams.length()-1);
}

list.add(methodName+"("+bodyParams.toString()+") {");

String fetchURL="`"+"/"+className+urlPath;
if(httpMethod.equals("GET") && queryParams.length()>0)
{
fetchURL+="?"+queryParams.toString()+"`";
}
else
{
fetchURL+="`";
}

list.add("return fetch("+fetchURL+", {");
list.add("method : '"+httpMethod+",");
boolean isJSONApplied=serviceAnnotation.isJSON();

if(httpMethod.equals("POST"))
{
if(isJSONApplied)
{
if(hasBodyParams)
{
list.add("headers : {'Content-Type' : 'application/json'},");
list.add("body : JSON.stringify({"+bodyParams.toString()+"}),");
}
}else if(hasBodyParams)
{
list.add("headers : {'Content-Type' : 'application/x-form-urlencoded'},");
list.add("body : new URLSearchParams("+bodyParams.toString()+").toString(),");
}

}

list.add("})");

list.add(".then(response=>{");
list.add("if(!response.ok){");
list.add("throw new Error (`HTTP error! Status : ${response.status}`)");
list.add("}");
if(!returnType.equals("void"))
{
if(isJSONApplied)
{
list.add("return response.json();");
}
else if(httpMethod.equals("GET"))
{
list.add("return response;");
}
}
list.add("})");
list.add(".catch(error)=>{");
list.add("console.error('Fetch error : ',error);");
list.add("throw error");
list.add("});");
list.add("}");
list.add("");
}

private void scanClassesDirectory(String directoryPath)
{
try
{
File directory = new File(directoryPath);
File[] files = directory.listFiles();
if(files!=null)
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