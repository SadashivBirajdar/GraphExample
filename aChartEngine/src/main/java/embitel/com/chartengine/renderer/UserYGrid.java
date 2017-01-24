package embitel.com.chartengine.renderer;

import java.io.Serializable;

/** The userYGrid class encapsulate the necessary information
 * to draw user specified grid.
 * If title is not defined yValue is used as title
 * 
 */
public class UserYGrid implements Serializable {
  public String getTitle() {
    return title;
  }

  public double getYValue() {
    return yValue;
  }

  public int getGridColor() {
    return yGridColor;
  }

  public BasicStroke getStroke() {
    return yStroke;
  }

  String title;
  double yValue;
  int yGridColor;
  BasicStroke yStroke;
  
  public UserYGrid(String title, double yValue, int yGridColor, BasicStroke yStroke) {
    super();
    this.title = title;
    this.yValue = yValue;
    this.yGridColor = yGridColor;
    this.yStroke = yStroke;
  }

  public UserYGrid(double yValue, int yGridColor, BasicStroke yStroke) {
    super();
    title=null;
    this.yValue = yValue;
    this.yGridColor = yGridColor;
    this.yStroke = yStroke;
  }
  
  public UserYGrid(double yValue, int yGridColor) {
    super();
    title=null;
    this.yValue = yValue;
    this.yGridColor = yGridColor;
    this.yStroke = BasicStroke.SOLID;
  }
  
}
