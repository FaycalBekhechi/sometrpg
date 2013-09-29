package com.ziodyne.sometrpg;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
 public static void main(String[] args) {
   LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
   cfg.title = "sometrpg";
   cfg.useGL20 = false;
   cfg.width = 480;
   cfg.height = 320;
   cfg.samples = 8;
   
   new LwjglApplication(new SomeTRPG(), cfg);
 }
}
