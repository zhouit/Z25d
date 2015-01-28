package com.arpg.astar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{
  public int row, column;
  // 就是从起点到当前点的代价
  public int g;
  // h是当前点到终点的估计代价
  public int h;
  public Node parent;

  /**
   * 以x/y坐标构造
   * 
   * @param x
   * @param y
   */
  public Node(int row, int column){
    this.row = row;
    this.column = column;
  }

  public int getCost(Node target){
    int dx = target.row - row;
    int dy = target.column - column;

    return (int) (Math.sqrt(dx * dx + dy * dy) * 10);
  }

  public List<Node> get8Limit(int rows, int columns){
    List<Node> result = new ArrayList<Node>(8);
    for(int i = Math.max(0, row - 1); i <= Math.min(rows - 1, row + 1); i++){
      for(int j = Math.max(0, column - 1); j <= Math.min(columns - 1, column + 1); j++){
        Node temp = new Node(i, j);
        if(i == row && j == column) continue;
        result.add(temp);
      }
    }

    return result;
  }

  public List<Node> get4Limit(int rows, int columns){
    List<Node> result = new ArrayList<Node>(4);
    // 上
    if(row - 1 >= 0) result.add(new Node(row - 1, column));
    // 下
    if(row + 1 < rows - 1) result.add(new Node(row + 1, column));
    // 左
    if(column - 1 >= 0) result.add(new Node(row, column - 1));
    // 右
    if(column + 1 < columns - 1) result.add(new Node(row, column + 1));

    return result;
  }

  @Override
  public boolean equals(Object obj){
    if(this == obj) return true;
    if(obj == null) return false;
    if(getClass() != obj.getClass()) return false;
    Node other = (Node) obj;

    return other.row == row && other.column == column;
  }

  public Point getPoint(){
    return new Point(column, row);
  }

  public String toString(){
    return "[row=" + row + " ,column=" + column + " ,f=" + (h + g) + "]";
  }

  @Override
  public int compareTo(Node node){
    if(h + g > node.h + node.g) return 1;
    if(h + g == node.h + node.g) return 0;
    return -1;
  }

}
