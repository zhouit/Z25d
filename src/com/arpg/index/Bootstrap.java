package com.arpg.index;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import com.arpg.ContextContainer;
import com.arpg.utils.RpgConstants;

public class Bootstrap extends Application{
  private ContextContainer container;

  public void start(Stage stage) throws Exception{
    stage.setResizable(false);
    stage.setTitle("ARPG-东方上人");

    Canvas canvas = new Canvas(RpgConstants.CANVAS_WIDTH, RpgConstants.CANVAS_HEIGHT);
    container = new ContextContainer(new IndexController(), canvas);
    Scene scene = new Scene(container.getSurface(), RpgConstants.CANVAS_WIDTH - 10,
        RpgConstants.CANVAS_HEIGHT - 10);
    container.startUp();
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args){
    launch(args);
  }

  public void stop(){
    container.shoutdown();
  }

}
