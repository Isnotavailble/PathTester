package com.isnotavailable.me.pathtester.controllers;

import com.isnotavailable.me.pathtester.dtos.*;
import com.isnotavailable.me.pathtester.services.BfsService;
import com.isnotavailable.me.pathtester.services.AstarService;
import com.isnotavailable.me.pathtester.services.DfsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
@CrossOrigin("*")
public class MainController {

    private final BfsService bfsService;
    private final AstarService astarService;
    private final DfsService dfsService;

    public MainController(BfsService bfsService, AstarService astarService, DfsService dfsService) {
        this.bfsService = bfsService;
        this.astarService = astarService;
        this.dfsService = dfsService;
    }

    @PostMapping("bfs")
    public ResponseEntity<?> findPath(@RequestBody BfsRequest request) {
        int[][] matrix = request.matrix();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return ResponseEntity.badRequest().body("Matrix must not be empty");
        }

        if (request.start() == null || request.end() == null) {
            return ResponseEntity.badRequest().body("Start and end points must be specified");
        }

        // Extract start and end coordinates using record accessors
        int startRow = request.start().row();
        int startCol = request.start().column();
        int endRow = request.end().row();
        int endCol = request.end().column();

        long startTime = System.nanoTime();
        // Delegate graph generation and BFS execution to BfsService
        BfsResult bfsResult = bfsService.findPath(matrix, startRow, startCol, endRow, endCol);
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1_000_000.0;

        if (bfsResult == null) {
            return ResponseEntity.badRequest().body("Start or end point is invalid/not walkable");
        }

        // Convert Node lists to simple representation to avoid circular reference recursion in Jackson
        List<Map<String, Integer>> pathResponse = null;
        if (bfsResult.shortestPath() != null) {
            pathResponse = new ArrayList<>();
            for (Node node : bfsResult.shortestPath()) {
                pathResponse.add(Map.of("row", node.getRow(), "column", node.getColumn()));
            }
        }

        List<Map<String, Integer>> traversalResponse = new ArrayList<>();
        for (Node node : bfsResult.traversalOrder()) {
            traversalResponse.add(Map.of("row", node.getRow(), "column", node.getColumn()));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("path", pathResponse);
        response.put("traversalOrder", traversalResponse);
        response.put("timeTaken", timeTaken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("astar")
    public ResponseEntity<?> findPathAstar(@RequestBody AstarRequest request) {
        int[][] matrix = request.matrix();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return ResponseEntity.badRequest().body("Matrix must not be empty");
        }

        if (request.start() == null || request.end() == null) {
            return ResponseEntity.badRequest().body("Start and end points must be specified");
        }

        int startRow = request.start().row();
        int startCol = request.start().column();
        int endRow = request.end().row();
        int endCol = request.end().column();

        long startTime = System.nanoTime();
        AstarResult astarResult = astarService.findPath(matrix, startRow, startCol, endRow, endCol);
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1_000_000.0;

        if (astarResult == null) {
            return ResponseEntity.badRequest().body("Start or end point is invalid/not walkable");
        }

        List<Map<String, Integer>> pathResponse = null;
        if (astarResult.shortestPath() != null) {
            pathResponse = new ArrayList<>();
            for (Node node : astarResult.shortestPath()) {
                pathResponse.add(Map.of("row", node.getRow(), "column", node.getColumn()));
            }
        }

        List<Map<String, Integer>> traversalResponse = new ArrayList<>();
        for (Node node : astarResult.traversalOrder()) {
            traversalResponse.add(Map.of("row", node.getRow(), "column", node.getColumn()));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("path", pathResponse);
        response.put("traversalOrder", traversalResponse);
        response.put("timeTaken", timeTaken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("dfs")
    public ResponseEntity<?> findPathDfs(@RequestBody DfsRequest request) {
        int[][] matrix = request.matrix();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return ResponseEntity.badRequest().body("Matrix must not be empty");
        }

        if (request.start() == null || request.end() == null) {
            return ResponseEntity.badRequest().body("Start and end points must be specified");
        }

        int startRow = request.start().row();
        int startCol = request.start().column();
        int endRow = request.end().row();
        int endCol = request.end().column();

        long startTime = System.nanoTime();
        DfsResult dfsResult = dfsService.findPath(matrix, startRow, startCol, endRow, endCol);
        long endTime = System.nanoTime();
        double timeTaken = (endTime - startTime) / 1_000_000.0;

        if (dfsResult == null) {
            return ResponseEntity.badRequest().body("Start or end point is invalid/not walkable");
        }

        List<Map<String, Integer>> pathResponse = null;
        if (dfsResult.shortestPath() != null) {
            pathResponse = new ArrayList<>();
            for (Node node : dfsResult.shortestPath()) {
                pathResponse.add(Map.of("row", node.getRow(), "column", node.getColumn()));
            }
        }

        List<Map<String, Integer>> traversalResponse = new ArrayList<>();
        for (Node node : dfsResult.traversalOrder()) {
            traversalResponse.add(Map.of("row", node.getRow(), "column", node.getColumn()));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("path", pathResponse);
        response.put("traversalOrder", traversalResponse);
        response.put("timeTaken", timeTaken);

        return ResponseEntity.ok(response);
    }
}
