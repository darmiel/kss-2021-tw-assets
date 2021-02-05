package io.d2a.list;

public class LystReference<T> {

  public final T obj;

  public LystReference<T> next;

  public LystReference(final T obj) {
    this.obj = obj;
  }

}
