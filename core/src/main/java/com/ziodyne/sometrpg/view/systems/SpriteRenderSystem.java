package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;

import java.util.Set;

public class SpriteRenderSystem extends EntitySystem {

  private final OrthographicCamera camera;
  private final SpriteBatch batch;

  @Mapper
  private ComponentMapper<Position> positionMapper;

  @Mapper
  private ComponentMapper<Sprite> spriteMapper;

  @Mapper
  private ComponentMapper<Shader> shaderMapper;

  private ShaderProgram defaultShader;

  public interface Factory {
    public SpriteRenderSystem create(OrthographicCamera camera);
  }

  @AssistedInject
  @SuppressWarnings("unchecked")
  SpriteRenderSystem(@Assisted OrthographicCamera camera, SpriteBatch spriteBatch) {
    super(Aspect.getAspectForAll(Position.class, Sprite.class));
    this.camera = camera;
    this.batch = spriteBatch;
    this.defaultShader = SpriteBatch.createDefaultShader();
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void begin() {
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
  }

  @Override
  protected void end() {
    batch.end();
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entites) {
    // Group eneities by their layer
    SetMultimap<SpriteLayer, Entity> entitiesByLayer = HashMultimap.create();
    for (int i = 0; i < entites.size(); i++) {
      Entity entity = entites.get(i);
      Sprite spriteComponent = spriteMapper.get(entity);

      Set<Entity> entitiesForLayer = entitiesByLayer.get(spriteComponent.getLayer());
      entitiesForLayer.add(entity);
    }

    // Render each layer in declaration order
    for (SpriteLayer layer : SpriteLayer.values()) {
      for (Entity entity : entitiesByLayer.get(layer)) {
        render(entity);
      }
    }
  }

  private void render(Entity entity) {
    Position pos = positionMapper.get(entity);
    Sprite sprite = spriteMapper.get(entity);

    Shader shaderComponent = shaderMapper.getSafe(entity);
    ShaderProgram program = defaultShader;
    if (shaderComponent != null) {
      shaderComponent.update(world.getDelta());
      program = shaderComponent.getShader();
    }

    batch.setShader(program);

    Color color = Color.WHITE;
    batch.setColor(color.r, color.g, color.b, sprite.getAlpha());

    float x = pos.getX();
    float y = pos.getY();
    Texture texture = sprite.getTexture();

    if (texture == null) {
      TextureRegion region = sprite.getRegion();
      batch.draw(region, x, y, region.getRegionWidth()/32f, region.getRegionHeight()/32f);
    } else {
      float width = sprite.getWidth();
      float height = sprite.getHeight();

      batch.draw(texture, x, y, width, height);
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }
}
