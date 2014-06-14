package com.momega.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martin on 6/14/14.
 */
public class Tree<V> {

    private MappedList<V,V> children = new MappedList<>();
    private Map<V, V> parents = new HashMap<>();
    private V root;

    public void add(V item, V parent) {
        children.add(parent, item);
        if (parent != null) {
            parents.put(item, parent);
        } else {
            root = parent;
        }
    }

    public List<V> getChildren(V node) {
        return children.getValues(node);
    }

    public V getParent(V node) {
        if (node == root) {
            return null;
        }
        return parents.get(node);
    }

    public V getRoot() {
        return root;
    }
}
