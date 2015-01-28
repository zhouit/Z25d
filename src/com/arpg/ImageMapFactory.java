package com.arpg;

import java.awt.Point;
import java.io.File;

import javafx.scene.image.Image;

import com.arpg.utils.ImageUtils;
import com.arpg.utils.MathUtils;
import com.arpg.utils.ResourceManager;
import com.arpg.utils.RpgConstants;

/**
 * 游戏地图界面辅助类
 * 
 * @author zhou
 *
 */
public class ImageMapFactory{
  private static final int MIN_WIDTH = 150; // 小地图宽
  private static final int MIN_HEIGHT = 120; // 小地图高
  private static final int MAP_PADDING = 5; // 小地图与窗口间隔

  /**
   * 地图路径数组,基于行列模式
   */
  private int[][] maps = null;
  private Image[][] images = null;
  /**
   * 地图透明层
   */
  private Image[][] masks = null;
  /* 地图行数 */
  private int mapRows = 0;
  /* 地图列数 */
  private int mapColumns = 0;
  private Image bgimg = null;
  /**
   * 界面小地图
   */
  private Image deflate = null;

  public ImageMapFactory(String imageFile, String mapFile){
    this(imageFile, null, mapFile);
  }

  public ImageMapFactory(String imageFile, String frontImage, String mapFile){
    this.maps = ResourceManager.loadCvs(mapFile);
    this.bgimg = ResourceManager.loadImage(imageFile);
    this.deflate = ResourceManager.loadImage(imageFile, MIN_WIDTH, MIN_HEIGHT);
    this.mapColumns = (int) bgimg.getWidth() / RpgConstants.CS;
    this.mapRows = (int) bgimg.getHeight() / RpgConstants.CS;
    this.images = ImageUtils.split(bgimg, RpgConstants.CS, RpgConstants.CS);

    if(frontImage != null && new File(frontImage).exists()){
      masks = ImageUtils.split(ResourceManager.loadImage(frontImage), RpgConstants.CS,
          RpgConstants.CS);
    }
  }

  public Image getDeflateImage(){
    return deflate;
  }

  public double getDeflateX(){
    return MIN_WIDTH / getImageWidth();
  }

  public double getDeflateY(){
    return MIN_HEIGHT / getImageHeight();
  }

  /**
   * 获取小地图应该显示在当前地图的x坐标
   * 
   * @param isHeroMapLeft
   *          当前英雄是否在地图左边
   * @param mousePoint
   * @return
   */
  public int getDeflateImageX(boolean isHeroMapLeft, Point mousePoint){
    int value = isHeroMapLeft ? (RpgConstants.CANVAS_WIDTH - MIN_WIDTH - MAP_PADDING) : MAP_PADDING;
    /* 判断是否鼠标在该小地图的显示范围 */
    if(mousePoint != null
        && MathUtils.isInRectangle(value, 0, MIN_WIDTH + MAP_PADDING, MIN_HEIGHT + MAP_PADDING,
            mousePoint)){
      value = isHeroMapLeft ? MAP_PADDING : (RpgConstants.CANVAS_WIDTH - MIN_WIDTH - MAP_PADDING);
    }

    return value;
  }

  public int getDeflateImageY(){
    return MAP_PADDING;
  }

  public Image[][] getImages(){
    return images;
  }

  public Image[][] getMasks(){
    return masks;
  }

  public int[][] getMap(){
    return maps;
  }

  public double getImageHeight(){
    return bgimg.getHeight();
  }

  public double getImageWidth(){
    return bgimg.getWidth();
  }

  /**
   * 获取地图行数
   * 
   * @return
   */
  public int getMapRows(){
    return mapRows;
  }

  /**
   * 获取地图列数
   * 
   * @return
   */
  public int getMapColumns(){
    return mapColumns;
  }

}
