package com.omniteam.backofisbackend.base.annotions;

import com.omniteam.backofisbackend.enums.Logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.METHOD )
public @interface LogMethodCall {

    Logger logLevel() default Logger.Info;
    String value() default "Logging Started";

}
