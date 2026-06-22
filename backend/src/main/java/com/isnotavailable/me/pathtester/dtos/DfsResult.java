package com.isnotavailable.me.pathtester.dtos;

import java.util.List;

public record DfsResult(List<Node> shortestPath, List<Node> traversalOrder) {}
