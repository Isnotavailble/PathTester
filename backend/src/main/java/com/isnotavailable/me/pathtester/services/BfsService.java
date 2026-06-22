package com.isnotavailable.me.pathtester.services;

import com.isnotavailable.me.pathtester.dtos.BfsResult;
import com.isnotavailable.me.pathtester.dtos.Node;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Service
public class BfsService {

    private final GraphGeneratorService graphGeneratorService;

    public BfsService(GraphGeneratorService graphGeneratorService) {
        this.graphGeneratorService = graphGeneratorService;
    }

    /**
     * Integrates with GraphGeneratorService to generate the graph,
     * locate the start and end nodes, and perform the BFS pathfinding.
     */
    public BfsResult findPath(int[][] matrix, int startRow, int startCol, int endRow, int endCol) {
        Map<List<Integer>, Node> graph = graphGeneratorService.generate(matrix);
        if (graph == null || graph.isEmpty()) {
            return null;
        }

        Node startNode = graph.get(List.of(startCol, startRow));
        Node endNode = graph.get(List.of(endCol, endRow));

        if (startNode == null || endNode == null) {
            return null;
        }

        List<Node> shortestPath = findShortestPath(startNode, endNode);
        List<Node> traversalOrder = traverseGraph(startNode, endNode);

        return new BfsResult(shortestPath, traversalOrder);
    }

    /**
     * Traverses the reachable graph from the startNode using BFS, stopping if targetNode is found.
     * @param startNode the starting node
     * @return list of nodes in visited order
     */
    public List<Node> traverseGraph(Node startNode) {
        return traverseGraph(startNode, null);
    }

    /**
     * Traverses the reachable graph from the startNode using BFS, stopping if targetNode is found.
     * @param startNode the starting node
     * @param targetNode the target node (endpoint) to stop traversal at, or null to traverse fully
     * @return list of nodes in visited order
     */
    public List<Node> traverseGraph(Node startNode, Node targetNode) {
        List<Node> visitedOrder = new ArrayList<>();
        if (startNode == null) {
            return visitedOrder;
        }

        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visitedOrder.add(current);

            if (current == targetNode) {
                break;
            }

            List<Node> neighbours = current.getNeighbours();
            if (neighbours != null) {
                for (Node neighbour : neighbours) {
                    if (!visited.contains(neighbour)) {
                        visited.add(neighbour);
                        queue.add(neighbour);
                    }
                }
            }
        }

        return visitedOrder;
    }

    /**
     * Finds the shortest path from startNode to targetNode using BFS.
     * @param startNode starting node
     * @param targetNode target node
     * @return list of nodes from start to target (inclusive), or null if no path exists
     */
    public List<Node> findShortestPath(Node startNode, Node targetNode) {
        if (startNode == null || targetNode == null) {
            return null;
        }

        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        Map<Node, Node> parentMap = new HashMap<>();

        queue.add(startNode);
        visited.add(startNode);
        parentMap.put(startNode, null);

        boolean pathFound = false;
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current == targetNode) {
                pathFound = true;
                break;
            }

            List<Node> neighbours = current.getNeighbours();
            if (neighbours != null) {
                for (Node neighbour : neighbours) {
                    if (!visited.contains(neighbour)) {
                        visited.add(neighbour);
                        parentMap.put(neighbour, current);
                        queue.add(neighbour);
                    }
                }
            }
        }

        if (!pathFound) {
            return null;
        }

        // Reconstruct path
        LinkedList<Node> path = new LinkedList<>();
        Node step = targetNode;
        while (step != null) {
            path.addFirst(step);
            step = parentMap.get(step);
        }

        return path;
    }
}
