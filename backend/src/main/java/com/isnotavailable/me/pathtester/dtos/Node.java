package com.isnotavailable.me.pathtester.dtos;


import java.util.List;

public class Node {
    private int row;
    private int column;
    private List<Node> neighbours;

    public Node(){}
    public Node(int row,int column,List<Node> neighbours){
        this.neighbours = neighbours;
        this.column = column;
        this.row = row;

    }
    public void setNeighbour(List<Node> neighbours){
        this.neighbours = neighbours;
    }
    public void setRow(int row){
        this.row = row;
    }
    public void setColumn(int col){
        this.column = col;
    }
    public List<Node> getNeighbours(){
        return this.neighbours;
    }
    public int getRow(){
        return this.row;
    }
    public int getColumn(){
        return this.column;
    }
}
