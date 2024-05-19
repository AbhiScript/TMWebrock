package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;
public class Service
{
private Class serviceClass;
private String path;
private String pathName;
private Method service;
private boolean getClassAnnotation;
private boolean postClassAnnotation;
private String forwardTo;
private boolean runOnStart;
private int priority;
private boolean injectApplicationDirectory;
private boolean injectSessionScope;
private boolean injectApplicationScope;
private boolean injectRequestScope;
private List<AutowiredClass> autowiredClassList;
private boolean isAutowiredApplied;

public Service()
{
this.getClassAnnotation=false;
this.postClassAnnotation=false;
this.forwardTo="";
this.runOnStart=false;
this.priority=-1;
this.isAutowiredApplied=false;
}

// service class, path, service
public void setServiceClass(Class serviceClass)
{
this.serviceClass=serviceClass;
}
public Class getServiceClass()
{
return this.serviceClass;
}
public void setPath(String path)
{
this.path=path;
}
public String getPath()
{
return this.path;
}
public void setService(Method service)
{
this.service=service;
}
public Method getService()
{
return this.service;
}

public void setGetClassAnnotation(boolean getClassAnnotation)
{
this.getClassAnnotation=getClassAnnotation;
}
public boolean getGetClassAnnotation()
{
return this.getClassAnnotation;
}
public void setPostClassAnnotation(boolean postClassAnnotation)
{
this.postClassAnnotation=postClassAnnotation;
}
public boolean getPostClassAnnotation()
{
return this.postClassAnnotation;
}


public void setForwardTo(String forwardTo)
{
this.forwardTo=forwardTo;
}
public String getForwardTo()
{
return this.forwardTo;
}


public void setPathName(String pathName)
{
this.pathName=pathName;
}
public String getPathName()
{
return this.pathName;
}


public void setRunOnStart(boolean runOnStart)
{
this.runOnStart=runOnStart;
}
public boolean getRunOnStart()
{
return this.runOnStart;
}
public void setPriority(int priority)
{
this.priority=priority;
}
public int getPriority()
{
return this.priority;
}

public void setInjectApplicationDirectory(boolean injectApplicationDirectory)
{
this.injectApplicationDirectory=injectApplicationDirectory;
}
public boolean getInjectApplicationDirectory()
{
return this.injectApplicationDirectory;
}
public void setInjectSessionScope(boolean injectSessionScope)
{
this.injectSessionScope=injectSessionScope;
}
public boolean getInjectSessionScope()
{
return this.injectSessionScope;
}
public void setInjectApplicationScope(boolean injectApplicationScope)
{
this.injectApplicationScope=injectApplicationScope;
}
public boolean getInjectApplicationScope()
{
return this.injectApplicationScope;
}
public void setInjectRequestScope(boolean injectRequestScope)
{
this.injectRequestScope=injectRequestScope;
}
public boolean getInjectRequestScope()
{
return this.injectRequestScope;
}

public void setAutowiredClassList(List<AutowiredClass> autowiredClassList)
{
this.autowiredClassList=autowiredClassList;
}
public List<AutowiredClass> getAutowiredClassList()
{
return this.autowiredClassList;
}
public void setIsAutowiredApplied(boolean isAutowiredApplied)
{
this.isAutowiredApplied=isAutowiredApplied;
}
public boolean getIsAutowiredApplied()
{
return this.isAutowiredApplied;
}


}