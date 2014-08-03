package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.util.CollectionUtils;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.components.VoidSprite;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import com.ziodyne.sometrpg.view.rendering.Sprite;
import com.ziodyne.sometrpg.view.rendering.SpriteBatchRenderer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ziodyne.sometrpg.util.CollectionUtils.groupBy;

public class SpriteRenderSystem extends EntitySystem {
  private static final Logger LOG = new GdxLogger(SpriteRenderSystem.class);

  @Mapper
  private ComponentMapper<Position> positionMapper;

  @Mapper
  private ComponentMapper<SpriteComponent> spriteMapper;

  @Mapper
  private ComponentMapper<Shader> shaderMapper;

  private final SpriteBatchRenderer spriteBatchRenderer;

  public interface Factory {
    public SpriteRenderSystem create(OrthographicCamera camera);
  }

  @AssistedInject
  @SuppressWarnings("unchecked")
  SpriteRenderSystem(@Assisted OrthographicCamera camera, SpriteBatch spriteBatch) {
    super(Aspect.getAspectForAll(Position.class, SpriteComponent.class));
    spriteBatchRenderer = new SpriteBatchRenderer(camera, spriteBatch);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void begin() {
    spriteBatchRenderer.begin();
  }

  @Override
  protected void end() {
    spriteBatchRenderer.end();
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entites) {
    // why oh why doesn't this implement Collection? fuck.
    List<Entity> entityList = new ArrayList<>(entites.size());
    for (int i = 0; i < entites.size(); i++) {
      Entity entity = entites.get(i);
      entityList.add(entity);
    }

    // Render each entity sorted by zIndex
    entityList.stream()
      .sorted(byZIndex(spriteMapper))
      .forEach(this::render);
  }

  private static Comparator<Entity> byZIndex(ComponentMapper<SpriteComponent> spriteMapper) {
    return (o1, o2) -> spriteMapper.get(o1).getzIndex() - spriteMapper.get(o2).getzIndex();
  }

  private void render(Entity entity) {
    Position pos = positionMapper.get(entity);
    Vector2 position = new Vector2(pos.getX(), pos.getY());

    SpriteComponent spriteComponent = spriteMapper.get(entity);
    Sprite sprite = spriteComponent.getSprite();

    Shader shaderComponent = shaderMapper.getSafe(entity);
    if (shaderComponent != null) {
      shaderComponent.update(world.getDelta());
      spriteBatchRenderer.render(sprite, position, shaderComponent.getShader());
    } else {
      spriteBatchRenderer.render(sprite, position);
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }
}
