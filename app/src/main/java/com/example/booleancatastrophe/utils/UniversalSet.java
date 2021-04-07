package com.example.booleancatastrophe.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * A Set that appears to contain everything.
 *
 * <p>The only meaningful operations provided by this set
 * are element membership (always returning true)
 * and equality checks to other objects.
 * All other operations are either unsupported
 * (the Universal Set should not allow addition or deletion of elements)
 * or meaningless (size of the set, viewing contained elements)
 * and should not be called upon.
 *
 * @param <E> The type, required by the Set interface
 *
 * @see Set
 */
public class UniversalSet<E> implements Set<E> {
    @Override
    public int size() { return -1; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public boolean contains(@Nullable Object o) { return true; }

    @NonNull
    @Override
    public Iterator<E> iterator() { return null; }

    @NonNull
    @Override
    public Object[] toArray() { return new Object[0]; }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        if(a.length == 0){ a = (T[]) new Object[0]; };
        a[0] = null;
        return a;
    }

    @Override
    public boolean add(Object e) {
        throw new UnsupportedOperationException("Cannot add items to a Universal Set");
    }

    @Override
    public boolean remove(@Nullable Object o) {
        throw new UnsupportedOperationException("Cannot remove items from a Universal Set");
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) { return true; }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("Cannot add items to a Universal Set");
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("Cannot remove items from a Universal Set");
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("Cannot remove items from a Universal Set");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Cannot remove items from a Universal Set");
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if(o == null){ return false; }
        if(!o.getClass().equals(this.getClass())){ return false; }
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
