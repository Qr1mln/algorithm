package com.qr.astart;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 在已有像素图上，根据节点列表（含 x、y）标记对应像素点。
 */
public class DrawPathPixels {

    private static final Color DEFAULT_MARK = Color.RED; // 默认标记色
    private static final int DEFAULT_BLOCK = 1;          // 默认点大小（像素）

    /**
     * 在输入像素图上标记路径节点。
     * @param sourceImagePath 输入像素图片（背景）
     * @param paths           节点列表
     * @param outputPath      输出图片
     */
    public static void draw(String sourceImagePath, List<Node> paths, String outputPath) throws IOException {
        draw(sourceImagePath, paths, outputPath, DEFAULT_MARK, DEFAULT_BLOCK);
    }

    /**
     * 带颜色与块大小的重载。blockSize>1 时会以 (x,y) 为左上角画一个实心方块，便于观察。
     */
    public static void draw(String sourceImagePath, List<Node> paths,
                            String outputPath, Color markColor, int blockSize) throws IOException {
        BufferedImage img = ImageIO.read(new File(sourceImagePath));
        int w = img.getWidth(), h = img.getHeight();
        int rgb = markColor.getRGB();
        int bs = Math.max(1, blockSize);

        for (Node n : paths) {
            if (n == null) continue;
            int x = n.getX(), y = n.getY();
            if (x < 0 || x >= w || y < 0 || y >= h) continue; // 越界跳过
            // 画一个 bs x bs 的方块，中心对齐到 (x,y)
            int x0 = x - bs / 2, y0 = y - bs / 2;
            for (int dy = 0; dy < bs; dy++) {
                for (int dx = 0; dx < bs; dx++) {
                    int px = x0 + dx, py = y0 + dy;
                    if (px >= 0 && px < w && py >= 0 && py < h) img.setRGB(px, py, rgb);
                }
            }
        }

        ImageIO.write(img, "png", new File(outputPath));
        System.out.println("已保存：" + outputPath + "，标记节点数=" + paths.size());
    }

    public static void main(String[] args) throws IOException {
        // 用你给出的 list 生成节点（x∈[3,14]，y∈[3, 17-x]，f=29）
        List<Node> paths = new ArrayList<>();
        for (int x = 3; x <= 14; x++) {
            for (int y = 3; y <= 17 - x; y++) {
                paths.add(new Node(x, y));
            }
        }

        draw(
                "pixel_points.png",   // 输入像素图片
                paths,         // 节点列表
                "output.png",  // 输出图片
                Color.YELLOW,     // 标记色
                3              // 块大小（单像素看不清可调大）
        );
    }
}
