package org.example.tree;

import org.example.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BPlusTree {

    private Node root;
    private final int maxOrder;

    public BPlusTree(int maxOrder) {
        this.maxOrder = maxOrder;
        this.root = new LeafNode(maxOrder);
    }

    private int hashName(String name) {
        String upperName = name.toUpperCase();
        int[] letterIndexes = new int[5];

        for (int i = 0; i < upperName.length() && i < 5; i++) {
            letterIndexes[i] = upperName.charAt(i) - 'A';
        }

        int hashcode = Integer.MIN_VALUE;
        hashcode += letterIndexes[4];
        hashcode += letterIndexes[3] * 100;
        hashcode += letterIndexes[2] * 10000;
        hashcode += letterIndexes[1] * 1000000;
        hashcode += letterIndexes[0] * 100000000;

        if (upperName.length() > 5)
            hashcode += 1;

        return hashcode;
    }

    public void insert(String name, String phone) {
        int key = hashName(name);
        Contact contact = new Contact(key, name, phone);

        Node newNode = insert(root, key, contact);
        if (newNode != null) {
            InternalNode newRoot = new InternalNode(maxOrder);
            newRoot.keys.add(newNode.keys.get(0));
            newRoot.children.add(root);
            newRoot.children.add(newNode);
            root = newRoot;
        }
    }

    private Node insert(Node node, int key, Contact contact) {
        if (node.isLeaf()) {
            LeafNode leaf = (LeafNode) node;
            int pos = Collections.binarySearch(leaf.keys, key);
            if (pos < 0) pos = -pos - 1;
            leaf.keys.add(pos, key);
            leaf.values.add(pos, contact);

            if (leaf.keys.size() <= maxOrder) return null;
            return splitLeafNode(leaf);
        } else {
            InternalNode internal = (InternalNode) node;
            int pos = Collections.binarySearch(internal.keys, key);
            if (pos < 0) pos = -pos - 1;
            Node newNode = insert(internal.children.get(pos), key, contact);
            if (newNode != null) {
                internal.keys.add(pos, newNode.keys.get(0));
                internal.children.add(pos + 1, newNode);
                if (internal.keys.size() <= maxOrder) return null;
                return splitInternalNode(internal);
            }
            return null;
        }
    }

    private Node splitLeafNode(LeafNode leaf) {
        LeafNode newLeaf = new LeafNode(maxOrder);
        int from = leaf.keys.size() / 2;
        newLeaf.keys.addAll(leaf.keys.subList(from, leaf.keys.size()));
        newLeaf.values.addAll(leaf.values.subList(from, leaf.values.size()));
        leaf.keys.subList(from, leaf.keys.size()).clear();
        leaf.values.subList(from, leaf.values.size()).clear();
        newLeaf.next = leaf.next;
        leaf.next = newLeaf;

        return newLeaf;
    }

    private Node splitInternalNode(InternalNode internal) {
        InternalNode newInternal = new InternalNode(maxOrder);
        int from = (internal.keys.size() + 1) / 2;
        newInternal.keys.addAll(internal.keys.subList(from, internal.keys.size()));
        newInternal.children.addAll(internal.children.subList(from, internal.children.size()));
        internal.keys.subList(from - 1, internal.keys.size()).clear();
        internal.children.subList(from, internal.children.size()).clear();
        return newInternal;
    }

    public List<Contact> search(String name) {
        int key = hashName(name);
        return search(root, key);
    }

    private List<Contact> search(Node node, int key) {
        if (node.isLeaf()) {
            LeafNode leaf = (LeafNode) node;
            List<Contact> result = new ArrayList<>();
            for (int i = 0; i < leaf.keys.size(); i++) {
                if (leaf.keys.get(i) == key) result.add(leaf.values.get(i));
            }
            return result;
        } else {
            InternalNode internal = (InternalNode) node;
            return search(internal.getChild(key), key);
        }
    }

    public List<Contact> getAllAfter(String name) {
        int key = hashName(name);
        List<Contact> result = new ArrayList<>();

        LeafNode leaf = findFirstLeaf(root);
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (leaf.keys.get(i) >= key) {
                result.add(leaf.values.get(i));
            }
        }

        while (leaf.next != null) {
            leaf = leaf.next;
            for (int i = 0; i < leaf.keys.size(); i++) {
                if (leaf.keys.get(i) >= key) {
                    result.add(leaf.values.get(i));
                }
            }
        }

        return result;
    }

    public List<Contact> getAllBefore(String name) {
        int key = hashName(name);
        List<Contact> result = new ArrayList<>();

        LeafNode leaf = findFirstLeaf(root);
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (leaf.keys.get(i) <= key) {
                result.add(leaf.values.get(i));
            }
        }

        while (leaf.next != null) {
            leaf = leaf.next;
            for (int i = 0; i < leaf.keys.size(); i++) {
                if (leaf.keys.get(i) <= key) {
                    result.add(leaf.values.get(i));
                }
            }
        }

        return result;
    }

    private LeafNode findFirstLeaf(Node node) {
        if (node.isLeaf()) return (LeafNode) node;
        InternalNode internalNode = (InternalNode) node;
        return findFirstLeaf((internalNode.children.get(0)));
    }

    public void delete(String name) {
        int key = hashName(name);
        delete(root, key);
    }

    private void delete(Node node, int key) {
        if (node.isLeaf()) {
            LeafNode leaf = (LeafNode) node;
            int pos = Collections.binarySearch(leaf.keys, key);
            if (pos >= 0) {
                leaf.keys.remove(pos);
                leaf.values.remove(pos);
            }
        } else {
            InternalNode internal = (InternalNode) node;
            delete(internal.getChild(key), key);
        }
    }

    public static void main(String[] args) {
        BPlusTree tree = new BPlusTree(4);

        tree.insert("Alice", "+380 11 111-11-11");
        tree.insert("Bob", "+380 22 222-22-22");
        tree.insert("Zzz", "+380 11 333-33-33");
        tree.insert("Charlie", "+380 44 444-44-44");
        tree.insert("Charlie", "+380 55 555-55-55");
        tree.insert("David", "+380 66 666-66-66");
        tree.insert("Eve", "+380 77 777-77-77");
        tree.insert("Yurii", "+380 88 888-88-88");
        tree.insert("Yulia", "+380 99 999-99-99");
        tree.insert("Alexandr", "+380 12 345-67-89");
        tree.insert("Tim", "+380 98 765-43-21");
        tree.insert("Anton", "+380 96 123-33-44");
        tree.insert("Vlad", "+380 11 222-33-44");
        tree.insert("Artem", "+380 55 666-77-88");
        tree.insert("Igor", "+380 56 732-234-87");

        System.out.println("Search results for 'Bob': " + tree.search("Bob"));
//        System.out.println("Search results for names greater than 'Bob': " + tree.searchAllGreater("Bob"));

        System.out.println("Search results before deleting 'Charlie': " + tree.search("Charlie"));
        tree.delete("Charlie");
        System.out.println("Search results after deleting 'Charlie': " + tree.search("Charlie"));

//        System.out.println(tree.getAllElementsSorted());
        System.out.println(tree.getAllAfter("Charlie"));
    }
}
