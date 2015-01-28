package com.arpg;

import javafx.scene.image.Image;

import com.arpg.utils.ImageUtils;
import com.arpg.utils.ResourceManager;

/**
 * 游戏精灵UI辅助类
 * 
 * @author zhou
 *
 */
public class ImageSpriteFactory{
  private static Image heroHeader = ResourceManager.loadImage("resource/image/heroHeader.png");
  private static Image enmeyHeader = ResourceManager.loadImage("resource/image/enmeyHeader.png");
  private static final int SPRITE_WIDTH = 70;
  private static final int SPRITE_HEIGHT = 124;

  private Image[][] images;

  private int imageWidth;
  private int imageHeight;
  private int size = 0;

  public ImageSpriteFactory(String fileName){
    this(fileName, SPRITE_WIDTH, SPRITE_HEIGHT, 4);
  }

  public ImageSpriteFactory(String fileName, int width, int height, int size){
    this.size = size;
    this.imageWidth = width;
    this.imageHeight = height;
    this.images = ImageUtils.split(ResourceManager.loadImage(fileName), width, height);
  }

  public Image[] getMove(int index){
    return images[index];
  }

  public int getSize(){
    return size - 1;
  }

  public int getSpriteHeight(){
    return imageHeight;
  }

  public int getSpriteWidth(){
    return imageWidth;
  }

  /**
   * 获取英雄头像属性面板
   * 
   * @return
   */
  public static Image getHeroHeader(){
    return heroHeader;
  }

  /**
   * 获取敌人头像属性面板
   * 
   * @return
   */
  public static Image getEnmeyHeader(){
    return enmeyHeader;
  }

}
