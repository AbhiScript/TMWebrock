package com.thinking.machines.webrock.pojo;
import java.lang.reflect.*;
public class AutowiredClass
{
private String name;
private Type type;
private String methodName;
private Object object;
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setType(Type type)
{
this.type=type;
}
public Type getType()
{
return this.type;
}
public void setMethodName(String methodName)
{
this.methodName=methodName;
}
public String getMethodName()
{
return this.methodName;
}

public void setObject(Object object)
{
this.object=object;
}
public Object getObject()
{
return this.object;
}
}