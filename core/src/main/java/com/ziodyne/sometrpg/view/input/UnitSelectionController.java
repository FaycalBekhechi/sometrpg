package com.ziodyne.sometrpg.view.input;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.view.components.Position;

public class UnitSelectionController extends InputAdapter {

  private final World world;
  private final Battle battle;
  private final OrthographicCamera camera;

  public UnitSelectionController(World world, Battle battle, OrthographicCamera camera) {
    this.world = world;
    this.battle = battle;
    this.camera = camera;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    TagManager tagManager = world.getManager(TagManager.class);
    Entity mapSelector = tagManager.getEntity("map_selector");
    if (mapSelector != null) {

      Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
      camera.unproject(clickCoordinates);
      int x = (int)Math.floor(clickCoordinates.x);
      int y = (int)Math.floor(clickCoordinates.y);

      Tile tile = battle.getMap().getTile(x, y);
      if (tile != null) {
        Unit unit = tile.getOccupyingUnit();
        if (unit != null) {
          mapSelector.enable();
          Position pos = world.getMapper(Position.class).get(mapSelector);
          pos.setX(x);
          pos.setY(y);
        } else {
          mapSelector.disable();
        }
      } else {
        mapSelector.disable();
      }

      return true;
    }
    return false;
  }
}
