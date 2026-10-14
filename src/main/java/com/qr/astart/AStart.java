package com.qr.astart;

import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Data
class Grid{
    private String name;
    private int x;
    private int y;
    private int f;

    public Grid(String name,int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void manhattan(Grid start, Grid end) {
        //f = g + h
        int g = Math.abs(start.getX()-this.getX())+Math.abs(start.getY()-this.getY());
        int h = Math.abs(end.getX()-this.getX())+Math.abs(end.getY()-this.getY());
        this.f = g+h;
    }

    public String getPosition(){
        return "("+x+","+y+")";
    }
}

public class AStart {

    List<Grid> openList = new ArrayList<>();
    List<Grid> closeList = new ArrayList<>();
    Set<Grid> mergeList = new LinkedHashSet<>();

    Grid start = new Grid("a0", 0, 0);
    Grid end = new Grid("z0", 5, 10);

    public boolean exist(Grid grid){
        mergeList.addAll(openList);
        mergeList.addAll(closeList);
        return mergeList.stream().noneMatch((g)->g.getPosition().equals(grid.getPosition()));
    }

    public  void start() throws IOException {
        System.out.println("Hello World!");
        // 读取图片文件
        BufferedImage image = ImageIO.read(new File("D:\\IdeaProjects\\algorithm\\src\\main\\java\\com\\qr\\astart\\gridMap.png"));

        int width = image.getWidth();
        int height = image.getHeight();
        System.out.println("尺寸: " + width + " x " + height);


        int tag = 97;
        System.out.println(tag);
        Grid curNode = start;
        // 起始格子
        Grid o = new Grid("a0", curNode.getX(), curNode.getY());
        o.manhattan(start, end);
        openList.add(o);
        for (int x = 0; x < 100; x++) {
            char title = (char) (tag + x);
            // 取上面格子
            Grid top = new Grid(title + "1", curNode.getX(), curNode.getY() - 1);
            if (exist(top)) {
                top.manhattan(start, end);
                openList.add(top);
            }
            // 取右边格子
            Grid right = new Grid(title + "2", curNode.getX() + 1, curNode.getY());
            if (exist(right)) {
                right.manhattan(start, end);
                openList.add(right);
            }
            // 取下边格子
            Grid bottom = new Grid(title + "3", curNode.getX(), curNode.getY() + 1);
            if (exist(bottom)) {
                bottom.manhattan(start, end);
                openList.add(bottom);
            }
            // 取左边边格子
            Grid left = new Grid(title + "4", curNode.getX() - 1, curNode.getY());
            if (exist(left)) {
                left.manhattan(start, end);
                openList.add(left);
            }
            openList.sort(Comparator.comparingInt(Grid::getF));
            curNode = openList.get(0);
            closeList.add(curNode);
            openList.remove(curNode);
        }
    }

    public static void main(String[] args) throws IOException {
        AStart aStart =new AStart();
        aStart.start();
        //aStart.openList.forEach(System.out::println);
        aStart.closeList.forEach(System.out::println);
        // 2. 指定各颜色与起止位置
        Color bgColor    = Color.BLACK;   // 背景色
        Color drawColor  = Color.YELLOW;   // 路径（中间节点）绘制颜色
        Color startColor = Color.GREEN;     // 起始节点颜色
        Color endColor   = Color.RED;    // 结束节点颜色
        int[] startPos   = {0, 0};        // 起始位置 {x, y}
        int[] endPos     = {0, 99};       // 结束位置 {x, y}

        // 3. 绘制像素路径
        DrawPixels.drawPath(
                aStart.closeList,
                100, 100,
                bgColor,
                drawColor,
                startColor, aStart.start,
                endColor,   aStart.end,
                "pixel_100x100.png"
        );
    }
}
