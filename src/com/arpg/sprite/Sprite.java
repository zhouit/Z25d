package com.arpg.sprite;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.arpg.ImageSpriteFactory;
import com.arpg.MapContainer;
import com.arpg.fight.Magic;
import com.arpg.sprite.SpriteState.Injury;
import com.arpg.utils.ImageUtils;
import com.arpg.utils.JavafxImageWriter;
import com.arpg.utils.MathUtils;
import com.arpg.utils.RpgConstants;

/**
 * 精灵类
 * 
 * @author zhou
 *
 */
public class Sprite{
  private static final int SPEED = 4;
  public static final double PROB_MOVE = 0.02;
  // 角色名称,战队
  private String name, group;
  // 角色血量(当前值,最大值)
  private int hp = 900, maxHp = 1000;
  // 角色魔法值(当前值,最大值)
  private int mp = 450, maxMp = 500;
  // 精灵状态,精灵默认状态为RUN
  private SpriteState state;
  private SpriteType type = SpriteType.PLAYER;
  // 每个精灵的脚本文件(随着剧情发展可而改变)
  private String script;

  /* 精灵在Tile上的值 */
  private int x, y;
  /* 精灵的真实位置 */
  private int px, py;
  private int direction;
  /* 计步器 */
  private int stepCounter = 0;
  /**
   * 是否正在移动
   */
  private volatile boolean moving;
  /**
   * 修正精灵移动距离(当前Cell的移动距离),由于地图单元Cell长宽比较高,如果每次移动一个Tile则精灵移动轨迹会比较生硬。
   * 所以在精灵move过程中按speed移动, 当精灵移动超过一个
   * Cell宽高时,随即设置停止移动状态。而用户必须是当前精灵不在移动状态(moving)才能开始下一次移动(setMoving),
   * 这样才能保证精灵每次移动的为一整格Cell。 同时在设置精灵移动时(见setMoving方法)将moveLength置0。
   */
  private int movingLength;
  /* 移动类型,npc为自由移动 */
  private boolean autoMove;

  private ImageSpriteFactory sfactory;
  private MapContainer map;
  /* 精灵的魔法 */
  private List<Magic> magics;

  public Sprite(String spriteFile, int direction){
    this.sfactory = new ImageSpriteFactory(spriteFile);
    this.state = SpriteState.RUN;
    this.autoMove = true;
    this.direction = direction;
    this.magics = new ArrayList<Magic>();
  }

  /**
   * 在offsetX/offsetY偏移处画出当前精灵
   * 
   * @param context
   * @param offsetX
   * @param offsetY
   */
  public void draw(GraphicsContext context, int offsetX, int offsetY){
    stepCounter++;
    stepCounter = stepCounter % 100;

    int nx = px + offsetX - getSpriteOffsetX();
    int ny = py + offsetY - getSpriteOffsetY();

    /* 画人物的影子 */
    context.setFill(Color.rgb(0, 0, 0, 0.5));
    context.fillOval(nx + 15, ny + sfactory.getSpriteHeight() - sfactory.getSpriteHeight() / 5,
        RpgConstants.CS * 5 / 4, RpgConstants.CS * 2 / 3);

    context.drawImage(getSpriteImage(), nx, ny);
    ImageUtils.drawStyleString(context, name, nx + getSpriteOffsetX() - 25, ny + getSpriteOffsetY()
        + 36);
    ImageUtils.drawStyleString(context, group, nx + getSpriteOffsetX() - 25, ny
        + getSpriteOffsetY() + 50);
  }

  /**
   * 当前是否可以与npc对话
   * 
   * @param x
   * @param y
   * @return
   */
  public Sprite talkWith(int x, int y){
    Sprite talkTo = null;
    for(Sprite sprite : map.getRoles()){
      if(!sprite.isHero() && x >= sprite.getX() - 1 && x <= sprite.getX() && y >= sprite.getY() - 3
          && y < sprite.getY()){
        talkTo = sprite;
        break;
      }
    }

    return talkTo;
  }

  /**
   * 精灵移动
   */
  public void move(){
    switch(direction){
    case RpgConstants.LEFT:
      moveLeft();
      break;
    case RpgConstants.RIGHT:
      moveRight();
      break;
    case RpgConstants.UP:
      moveUp();
      break;
    case RpgConstants.DOWN:
      moveDown();
      break;
    case RpgConstants.LEFT_UP:
      moveLeftUp();
      break;
    case RpgConstants.LEFT_DOWN:
      moveLeftDown();
      break;
    case RpgConstants.RIGHT_UP:
      moveRightUp();
      break;
    case RpgConstants.RIGHT_DOWN:
      moveRightDown();
      break;
    }
  }

  private void moveLeft(){
    int nextX = Math.max(x - 1, 0);
    int nextY = y;
    if(!map.isHit(nextX, nextY)){
      px -= Sprite.SPEED;
      movingLength += Sprite.SPEED;
      if(movingLength >= RpgConstants.CS){
        x--;
        px = x * RpgConstants.CS;
        /* 当精灵移动超过一格(RpgConstants.CS)时,随即设置停止移动 ,而在setMoving中将moveLength置0 */
        moving = false;
      }
    }else{
      moving = false;
      px = x * RpgConstants.CS;
      py = y * RpgConstants.CS;
    }
  }

  private void moveRight(){
    int nextX = Math.min(map.getColumns() - 1, x + 1);
    int nextY = y;
    if(!map.isHit(nextX, nextY)){
      px += Sprite.SPEED;
      movingLength += Sprite.SPEED;
      if(movingLength >= RpgConstants.CS){
        x++;
        px = x * RpgConstants.CS;
        moving = false;
      }
    }else{
      moving = false;
      px = x * RpgConstants.CS;
      py = y * RpgConstants.CS;
    }
  }

  private void moveUp(){
    int nextX = x;
    int nextY = Math.max(y - 1, 0);
    if(!map.isHit(nextX, nextY)){
      py -= Sprite.SPEED;
      movingLength += Sprite.SPEED;
      if(movingLength >= RpgConstants.CS){
        y--;
        py = y * RpgConstants.CS;
        moving = false;
      }
    }else{
      moving = false;
      px = x * RpgConstants.CS;
      py = y * RpgConstants.CS;
    }
  }

  private void moveDown(){
    int nextX = x;
    int nextY = y + 1;
    if(!map.isHit(nextX, nextY)){
      py += Sprite.SPEED;
      movingLength += Sprite.SPEED;
      if(movingLength >= RpgConstants.CS){
        y++;
        py = y * RpgConstants.CS;
        moving = false;
      }
    }else{
      moving = false;
      px = x * RpgConstants.CS;
      py = y * RpgConstants.CS;
    }
  }

  private void moveLeftUp(){
    int nextX = Math.max(x - 1, 0);
    int nextY = Math.max(y - 1, 0);
    if(!map.isHit(nextX, nextY)){
      px -= Sprite.SPEED;
      py -= Sprite.SPEED;
      movingLength += Sprite.SPEED;
      if(movingLength >= RpgConstants.CS){
        x--;
        px = x * RpgConstants.CS;
        y--;
        py = y * RpgConstants.CS;
        moving = false;
      }
    }else{
      moving = false;
      px = x * RpgConstants.CS;
      py = y * RpgConstants.CS;
    }
  }

  private void moveRightDown(){
    int nextX = Math.min(x + 1, map.getRows() - 1);
    int nextY = Math.min(y + 1, map.getColumns() - 1);
    if(!map.isHit(nextX, nextY)){
      px += Sprite.SPEED;
      py += Sprite.SPEED;
      movingLength += Sprite.SPEED;
      if(movingLength >= RpgConstants.CS){
        x++;
        px = x * RpgConstants.CS;
        y++;
        py = y * RpgConstants.CS;
        moving = false;
      }
    }else{
      moving = false;
      px = x * RpgConstants.CS;
      py = y * RpgConstants.CS;
    }
  }

  private void moveLeftDown(){
    int nextX = Math.max(x - 1, 0);
    int nextY = Math.min(y + 1, map.getColumns() - 1);
    if(!map.isHit(nextX, nextY)){
      px -= Sprite.SPEED;
      py += Sprite.SPEED;
      movingLength += Sprite.SPEED;
      if(movingLength >= RpgConstants.CS){
        x--;
        px = x * RpgConstants.CS;
        y++;
        py = y * RpgConstants.CS;
        moving = false;
      }
    }else{
      moving = false;
      px = x * RpgConstants.CS;
      py = y * RpgConstants.CS;
    }
  }

  private void moveRightUp(){
    int nextX = Math.min(x + 1, map.getRows() - 1);
    int nextY = Math.max(y - 1, 0);
    if(!map.isHit(nextX, nextY)){
      px += Sprite.SPEED;
      py -= Sprite.SPEED;
      movingLength += Sprite.SPEED;
      if(movingLength >= RpgConstants.CS){
        x++;
        px = x * RpgConstants.CS;
        y--;
        py = y * RpgConstants.CS;
        moving = false;
      }
    }else{
      moving = false;
      px = x * RpgConstants.CS;
      py = y * RpgConstants.CS;
    }
  }

  /**
   * 设置精灵所在地图
   * 
   * @param map
   */
  public void setMapContainer(MapContainer map){
    this.map = map;
  }

  public void setName(String name){
    this.name = name;
  }

  public String getName(){
    return name;
  }

  public void setGroup(String group){
    this.group = group;
  }

  public String getGroup(){
    return group;
  }

  public void addMagic(Magic magic){
    this.magics.add(magic);
  }

  public List<Magic> getMagics(){
    return magics;
  }

  public String getScript(){
    return script;
  }

  /**
   * 设置当前将要执行的脚本文件
   * 
   * @param script
   */
  public void setScript(String script){
    this.script = script;
  }

  public int getHp(){
    return hp;
  }

  /**
   * 设置血量,当血量小于0时,角色转换为死亡状态
   * 
   * @param hp
   */
  public void setHp(int hp){
    if(hp <= 0){
      state = SpriteState.DEAD;
    }

    this.hp = Math.max(0, hp);
    this.hp = Math.min(maxHp, this.hp);
  }

  public int getMaxHp(){
    return maxHp;
  }

  public void setMaxHp(int maxHp){
    this.maxHp = maxHp;
  }

  public int getMp(){
    return mp;
  }

  public void setMp(int mp){
    this.mp = Math.min(mp, maxMp);
  }

  public int getMaxMp(){
    return maxMp;
  }

  public void setMaxMp(int maxMp){
    this.maxMp = maxMp;
  }

  /**
   * 根据状态获取精灵图片
   * 
   * @return
   */
  public Image getSpriteImage(){
    Image result = sfactory.getMove(direction)[stepCounter / 25];
    if(state == SpriteState.STAND){
      result = sfactory.getMove(direction)[0];
    }else if(isInjury()){
      Magic magic = ((Injury) state).getMagic();
      if(magic != null && !magic.isOver()){
        JavafxImageWriter iwriter = new JavafxImageWriter(result, true);
        iwriter.hue(magic.getHue());
        result = iwriter.getImage();
      }
    }

    return result;
  }

  /**
   * 设置精灵状态
   * 
   * @param state
   */
  public void setSpriteState(SpriteState state){
    this.state = state;
  }

  public SpriteState getSpriteState(){
    return state;
  }

  /**
   * 是否为受攻击状态
   * 
   * @return
   */
  public boolean isInjury(){
    return state instanceof Injury;
  }

  public SpriteType getSpriteType(){
    return type;
  }

  public void setSpriteType(SpriteType type){
    this.type = type;
  }

  /**
   * 设置精灵的位置
   * 
   * @param point
   */
  public void setPosition(Point point){
    this.x = point.x;
    this.y = point.y;
    this.px = this.x * RpgConstants.CS;
    this.py = this.y * RpgConstants.CS;
  }

  public void setDirection(int direction){
    this.direction = direction;
  }

  public int getDirection(){
    return direction;
  }

  public boolean isMoving(){
    return moving;
  }

  /**
   * 设置是否移动
   * 
   * @param flag
   */
  public void setMoving(boolean flag){
    moving = flag;
    movingLength = 0;
  }

  public void setAutoMove(boolean autoMove){
    this.autoMove = autoMove;
  }

  /**
   * 是否在当前英雄的攻击范围
   * 
   * @param radius
   * @param target
   * @return
   */
  public boolean isAttackArea(int radius, Sprite target){
    return MathUtils.isInCicrle(px, py, radius, target.getPx(), target.getPy());
  }

  public Point getPoint(){
    return new Point(x, y);
  }

  /**
   * 当前精灵是否为英雄
   * 
   * @return
   */
  public boolean isHero(){
    return !autoMove;
  }

  public int getX(){
    return x;
  }

  public int getY(){
    return y;
  }

  public int getPx(){
    return px;
  }

  public int getPy(){
    return py;
  }

  /**
   * 由于角色图片Sprite为 70*124,而地图单元格为32*32,所以Sprite要占3行2列的单元格
   * 
   * @return Sprite在地图上的x偏移值
   */
  public int getSpriteOffsetX(){
    return sfactory.getSpriteWidth() - RpgConstants.CS;
  }

  /**
   * 由于角色图片Sprite为 70*124,而地图单元格为32*32,所以Sprite要占3行2列的单元格
   * 
   * @return Sprite在地图上的y偏移值
   */
  public int getSpriteOffsetY(){
    return sfactory.getSpriteHeight() - RpgConstants.CS;
  }

  /**
   * 精灵图片宽度
   * 
   * @return
   */
  public int getSpriteWidth(){
    return sfactory.getSpriteWidth();
  }

  /**
   * 精灵图片高窟
   * 
   * @return
   */
  public int getSpriteHeight(){
    return sfactory.getSpriteHeight();
  }

}
