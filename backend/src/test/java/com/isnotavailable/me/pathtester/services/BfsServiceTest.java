package com.isnotavailable.me.pathtester.services;

import com.isnotavailable.me.pathtester.dtos.BfsResult;
import com.isnotavailable.me.pathtester.dtos.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BfsServiceTest {

    @Test
    public void testFindPath_stopsAtEndpoint() {
        GraphGeneratorService generator = new GraphGeneratorService();
        BfsService bfsService = new BfsService(generator);

        // 1 = walkable, 0 = obstacle. A grid of 1 row, 5 columns: [1, 1, 1, 1, 1]
        int[][] matrix = {
            {1, 1, 1, 1, 1}
        };

        // start at (row 0, col 0), end at (row 0, col 2)
        BfsResult result = bfsService.findPath(matrix, 0, 0, 0, 2);

        assertNotNull(result);
        List<Node> traversal = result.traversalOrder();
        assertNotNull(traversal);

        // It should have traversed only up to the end node (index 2: columns 0, 1, 2)
        assertEquals(3, traversal.size());
        assertEquals(0, traversal.get(0).getColumn());
        assertEquals(1, traversal.get(1).getColumn());
        assertEquals(2, traversal.get(2).getColumn());

        List<Node> shortestPath = result.shortestPath();
        assertNotNull(shortestPath);
        assertEquals(3, shortestPath.size());
    }
}
