package com.arpg;

import java.awt.Point;

import javafx.scene.image.Image;

import com.arpg.sprite.Sprite;
import com.arpg.utils.ResourceManager;

/**
 * 地图单向传送点
 * 
 * @author zhou
 *
 */
public class Tepleporter{
  private static final Image img = ResourceManager.loadImage("resource/image/teple.png");

  /**
   * 当前地图的传送位置
   */
  private Point current;
  /**
   * 传送至地图后的位置
   */
  private Point target;
  /**
   * 将传送至的地图
   */
  private MapContainer map;

  /**
   * 构造一个在cx、cy处传送至地图mc的tx、ty位置
   * 
   * @param cx
   * @param cy
   * @param mc
   * @param tx
   * @param ty
   */
  public Tepleporter(int cx, int cy, MapContainer mc, int tx, int ty){
    this.map = mc;
    this.current = new Point(cx, cy);
    this.target = new Point(tx, ty);
  }

  public MapContainer send(Sprite sprite){
    MapContainer result = null;
    if(sprite.isHero() && current.x == sprite.getX() && current.y == sprite.getY()){
      result = map;
    }

    return result;
  }

  /**
   * 获取传送到的地图
   * 
   * @return
   */
  public MapContainer getSendTo(){
    return map;
  }

  /**
   * 获取传送点图片
   * 
   * @return
   */
  public static Image getTepleImage(){
    return img;
  }

  /**
   * 获取传送后的位置
   * 
   * @return
   */
  public Point getTransfer(){
    return target;
  }

  /**
   * 获取当前传送点位置
   * 
   * @return
   */
  public Point getCurrent(){
    return current;
  }

}
