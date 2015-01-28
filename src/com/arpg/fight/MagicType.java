package com.arpg.fight;

/**
 * 魔法类型
 * 
 * @author zhou
 *
 */
public enum MagicType {
  SINGLE("单体攻击"), AOE("范围攻击"), MINE("自己", false);

  private String name;
  /* 魔法施放对象,敌人or自己 */
  private boolean enmey;

  private MagicType(String name){
    this(name, true);
  }

  private MagicType(String name, boolean enmey){
    this.name = name;
    this.enmey = enmey;
  }

  public String getName(){
    return name;
  }

  public boolean isEnmey(){
    return enmey;
  }

}
