package com.arpg.editor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import com.arpg.utils.IOUtils;
import com.arpg.utils.ResourceManager;

public class MapConfig implements EventHandler<MouseEvent>{
  private int cellWidth;
  private Image image;
  private int[][] config;
  private Canvas canvas;
  private GraphicsContext gc;

  public MapConfig(){
  }

  public void setCellWidth(int cellWidth){
    this.cellWidth = cellWidth;
  }

  public Node loadImg(Image image){
    this.image = image;
    int col = (int) image.getWidth() / cellWidth;
    int row = (int) image.getHeight() / cellWidth;
    config = new int[row][col];

    if(canvas != null) canvas.setOnMouseClicked(null);

    this.canvas = new Canvas(image.getWidth(), image.getHeight());
    canvas.setOnMouseClicked(this);
    gc = canvas.getGraphicsContext2D();
    gc.drawImage(image, 0, 0);

    return canvas;
  }

  public boolean isBgLoad(){
    return image != null;
  }

  public void loadConfig(File file){
    config = ResourceManager.loadCvs(file.getAbsolutePath());
    repaint();
  }

  @Override
  public void handle(MouseEvent event){
    int y = (int) event.getY() / cellWidth;
    int x = (int) event.getX() / cellWidth;

    if(config[y][x] > 0){
      config[y][x] = 0;
      repaint();
    }else{
      config[y][x] = 1;
      gc.setStroke(Color.RED);
      gc.strokeRect(x * cellWidth, y * cellWidth, cellWidth, cellWidth);
    }
  }

  private void repaint(){
    gc.clearRect(0, 0, (int) image.getWidth(), (int) image.getHeight());
    gc.drawImage(image, 0, 0);
    for(int i = 0; i < config.length; i++){
      for(int j = 0; j < config[i].length; j++){
        if(config[i][j] < 1) continue;

        gc.setStroke(Color.RED);
        gc.strokeRect(j * cellWidth, i * cellWidth, cellWidth, cellWidth);
      }
    }
  }

  public void save(File file){
    PrintWriter writer = null;
    try{
      writer = new PrintWriter(file);
      for(int i = 0; i < config.length; i++){
        for(int j = 0; j < config[i].length - 1; j++){
          writer.print(config[i][j] + ",");
        }
        writer.println(config[i][config[i].length - 1]);
      }
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }finally{
      IOUtils.closeQuietly(writer);
    }
  }

}
