// 
// Decompiled by Procyon v0.5.36
// 

package us.myles.viaversion.libs.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Expose {
    boolean serialize() default true;
    
    boolean deserialize() default true;
}
