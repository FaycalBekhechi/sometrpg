package com.ziodyne.sometrpg.logging;

public interface Logger {
  public void log(String msg);
  public void error(String msg);
  public void error(String msg, Throwable throwable);
  public void debug(String msg);
}
