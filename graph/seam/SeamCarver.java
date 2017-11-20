import edu.princeton.cs.algs4.Picture;

import java.awt.*;

/**
 * Data type to find and remove a vertical seam and horizontal seam of minimum total energy
 * in a picture
 */
public class SeamCarver {
    enum Orientation
    {
        VERTICAL,
        HORIZONTAL
    }

    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        picture = new Picture(picture);
    }

    // current picture
    public Picture picture()
    {

        return picture;
    }

    // width of current picture
    public int width()
    {
        return picture.width();
    }

    // height of current picture
    public  int height()
    {
        return picture.height();
    }


    // energy of pixel at column x and row y
    public double energy(int x, int y)
    {
        if (x < 0 || x > picture.width()-1)
        {
            throw new IllegalArgumentException("Invalid argument x: " + x);
        }

        if (y < 0 || y > picture.height()-1)
        {
            throw new IllegalArgumentException("Invalid argument y: " + y);
        }

        if (x == 0 || y == 0 || x == picture.width()-1 || y == picture.height()-1)
        {
            return 1000;
        }

        // Compute gradient with respect to x square
        double gradientX2 =gradSquare(x-1, y, x+1, y);
        double gradientY2 =gradSquare(x, y-1, x, y+1);
        return Math.sqrt(gradientX2 + gradientY2);
    }

    private double gradSquare(int x1, int y1, int x2, int y2)
    {
        Color c1 = picture.get(x1, y1);
        Color c2 = picture.get(x2, y2);

        // TODO: use c1.getRGB() & bit operators
        double deltaSq = (c1.getRed() - c2.getRed())*(c1.getRed() - c2.getRed())
                + (c1.getGreen() - c2.getGreen())*(c1.getGreen() - c2.getGreen())
                + (c1.getBlue() - c2.getBlue())*(c1.getBlue() - c2.getBlue());

        return deltaSq;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        return null;
    }

    private class Cell
    {
        int x;
        int y;
        Cell(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    private class EnergyTable
    {
        int W;
        int H;
        double[][] energyMatrix;
        public EnergyTable(Orientation orientation)
        {
            if (orientation == Orientation.VERTICAL)
            {
                W = picture.width();
                H = picture.height();
                energyMatrix = new double[W][H];
                for (int y = 0;  y < H; y++)
                {
                    for (int x = 0; x < W; x++)
                    {
                        energyMatrix[x][y] = energy(x, y);
                    }
                }
            } if (orientation == Orientation.HORIZONTAL)
            {
                W = picture.height();
                H = picture.width();
                energyMatrix = new double[W][H];
                for (int y = 0;  y < H; y++)
                {
                    for (int x = 0; x < W; x++)
                    {
                        energyMatrix[x][y] = energy(y, x);
                    }
                }
            }
        }
    }

    // sequence of indices for vertical seam
    public  int[] findVerticalSeam()
    {
        EnergyTable energyTable = new EnergyTable(Orientation.VERTICAL);

        // store min accumulative energy to pixel x, y
        double[][] distTo = new double[picture.width()][energyTable.H];
        for (int y = 0; y < energyTable.H; y++)
        {
            for (int x = 0;  x < energyTable.W; x++)
            {
                if (y ==0)
                {
                    distTo[x][y] = energyTable.energyMatrix[x][y];
                }
                else
                {
                    distTo[x][y] = Double.POSITIVE_INFINITY;
                }
            }
        }

        Cell[][] edgeTo = new Cell[energyTable.W][energyTable.H];
        for (int y = 0; y < energyTable.H-1; y++)
        {
            for (int x = 0;  x < energyTable.W; x++)
            {
                relax(x, y, distTo, edgeTo, energyTable);
            }
        }

        return findVerticalSeam(distTo, edgeTo);
    }

    private int[] findVerticalSeam(double[][] distTo, Cell[][] edgeTo) {
        double minDist = distTo[0][picture.height()-1];
        int minX = 0;
        for (int x = 1; x < picture.width(); x++)
        {
            if (minDist < distTo[x][picture.height()-1])
            {
                minDist = distTo[x][picture.height()-1];
                minX = x;
            }
        }

        int[] seamColumns = new int[picture.height()];
        seamColumns[picture.height()-1] = minX;
        Cell cell = edgeTo[minX][picture.height()-1];
        while (cell != null && cell.y >=0 )
        {
            seamColumns[cell.y] = cell.x;
            cell = edgeTo[cell.x][cell.y];
        }
        return seamColumns;
    }

    private void relax(int x, int y, double[][] distTo, Cell[][] edgeTo, EnergyTable energyTable)
    {
        // relax x, y+1
        if (distTo[x][y+1] > distTo[x][y] + energyTable.energyMatrix[x][y+1])
        {
            distTo[x][y+1] = distTo[x][y] + energyTable.energyMatrix[x][y+1];
            edgeTo[x][y+1] = new Cell(x, y);
        }

        // relax x-1, y+1
        if (x > 0 && distTo[x-1][y+1] > distTo[x][y] + energyTable.energyMatrix[x-1][y+1])
        {
            distTo[x-1][y+1] = distTo[x][y] + energyTable.energyMatrix[x-1][y+1];
            edgeTo[x-1][y+1] = new Cell(x, y);
        }

        // relax x+1, y+1
        if (x < picture.width()-1 && distTo[x+1][y+1] > distTo[x][y] + energyTable.energyMatrix[x+1][y+1])
        {
            distTo[x+1][y+1] = distTo[x][y] + energyTable.energyMatrix[x+1][y+1];
            edgeTo[x+1][y+1] = new Cell(x, y);
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        findVerticalSeam();
    }
}

