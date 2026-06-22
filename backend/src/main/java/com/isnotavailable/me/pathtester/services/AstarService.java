package com.isnotavailable.me.pathtester.services;

import com.isnotavailable.me.pathtester.dtos.AstarResult;
import com.isnotavailable.me.pathtester.dtos.Node;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AstarService {

    private final GraphGeneratorService graphGeneratorService;

    public AstarService(GraphGeneratorService graphGeneratorService) {
        this.graphGeneratorService = graphGeneratorService;
    }

    /**
     * Integrates with GraphGeneratorService to generate the graph,
     * locate start and end nodes, and perform A* pathfinding.
     */
    public AstarResult findPath(int[][] matrix, int startRow, int startCol, int endRow, int endCol) {
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
        Map<Node, Node> parentMap = new HashMap<>();
        Map<Node, Double> bestG = new HashMap<>();
        Set<Node> visited = new HashSet<>();

        PriorityQueue<AstarNodeWrapper> openSet = new PriorityQueue<>();

        bestG.put(startNode, 0.0);
        double hStart = calculateHeuristic(startNode, endNode);
        openSet.add(new AstarNodeWrapper(startNode, 0.0, hStart));

        boolean pathFound = false;

        while (!openSet.isEmpty()) {
            AstarNodeWrapper currentWrapper = openSet.poll();
            Node current = currentWrapper.node();

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            traversalOrder.add(current);

            if (current == endNode) {
                pathFound = true;
                break;
            }

            List<Node> neighbours = current.getNeighbours();
            if (neighbours != null) {
                for (Node neighbour : neighbours) {
                    double stepCost = calculateStepCost(current, neighbour);
                    double tentativeG = currentWrapper.gScore() + stepCost;

                    if (tentativeG < bestG.getOrDefault(neighbour, Double.MAX_VALUE)) {
                        bestG.put(neighbour, tentativeG);
                        parentMap.put(neighbour, current);
                        double f = tentativeG + calculateHeuristic(neighbour, endNode);
                        openSet.add(new AstarNodeWrapper(neighbour, tentativeG, f));
                    }
                }
            }
        }

        List<Node> shortestPath = null;
        if (pathFound) {
            LinkedList<Node> path = new LinkedList<>();
            Node step = endNode;
            while (step != null) {
                path.addFirst(step);
                step = parentMap.get(step);
            }
            shortestPath = path;
        }

        return new AstarResult(shortestPath, traversalOrder);
    }

    private double calculateStepCost(Node from, Node to) {
        int dx = Math.abs(from.getColumn() - to.getColumn());
        int dy = Math.abs(from.getRow() - to.getRow());
        if (dx == 1 && dy == 1) {
            return Math.sqrt(2); // Diagonal step
        }
        return 1.0; // Cardinal step
    }

    private double calculateHeuristic(Node a, Node b) {
        double dx = a.getColumn() - b.getColumn();
        double dy = a.getRow() - b.getRow();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private record AstarNodeWrapper(Node node, double gScore, double fScore) implements Comparable<AstarNodeWrapper> {
        @Override
        public int compareTo(AstarNodeWrapper other) {
            return Double.compare(this.fScore, other.fScore);
        }
    }
}
