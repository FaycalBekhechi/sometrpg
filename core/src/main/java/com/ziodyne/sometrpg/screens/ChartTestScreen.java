package com.ziodyne.sometrpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

public class ChartTestScreen implements Screen {
  private Mesh mesh;
  private OrthographicCamera camera;
  
  public ChartTestScreen() {
    camera = new OrthographicCamera(800, 400);
    camera.translate(0, 0);

    MeshBuilder builder = new MeshBuilder();
    builder.begin(new VertexAttributes(new VertexAttribute(Usage.Position, 3, "a_position"),
                                       new VertexAttribute(Usage.ColorPacked, 4, "a_color")));

    builder.vertex(new float[]{ 0, 0, 0, Color.toFloatBits(0, 0, 255, 255) });
    builder.vertex(new float[]{ 0, 2, 0, Color.toFloatBits(255, 0, 0, 255) });
    builder.vertex(new float[]{ -2, 1, 0, Color.toFloatBits(0, 255, 0, 255)});
    builder.vertex(new float[]{ -2, -1, 0, Color.toFloatBits(0, 0, 255, 255) });
    builder.vertex(new float[]{ 0, -2, 0, Color.toFloatBits(0, 255, 0, 255) });
    builder.vertex(new float[]{ 2, -1, 0, Color.toFloatBits(255, 0, 0, 255) });
    builder.vertex(new float[]{ 2, 1, 0, Color.toFloatBits(0, 255, 0, 255) });

    builder.index((short)0, (short)1, (short)2);
    builder.index((short)0, (short)2, (short)3);
    builder.index((short)0, (short)3, (short)4);
    builder.index((short)0, (short)4, (short)5);
    builder.index((short)0, (short)5, (short)6);
    builder.index((short)0, (short)6, (short)1);


    mesh = builder.end();
  }
  
  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    camera.update();
    camera.zoom = 0.01f;
    camera.apply(Gdx.gl10);
    mesh.render(GL10.GL_TRIANGLE_FAN);
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
