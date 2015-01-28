package com.arpg.fight;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.arpg.AbstarctController;
import com.arpg.Controller;
import com.arpg.ImageSpriteFactory;
import com.arpg.sprite.Sprite;
import com.arpg.sprite.SpriteState;
import com.arpg.sprite.SpriteState.Injury;
import com.arpg.utils.ResourceManager;
import com.arpg.utils.RpgConstants;

/**
 * 战斗控制器
 * 
 * @author zhou
 *
 */
public final class FightController extends AbstarctController{
  private static Image fightBg = ResourceManager.loadImage("resource/image/fight.png");
  private static Image magicBg = ResourceManager.loadImage("resource/image/BottomMenu.png");

  private List<MagicControl> mcs;

  private static final int ENEMY_X = 250;
  private static final int ENEMY_Y = 80;
  private static final int SPRITE_X = 350;
  private static final int SPRITE_Y = 280;
  // 当战斗结束时执行的控制器
  private Controller exit;

  private Sprite sprite;
  private Sprite enmey;
  /* 当前精灵施放的魔法 */
  private Magic spriteMagic;

  public FightController(Controller exit, Sprite hero, Sprite enmey){
    this.sprite = hero;
    this.sprite.setSpriteState(SpriteState.STAND);
    this.sprite.setDirection(RpgConstants.LEFT_UP);
    this.enmey = enmey;
    this.enmey.setSpriteState(SpriteState.STAND);
    this.enmey.setDirection(RpgConstants.DOWN);
    this.exit = exit;

    mcs = new ArrayList<>();
    for(Magic temp : sprite.getMagics()){
      mcs.add(new MagicControl(temp));
    }
  }

  @Override
  public void draw(GraphicsContext context){
    context.drawImage(fightBg, 0, 0);
    // 绘制角色头像属性面板
    drawSpritePanel(context);
    drawEnmeyPanel(context);
    // 绘制精灵图片
    context.drawImage(sprite.getSpriteImage(), SPRITE_X, SPRITE_Y);
    // 绘制敌人
    drawEnemy(context);

    drawHeroMagics(context);
  }

  @Override
  public Controller invoke(){
    Controller result = this;
    if(enmey.getSpriteState() == SpriteState.DEAD){
      result = exit;
    }

    return result;
  }

  @Override
  public void handle(Event event){
    if(event.getEventType() == KeyEvent.KEY_RELEASED){
      KeyEvent ke = (KeyEvent) event;
      KeyCode code = ke.getCode();
      if(code == KeyCode.Q){
        mcs.get(0).invoke();
      }else if(code == KeyCode.W){
        mcs.get(1).invoke();
      }else if(code == KeyCode.E){
        mcs.get(2).invoke();
      }else if(code == KeyCode.R){
        mcs.get(3).invoke();
      }
    }
  }

  /**
   * 绘制敌人,释放在敌人身上的魔法,以及伤害数值
   * 
   * @param context
   */
  void drawEnemy(GraphicsContext context){
    if(spriteMagic != null && !spriteMagic.isOver()){
      context.drawImage(Magic.TITLE, 280, 20);
      context.setFill(spriteMagic.getHue());
      context.setFont(Font.font("KaiTi", FontWeight.BOLD, 25));
      context.fillText(spriteMagic.getName(), 305, 45);
    }

    DropShadow effect = new DropShadow();
    effect.setBlurType(BlurType.THREE_PASS_BOX);
    effect.setColor(Color.GOLD);
    effect.setRadius(10.0);
    effect.setSpread(0.5);
    context.setEffect(effect);
    context.drawImage(enmey.getSpriteImage(), ENEMY_X, ENEMY_Y);
    context.setEffect(null);

    if(spriteMagic == null || spriteMagic.isOver()){
      enmey.setSpriteState(SpriteState.STAND);
      return;
    }
    // 显示魔法动画
    context.drawImage(spriteMagic.current(),
        ENEMY_X + (enmey.getSpriteWidth() - spriteMagic.getImageWidth()) / 2,
        ENEMY_Y + (enmey.getSpriteHeight() - spriteMagic.getImageHeight()) / 2);

    // 显示伤害值
    if(spriteMagic.showInjure()){
      context.setFill(spriteMagic.getHue());
      context.setFont(Font.font("KaiTi", FontWeight.BOLD, 20));
      context.fillText("-" + spriteMagic.getInjure(), ENEMY_X + sprite.getSpriteWidth() / 2 - 20,
          ENEMY_Y + 10);
    }

    // 计算伤害
    if(spriteMagic.isInjure()){
      enmey.setHp(enmey.getHp() - spriteMagic.getInjure());
    }
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
    context.fillText(sprite.getName(), 130, 18);
    context.setFont(Font.font("KaiTi", FontWeight.BOLD, 14));
    context.fillText("99", 37, 86);

    Paint blood = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0,
        Color.rgb(201, 238, 206)), new Stop(0.5, Color.rgb(60, 224, 70)), new Stop(1.0, Color.rgb(
        106, 234, 130)));
    context.setFill(blood);
    context.fillRoundRect(90, 26, 168 * sprite.getHp() / sprite.getMaxHp(), 24, 20, 20);

    LinearGradient magic = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0,
        Color.rgb(192, 195, 238)), new Stop(0.5, Color.rgb(86, 104, 226)), new Stop(1.0, Color.rgb(
        100, 104, 233)));
    context.setFill(magic);
    context.fillRoundRect(92, 53, 115 * sprite.getMp() / sprite.getMaxMp(), 14, 12, 12);

    LinearGradient other = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0,
        Color.rgb(254, 209, 172)), new Stop(0.5, Color.rgb(255, 142, 40)), new Stop(1.0, Color.rgb(
        255, 177, 74)));
    context.setFill(other);
    context.fillRoundRect(95, 71.5, 40, 11, 10, 10);
  }

  /**
   * 绘制敌人属性面板
   * 
   * @param context
   */
  private void drawEnmeyPanel(GraphicsContext context){
    context.drawImage(ImageSpriteFactory.getEnmeyHeader(), RpgConstants.CANVAS_WIDTH - 180, 5);
    context.drawImage(ResourceManager.loadImage("resource/image/role/face/enmey.png"),
        RpgConstants.CANVAS_WIDTH - 63, 12);
    context.setFill(Color.RED);
    context.setFont(Font.font("KaiTi", FontWeight.BOLD, 18));
    context.fillText(enmey.getName(), RpgConstants.CANVAS_WIDTH - 170, 25);
    context.setFont(Font.font("KaiTi", FontWeight.BOLD, 14));
    context.fillText("90", RpgConstants.CANVAS_WIDTH - 74, 68);

    Paint blood = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0.0,
        Color.rgb(201, 238, 206)), new Stop(0.5, Color.rgb(60, 224, 70)), new Stop(1.0, Color.rgb(
        106, 234, 130)));
    context.setFill(blood);
    /* 整个血条长度为100 */
    context.fillRoundRect(
        RpgConstants.CANVAS_WIDTH - 175 + 100 - 100 * enmey.getHp() / enmey.getMaxHp(), 34.5, 100
            * enmey.getHp() / enmey.getMaxHp(), 14, 15, 15);
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
   * 魔法技能组件
   * 
   * @author zhou
   *
   */
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
      if(sprite.getMp() < magic.getCost() || sec <= magic.getCd()) return;

      watch.start();
      spriteMagic = magic;
      sprite.setMp(sprite.getMp() - spriteMagic.getCost());
      spriteMagic.prepare();
      enmey.setSpriteState(new Injury(spriteMagic));
    }

  }

}
