package com.ziodyne.sometrpg.view.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.IntMap;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.components.ScreenPosition;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.rendering.Sprite;

/**
 * A system to render sprites in screen space, based on z-index order.
 */
public class ScreenSpriteRenderSystem extends IteratingSystem {
  private final OrthographicCamera camera;
  private final SpriteBatch batch;
  public final Family family;

  private List<Entity> zSortedEntities;

  public interface Factory {
    public ScreenSpriteRenderSystem create(OrthographicCamera camera);
  }

  @AssistedInject
  ScreenSpriteRenderSystem(@Assisted OrthographicCamera camera, SpriteBatch batch) {
    super(Family.getFamilyFor(SpriteComponent.class, ScreenPosition.class));
    this.family = Family.getFamilyFor(SpriteComponent.class, ScreenPosition.class);
    this.camera = camera;
    this.batch = batch;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

  }

  @Override
  public void addedToEngine(Engine engine) {

    super.addedToEngine(engine);
    zSortedEntities = getZOrdered(engine.getEntitiesFor(family));
  }

  // Get a real collection of entities ordered in ascending z-index order.
  private List<Entity> getZOrdered(Iterable<IntMap.Entry<Entity>> bag) {
    List<Entity> result = new ArrayList<>();

    for (IntMap.Entry<Entity> entityEntry : bag) {
      result.add(entityEntry.value);
    }

    Collections.sort(result, new ZIndexComparator());

    return result;
  }

  private static class ZIndexComparator implements Comparator<Entity> {


    @Override
    public int compare(Entity o1, Entity o2) {

      return o1.getComponent(ScreenPosition.class).getzIndex() -
        o2.getComponent(ScreenPosition.class).getzIndex();
    }
  }
}
