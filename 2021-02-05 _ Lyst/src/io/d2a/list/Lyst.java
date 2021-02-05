package io.d2a.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A VERY bad custom implementation of a linked list
 *
 * @param <T>
 */
public class Lyst<T> implements Collection<T> {

  private LystReference<T> first;

  public LystReference<T> getRefAt(int pos) {
    LystReference<T> ref = first;
    for (int i = 0; i < pos; i++) {
      if (ref == null) {
        continue;
      }
      ref = ref.next;
    }
    return ref;
  }

  public T get(int pos) {
    final LystReference<T> ref = getRefAt(pos);
    return ref != null ? ref.obj : null;
  }

  @Override
  public boolean add(final T obj) {
    LystReference<T> ref = first;

    if (ref == null) {
      first = new LystReference<>(obj);
      return true;
    }

    while (true) {
      final LystReference<T> next = ref.next;
      if (next == null) {
        break;
      }
      ref = ref.next;
    }

    ref.next = new LystReference<>(obj);
    return true;
  }

  @Override
  public int size() {
    int size = 0;
    LystReference<T> ref = this.first;
    while (ref != null) {
      size++;
      if (ref.next == null) {
        break;
      } else {
        ref = ref.next;
      }
    }
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean remove(final Object o) {
    LystReference<T> ref = first;
    for (int i = 0; i < size(); i++) {
      if (ref == null) {
        break;
      }

      if (o.equals(ref.obj)) {
        return remove(i);
      }

      ref = ref.next;
    }
    return false;
  }

  public boolean remove(final int pos) {
    if (pos == 0) {
      if (first == null) {
        return false;
      }

      final T obj = first.obj;
      first = first.next;
      return true;
    }

    final LystReference<T> ref = getRefAt(pos);
    if (ref == null) {
      return false;
    }
    final LystReference<T> prevRef = getRefAt(pos - 1);
    if (prevRef == null) {
      return false;
    }
    prevRef.next = getRefAt(pos + 1);
    return true;
  }

  @Override
  public boolean contains(final Object o) {
    LystReference<T> ref = first;
    for (int i = 0; i < size(); i++) {
      if (ref == null) {
        break;
      }

      if (o.equals(ref.obj)) {
        return true;
      }

      ref = ref.next;
    }
    return false;
  }

  @Override
  public void clear() {
    this.first = null;
  }

  @Override
  public boolean containsAll(final Collection<?> c) {
    for (final Object o : c) {
      if (!contains(o)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addAll(final Collection<? extends T> c) {
    for (final T t : c) {
      if (!add(t)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    boolean allDeleted = true;
    for (final Object o : c) {
      if (!remove(o)) {
        allDeleted = false;
      }
    }
    return allDeleted;
  }

  @Override
  public boolean removeIf(final Predicate<? super T> filter) {
    LystReference<T> ref = first;
    for (int i = 0; i < size(); i++) {
      if (ref == null) {
        break;
      }

      if (filter.test(ref.obj)) {
        return remove(i);
      }

      ref = ref.next;
    }
    return false;
  }

  @Override
  public Iterator<T> iterator() {
    return new LystReferenceIterator<>(first);
  }

  @Override
  public void forEach(final Consumer<? super T> action) {

  }

  @Override
  public Object[] toArray() {
    final Object[] objects = new Object[size()];

    LystReference<T> ref = first;
    for (int i = 0; i < size(); i++) {
      if (ref == null) {
        break;
      }

      objects[i] = ref.obj;
      ref = ref.next;
    }

    return objects;
  }

  @Override
  public <T> T[] toArray(final T[] a) {
    throw new NotImplementedException();
  }


  @Override
  public boolean retainAll(final Collection<?> c) {
    throw new NotImplementedException();
  }


  @Override
  public Spliterator<T> spliterator() {
    throw new NotImplementedException();
  }

  @Override
  public Stream<T> stream() {
    throw new NotImplementedException();
  }

  @Override
  public Stream<T> parallelStream() {
    throw new NotImplementedException();
  }

}