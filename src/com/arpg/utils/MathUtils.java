package com.arpg.utils;

import java.awt.Point;
import java.util.Random;

public class MathUtils{

  private static final Random random = new Random();

  private MathUtils(){
  }

  /**
   * 获取随即数生成器
   * 
   * @return
   */
  public static Random getRandomGen(){
    return random;
  }

  /**
   * 指定点是否在矩形内
   * 
   * @param x
   * @param y
   * @param width
   * @param height
   * @param target
   * @return
   */
  public static boolean isInRectangle(int x, int y, int width, int height, Point target){
    return target.x >= x && target.x <= x + width && target.y >= y && target.y < y + height;
  }

  /**
   * 指定点是否在圆内
   * 
   * @param x
   * @param y
   * @param radius
   * @param targetX
   * @param targetY
   * @return
   */
  public static boolean isInCicrle(int x, int y, int radius, int targetX, int targetY){
    return Math.pow(x - targetX, 2) + Math.pow(y - targetY, 2) <= Math.pow(radius, 2);
  }

}
