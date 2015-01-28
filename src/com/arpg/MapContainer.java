package com.arpg;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.arpg.astar.Astar;
import com.arpg.sprite.Sprite;
import com.arpg.sprite.SpriteContainer;
import com.arpg.utils.FontUtils;
import com.arpg.utils.ImageUtils;
import com.arpg.utils.MathUtils;
import com.arpg.utils.RpgConstants;

/**
 * 地图容器
 * 
 * @author zhou
 *
 */
public class MapContainer{
  private ImageMapFactory factory;
  /* 地图名称 */
  private String name = "东海龙宫";
  /* 精灵集合 */
  private SpriteContainer sprites;
  /* 该地图上所有传送点 */
  private List<Tepleporter> transfers;

  private int firstTileX;
  private int firstTileY;
  private int lastTileX;
  private int lastTileY;

  private boolean showGrid = false;

  public MapContainer(String bgfile, String mapFile){
    int point = bgfile.lastIndexOf(".");
    this.factory = new ImageMapFactory(bgfile, bgfile.substring(0, point) + "_front.png", mapFile);
    this.sprites = new SpriteContainer();
    this.transfers = new LinkedList<Tepleporter>();
  }

  public void draw(GraphicsContext context, Point mousePoint){
    int offsetX = getOffsetX(), offsetY = getOffsetY();

    firstTileX = RpgConstants.pixelsToTiles(-offsetX);
    lastTileX = firstTileX + RpgConstants.pixelsToTiles(RpgConstants.CANVAS_WIDTH) + 1;
    lastTileX = Math.min(lastTileX, getRows());

    firstTileY = RpgConstants.pixelsToTiles(-offsetY);
    lastTileY = firstTileY + RpgConstants.pixelsToTiles(RpgConstants.CANVAS_HEIGHT) + 1;
    lastTileY = Math.min(lastTileY, getColumns());
    for(int i = firstTileX; i < lastTileX; i++){
      for(int j = firstTileY; j < lastTileY; j++){
        context.drawImage(factory.getImages()[j][i], RpgConstants.tilesToPixels(i) + offsetX,
            RpgConstants.tilesToPixels(j) + offsetY);
        if(!showGrid) continue;

        context.setStroke(factory.getMap()[j][i] == 1 ? Color.WHITE : Color.BLUE);
        context.strokeRect(RpgConstants.tilesToPixels(i) + offsetX, RpgConstants.tilesToPixels(j)
            + offsetY, RpgConstants.CS - 2, RpgConstants.CS - 2);
        context.restore();
      }
    }

    drawTransfers(context, mousePoint);
    // 绘制角色群
    sprites.draw(context, offsetX, offsetY);
    drawMask(context);

    drawMiniature(context, mousePoint);
  }

  /**
   * 主要处理角色和建筑物、植物等的遮挡关系
   * 
   * @param context
   */
  private void drawMask(GraphicsContext context){
    if(factory.getMasks() == null) return;

    int offsetX = getOffsetX(), offsetY = getOffsetY();
    for(int i = firstTileX; i < lastTileX; i++){
      for(int j = firstTileY; j < lastTileY; j++){
        context.drawImage(factory.getMasks()[j][i], RpgConstants.tilesToPixels(i) + offsetX,
            RpgConstants.tilesToPixels(j) + offsetY);
      }
    }
  }

  /**
   * 绘制地图传送点
   * 
   * @param context
   * @param mousePoint
   *          当前鼠标位置
   */
  private void drawTransfers(GraphicsContext context, Point mousePoint){
    for(Tepleporter tepleporter : transfers){
      Point current = tepleporter.getCurrent();
      double x = getOffsetX() + current.getX() * RpgConstants.CS + RpgConstants.CS / 2 - 56;
      double y = getOffsetY() + current.getY() * RpgConstants.CS + RpgConstants.CS / 2 - 40;
      context.drawImage(Tepleporter.getTepleImage(), x, y);
      /* 鼠标悬浮在传送点上,显示传送点信息 */
      if(mousePoint != null && MathUtils.isInRectangle((int) x, (int) y, 112, 80, mousePoint)){
        context.setFill(Color.WHITE);
        String name = tepleporter.getSendTo().getName();
        context.fillText(name, x + FontUtils.getFontBounds(name).getWidth() / 2, y + 80 + 10);
      }
    }
  }

  /**
   * 绘制小地图
   * 
   * @param context
   * @param mousePoint
   *          当前鼠标悬浮的位置
   */
  private void drawMiniature(GraphicsContext context, Point mousePoint){
    context.setGlobalAlpha(0.6);
    int minX = factory.getDeflateImageX(isMapLeft(getHero().getPx()), mousePoint);
    context.drawImage(factory.getDeflateImage(), minX, factory.getDeflateImageY());
    for(Sprite sprite : sprites.getCharacters()){
      double sx = factory.getDeflateX() * sprite.getX() * RpgConstants.CS;
      double sy = factory.getDeflateY() * sprite.getY() * RpgConstants.CS;
      ImageUtils.drawHexagram(context, sprite.isHero() ? Color.RED : Color.BLUE, minX + sx,
          factory.getDeflateImageY() + sy, 5);
    }
    context.setGlobalAlpha(1.0d);
    context.setStroke(Color.BLACK);
    context.setLineWidth(0.5d);
    context.strokeRect(minX, factory.getDeflateImageY(), 150, 120);
  }

  /**
   * 显示地图网格
   * 
   * @param show
   */
  public void showGrid(boolean show){
    this.showGrid = show;
  }

  /**
   * 当前位置是否阻塞
   * 
   * @param x
   * @param y
   * @return
   */
  public boolean isHit(int x, int y){
    return factory.getMap()[y][x] == 1 || sprites.isHit(x, y);
  }

  /**
   * 在该地图x/y处添加到指定地图mx/my的传送点
   * 
   * @param x
   * @param y
   * @param map
   *          传送后的地图
   * @param mx
   * @param my
   */
  public void addTransfer(int x, int y, MapContainer map, int mx, int my){
    this.transfers.add(new Tepleporter(x, y, map, mx, my));
  }

  public List<Tepleporter> getTransfers(){
    return transfers;
  }

  public List<Point> findPath(Point from, Point to){
    return Astar.doAnlyse(factory.getMap(), from, to);
  }

  /**
   * 获取地图名称
   * 
   * @return
   */
  public String getName(){
    return name;
  }

  /**
   * 获取整个地图宽
   * 
   * @return
   */
  public int getWidth(){
    return (int) factory.getImageWidth();
  }

  /**
   * 获取整个地图高
   * 
   * @return
   */
  public int getHeight(){
    return (int) factory.getImageHeight();
  }

  public int getRows(){
    return factory.getMapRows();
  }

  public int getColumns(){
    return factory.getMapColumns();
  }

  /**
   * 获取地图X偏移量,应该为负值或0
   * 
   * @return
   */
  public int getOffsetX(){
    // X偏移位置
    int offsetX = RpgConstants.CANVAS_WIDTH / 2 - sprites.getHero().getPx();
    offsetX = Math.min(offsetX, 0);
    offsetX = Math.max(offsetX, RpgConstants.CANVAS_WIDTH - getWidth());
    return offsetX;
  }

  /**
   * 获取地图Y偏移量,应该为负值或0
   * 
   * @return
   */
  public int getOffsetY(){
    // Y偏移位置
    int offsetY = RpgConstants.CANVAS_HEIGHT / 2 - sprites.getHero().getPy();
    // 计算Y偏移量
    offsetY = Math.min(offsetY, 0);
    offsetY = Math.max(offsetY, RpgConstants.CANVAS_HEIGHT - getHeight());
    return offsetY;
  }

  public int getFirstTileX(){
    return firstTileX;
  }

  public int getFirstTileY(){
    return firstTileY;
  }

  /**
   * 获取地图上所有角色
   * 
   * @return
   */
  public Set<Sprite> getRoles(){
    return sprites.getCharacters();
  }

  /**
   * 获取当前地图上的英雄
   * 
   * @return
   */
  public Sprite getHero(){
    return sprites.getHero();
  }

  /**
   * 当前x坐标是否在地图左边
   * 
   * @param x
   * @return
   */
  boolean isMapLeft(int x){
    return x + getOffsetX() < RpgConstants.CANVAS_WIDTH / 2;
  }

  /**
   * 初始化地图Hero,同时将设置精灵所在地图为本地图
   * 
   * @param hero
   */
  public void initHero(Sprite hero){
    hero.setAutoMove(false);
    sprites.setHero(hero);
    hero.setMapContainer(this);
  }

  /**
   * 为该化地图添加角色,同时将设置精灵所在地图为本地图
   * 
   * @param sprite
   */
  public void addCharacter(Sprite sprite){
    sprites.addCharacter(sprite);
    sprite.setMapContainer(this);
  }

  /**
   * 删除精灵
   * 
   * @param sprite
   */
  public void removeSprite(Sprite sprite){
    sprites.removeCharacter(sprite);
    sprite.setMapContainer(null);
  }

}
