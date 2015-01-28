package com.arpg.utils;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * 游戏常量
 * 
 * @author zhou
 *
 */
public final class RpgConstants{
  private final static Map<Point, Integer> directions = new HashMap<Point, Integer>(8);
  // 游戏是否结束
  public static boolean gameOver = false;

  static{
    directions.put(new Point(1, -1), RpgConstants.RIGHT_UP);
    directions.put(new Point(-1, -1), RpgConstants.LEFT_UP);
    directions.put(new Point(1, 1), RpgConstants.RIGHT_DOWN);
    directions.put(new Point(-1, 1), RpgConstants.LEFT_DOWN);

    directions.put(new Point(0, -1), RpgConstants.UP);
    directions.put(new Point(-1, 0), RpgConstants.LEFT);
    directions.put(new Point(1, 0), RpgConstants.RIGHT);
    directions.put(new Point(0, 1), RpgConstants.DOWN);
  }

  public final static int DOWN = 0;
  public final static int LEFT = 1;
  public final static int RIGHT = 2;
  public final static int UP = 3;

  public final static int LEFT_DOWN = 4;
  public final static int RIGHT_DOWN = 5;
  public final static int LEFT_UP = 6;
  public final static int RIGHT_UP = 7;

  /**
   * 游戏地图单元格宽高
   */
  public static final int CS = 32;

//  public final static int CANVAS_WIDTH = 640;
  public final static int CANVAS_WIDTH = 800;
  public final static int CANVAS_HEIGHT = 480;

  private RpgConstants(){
  }

  public static int pixelsToTiles(double pixels){
    return (int) Math.floor(pixels / CS);
  }

  public static int tilesToPixels(int tiles){
    return tiles * CS;
  }

  public static int getDirection(Point from, Point to){
    return directions.get(new Point(to.x - from.x, to.y - from.y));
  }

}
