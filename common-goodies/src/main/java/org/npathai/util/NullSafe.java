package org.npathai.util;

public class NullSafe {

    public static <T> void ifNotNull(T object, ThrowingConsumer<T> consumer) throws Exception {
        if (object != null) {
            consumer.accept(object);
        }
    }
}
