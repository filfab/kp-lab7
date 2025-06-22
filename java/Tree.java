public class Tree<T extends Comparable<T>> {
    /** Root node of the tree */
    private Node root;
    public final String type;

    public Tree(String type) {
        this.type = type;
    }

    private class Node {
        private Node left;
        private Node right;
        private Node p;
        private T key;

        Node(T key) {
            this.key = key;
        }
    }


    private Node search(Node x, T k) {
        if (x==null || x.key.compareTo(k)==0) { return x; }

        if (k.compareTo(x.key) < 0) {
            return search(x.left, k);
        } else {
            return search(x.right, k);
        }
    }


    public boolean contains(T k) {
        return (search(root, k) != null) ? true : false;
    }


    public void insert(T z) {
        Node node = new Node(z);
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            if (z.compareTo(x.key) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        node.p = y;
        if (y == null) {
            root = node;
        } else if (z.compareTo(y.key)<0) {
            y.left = node;
        } else {
            y.right = node;
        }
    }


    private Node successor(Node x) {
        if (x.right != null) { return minimum(x.right); }
        Node y = x.p;
        while (y!=null && x==y.right) {
            x = y;
            y = y.p;
        }
        return y;
    }


    private Node minimum(Node x) {
        while (x.left != null) {
            x = x.left;
        }
        return x;
    }

    public void delete(T _z) {
        Node x, y;
        Node z = search(root, _z);
        if (z == null) { return; }

        if (z.left==null || z.right==null) {
            y = z;
        } else {
            y = successor(z);
        }

        if (y.left != null) {
            x = y.left;
        } else {
            x = y.right;
        }

        if (x != null) {
            x.p = y.p;
        }

        if (y.p == null) {
            root = x;
        } else if (y == y.p.left) {
            y.p.left = x;
        } else {
            y.p.right = x;
        }

        if (y != z) {
            z.key = y.key;
        }
    }

    public String draw() {
        if (root != null) {
            return "(" + root.key + ")\n" + print(root.left, "", "l") + print(root.right, "", "p");
        }
        return "";
    }

    private String print(Node x, String prefix, String side) {
        if (x != null) {
            return prefix + "^" + side + "---(" + x.key + ")\n"
            + print(x.left, prefix + (x.p.right==null ? "     " : "|    "), "l")
            + print(x.right, prefix + (x.p.right==null ? "     " : "|    "), "p");
        }
        return "";
    }

}
