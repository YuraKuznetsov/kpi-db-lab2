package org.example.tree;

import org.example.Contact;

import java.util.ArrayList;
import java.util.List;

class LeafNode extends Node {

    List<Contact> values;
    LeafNode next;

    LeafNode(int maxDegree) {
        super(maxDegree);
        this.values = new ArrayList<>();
        this.next = null;
    }

    boolean isLeaf() {
        return true;
    }
}
