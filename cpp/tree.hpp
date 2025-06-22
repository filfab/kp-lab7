#include <string>
#include <iostream>
#include <vector>
#include <sstream>
#include "node.hpp"
#include "util.hpp"

template <typename T>
class Tree {
private:
    Node<T>* root;
    inline Node<T>* search(Node<T>* x, T k);
    inline Node<T>* successor(Node<T>* x);
    inline Node<T>* minimum(Node<T>* x);
    inline void print(Node<T>* x, std::string prefix);

public:
    Tree();
    ~Tree();
    inline bool contains(T k);
    inline void insert(T z);
    inline void deletee(T z);
    inline void draw();
    inline std::string interpret(std::string cmd);
};

template <typename T>
Tree<T>::Tree() : root(nullptr) {}

template <typename T>
Tree<T>::~Tree() {}

template <typename T>
Node<T>* Tree<T>::search(Node<T>* x, T k) {
    if (x==nullptr || x->key==k) { return x; }
    return (k < x->key) ? search(x->left, k) : search(x->right, k);
}

template <typename T>
bool Tree<T>::contains(T k) {
    return search(root, k) != nullptr;
}

template <typename T>
void Tree<T>::insert(T z) {
    Node<T>* node = new Node<T>(z);
    Node<T>* y = nullptr;
    Node<T>* x = root;
    while (x != nullptr) {
        y = x;
        if (z < x->key) { x = x->left; }
        else { x = x->right; }
    }
    node->p = y;
    if (y == nullptr) { root = node; }
    else if (z < y->key) { y->left = node; }
    else { y->right = node; }
}

template <typename T>
Node<T>* Tree<T>::successor(Node<T>* x) {
    if (x->right != nullptr) { return minimum(x->right); }
    Node<T>* y = x->p;
    while (y!=nullptr && x==y->right) {
        x = y;
        y = y->p;
    }
    return y;
}

template <typename T>
Node<T>* Tree<T>::minimum(Node<T>* x) {
    while (x->left != nullptr) { x = x->left; }
    return x;
}

template <typename T>
void Tree<T>::deletee(T _z) {
    Node<T>* y;
    Node<T>* x = nullptr;
    Node<T>* z = search(root, _z);
    if (z == nullptr) return;

    if (z->left==nullptr || z->right==nullptr) {
        y = z;
    } else {
        y = successor(z);
    }

    if (y->left != nullptr) {
        x = y->left;
    } else {
        x = y->right;
    }

    if (x != nullptr) { x->p = y->p; }

    if (y->p == nullptr) { root = x; }
    else if (y == y->p->left) { y->p->left = x; }
    else { y->p->right = x; }

    if (y != z) { z->key = y->key; }

    delete y;
}

template <typename T>
void Tree<T>::draw() {
    if (root != nullptr) {
        std::cout << root->key << std::endl;
        print(root->left, "");
        print(root->right, "");
    }
}

template <typename T>
void Tree<T>::print(Node<T>* x, std::string prefix) {
    if (x != nullptr) {
        std::cout << prefix << "^---" << x->key << std::endl;
        print(x->left, prefix + (x->p->right==nullptr ? "    " : "|   "));
        print(x->right, prefix + (x->p->right==nullptr ? "    " : "|   "));
    }
}

template <typename T>
std::string Tree<T>::interpret(std::string cmd) {
    std::vector<std::string> parts = split(cmd, ' ');
    if (parts.size() < 2) {
        if (parts.size()>0 && parts[0]=="draw") { draw(); return ""; }
        return "!! Invalid number of argumnents";
    }

    T arg;
    std::stringstream x(parts[1]);
    if (!(x >> arg)) { return "!! Invalid value: " + parts[1]; }

    if (parts[0] == "search") { return contains(arg) ? "Tree contains "+parts[1] : "Tree does not contain "+parts[1]; }
    else if (parts[0] == "insert") { insert(arg); return "Inserted "+parts[1]; }
    else if (parts[0] == "delete") { deletee(arg); return "Deleted "+parts[1]; }
    else { return "!! Unknown command"; }
}
