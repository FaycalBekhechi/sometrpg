package com.ziodyne.sometrpg;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.ziodyne.sometrpg.view.SomeTRPG;

public class Main {
 public static void main(String[] args) {
   LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
   cfg.title = "sometrpg";
   cfg.width = 1280;
   cfg.height = 720;
   cfg.samples = 8;

   new LwjglApplication(new SomeTRPG(), cfg);
 }
}
