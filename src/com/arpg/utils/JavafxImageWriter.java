package com.arpg.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class JavafxImageWriter{
  private WritableImage wimg;
  private float alpha = 1.0f;

  public JavafxImageWriter(Image image){
    this(image, false);
  }

  public JavafxImageWriter(Image image, boolean copy){
    boolean how = copy || !(image instanceof WritableImage);
    if(how){
      this.wimg = new WritableImage((int) image.getWidth(), (int) image.getHeight());
      fillImage(image);
    }else{
      this.wimg = (WritableImage) image;
    }
  }

  public JavafxImageWriter(int width, int height){
    this.wimg = new WritableImage(width, height);
  }

  private PixelWriter getPixelWriter(){
    return wimg.getPixelWriter();
  }

  public Image getImage(){
    return wimg;
  }

  public void setAlpha(float alpha){
    this.alpha = alpha;
  }

  public void fillImage(Image img){
    fillImage(img, 0, 0);
  }

  /**
   * 在指定坐标处填充图片
   * 
   * @param img
   * @param dstx
   * @param dsty
   */
  public void fillImage(Image img, int dstx, int dsty){
    if(img == null) return;

    int width = (int) Math.min(wimg.getWidth(), img.getWidth());
    int height = (int) Math.min(wimg.getHeight(), img.getHeight());
    getPixelWriter().setPixels(dstx, dsty, width, height, img.getPixelReader(), 0, 0);
  }

  /**
   * 绘制图片,img中不透明区域不会被draw
   * 
   * @param img
   * @param dstx
   * @param dsty
   */
  public void drawImage(Image img, int dstx, int dsty){
    PixelWriter writer = getPixelWriter();
    PixelReader reader = wimg.getPixelReader();
    PixelReader ireader = img.getPixelReader();
    int width = (int) Math.min(wimg.getWidth(), img.getWidth());
    int height = (int) Math.min(wimg.getHeight(), img.getHeight());
    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        int argb = reader.getArgb(dstx + x, dsty + y);
        int iargb = ireader.getArgb(x, y);
        // 不叠加目标图像完全透明区域
        if(iargb >>> 24 < 100) continue;

        int red = (int) ((argb >>> 16 & 0xFF) * (1 - alpha))
            + (int) ((iargb >>> 16 & 0xFF) * alpha);
        int green = (int) ((argb >>> 8 & 0xFF) * (1 - alpha))
            + (int) ((iargb >>> 8 & 0xFF) * alpha);
        int blue = (int) ((argb & 0xFF) * (1 - alpha)) + (int) ((iargb & 0xFF) * alpha);
        writer.setColor(dstx + x, dsty + y, Color.rgb(red, green, blue));
      }
    }
  }

  public void drawImage(Image img){
    drawImage(img, 0, 0);
  }

  /**
   * 彩色转灰度心理学公式:gray=R*0.299+G*0.587+B*0.114
   */
  public void gray(){
    PixelWriter writer = getPixelWriter();
    PixelReader reader = wimg.getPixelReader();
    for(int y = 0; y < wimg.getHeight(); y++){
      for(int x = 0; x < wimg.getWidth(); x++){
        Color color = reader.getColor(x, y);
        double value = 0.3 * color.getRed() + 0.6 * color.getGreen() + 0.1 * color.getBlue();
        color = Color.color(value, value, value, color.getOpacity());
        writer.setColor(x, y, color);
      }
    }
  }

  public void hue(Color colorable){
    PixelWriter writer = getPixelWriter();
    PixelReader reader = wimg.getPixelReader();
    for(int y = 0; y < wimg.getHeight(); y++){
      for(int x = 0; x < wimg.getWidth(); x++){
        int argb = reader.getArgb(x, y);
        // 不叠加目标图像完全透明区域
        if(argb >>> 24 < 100) continue;

        int red = (int) ((argb >>> 16 & 0xFF) * 0.6) + (int) (colorable.getRed() * 255 * 0.4);
        int green = (int) ((argb >>> 8 & 0xFF) * 0.6) + (int) (colorable.getGreen() * 255 * 0.4);
        int blue = (int) ((argb & 0xFF) * 0.6) + (int) (colorable.getBlue() * 255 * 0.4);

        writer.setColor(x, y, Color.rgb(red, green, blue));
      }
    }
  }

}
