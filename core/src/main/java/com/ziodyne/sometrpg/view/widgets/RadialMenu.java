package com.ziodyne.sometrpg.view.widgets;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.ziodyne.sometrpg.view.components.ShapeComponent;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class RadialMenu extends Widget {
  private static final float PADDING = 5f;

  private final float radius;
  private final List<Item> items;
  private final EntityFactory entityFactory;
  private final Vector2 position;
  private Consumer<String> clickHandler = (s) -> {};
  private final RangeMap<Float, String> radiusRangeToItemName = TreeRangeMap.create();
  private final OrthographicCamera orthographicCamera;

  public static class Item {
    private final String label;
    private final String name;
    private final float sizeInDegrees;

    public Item(String name, float size) {
      this(null, name, size);
    }

    public Item(String label, String name, float sizeInDegrees) {

      this.label = label;
      this.name = name;
      this.sizeInDegrees = sizeInDegrees;
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

  public RadialMenu(Engine engine, EntityFactory entityFactory, Vector2 position, OrthographicCamera camera, Collection<Item> items, float radius) {

    super(engine);

    this.entityFactory = entityFactory;
    this.position = position;
    this.orthographicCamera = camera;
    this.items = Lists.newArrayList(items);
    this.radius = radius;

    float deg = 0f;
    for (Item item : items) {
      Range<Float> degrees = Range.closedOpen(deg, deg+item.sizeInDegrees);
      radiusRangeToItemName.put(degrees, item.name);
      deg += item.sizeInDegrees;
    }

    logDebug("Radial Menu: " + radiusRangeToItemName);
  }

  public void setClickHandler(Consumer<String> clickHandler) {

    this.clickHandler = clickHandler;
  }

  private Vector2 getRotatedOuterRimPoint(float degrees, float radius) {
    Vector2 pointAbove = new Vector2(position.x, position.y + radius);
    return com.ziodyne.sometrpg.logic.util.MathUtils.rotateAroundPoint(position, pointAbove, degrees);
  }

  private Vector2 getRotatedOuterRimPoint(float degrees) {
    return getRotatedOuterRimPoint(degrees, radius);
  }

  @Override
  public void render() {

    ShapeComponent shapeComponent = new ShapeComponent((shapeRenderer) -> {
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      Gdx.gl.glEnable(GL20.GL_BLEND);

      float x = position.x;
      float y = position.y;

      Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
      Gdx.gl.glClearStencil(0);
      Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
      Gdx.gl.glColorMask(false, false, false, false);
      Gdx.gl.glDepthMask(false);
      Gdx.gl.glStencilFunc(GL20.GL_NOTEQUAL, 1, 1);
      Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE);

      shapeRenderer.setColor(1, 1, 1, 1f);
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      shapeRenderer.circle(x, y, 40);


      float deg = 0f;
      for (Item item : items) {
        deg += item.sizeInDegrees;
        Vector2 outerRimPoint = getRotatedOuterRimPoint(deg);
        float lineMaskSize = (MathUtils.degreesToRadians * PADDING) * radius;
        shapeRenderer.rectLine(x, y, outerRimPoint.x, outerRimPoint.y, lineMaskSize);
      }

      shapeRenderer.end();

      Gdx.gl.glColorMask(true, true, true, true);
      Gdx.gl.glDepthMask(true);
      Gdx.gl.glStencilFunc(GL20.GL_STENCIL_FUNC, 1, 1);
      Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);

      shapeRenderer.setColor(0, 0, 0, 0.5f);
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

      deg = 90f;
      for (Item item : items) {
        shapeRenderer.arc(x, y, radius, deg, item.sizeInDegrees, 50);
        deg += item.sizeInDegrees;
      }

      shapeRenderer.end();


      Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
    });

    Entity entity = new Entity();
    entity.add(shapeComponent);
    newEntity(entity);

    for (int i = 0; i < items.size(); i++) {
      Item item = items.get(i);
      if (item.label != null) {
        float deg = (i * item.sizeInDegrees) + (item.sizeInDegrees / 2);
        Vector2 textOffset = getRotatedOuterRimPoint(deg, radius * 0.75f);
        Entity labelEntity = entityFactory.createText(item.label, textOffset, new Vector2());
        newEntity(labelEntity);
      }
    }
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    Vector3 clickCoords = new Vector3(screenX, screenY, 0);
    orthographicCamera.unproject(clickCoords);
    Vector2 worldSpaceCoords = new Vector2(clickCoords.x, clickCoords.y);
    if (worldSpaceCoords.dst(position) > radius) {
      logDebug("Detected click outside the circle. Ignoring.");
      return false;
    }

    Vector2 posToClick = worldSpaceCoords.cpy().sub(position.cpy());
    Vector2 posUp = new Vector2(position.x, position.y+1).sub(position);

    float angleBetween = posToClick.angle(posUp);
    if (angleBetween < 0) {
      angleBetween += 360f;
    }

    logDebug("Detected click at angle: " + angleBetween);
    String clickedItemName = radiusRangeToItemName.get(angleBetween);

    if (clickedItemName == null) {
      return false;
    }

    clickHandler.accept(clickedItemName);

    return true;
  }

  public float getRadius() {
    return radius;
  }
}
