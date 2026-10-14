package com.qr.astart;

import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@Data
@ToString
public class Node {
    private int x;
    private int y;
    private int f;
    private int num;
    static int i = 0;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node(Node s,Node d) {
        this.x = s.getX() + d.getX();
        this.y = s.getY() + d.getY();
        this.num = i++;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
