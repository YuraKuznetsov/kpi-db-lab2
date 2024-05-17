package org.example.tree;

import org.example.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BPlusTreeTest {

    BPlusTree tree;

    @BeforeEach
    void setUp() {
        tree = new BPlusTree(4);

        tree.insert("Alice", "+380 11 111-11-11");
        tree.insert("Bob", "+380 22 222-22-22");
        tree.insert("Zzz", "+380 11 333-33-33");
        tree.insert("Igor", "+380 44 444-44-44");
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
    }

    @Test
    void shouldReturnEmptyList_whenNameDoesNotExists() {
        List<Contact> contacts = tree.search("asdfasf");
        System.out.println(contacts);
    }

    @Test
    void shouldReturnContact_whenNameExists() {
        List<Contact> contacts = tree.search("Yurii");
        System.out.println(contacts);
    }

    @Test
    void shouldReturnEmptyList_whenContactWasDeleted() {
        tree.delete("Yurii");
        System.out.println(tree.search("Yurii"));
    }

    @Test
    void shouldReturnAllContacts_whereNamesAreBeforeGivenOne() {
        List<Contact> contactsBefore = tree.getAllBefore("Bob");
        System.out.println(contactsBefore);
    }

    @Test
    void shouldReturnAllContacts_whereNamesAreAfterGivenOne() {
        List<Contact> contactsBefore = tree.getAllAfter("Vlad");
        System.out.println(contactsBefore);
    }
}