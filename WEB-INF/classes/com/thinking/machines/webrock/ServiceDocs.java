package com.thinking.machines.webrock;

import com.thinking.machines.webrock.annotations.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.Table;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.annotation.*;

public class ServiceDocs extends HttpServlet
{
private static String PDF_File="c:/tomcat9/webapps/TMWebRock/WEB-INF/serviceDocs/ServiceDocs.pdf";
private static List<String> fileList;

private static String fullPath="";
public void init()
{
ServletContext servletContext = getServletContext();
ServletConfig servletConfig = getServletConfig();
String packageName = servletConfig.getInitParameter("CREATE_SERVICE_DOCS");
String path=servletContext.getRealPath("WEB-INF/classes");
fileList=new ArrayList<>();
scanClassesDirectory(path+"/"+packageName);
fullPath=path+File.separator+packageName;
PdfWriter writer=null;
PdfDocument pdf=null;
Document document=null;
PdfFont titleFont=null;
Text titleText=null;
Paragraph title=null;
try
{
writer=new PdfWriter(PDF_File);
pdf=new PdfDocument(writer);
document=new Document(pdf);
titleFont=PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
titleText=new Text("Service Documentation").setFont(titleFont);
title=new Paragraph(titleText).setTextAlignment(TextAlignment.CENTER);
document.add(title);
}catch(Exception e)
{
e.printStackTrace();
}

for(String filePath : fileList)
{
String fileName=filePath;
char backSlash='\\';
int lastIndex=fileName.lastIndexOf(backSlash);
try
{
fileName=fileName.substring(lastIndex + 1);
int dot=fileName.indexOf(".");
String packagePath=filePath.substring(path.length()+1,filePath.length() - ".class".length()).replace('\\', '.');
Class c=Class.forName(packagePath);

Paragraph className=new Paragraph("Class : "+c.getName()).setFont(titleFont);
document.add(className);

Table table=new Table(6);
table.addCell("URL");
table.addCell("Method");
table.addCell("Return Type");
table.addCell("Parameters");
table.addCell("Class Annotations");
table.addCell("Method Annotations");

StringBuilder classAnnotations = new StringBuilder();
String classPathValue = "";
for (Annotation annotation : c.getAnnotations())
{
if(annotation instanceof Path)
{
classPathValue = ((Path) annotation).value();
}
classAnnotations.append(annotation.annotationType().getSimpleName()).append("\n");
}

for (Method method : c.getDeclaredMethods())
{
StringBuilder methodAnnotations = new StringBuilder();
String methodPathValue = "";
for(Annotation annotation : method.getAnnotations())
{
if(annotation instanceof Path)
{
methodPathValue=((Path) annotation).value();
}
methodAnnotations.append(annotation.annotationType().getSimpleName()).append("\n");
}

String url = classPathValue + methodPathValue;

table.addCell(url);
table.addCell(method.getName());
table.addCell(method.getReturnType().getName());

// Parameters
StringBuilder parameters = new StringBuilder();
for (Parameter parameter : method.getParameters())
{
parameters.append(parameter.getType().getName()).append(" ").append(parameter.getName()).append(", ");
}
if(parameters.length() > 0)
{
parameters.setLength(parameters.length() - 2); // remove last comma and space
}
table.addCell(parameters.toString());

// Class annotations
table.addCell(classAnnotations.toString());

// Method annotations
table.addCell(methodAnnotations.toString());
}

document.add(table);
}catch(Exception e)
{
System.out.println(e);
}
}
document.close();
}


private static void scanClassesDirectory(String directoryPath)
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