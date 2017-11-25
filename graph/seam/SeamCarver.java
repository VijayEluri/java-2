import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

/**
 * Data type to find and remove seams of minimum total energy in a picture.
 */
public class SeamCarver {
    private Picture picture;
    private int W;
    private int H;
    private int[][] colors;

    public SeamCarver(Picture picture) {
        if(picture == null)
        {
            throw new IllegalArgumentException("Invalid argument picture ");
        }
        this.picture = new Picture(picture);
        W = this.picture.width();
        H = this.picture.height();
        colors = new int[H][W];
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                colors[y][x] = picture.get(x, y).getRGB();
            }
        }
    }

    public Picture picture() {
        if (picture.width() != W || picture.height() != H) {
            picture = new Picture(W, H);
            for (int y = 0; y < H; y++) {
                for (int x = 0; x < W; x++) {
                    picture.set(x, y, new Color(colors[y][x]));
                }
            }
        }
        return picture;
    }

    public int width() {
        return W;
    }

    public int height() {
        return H;
    }

    public double energy(int x, int y) {
        if (x < 0 || x > W - 1) {
            throw new IllegalArgumentException("Invalid argument x: " + x);
        }

        if (y < 0 || y > H - 1) {
            throw new IllegalArgumentException("Invalid argument y: " + y);
        }

        if (x == 0 || y == 0 || x == W - 1 || y == H - 1) {
            return 1000;
        }

        double gradientX2 = gradSquare(x - 1, y, x + 1, y);
        double gradientY2 = gradSquare(x, y - 1, x, y + 1);
        return Math.sqrt(gradientX2 + gradientY2);
    }

    private double gradSquare(int x1, int y1, int x2, int y2) {
        int rgb1 = colors[y1][x1];
        int rgb2 = colors[y2][x2];
        int r1 = (rgb1 >> 16) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b1 = (rgb1 >> 0) & 0xFF;
        int b2 = (rgb2 >> 0) & 0xFF;

        double deltaSq = (r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);
        return deltaSq;
    }

    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    private void transpose() {
        int W2 = H;
        int H2 = W;
        int[][] colors2 = new int[H2][W2];
        for (int y = 0; y < H2; y++) {
            for (int x = 0; x < W2; x++) {
                colors2[y][x] = colors[x][y];
            }
        }
        colors = colors2;
        W = W2;
        H = H2;
    }

    private class Pixel {
        int y; // row y
        int x; // column x

        Pixel(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public int[] findVerticalSeam() {

        double[][] energyTable = new double[H][W];
        double[][] distTo = new double[H][W];
        Pixel[][] edgeTo = new Pixel[H][W];

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                energyTable[y][x] = energy(x, y);
                if (y == 0) {
                    distTo[y][x] = energyTable[y][x];
                } else {
                    distTo[y][x] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int y = 0; y < H - 1; y++) {
            for (int x = 0; x < W; x++) {
                relax(energyTable, x, y, distTo, edgeTo);
            }
        }

        return findVerticalSeam(distTo, edgeTo);
    }

    private void relax(double[][] energyTable, int x, int y, double[][] distTo, Pixel[][] edgeTo) {
        if (distTo[y + 1][x] > distTo[y][x] + energyTable[y + 1][x]) {
            distTo[y + 1][x] = distTo[y][x] + energyTable[y + 1][x];
            edgeTo[y + 1][x] = new Pixel(y, x);
        }

        if (x > 0 && distTo[y + 1][x - 1] > distTo[y][x] + energyTable[y + 1][x - 1]) {
            distTo[y + 1][x - 1] = distTo[y][x] + energyTable[y + 1][x - 1];
            edgeTo[y + 1][x - 1] = new Pixel(y, x);
        }

        if (x < W - 1 && distTo[y + 1][x + 1] > distTo[y][x] + energyTable[y + 1][x + 1]) {
            distTo[y + 1][x + 1] = distTo[y][x] + energyTable[y + 1][x + 1];
            edgeTo[y + 1][x + 1] = new Pixel(y, x);
        }
    }

    private int[] findVerticalSeam(double[][] distTo, Pixel[][] edgeTo) {
        double minDist = distTo[H - 1][0];
        int minX = 0;
        for (int x = 1; x < W; x++) {
            if (minDist > distTo[H - 1][x]) {
                minDist = distTo[H - 1][x];
                minX = x;
            }
        }

        int[] seamColumns = new int[H];
        seamColumns[H - 1] = minX;
        Pixel cell = edgeTo[H - 1][minX];
        while (cell != null && cell.y >= 0) {
            seamColumns[cell.y] = cell.x;
            cell = edgeTo[cell.y][cell.x];
        }
        return seamColumns;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Invalid argument seam ");
        }
        transpose();

        W = W - 1;
        for (int y = 0; y < H; y++) {
            int x = seam[y];
            System.arraycopy(colors[y], x + 1, colors[y], x, W - x);
        }
        transpose();
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length < H) {
            throw new IllegalArgumentException("Invalid argument seam ");
        }
        W = W - 1;
        for (int y = 0; y < H; y++) {
            int x = seam[y];
            System.arraycopy(colors[y], x + 1, colors[y], x, W - x);
        }
    }
}