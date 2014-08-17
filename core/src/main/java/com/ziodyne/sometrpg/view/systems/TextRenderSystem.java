package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Text;

public class TextRenderSystem extends IteratingSystem {

  private final SpriteBatch batch;
  private final static String FRAGMENT_SHADER = "#ifdef GL_ES\n" +
    "precision mediump float;\n" +
    "#endif\n" +
    "\n" +
    "uniform sampler2D u_texture;\n" +
    "\n" +
    "varying vec4 v_color;\n" +
    "varying vec2 v_texCoord;\n" +
    "\n" +
    "const float smoothing = 1.0/16.0;\n" +
    "\n" +
    "void main() {\n" +
    "    float distance = texture2D(u_texture, v_texCoord).a;\n" +
    "    float alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);\n" +
    "    gl_FragColor = vec4(v_color.rgb, alpha);\n" +
    "}";

  private final static String VERTEX_SHADER = "uniform mat4 u_projTrans;\n" +
    "\n" +
    "attribute vec4 a_position;\n" +
    "attribute vec2 a_texCoord0;\n" +
    "attribute vec4 a_color;\n" +
    "\n" +
    "varying vec4 v_color;\n" +
    "varying vec2 v_texCoord;\n" +
    "\n" +
    "void main() {\n" +
    "    gl_Position = u_projTrans * a_position;\n" +
    "    v_texCoord = a_texCoord0;\n" +
    "    v_color = a_color;\n" +
    "}";

  private final ShaderProgram program;

  @Inject
  TextRenderSystem(SpriteBatch batch) {
    super(Family.getFamilyFor(Position.class, Text.class));
    this.batch = batch;
    this.program = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
  }

  @Override
  public void update(float deltaTime) {

    batch.begin();
    batch.setShader(program);
    super.update(deltaTime);
    batch.setShader(null);
    batch.end();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    Position pos = entity.getComponent(Position.class);
    Text text = entity.getComponent(Text.class);
    BitmapFont font = text.getFont();
    font.draw(batch, text.getCharacters(), pos.getX(), pos.getY());
  }
}
