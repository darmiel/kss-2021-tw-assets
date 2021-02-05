package io.d2a.list;

import java.util.Iterator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class LystReferenceIterator<T> implements Iterator<T> {

  private LystReference<T> ref;

  public LystReferenceIterator(final LystReference<T> ref) {
    this.ref = ref;
  }

  @Override
  public boolean hasNext() {
    return ref != null && ref.next != null;
  }

  @Override
  public T next() {
    if (ref != null) {
      ref = ref.next;
    }
    if (ref != null) {
      return ref.obj;
    }
    return null;
  }

  @Override
  public void remove() {
    throw new NotImplementedException();
  }

}
