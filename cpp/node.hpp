#pragma once

template <typename T>
class Node {
public:
    Node* left;
    Node* right;
    Node* p;
    T key;

    Node();
    Node(T key);
    ~Node();
};

template <typename T>
Node<T>::Node() : left(nullptr), right(nullptr), p(nullptr) {}

template <typename T>
Node<T>::Node(T key) : key(key), left(nullptr), right(nullptr), p(nullptr) {}

template <typename T>
Node<T>::~Node() {}