package com.ziodyne.sometrpg.view.widgets;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class RadialMenu extends InputAdapter implements Disposable, Renderable{

  private static final int MAX_ITEMS = 3;

  private final List<Item> items;
  private final Set<Entity> entities = new HashSet<>();
  private final Engine engine;
  private final EntityFactory entityFactory;
  private final Vector2 position;
  private Consumer<String> clickHandler = (s) -> {};
  private final RangeMap<Float, String> radiusRangeToItemName = TreeRangeMap.create();
  private final OrthographicCamera orthographicCamera;

  public static class Item {
    private final String label;
    private final String name;

    public Item(String label, String name) {

      this.label = label;
      this.name = name;
    }

    public int hashCode() {
      return Objects.hash(label, name);
    }

    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }

      if (getClass() != obj.getClass()) {
        return false;
      }

      Item other = (Item)obj;
      return Objects.equals(this.name, other.name);
    }
  }

  public RadialMenu(Engine engine, EntityFactory entityFactory, Vector2 position, OrthographicCamera camera, Collection<Item> items) {

    if (items.size() > MAX_ITEMS) {
      throw new IllegalArgumentException("Cannot have more than " + MAX_ITEMS + " items in a radial menu right now.");
    }

    this.engine = engine;
    this.entityFactory = entityFactory;
    this.position = position;
    this.orthographicCamera = camera;
    this.items = Lists.newArrayList(items);

    int numItems = this.items.size();
    float degreesPerWedge = 360f / numItems;
    for (int i = 0; i < numItems; i++) {
      Range<Float> degrees = Range.closedOpen(i*degreesPerWedge, (i*degreesPerWedge) + degreesPerWedge);
      radiusRangeToItemName.put(degrees, this.items.get(i).name);
    }
  }

  public void setClickHandler(Consumer<String> clickHandler) {

    this.clickHandler = clickHandler;
  }

  @Override
  public void render() {

    Entity testWedge = entityFactory.createRadialMenuWedge(position);
    entities.add(testWedge);
    engine.addEntity(testWedge);
  }

  @Override
  public void dispose() {
    entities.stream()
      .forEach(engine::removeEntity);
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {

    Vector3 clickCoords = new Vector3(screenX, screenY, 0);
    orthographicCamera.unproject(clickCoords);

    Vector2 clickCoords2 = new Vector2(clickCoords.x, clickCoords.y);
    Vector2 up = new Vector2(0, 1);

    float angleBetween = clickCoords2.angle(up);
    String clickedItemName = radiusRangeToItemName.get(angleBetween);

    clickHandler.accept(clickedItemName);

    return true;
  }
}
