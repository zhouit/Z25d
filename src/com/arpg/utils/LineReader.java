package com.arpg.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * 行读取器
 * 
 * @author zhou
 *
 */
public class LineReader{
  private int index;
  private List<String> content;

  public LineReader(){
    this(new LinkedList<String>());
  }

  public LineReader(List<String> content){
    this.content = content;
    this.index = 0;
  }

  /**
   * 获取当前行数
   * 
   * @return
   */
  public int lineNumber(){
    return index;
  }

  /**
   * 总行数
   * 
   * @return
   */
  public int lines(){
    return content.size();
  }

  public void addLine(String line){
    this.content.add(line);
  }

  public boolean hasNext(){
    return index < content.size();
  }

  public String next(){
    return content.get(index++);
  }

}
