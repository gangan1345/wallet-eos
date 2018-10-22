package com.develop.wallet.eos.model.transaction.push;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Transaction signature serialization order
 *
 * @author Angus
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldAnnotation {

    /**
     * field order
     *
     * @return
     */
    int order();
}
