package com.ziodyne.sometrpg.view.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.components.ScreenPosition;
import com.ziodyne.sometrpg.view.components.Sprite;

/**
 * A system to render sprites in screen space, based on z-index order.
 */
public class ScreenSpriteRenderSystem extends EntitySystem {
  private final OrthographicCamera camera;
  private final SpriteBatch batch;

  @Mapper
  private ComponentMapper<ScreenPosition> positionComponentMapper;


  public interface Factory {
    public ScreenSpriteRenderSystem create(OrthographicCamera camera);
  }

  @AssistedInject
  ScreenSpriteRenderSystem(@Assisted OrthographicCamera camera, SpriteBatch batch) {
    super(Aspect.getAspectForAll(Sprite.class, ScreenPosition.class));
    this.camera = camera;
    this.batch = batch;
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    // Render in z-index order
    for (Entity entity : getZOrdered(entityImmutableBag)) {

    }
  }

  // Get a real collection of entities ordered in ascending z-index order.
  private List<Entity> getZOrdered(ImmutableBag<Entity> bag) {
    List<Entity> result = new ArrayList<>();

    for (int i = 0; i < bag.size(); i++) {
      result.add(bag.get(i));
    }

    Collections.sort(result, new ZIndexComparator(positionComponentMapper));

    return result;
  }

  @Override
  protected boolean checkProcessing() {

    return true;
  }

  private static class ZIndexComparator implements Comparator<Entity> {
    private final ComponentMapper<ScreenPosition> positionComponentMapper;

    private ZIndexComparator(ComponentMapper<ScreenPosition> positionComponentMapper) {

      this.positionComponentMapper = positionComponentMapper;
    }

    @Override
    public int compare(Entity o1, Entity o2) {

      return positionComponentMapper.get(o1).getzIndex() -
        positionComponentMapper.get(o2).getzIndex();
    }
  }
}
