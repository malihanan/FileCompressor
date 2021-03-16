package com.malihanan.compressor.algorithms.statistical;

public class Node implements Comparable<Node>
{
    private int byteValue;
    private int count;
    private Node left, right;

    public Node(int byteValue)
    {
        this.byteValue = byteValue;
        this.left = this.right = null;
    }

    public Node(int byteValue, int count)
    {
        this(byteValue);
        this.count = count;
    }

    public Node(Node left, Node right)
    {
        this.left = left;
        this.right = right;
    }

    public Node(int count, Node left, Node right)
    {
        this(left, right);
        this.count = count;
    }

    public int getByteValue() {
        return byteValue;
    }

    public int getCount() {
        return count;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    @Override
    public int compareTo(Node o) {
        return this.count - o.count;
    }

    @Override
    public String toString() {
        return "Byte: " + this.byteValue + " Count: " + this.count;
    }
}
