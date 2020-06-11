package org.npathai.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Documents that the type is for testing purposes only.
 */
@Target(ElementType.TYPE)
public @interface TestingUtil {

}
