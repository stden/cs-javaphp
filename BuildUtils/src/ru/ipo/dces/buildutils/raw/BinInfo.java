package ru.ipo.dces.buildutils.raw;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by IntelliJ IDEA.
 * User: putin
 * Date: 01.11.2010
 * Time: 16:17:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BinInfo {

    public static final String NO_DEFAULT_VALUE = "__no__default__";
    public static final String NEW_INSTANCE_DEFAULT_VALUE = "__new__";

    /**
     * Default value that is used when instantiating bin (bean) in java. Empty string means
     * no default value.
     * "new" means
     * @return java default value
     */
    String defaultValue() default NO_DEFAULT_VALUE;

    /**
     * Default value that is used when instantiating bin (bean) in php for test purposes.
     * TODO evaluate based on defaultValue()
     * @return php default value
     */
    String phpDefaultValue() default "";

    /**
     * Returns true if user may edit the field, true by default
     * @return whether a user may edit a field
     */
    boolean editable() default true;    //if null is not allowed, renderer instantiates fields if it is null, and editors must not allow to set it null

    /**
     * 'No modification value' is a value that is treated by server as a command not to modify the previous
     * value. Usually this is null for object types, -1 for integers, this
     * behaviour is specified by NO_MODIFICATION_VALUE_AUTODETECT value. Field default is 
     * @return value that means no modification
     */
//    String noModificationValue() default "";

    /**
     * Title that user sees in an editor
     * TODO should not be hardcoded, but is to be read from localization resources
     * @return title for a user
     */
    String title() default "";    
}