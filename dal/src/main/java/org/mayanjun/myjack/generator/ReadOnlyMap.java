package org.mayanjun.myjack.generator;

import java.util.*;

/**
 * Read-only to not-same package
 *
 * @author mayanjun(7/3/16)
 */
public class ReadOnlyMap<K, V> implements Map<K, V> {

    private Map<K, V> container;

    public ReadOnlyMap() {
        this.container = new HashMap<K, V>();
    }

    @Override
    public int hashCode() {
        return container.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return container.equals(o);
    }

    @Override
    public int size() {
        return container.size();
    }

    @Override
    public boolean isEmpty() {
        return container.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return container.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return container.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return container.get(key);
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("Read only map, can not be modified");
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Read only map, can not be modified");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Read only map, can not be modified");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Read only map, can not be modified");
    }

    @Override
    public Set<K> keySet() {
        return container.keySet();
    }

    @Override
    public Collection<V> values() {
        return new ReadOnlyList(container.values());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return container.entrySet();
    }

    V putInternal(K key, V value) {
        return container.put(key, value);
    }

    void putAllInternal(Map<? extends K, ? extends V> m) {
        container.putAll(m);
    }

    V removeInternal(Object key) {
        return container.remove(key);
    }

    public static class ReadOnlyList<E> extends ArrayList<E> {

        public ReadOnlyList() {
        }

        public ReadOnlyList(Collection<? extends E> c) {
            super(c);
        }

        public ReadOnlyList(int initialCapacity) {
            super(initialCapacity);
        }

        @Override
        public E set(int index, E element) {
            throw new UnsupportedOperationException("Read only list");
        }

        @Override
        public boolean add(E e) {
            throw new UnsupportedOperationException("Read only list");
        }

        @Override
        public void add(int index, E element) {
            throw new UnsupportedOperationException("Read only list");
        }

        @Override
        public E remove(int index) {
            throw new UnsupportedOperationException("Read only list");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Read only list");
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException("Read only list");
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            throw new UnsupportedOperationException("Read only list");
        }

        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException("Read only list");
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException("Read only list");
        }
    }
}
