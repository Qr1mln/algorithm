package com.qr.astart;

import lombok.NonNull;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class AStarUtils {

    public static int f(Node d,Node s, Node e){
        int g = manhattan(d.getX(),d.getY(),s.getX(),s.getY());
        int h = manhattan(d.getX(),d.getY(),e.getX(),e.getY());
        return g + h;
    }

    public static int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static List<Node> plan(@NonNull BufferedImage map, @NonNull Node s, @NonNull Node e) {
        List<Node> openList = new ArrayList<>();
        List<Node> closeList = new LinkedList<>();
        // 获取到起始节点(s),设为当前节点。
        Node curNode = s;
        curNode.setF(f(curNode, s, e));
        openList.add(curNode);
        // 计算s的周围4个方位的 总代价值 = f = g + h，然后放到打开列表。
        List<Node> directions = new LinkedList<>();
        // 上
        directions.add(new Node(0,-1));
        // 右
        directions.add(new Node(1,0));
        // 下
        directions.add(new Node(0,1));
        // 左
        directions.add(new Node(-1,0));
        int i = 0;
        while (!(curNode.getX()==e.getX() && curNode.getY()==e.getY())){
            System.out.println("step:"+ ++i);
            System.out.println("open");
            openList.forEach(System.out::println);
            System.out.println("close");
            closeList.forEach(System.out::println);
            for (Node direct : directions) {
                Node d = new Node(curNode, direct);
                // 如果计算过就放弃
                if(openList.contains(d)||closeList.contains(d)){
                    continue;
                }
                int f = f(d, s, e);
                d.setF(f);
                //System.out.println(d);
                openList.add(d);
            }
            // 找到总代价最低的节点设为当前节点，放到关闭列表。
            openList.sort(Comparator.comparingInt(Node::getF));
            Node first = openList.get(0);
            curNode = first;
            openList.remove(curNode);
            closeList.add(curNode);
            // 直到找到目标节点
        }
        return closeList;
    }
}
