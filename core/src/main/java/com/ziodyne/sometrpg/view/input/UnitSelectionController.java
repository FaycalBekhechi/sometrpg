package com.ziodyne.sometrpg.view.input;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.logic.models.battle.DefaultBattle;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.components.Position;

public class UnitSelectionController extends InputAdapter {

  private final World world;
  private final DefaultBattle battle;
  private final OrthographicCamera camera;

  public UnitSelectionController(World world, DefaultBattle battle, OrthographicCamera camera) {
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

      Position pos = world.getMapper(Position.class).get(mapSelector);
      if (mapSelector.isEnabled() && x == pos.getX() && y == pos.getY()) {
        return false;
      }

      Tile tile = battle.getMap().getTile(x, y);
      if (tile != null) {
        Combatant combatant = tile.getCombatant();
        if (combatant != null) {
          mapSelector.enable();
          pos.setX(x);
          pos.setY(y);
          return true;
        } else {
          if (mapSelector.isEnabled()) {
            mapSelector.disable();
            return true;
          }
          return false;
        }
      } else {
        mapSelector.disable();
        return false;
      }
    }
    return false;
  }
}
