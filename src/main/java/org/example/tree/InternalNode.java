package org.example.tree;

import java.util.ArrayList;
import java.util.List;

class InternalNode extends Node {

    List<Node> children;

    InternalNode(int maxDegree) {
        super(maxDegree);
        this.children = new ArrayList<>();
    }

    boolean isLeaf() {
        return false;
    }

    Node getChild(int key) {
        for (int i = 0; i < keys.size(); i++)
            if (key < keys.get(i))
                return children.get(i);

        return children.get(keys.size());
    }
}