package org.npathai.util;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T o) throws Exception;
}
