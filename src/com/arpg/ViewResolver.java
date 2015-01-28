package com.arpg;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;

import com.arpg.utils.ImageUtils;
import com.sun.javafx.perf.PerformanceTracker;

//SurfaceViewResovler
public class ViewResolver implements EventHandler<Event>{
  private Controller controller;
  private boolean showFps = false;

  public ViewResolver(Controller boot){
    this.controller = boot;
  }

  public void showFps(boolean showFps){
    this.showFps = showFps;
  }

  public void renderView(GraphicsContext context){
    ImageUtils.clear(context);
    controller.draw(context);
    controller = controller.invoke();
    if(showFps){
      float fps = getFps(context.getCanvas());
      System.out.println("fps-->" + fps);
    }
  }

  public float getFps(Node node){
    return PerformanceTracker.getSceneTracker(node.getScene()).getInstantFPS();
  }

  /**
   * 获取ui界面的控制组件
   * 
   * @return
   */
  public Node getControlBar(){
    return controller.getControlBar();
  }

  @Override
  public void handle(Event event){
    controller.handle(event);
  }

}
