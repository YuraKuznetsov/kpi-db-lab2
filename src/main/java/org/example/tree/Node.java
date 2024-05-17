package org.example.tree;

import java.util.ArrayList;
import java.util.List;

abstract class Node {

    List<Integer> keys;
    int maxOrder;

    Node(int maxOrder) {
        this.maxOrder = maxOrder;
        this.keys = new ArrayList<>();
    }

    abstract boolean isLeaf();
}
