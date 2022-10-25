package com.hero.libannotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface FragmentDestination {

    String pageUrl();
    boolean needLogin() default false;
    boolean asStarter() default false;


}
