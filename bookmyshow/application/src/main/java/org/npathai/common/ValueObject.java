package org.npathai.common;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import static java.util.Objects.requireNonNull;

public abstract class ValueObject<T> {

  private final T value;

  public ValueObject(T value) {
    this.value = requireNonNull(value, "ValueObject will not take a null reference");
  }

  public T getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}