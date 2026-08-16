package com.isnotavailable.me.pathtester.services;

import com.isnotavailable.me.pathtester.dtos.DfsResult;
import com.isnotavailable.me.pathtester.dtos.Node;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DfsService {

    private final GraphGeneratorService graphGeneratorService;

    public DfsService(GraphGeneratorService graphGeneratorService) {
        this.graphGeneratorService = graphGeneratorService;
    }

    /**
     * Integrates with GraphGeneratorService to generate the graph,
     * locate start and end nodes, and perform standard DFS pathfinding.
     */
    public DfsResult findPath(int[][] matrix, int startRow, int startCol, int endRow, int endCol) {
        Map<List<Integer>, Node> graph = graphGeneratorService.generate(matrix);
        if (graph == null || graph.isEmpty()) {
            return null;
        }

        Node startNode = graph.get(List.of(startCol, startRow));
        Node endNode = graph.get(List.of(endCol, endRow));

        if (startNode == null || endNode == null) {
            return null;
        }

        List<Node> traversalOrder = new ArrayList<>();
        Set<Node> visited = new HashSet<>();
        List<Node> path = new ArrayList<>();

        dfs(startNode, endNode, visited, traversalOrder, path);

        List<Node> shortestPath = path.isEmpty() ? null : path;

        return new DfsResult(shortestPath, traversalOrder);
    }

    private boolean dfs(Node current, Node target, Set<Node> visited, List<Node> traversalOrder, List<Node> path) {
        visited.add(current);
        traversalOrder.add(current);

        if (current == target) {
            path.add(current);
            return true;
        }

        List<Node> neighbours = current.getNeighbours();
        if (neighbours != null) {
            for (Node neighbour : neighbours) {
                if (!visited.contains(neighbour)) {
                    if (dfs(neighbour, target, visited, traversalOrder, path)) {
                        path.addFirst(current); // Add current to path on recursive success
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
