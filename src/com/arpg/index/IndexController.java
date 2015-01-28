package com.arpg.index;

import java.awt.Point;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import com.arpg.AbstarctController;
import com.arpg.Controller;
import com.arpg.MapContainer;
import com.arpg.RpgController;
import com.arpg.fight.Magic;
import com.arpg.sprite.Sprite;
import com.arpg.sprite.SpriteType;
import com.arpg.utils.ImageUtils;
import com.arpg.utils.JavafxImageWriter;
import com.arpg.utils.ResourceManager;
import com.arpg.utils.RpgConstants;

/**
 * 游戏启动界面
 * 
 * @author zhou
 *
 */
public final class IndexController extends AbstarctController{
  private static Image bg = ResourceManager.loadImage("resource/image/index/index.png");
  private Controller next;
  private StackPane menu;

  public IndexController(){
    menu = new StackPane();
    menu.getChildren().add(new ImageView(initIndexImage(300, 240)));
    menu.setLayoutX(RpgConstants.CANVAS_WIDTH / 2 - 200);
    menu.setLayoutY(RpgConstants.CANVAS_HEIGHT / 2 - 120);
  }

  @Override
  public void draw(GraphicsContext context){
    context.drawImage(bg, 0, 0);
  }

  @Override
  public void handle(Event event){
    if(event.getEventType() == MouseEvent.MOUSE_CLICKED){
      enter();
    }
  }

  private void enter(){
    MapContainer map = new MapContainer("resource/image/map/map.png", "resource/image/map/map.map");
    map.showGrid(false);
    // MapContainer map = new MapContainer("resource/image/map/maze.jpg",
    // "resource/image/map/maze.map");

    Sprite hero = new Sprite("resource/image/role/gm.png", RpgConstants.DOWN);
    hero.setPosition(new Point(7, 8));
    hero.setName("东方上人");
    hero.addMagic(new Magic("烈火", "resource/image/magic/fire.png", 19, Color.RED));
    hero.addMagic(new Magic("狂风", "resource/image/magic/wind.png", 15, Color.GRAY));
    hero.addMagic(new Magic("剧毒", "resource/image/magic/poison.png", 12, Color.GREEN));
    hero.addMagic(new Magic("冰封", "resource/image/magic/ice.png", 13, Color.SKYBLUE));
    hero.addMagic(new Magic("混乱", "resource/image/magic/confusion.png", 15, Color.YELLOW));
    hero.addMagic(new Magic("雷电", "resource/image/magic/thunder.png", 20, Color.SNOW));

    Sprite npc = new Sprite("resource/image/role/assassin.png", RpgConstants.LEFT);
    npc.setScript("script.sc");
    npc.setPosition(new Point(14, 10));
    npc.setSpriteType(SpriteType.NPC);
    npc.setName("西门吐血");

    map.initHero(hero);
    map.addCharacter(npc);

    MapContainer hill = new MapContainer("resource/image/map/hill.jpg",
        "resource/image/map/hill.map");
    Sprite monster = new Sprite("resource/image/role/rogue.png", RpgConstants.LEFT);
    monster.setPosition(new Point(15, 4));
    monster.setName("小熊s");
    monster.setSpriteType(SpriteType.MONSTER);
    hill.addCharacter(monster);

    map.addTransfer(22, 1, hill, 9, 12);
    next = new RpgController(map);
  }

  @Override
  public Controller invoke(){
    return next != null ? next : this;
  }

  @Override
  public Node getControlBar(){
    return menu;
  }

  /**
   * 创建程序启动选择菜单背景(大于240*120)
   * 
   * @param width
   * @param height
   * @return
   */
  static Image initIndexImage(int width, int height){
    JavafxImageWriter iw = new JavafxImageWriter(width, height);
    // 上
    Image topSide = ResourceManager.loadImage("resource/image/index/top_side.png");
    iw.fillImage(topSide);
    iw.fillImage(ImageUtils.reverseH(topSide), width - (int) topSide.getWidth(), 0);
    int fixTopWidth = width - 2 * (int) topSide.getWidth();
    if(fixTopWidth > 0){
      Image topCenter = ResourceManager.loadImage("resource/image/index/top_center.png");
      Image fixCenter = ImageUtils.clip(topCenter, fixTopWidth, (int) topCenter.getHeight(), 0, 0,
          (int) topCenter.getWidth(), (int) topCenter.getHeight());
      iw.fillImage(fixCenter, (int) topSide.getWidth(), 0);
    }

    // 下
    Image bottomSide = ResourceManager.loadImage("resource/image/index/bottom_side.png");
    iw.fillImage(bottomSide, 0, height - (int) bottomSide.getHeight());
    iw.fillImage(ImageUtils.reverseH(bottomSide), width - (int) bottomSide.getWidth(), height
        - (int) bottomSide.getHeight());
    int fixBottomWidth = width - 2 * (int) bottomSide.getWidth();
    if(fixBottomWidth > 0){
      Image bottomCenter = ResourceManager.loadImage("resource/image/index/bottom_center.png");
      Image fixCenter = ImageUtils.clip(bottomCenter, fixBottomWidth, (int) bottomSide.getHeight(),
          0, 0, (int) bottomCenter.getWidth(), (int) bottomCenter.getHeight());
      iw.fillImage(fixCenter, (int) bottomSide.getWidth(), height - (int) bottomSide.getHeight());
    }

    // 左右边框
    int fixHeight = height - (int) topSide.getHeight() - (int) bottomSide.getHeight();
    if(fixHeight > 0){
      Image lrCenter = ResourceManager.loadImage("resource/image/index/left_right_center.png");
      Image fixLrCenter = ImageUtils.clip(lrCenter, (int) lrCenter.getWidth(), fixHeight, 0, 0,
          (int) lrCenter.getWidth(), (int) lrCenter.getHeight());
      iw.fillImage(fixLrCenter, 0, (int) topSide.getHeight());

      iw.fillImage(ImageUtils.reverseH(fixLrCenter), width - (int) lrCenter.getWidth(),
          (int) topSide.getHeight());

      // 中心区域
      int fixCenterWidth = width - 2 * (int) lrCenter.getWidth();
      if(fixCenterWidth > 0){
        Image center = ResourceManager.loadImage("resource/image/index/center.png");
        Image fixCenter = ImageUtils.clip(center, fixCenterWidth, fixHeight, 0, 0,
            (int) center.getWidth(), (int) center.getHeight());
        iw.fillImage(fixCenter, (int) lrCenter.getWidth(), (int) topSide.getHeight());
      }
    }

    return iw.getImage();
  }

}
