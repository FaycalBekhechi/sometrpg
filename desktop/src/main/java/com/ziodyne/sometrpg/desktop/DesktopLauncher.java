package com.ziodyne.sometrpg.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.ziodyne.sometrpg.view.SomeTRPG;

public class DesktopLauncher {
 public static void main(String[] args) {
   LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
   cfg.title = "sometrpg";
   cfg.width = 1600;
   cfg.height = 900;
   cfg.samples = 8;

   new LwjglApplication(new SomeTRPG(), cfg);
 }
}
