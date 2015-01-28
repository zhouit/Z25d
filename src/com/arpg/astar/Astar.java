package com.arpg.astar;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.arpg.utils.RpgConstants;

/**
 * 曼哈顿启发式算法:该方法的原理为时时对精灵前方的区域进行预测，一旦发现前方有障碍物，
 * 则即时启动A*寻路直到目的地。该方法可谓绝对皇室血统，一个字“正”，集所有优点之大成者
 * 
 * @author zhou
 *
 */
public class Astar{

  private Astar(){
  }

  /**
   * Astar查找路径
   * 
   * @param maze
   *          基于行列模式的二维数组地图
   * @param from
   * @param to
   * @return
   */
  public static List<Point> doAnlyse(int[][] maze, Point from, Point to){
    if(maze[to.y][to.x] == 1) return null;

    PriorityQueue<Node> opens = new PriorityQueue<Node>();
    Queue<Node> closes = new LinkedList<Node>();

    Node start = new Node(from.y, from.x);
    Node target = new Node(to.y, to.x);
    start.h = start.getCost(target);

    opens.add(start);
    Node current = null;
    while(!opens.isEmpty()){
      current = opens.poll();
      if(current.equals(target)) break;

      closes.add(current);
      // List<Node> limit = current.get4Limit(maze.length,maze[0].length);
      List<Node> limit = current.get8Limit(maze.length, maze[0].length);
      for(Node neighbor : limit){
        boolean isOpen = opens.contains(neighbor);
        boolean isClose = closes.contains(neighbor);
        boolean isHit = maze[neighbor.row][neighbor.column] == 0;
        if(!isOpen && !isClose && isHit && !isSlash(current, neighbor, maze)){
          neighbor.h = current.h + neighbor.getCost(current);
          neighbor.g = neighbor.getCost(target);
          neighbor.parent = current;
          opens.add(neighbor);
        }
      }
    }

    return getPath(current);
  }

  /**
   * 判断是否为可达的斜线通过(注:在田字格上一个斜向行走时, 如果田字格上的另一条斜线的两个点均为阻塞(即数字为1),则该行走方式不合法)
   * 
   * @param from
   * @param to
   * @param maze
   * @return
   */
  private static boolean isSlash(Node from, Node to, int[][] maze){
    boolean result = false;
    int direct = RpgConstants.getDirection(from.getPoint(), to.getPoint());
    if(direct == RpgConstants.LEFT_DOWN && maze[from.row][from.column - 1] == 1
        && maze[from.row + 1][from.column] == 1){
      result = true;
    }else if(direct == RpgConstants.RIGHT_DOWN && maze[from.row][from.column + 1] == 1
        && maze[from.row + 1][from.column] == 1){
      result = true;
    }else if(direct == RpgConstants.LEFT_UP && maze[from.row][from.column - 1] == 1
        && maze[from.row - 1][from.column] == 1){
      result = true;
    }else if(direct == RpgConstants.RIGHT_UP && maze[from.row][from.column + 1] == 1
        && maze[from.row - 1][from.column] == 1){
      result = true;
    }

    return result;
  }

  private static List<Point> getPath(Node node){
    LinkedList<Point> list = new LinkedList<Point>();
    while(node != null){
      // 注意:x行y列转换为坐标为(y,x)
      list.addFirst(new Point(node.column, node.row));
      node = node.parent;
    }

    return list;
  }

}
