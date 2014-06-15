package com.momega.common;

import org.apache.commons.collections.Predicate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The simple implementation of the tree.
 * Created by martin on 6/14/14.
 */
public class Tree<V> {

    private MappedList<V,V> children = new MappedList<>();
    private Map<V, V> parents = new HashMap<>();
    private V root;

    public void add(V item, V parent) {
        children.add(parent, item);
        parents.put(item, parent);
        if (parent == null) {
            root = item;
        }
    }

    public V findByPredicate(Predicate predicate) {
        for(V item : parents.keySet()) {
            if (predicate.evaluate(item)) {
                return item;
            }
        }
        return null;
    }

    public List<V> getChildren(V node) {
        return children.getValues(node);
    }

    public V getParent(V node) {
        return parents.get(node);
    }

    public V getRoot() {
        return root;
    }
}
