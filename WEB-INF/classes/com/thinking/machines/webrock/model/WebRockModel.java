package com.thinking.machines.webrock.model;
import com.thinking.machines.webrock.pojo.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
public class WebRockModel
{
public Map<String,Service> serviceMap;
public Map<Method,Annotation> methodMap;
public WebRockModel()
{
this.serviceMap=new HashMap();
this.methodMap=new HashMap();
}

}