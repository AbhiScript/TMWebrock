package com.thinking.machines.webrock.annotations;
import java.lang.annotation.*;
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface getSessionScope
{
public String value() default "";
}