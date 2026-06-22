package com.isnotavailable.me.pathtester.services;

import com.isnotavailable.me.pathtester.dtos.Node;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphGeneratorService {
    private final int[][] directions = {
        {-1, 0},  // Up
        {1, 0},   // Down
        {0, -1},  // Left
        {0, 1},   // Right
        {-1, -1}, // Up-Left
        {-1, 1},  // Up-Right
        {1, -1},  // Down-Left
        {1, 1}    // Down-Right
    };

    public Map<List<Integer>, Node> generate(int[][] matrix) {
        Map<List<Integer>, Node> graph = new HashMap<>();

        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return graph;
        }

        // Step 1: Create a Node object for each walkable cell in the matrix
        for (int col = 0; col < matrix[0].length; col++) {
            for (int row = 0; row < matrix.length; row++) {
                if (matrix[row][col] == 1) {
                    List<Integer> point = List.of(col, row);
                    Node node = new Node(row, col, new ArrayList<>());
                    graph.put(point, node);
                }
            }
        }

        // Step 2: For each walkable node, find and set its valid neighbours
        for (Map.Entry<List<Integer>, Node> entry : graph.entrySet()) {
            List<Integer> point = entry.getKey();
            Node currentNode = entry.getValue();
            int col = point.get(0);
            int row = point.get(1);

            List<Node> neighbours = new ArrayList<>();
            for (int[] dPoint : directions) {
                int dy = dPoint[0]; // row delta
                int dx = dPoint[1]; // col delta

                if (isValidDirection(matrix, row, col, dy, dx)) {
                    int targetRow = row + dy;
                    int targetCol = col + dx;
                    List<Integer> targetPoint = List.of(targetCol, targetRow);
                    Node targetNode = graph.get(targetPoint);
                    if (targetNode != null) {
                        neighbours.add(targetNode);
                    }
                }
            }
            currentNode.setNeighbour(neighbours);
        }

        return graph;
    }

    private boolean isWalkable(int[][] matrix, int row, int col) {
        return row >= 0 && row < matrix.length &&
               col >= 0 && col < matrix[0].length &&
               matrix[row][col] == 1;
    }

    public boolean isValidDirection(int[][] matrix, int row, int col, int dRow, int dCol) {
        if (dRow == -1 && dCol == 0) return canUp(matrix, row, col);
        if (dRow == 1 && dCol == 0) return canDown(matrix, row, col);
        if (dRow == 0 && dCol == -1) return canLeft(matrix, row, col);
        if (dRow == 0 && dCol == 1) return canRight(matrix, row, col);
        if (dRow == -1 && dCol == -1) return canUpLeft(matrix, row, col);
        if (dRow == -1 && dCol == 1) return canUpRight(matrix, row, col);
        if (dRow == 1 && dCol == -1) return canDownLeft(matrix, row, col);
        if (dRow == 1 && dCol == 1) return canDownRight(matrix, row, col);
        return false;
    }

    public boolean isValidMove(int[][] matrix, int row, int col, int targetRow, int targetCol) {
        return isValidDirection(matrix, row, col, targetRow - row, targetCol - col);
    }

    private boolean canUp(int[][] matrix, int row, int col) {
        return isWalkable(matrix, row - 1, col);
    }

    private boolean canDown(int[][] matrix, int row, int col) {
        return isWalkable(matrix, row + 1, col);
    }

    private boolean canLeft(int[][] matrix, int row, int col) {
        return isWalkable(matrix, row, col - 1);
    }

    private boolean canRight(int[][] matrix, int row, int col) {
        return isWalkable(matrix, row, col + 1);
    }

    private boolean canUpLeft(int[][] matrix, int row, int col) {
        return isWalkable(matrix, row - 1, col - 1) && isWalkable(matrix, row - 1, col) && isWalkable(matrix, row, col - 1);
    }

    private boolean canUpRight(int[][] matrix, int row, int col) {
        return isWalkable(matrix, row - 1, col + 1) && isWalkable(matrix, row - 1, col) && isWalkable(matrix, row, col + 1);
    }

    private boolean canDownLeft(int[][] matrix, int row, int col) {
        return isWalkable(matrix, row + 1, col - 1) && isWalkable(matrix, row + 1, col) && isWalkable(matrix, row, col - 1);
    }

    private boolean canDownRight(int[][] matrix, int row, int col) {
        return isWalkable(matrix, row + 1, col + 1) && isWalkable(matrix, row + 1, col) && isWalkable(matrix, row, col + 1);
    }
}
