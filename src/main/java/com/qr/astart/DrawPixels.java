package com.qr.astart;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawPixels {

    public static void main(String[] args) throws Exception {
        // 1. 算法计算出的最优路径：List<Grid>（直接用你的 Grid 实体）
        List<Grid> path = new ArrayList<>();
        for (int y = 0; y < 100; y++) {
            path.add(new Grid("p" + y, 0, y)); // 示例：一条竖直路径 x=0, y=0~99
        }

        // 2. 指定各颜色与起止位置
        Color bgColor    = Color.BLACK;   // 背景色
        Color drawColor  = Color.YELLOW;   // 路径（中间节点）绘制颜色
        Color startColor = Color.GREEN;     // 起始节点颜色
        Color endColor   = Color.RED;    // 结束节点颜色
        Grid startPos    = new Grid("start", 0, 0);    // 起始位置
        Grid endPos      = new Grid("end", 0, 99);     // 结束位置

        // 3. 绘制像素路径
        drawPath(
                path,
                100, 100,
                bgColor,
                drawColor,
                startColor, startPos,
                endColor,   endPos,
                "pixel_100x100.png"
        );
    }

    /**
     * 根据最优路径节点列表绘制像素路径。
     * 中间节点用 drawColor；起始位置用 startColor；结束位置用 endColor。
     * 起止位置若不在 path 内，也会单独画上对应颜色的标记。
     *
     * @param path       路径节点列表（List<Grid>）
     * @param width      图像宽
     * @param height     图像高
     * @param bgColor    背景色
     * @param drawColor  路径（中间节点）绘制颜色
     * @param startColor 起始节点颜色
     * @param startPos   起始节点 Grid；为 null 时取 path 第一个节点
     * @param endColor   结束节点颜色
     * @param endPos     结束节点 Grid；为 null 时取 path 最后一个节点
     * @param outputPath 输出路径
     */
    public static void drawPath(List<Grid> path,
                                int width,
                                int height,
                                Color bgColor,
                                Color drawColor,
                                Color startColor,
                                Grid startPos,
                                Color endColor,
                                Grid endPos,
                                String outputPath) throws IOException {

        // 起止位置缺省为路径的首尾
        if (startPos == null && path != null && !path.isEmpty()) startPos = path.get(0);
        if (endPos == null && path != null && !path.isEmpty()) endPos = path.get(path.size() - 1);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 填背景
        int bgRgb = bgColor.getRGB();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                img.setRGB(x, y, bgRgb);
            }
        }

        // 画路径：中间节点用绘制色，起止节点用各自颜色
        if (path != null) {
            for (Grid p : path) {
                if (p == null) continue;
                int x = p.getX();
                int y = p.getY();
                if (x < 0 || x >= width || y < 0 || y >= height) continue;

                Color c = drawColor;
                if (startPos != null && x == startPos.getX() && y == startPos.getY()) {
                    c = startColor;
                } else if (endPos != null && x == endPos.getX() && y == endPos.getY()) {
                    c = endColor;
                }
                img.setRGB(x, y, c.getRGB());
            }
        }

        // 起止位置若不在路径内，也单独画上标记
        drawMarker(img, startPos, startColor, width, height);
        drawMarker(img, endPos, endColor, width, height);

        // 保存原图
        ImageIO.write(img, "png", new File(outputPath));
        System.out.println("已保存：" + outputPath);

        // 保存放大版本，并在每个格子中间标记对应的 (x,y)
        // 自动计算格子边长，保证最长坐标文字能完整放进格子（不溢出）
        int fontSize = 12;
        Font labelFont = new Font("Monospaced", Font.PLAIN, fontSize);
        String sample = "(" + (width - 1) + "," + (height - 1) + ")"; // 最长标签
        BufferedImage measureBuf = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D mg = measureBuf.createGraphics();
        FontMetrics fm = mg.getFontMetrics(labelFont);
        int pad = 6;
        int scale = Math.max(fm.stringWidth(sample), fm.getHeight()) + pad;
        mg.dispose();
        measureBuf.flush();

        int bigW = width * scale;
        int bigH = height * scale;
        BufferedImage big = new BufferedImage(bigW, bigH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = big.createGraphics();
        g.setColor(bgColor);
        g.fillRect(0, 0, bigW, bigH);

        // 先建立 路径节点 -> 颜色 的映射
        Map<String, Color> cellColor = new HashMap<>();
        if (path != null) {
            for (Grid p : path) {
                if (p == null) continue;
                int x = p.getX();
                int y = p.getY();
                if (x < 0 || x >= width || y < 0 || y >= height) continue;

                Color c = drawColor;
                if (startPos != null && x == startPos.getX() && y == startPos.getY()) {
                    c = startColor;
                } else if (endPos != null && x == endPos.getX() && y == endPos.getY()) {
                    c = endColor;
                }
                cellColor.put(x + "," + y, c);
            }
        }

        // 遍历全部 100x100 格子：填充颜色并在中间标记 (x,y)
        g.setFont(labelFont);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = cellColor.getOrDefault(x + "," + y, bgColor);
                g.setColor(c);
                g.fillRect(x * scale, y * scale, scale, scale);

                g.setColor(contrastColor(c));
                String label = "(" + x + "," + y + ")";
                int tx = x * scale + (scale - fm.stringWidth(label)) / 2;
                int ty = y * scale + (scale - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(label, tx, ty);
            }
        }

        // 起止位置若不在路径内，也单独画上标记
        drawMarker(g, startPos, startColor, scale, width, height);
        drawMarker(g, endPos, endColor, scale, width, height);
        g.dispose();

        String bigPath = outputPath.replace(".png", "_big.png");
        ImageIO.write(big, "png", new File(bigPath));
        System.out.println("已保存放大版（含坐标标记）：" + bigPath);
    }

    private static void drawMarker(BufferedImage img, Grid pos, Color color, int width, int height) {
        if (pos == null) return;
        int x = pos.getX(), y = pos.getY();
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        img.setRGB(x, y, color.getRGB());
    }

    // 在放大图上用方块标记（起止位置不在路径内时）
    private static void drawMarker(Graphics2D g, Grid pos, Color color, int scale, int width, int height) {
        if (pos == null) return;
        int x = pos.getX(), y = pos.getY();
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        g.setColor(color);
        g.fillRect(x * scale, y * scale, scale, scale);
    }

    // 根据底色亮度选择对比文字颜色（深色底用白字，浅色底用黑字）
    private static Color contrastColor(Color c) {
        int lum = (c.getRed() * 299 + c.getGreen() * 587 + c.getBlue() * 114) / 1000;
        return lum < 128 ? Color.WHITE : Color.BLACK;
    }
}
