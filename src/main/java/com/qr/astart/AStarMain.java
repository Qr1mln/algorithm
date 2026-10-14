package com.qr.astart;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.qr.astart.DrawPathPixels.draw;

public class AStarMain {

    public static void main(String[] args) throws IOException {
        BufferedImage img = ImageIO.read(new File("pixel_points.png"));
        int width = img.getWidth();
        int height = img.getHeight();
        System.out.println("尺寸：" + width + " x " + height);

        Node s = new Node(1,1);
        Node e = new Node(4,4);

        List<Node> paths = AStarUtils.plan(img,s,e);
        draw(
                "pixel_points.png",   // 输入像素图片
                paths,         // 节点列表
                "output.png",  // 输出图片
                Color.YELLOW,     // 标记色
                1              // 块大小（单像素看不清可调大）
        );
    }
}
