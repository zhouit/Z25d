package com.arpg.fight;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.arpg.utils.ImageUtils;
import com.arpg.utils.ResourceManager;

/**
 * 魔法类
 * 
 * @author zhou
 *
 */
public class Magic{
  public static Image TITLE = ResourceManager.loadImage("resource/image/title.png");

  // 魔法名称(中文名)
  private String name;
  // 魔法级数
  private int level;
  // 此魔法伤害值
  private int injure = 200;
  // 施放魔法需要的mp
  private int cost = 60;
  // 魔法施放时间间隔
  private int cd = 10;
  // 魔法类型
  private MagicType type = MagicType.SINGLE;
  // 释放魔法时 精灵如何色相化
  private Color hue;
  // 魔法动画图片
  private String mimg;
  // 图片帧数
  private int frames;

  private Image[] animates;
  private int frameHelper;

  public Magic(String name, String img, int frames, Color hue){
    this.name = name;
    this.mimg = img;
    this.frames = frames;
    this.hue = hue;
    this.frameHelper = -1;
  }

  /**
   * 准备施放魔法的相关资源
   */
  public void prepare(){
    if(this.animates != null) return;

    this.animates = ImageUtils.split(ResourceManager.loadImage(mimg), frames);
    this.frameHelper = 0;
  }

  /**
   * 魔法释放当前帧的图片
   * 
   * @return
   */
  public Image current(){
    frameHelper++;
    return animates[frameHelper / 10];
  }

  /**
   * 是否可以计算伤害
   * 
   * @return
   */
  public boolean showInjure(){
    return frameHelper <= (animates.length - 2) * 10;
  }

  public boolean isInjure(){
    return frameHelper == (animates.length - 3) * 10;
  }

  public int getInjure(){
    return injure;
  }

  public void setCost(int cost){
    this.cost = cost;
  }

  public int getCost(){
    return cost;
  }

  public int getCd(){
    return cd;
  }

  public void setCd(int cd){
    this.cd = cd;
  }

  public MagicType getMagicType(){
    return type;
  }

  public void setMagicType(MagicType type){
    this.type = type;
  }

  public String getName(){
    return name;
  }

  public int getLevel(){
    return level;
  }

  public Color getHue(){
    return hue;
  }

  /**
   * 魔法动画是否播放完成
   * 
   * @return
   */
  public boolean isOver(){
    boolean result = animates == null || frameHelper >= (animates.length - 2) * 10 - 1;
    if(result) this.animates = null;
    return result;
  }

  public int getImageWidth(){
    return (int) animates[0].getWidth();
  }

  public int getImageHeight(){
    return (int) animates[0].getHeight();
  }

  /**
   * 获取用于展示魔法的小图标
   * 
   * @return
   */
  public String getMagicSkill(){
    int point = mimg.lastIndexOf(".");
    return mimg.substring(0, point) + "_skill.png";
  }

}
