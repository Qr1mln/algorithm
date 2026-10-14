package com.qr.astart;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 根据「起点坐标、终点坐标、图像高宽」生成像素点图像。
 * 在 width x height 的画布上，仅以不同颜色标记起点与终点像素点。
 */
public class DrawPixels {

    // 颜色配置
    private static final Color BG_COLOR = Color.BLACK;     // 背景色
    private static final Color START_COLOR = Color.GREEN;  // 起点色
    private static final Color END_COLOR = Color.RED;      // 终点色

    public static void main(String[] args) throws IOException {
        int startX = 1, startY = 1;  // 起点坐标
        int endX = 4, endY = 4;      // 终点坐标
        int width = 5, height = 5; // 图像高宽

        generate(startX, startY, endX, endY, width, height, "pixel_points.png");
    }

    /**
     * 生成像素点图像（原始尺寸，每个像素一个点）。
     * 背景填充，仅在起点、终点位置着色，不绘制两者之间的连线。
     *
     * @param startX 起点 x
     * @param startY 起点 y
     * @param endX   终点 x
     * @param endY   终点 y
     * @param width  图像宽
     * @param height 图像高
     * @param outputPath 输出文件
     */
    public static void generate(int startX, int startY, int endX, int endY,
                                int width, int height, String outputPath) throws IOException {
        validate(startX, startY, endX, endY, width, height);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 填背景
        int bg = BG_COLOR.getRGB();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.setRGB(x, y, bg);
            }
        }

        // 起点、终点单独着色
        img.setRGB(startX, startY, START_COLOR.getRGB());
        img.setRGB(endX, endY, END_COLOR.getRGB());

        ImageIO.write(img, "png", new File(outputPath));
        System.out.println("已保存：" + outputPath);
    }

    private static void validate(int startX, int startY, int endX, int endY, int width, int height) {
        if (width <= 0 || height <= 0)
            throw new IllegalArgumentException("图像高宽必须为正数");
        if (startX < 0 || startX >= width || startY < 0 || startY >= height)
            throw new IllegalArgumentException("起点坐标超出图像范围");
        if (endX < 0 || endX >= width || endY < 0 || endY >= height)
            throw new IllegalArgumentException("终点坐标超出图像范围");
    }
}
