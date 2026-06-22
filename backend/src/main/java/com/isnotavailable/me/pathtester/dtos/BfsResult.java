package com.isnotavailable.me.pathtester.dtos;

import java.util.List;

public record BfsResult(List<Node> shortestPath, List<Node> traversalOrder) {}
