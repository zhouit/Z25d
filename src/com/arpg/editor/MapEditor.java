package com.arpg.editor;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import com.arpg.utils.ResourceManager;
import com.arpg.utils.RpgConstants;

/**
 * 地图编辑器
 * 
 * @author zhou
 *
 */
public class MapEditor extends Application implements EventHandler<ActionEvent>{
  private Stage stage;
  private ScrollPane content;
  private MapConfig config;

  @Override
  public void start(Stage stage) throws Exception{
    config = new MapConfig();
    config.setCellWidth(RpgConstants.CS);
    BorderPane root = new BorderPane();
    root.setTop(loadMenus());
    content = new ScrollPane();
    content.setPrefSize(540, 540);
    root.setCenter(content);

    this.stage = stage;
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("RPG-MapEditor");
    // stage.setResizable(false);
    stage.show();
  }

  Node loadMenus(){
    MenuItem open = new MenuItem("打开场景");
    open.setOnAction(this);
    MenuItem openConfig = new MenuItem("打开地图文件");
    openConfig.setOnAction(this);
    MenuItem save = new MenuItem("保存地图");
    save.setOnAction(this);
    MenuItem exit = new MenuItem("退出");
    Menu menu = new Menu(" 文件 ");
    menu.getItems().addAll(open, openConfig, save, exit);

    Menu help = new Menu(" 帮助 ");
    MenuItem about = new MenuItem(" 关于\t");
    help.getItems().add(about);

    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(menu, help);

    return menuBar;
  }

  public static void main(String[] args){
    launch(args);
  }

  @Override
  public void handle(ActionEvent event){
    MenuItem item = (MenuItem) event.getSource();
    String label = item.getText();
    if("打开场景".equals(label)){
      FileChooser chooser = new FileChooser();
      chooser.getExtensionFilters().add(new ExtensionFilter("图片文件", "*.png", "*.jpg"));
      File f = chooser.showOpenDialog(stage);
      if(f != null){
        content.setContent(config.loadImg(ResourceManager.loadImage(f.getAbsolutePath())));
      }
    }else if("打开地图文件".equals(label)){
      FileChooser chooser = new FileChooser();
      chooser.getExtensionFilters().add(new ExtensionFilter("地图文件", "*.map"));
      File f = chooser.showOpenDialog(stage);
      if(f != null && config.isBgLoad()){
        config.loadConfig(f);
      }
    }else if("保存地图".equals(label)){
      FileChooser chooser = new FileChooser();
      chooser.getExtensionFilters().add(new ExtensionFilter("地图文件", "*.map"));
      File f = chooser.showSaveDialog(stage);
      if(f != null) config.save(f);
    }
  }
}
