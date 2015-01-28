package com.arpg.other;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SnowWord extends Application{
  private List<Point> snow = new LinkedList<>();
  double gravity = 0.15D;

  @Override
  public void start(Stage stage) throws Exception{
    WritableImage image = new WritableImage(500, 400);
    drawString(image, "东方上人", 60, 200);
    final PixelReader reader = image.getPixelReader();

    Canvas show = new Canvas(500, 400);
    final GraphicsContext context = show.getGraphicsContext2D();
    new AnimationTimer(){
      @Override
      public void handle(long arg0){
        context.clearRect(0, 0, 500, 400);

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, 500, 400);

        context.setFill(Color.WHITE);
        Iterator<Point> it = snow.iterator();
        while(it.hasNext()){
          Point p = it.next();
          p.vy += gravity * p.s;
          p.y += p.vy;
          if(p.y >= 400){
            it.remove();
          }else{
            if(reader.getColor((int) p.x, (int) p.y).equals(Color.BLACK)){
              p.y -= p.vy;
              p.vy = 0;
              p.y += 0.2;
            }

            context.fillOval(p.x, p.y, 2, 2);
          }
        }

        for(int i = 0; i < 10; i++){
          snow.add(new Point(Math.random() * 500, 0, Math.random() + 0.5));
        }
      }
    }.start();

    Scene scene = new Scene(new Group(show));
    stage.setScene(scene);
    stage.setTitle("粒子系统 - www.zhouhaocheng.cn");
    stage.show();
  }

  void noisy(PixelReader reader, GraphicsContext context){
    context.setFill(Color.BLACK);
    context.fillRect(0, 0, 500, 400);

    context.setFill(Color.WHITE);
    for(int i = 0; i < 500; i++){
      for(int j = 0; j < 400; j++){
        double r = Math.random();
        if(reader.getColor(i, j).equals(Color.BLACK)){
          if(r > 0.75) context.fillOval(i, j, 1, 1);
        }else if(r > 0.95){
          context.fillOval(i, j, 1, 1);
        }
      }
    }
  }

  public void drawString(WritableImage image, String text, int x, int y){
    BufferedImage buffer = SwingFXUtils.fromFXImage(image, null);
    Graphics g = buffer.getGraphics();
    g.setColor(java.awt.Color.BLACK);
    g.setFont(new Font("Microsoft YaHei", 1, 100));
    g.drawString(text, x, y);
    SwingFXUtils.toFXImage(buffer, image);
    g.dispose();
  }

  public static void main(String[] args){
    launch(args);
  }

  static class Point{
    double x; // x坐标
    double y; // y坐标
    double s; // 下降加速度
    double vy; // y轴方向速度

    public Point(double x, double y, double s){
      this.x = x;
      this.y = y;
      this.s = s;
      this.vy = 0;
    }

    public String toString(){
      return x + " , " + y + " , " + s + " , " + vy;
    }

  }

}
