package com.ziodyne.sometrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

public class ChartTestScreen implements Screen {
  private Mesh mesh;
  
  public ChartTestScreen() {
    MeshBuilder builder = new MeshBuilder();
    builder.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3, "a_position"),
                                       new VertexAttribute(Usage.ColorPacked, 4, "a_color")));
    
    builder.vertex(new float[]{ -0.5f, -0.5f, 0, Color.toFloatBits(255, 0, 0, 255) });
    builder.vertex(new float[]{ 0.5f, -0.5f, 0, Color.toFloatBits(0, 255, 0, 255)});
    builder.vertex(new float[]{ 0, 0.5f, 0, Color.toFloatBits(0, 0, 255, 255) });

    
    mesh = builder.end();
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    mesh.render(GL10.GL_TRIANGLES);
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void show() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void hide() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void pause() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void resume() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
    
  }

}
