package com.arpg.fight;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.arpg.AbstarctController;
import com.arpg.ImageSpriteFactory;
import com.arpg.MapContainer;
import com.arpg.sprite.Sprite;
import com.arpg.utils.MathUtils;
import com.arpg.utils.ResourceManager;
import com.arpg.utils.RpgConstants;

public class ActFightController extends AbstarctController{
  private static Image magicBg = ResourceManager.loadImage("resource/image/BottomMenu.png");

  /* 当前英雄施放的魔法 */
  private List<Magic> magics = Collections.synchronizedList(new ArrayList<Magic>());
  // 当前英雄在追逐精灵(在自由寻路时此为null)
  private Sprite dest;

  private List<MagicControl> mcs;
  private MapContainer map;
  private Sprite hero;
  // 角色自动寻路的路径集合
  private List<Point> paths;

  public ActFightController(MapContainer mc){
    this.map = mc;
    this.hero = mc.getHero();
    mcs = new ArrayList<>();
    for(Magic temp : hero.getMagics()){
      mcs.add(new MagicControl(temp));
    }
  }

  @Override
  public void draw(GraphicsContext context){
    /* 如果不存在目标或未追到目标,角色正常移动 */
    if(!isChase()) spriteMove();

    map.draw(context, getMousePoint());

    drawSpritePanel(context);

    drawHeroMagics(context);
  }

  /**
   * 绘制当前英雄的属性面板
   * 
   * @param context
   */
  private void drawSpritePanel(GraphicsContext context){
    context.drawImage(ImageSpriteFactory.getHeroHeader(), 5, 5);
    context.drawImage(ResourceManager.loadImage("resource/image/role/face/hero.png"), 25, 20);
    context.setFill(Color.WHITESMOKE);
    context.setFont(Font.font("KaiTi", FontWeight.BOLD, 18));
    context.fillText(hero.getName(), 130, 18);
    context.setFont(Font.font("KaiTi", FontWeight.BOLD, 14));
    context.fillText("99", 37, 86);

    Paint blood = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0,
        Color.rgb(201, 238, 206)), new Stop(0.5, Color.rgb(60, 224, 70)), new Stop(1.0, Color.rgb(
        106, 234, 130)));
    context.setFill(blood);
    context.fillRoundRect(90, 26, 168 * hero.getHp() / hero.getMaxHp(), 24, 20, 20);

    LinearGradient magic = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0,
        Color.rgb(192, 195, 238)), new Stop(0.5, Color.rgb(86, 104, 226)), new Stop(1.0, Color.rgb(
        100, 104, 233)));
    context.setFill(magic);
    context.fillRoundRect(92, 53, 115 * hero.getMp() / hero.getMaxMp(), 14, 12, 12);

    LinearGradient other = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0,
        Color.rgb(254, 209, 172)), new Stop(0.5, Color.rgb(255, 142, 40)), new Stop(1.0, Color.rgb(
        255, 177, 74)));
    context.setFill(other);
    context.fillRoundRect(95, 71.5, 40, 11, 10, 10);
  }

  /**
   * 绘制当前英雄的魔法技能面板
   * 
   * @param context
   */
  private void drawHeroMagics(GraphicsContext context){
    context.drawImage(magicBg, 0, RpgConstants.CANVAS_HEIGHT - 60);
    double x = 64;
    for(MagicControl mc : mcs){
      mc.drawView(context, x);
      x += 6.5 + mc.getImageWidth();
    }
  }

  /**
   * 角色群移动
   */
  private void spriteMove(){
    /* 处理角色自动寻路 */
    if(paths != null && paths.size() > 1 && !hero.isMoving()){
      int direction = RpgConstants.getDirection(paths.get(0), paths.get(1));
      hero.setDirection(direction);
      hero.setMoving(true);
      paths.remove(0);
    }

    for(Sprite role : map.getRoles()){
      if(role.isMoving()){
        role.move();
      }else if(!role.isHero() && MathUtils.getRandomGen().nextDouble() < Sprite.PROB_MOVE
          && !role.isInjury()){
        role.setDirection(MathUtils.getRandomGen().nextInt(8));
        role.setMoving(MathUtils.getRandomGen().nextInt(4) == 1);
      }
    }
  }

  /**
   * 是否追逐到目标
   * 
   * @return
   */
  private boolean isChase(){
    boolean value = dest != null && hero.isAttackArea(RpgConstants.CS * 3 / 2, dest);
    if(value) paths = null;

    return value;
  }

  final class MagicControl{
    private Magic magic;
    private Image iview;
    private Stopwatch watch;

    public MagicControl(Magic magic){
      this.magic = magic;
      this.iview = ResourceManager.loadImage(magic.getMagicSkill());
      this.watch = new Stopwatch();
    }

    public void drawView(GraphicsContext context, double x){
      int height = getImageHeight();
      int y = RpgConstants.CANVAS_HEIGHT - 60 + height + 3;
      context.drawImage(iview, x, y);
      /* 绘制魔法cd时间遮罩 */
      int sec = watch.count();
      if(sec <= magic.getCd()){
        int cdHeight = sec * height / magic.getCd();
        context.setFill(Color.BLACK);
        context.setGlobalAlpha(0.75);
        context.fillRect(x, cdHeight + y, getImageWidth(), height - cdHeight);
        context.setGlobalAlpha(1.0);
      }
    }

    public int getImageWidth(){
      return (int) iview.getWidth();
    }

    public int getImageHeight(){
      return (int) iview.getHeight();
    }

    public void invoke(){
      int sec = watch.count();
      if(hero.getMp() < magic.getCost() || sec <= magic.getCd()) return;

      watch.start();
      magics.add(magic);
      hero.setMp(hero.getMp() - magic.getCost());
      magic.prepare();
    }

  }

}
