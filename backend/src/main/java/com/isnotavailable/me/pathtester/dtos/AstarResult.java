package com.isnotavailable.me.pathtester.dtos;

import java.util.List;

public record AstarResult(List<Node> shortestPath, List<Node> traversalOrder) {}
