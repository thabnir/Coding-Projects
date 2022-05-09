package com.henry;

public class Grid {
    Node[][] grid;

    Grid(int width, int height) {
        grid = new Node[width][height];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new Node(x, y, true);
            }
        }
    }

    Grid(int width, int height, boolean[][] obstacleMap) {
        grid = new Node[width][height];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                grid[x][y] = new Node(x, y, obstacleMap[x][y]);
            }
        }
    }

    class Node {
        boolean crossable;
        int x;
        int y;

        Node(int x, int y, boolean crossable) {
            this.crossable = crossable;
            this.x = x;
            this.y = y;
        }
    }

    int getSizeX() {
        return grid.length;
    }

    int getSizeY() {
        return grid[0].length;
    }
}
