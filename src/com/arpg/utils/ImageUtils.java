package com.arpg.utils;

import java.io.File;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.imageio.ImageIO;

/**
 * 图片处理工具
 * 
 * @author zhou
 *
 */
public class ImageUtils{

  private ImageUtils(){
  }

  /**
   * 剪切图片
   * 
   * @param source
   * @param width
   * @param height
   * @param x
   * @param y
   * @return
   */
  public static Image clip(Image source, int width, int height, int x, int y){
    PixelReader reader = source.getPixelReader();
    return new WritableImage(reader, x, y, width, height);
  }

  /**
   * 剪切图片到指定宽高(reapet-x,reapet-y)
   * 
   * @param source
   * @param width
   *          新图片的宽度
   * @param height
   *          新图片的高度
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @return
   */
  public static Image clip(Image source, int width, int height, int x1, int y1, int x2, int y2){
    PixelReader reader = source.getPixelReader();
    WritableImage result = new WritableImage(width, height);
    PixelWriter writer = result.getPixelWriter();

    for(int y = 0; y < height; y++){
      for(int x = 0; x < width; x++){
        writer.setArgb(x, y, reader.getArgb(x1 + x % (x2 - x1), y1 + y % (y2 - y1)));
      }
    }

    return result;
  }

  /**
   * 水平翻转
   * 
   * @param image
   * @return
   */
  public static Image reverseH(Image image){
    PixelReader reader = image.getPixelReader();
    int width = (int) image.getWidth(), height = (int) image.getHeight();
    WritableImage result = new WritableImage(width, height);
    PixelWriter writer = result.getPixelWriter();

    for(int y = 0; y < height; y++){
      for(int x = 0; x < width / 2; x++){
        int value = reader.getArgb(width - x - 1, y);
        writer.setArgb(width - x - 1, y, reader.getArgb(x, y));
        writer.setArgb(x, y, value);
      }
    }

    return result;
  }

  /**
   * 存储图片到文件
   * 
   * @param image
   * @param file
   */
  public static void save(Image image, String file){
    try{
      ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(file));
    }catch(Exception e){
      System.out.println(e);
    }
  }

  /**
   * 设置整张图片透明度
   * 
   * @param image
   * @param alpha
   */
  public void alphaImage(WritableImage image, float alpha){
    PixelWriter writer = image.getPixelWriter();
    PixelReader reader = image.getPixelReader();
    for(int y = 0; y < image.getHeight(); y++){
      for(int x = 0; x < image.getWidth(); x++){
        Color color = reader.getColor(x, y);
        color = Color.color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        writer.setColor(x, y, color);
      }
    }
  }

  /**
   * 获取可写操作的图片
   * 
   * @param image
   * @return
   */
  public static WritableImage getWritableImage(Image image){
    WritableImage result = null;
    if(image instanceof WritableImage){
      result = (WritableImage) image;
    }else{
      result = new WritableImage(image.getPixelReader(), (int) image.getWidth(),
          (int) image.getHeight());
    }

    return result;
  }

  public static Image[][] split(Image source, int width, int height){
    int cols = (int) source.getWidth() / width;
    int rows = (int) source.getHeight() / height;
    Image[][] result = new Image[rows][cols];
    for(int i = 0; i < rows; i++){
      for(int j = 0; j < cols; j++){
        result[i][j] = clip(source, width, height, j * width, i * height);
      }
    }

    return result;
  }

  /**
   * 将原图切割为cols帧图
   * 
   * @param source
   * @param width
   * @return
   */
  public static Image[] split(Image source, int cols){
    int width = (int) source.getWidth() / cols;
    Image[] result = new Image[cols];
    for(int i = 0; i < cols; i++){
      result[i] = ImageUtils.clip(source, width, (int) source.getHeight(), i * width, 0);
    }

    return result;
  }

  /**
   * 清空画布
   * 
   * @param context
   */
  public static void clear(GraphicsContext context){
    context.clearRect(0, 0, context.getCanvas().getWidth(), context.getCanvas().getHeight());
  }

  /**
   * 画正三角形 x/y为三角形的正中心
   * 
   * @param context
   * @param x
   * @param y
   * @param r
   */
  public static void drawTriangle(GraphicsContext context, Color color, double x, double y, int r){
    double topX = x;
    double topY = y - r;
    double leftX = x - r * Math.cos(Math.PI / 6);
    double leftY = y + r * Math.sin(Math.PI / 6);
    double rightX = x + r * Math.cos(Math.PI / 6);
    double rightY = y + r * Math.sin(Math.PI / 6);
    double[] xpos = { topX, leftX, rightX };
    double[] ypos = { topY, leftY, rightY };
    context.setFill(color);
    context.fillPolygon(xpos, ypos, 3);
  }

  /**
   * 画六芒星
   * 
   * @param context
   * @param color
   * @param x
   * @param y
   * @param r
   */
  public static void drawHexagram(GraphicsContext context, Color color, double x, double y, int r){
    context.setFill(color);
    drawTriangle(context, color, x, y, r);
    drawRTriangle(context, color, x, y, r);
  }

  /**
   * 画倒三角形
   * 
   * @param context
   * @param color
   * @param x
   * @param y
   * @param r
   */
  public static void drawRTriangle(GraphicsContext context, Color color, double x, double y, int r){
    double bottomX = x;
    double bottomY = y + r;
    double leftX = x - r * Math.cos(Math.PI / 6);
    double leftY = y - r * Math.sin(Math.PI / 6);
    double rightX = x + r * Math.cos(Math.PI / 6);
    double rightY = y - r * Math.sin(Math.PI / 6);
    double[] xpos = { bottomX, leftX, rightX };
    double[] ypos = { bottomY, leftY, rightY };
    context.setFill(color);
    context.fillPolygon(xpos, ypos, 3);
  }

  /**
   * 画五角星
   * 
   * @param context
   * @param color
   * @param x
   * @param y
   * @param r
   */
  public static void drawPentagram(GraphicsContext context, Color color, double x, double y, int r){
    double[] pointXs = new double[5];
    double[] pointYs = new double[5];
    // B
    pointXs[0] = x;
    pointYs[0] = y - r;
    // E
    pointXs[1] = x + Math.sin(36 * Math.PI / 360) * r;
    pointYs[1] = y + Math.cos(36 * Math.PI / 360) * r;
    // C
    pointXs[2] = x - Math.sin(72 * Math.PI / 360) * r;
    pointYs[2] = y - Math.cos(72 * Math.PI / 360) * r;
    // A
    pointXs[3] = x + Math.sin(72 * Math.PI / 360) * r;
    pointYs[3] = y - Math.cos(72 * Math.PI / 360) * r;
    // D
    pointXs[4] = x - Math.sin(36 * Math.PI / 360) * r;
    pointYs[4] = y + Math.cos(36 * Math.PI / 360) * r;
    context.setStroke(color);
    context.strokePolygon(pointXs, pointYs, 5);
  }

  public static void drawStyleString(GraphicsContext context, String text, int x, int y){
    if(text == null || text.length() == 0) return;

    context.setFill(Color.BLACK);
    context.setFont(Font.font("KaiTi", FontWeight.NORMAL, 14));
    context.fillText(text, x - 1, y);
    context.fillText(text, x + 1, y);
    context.fillText(text, x, y + 1);
    context.fillText(text, x, y - 1);
    context.setFill(Color.WHITE);
    context.fillText(text, x, y);
    context.restore();
  }

}
