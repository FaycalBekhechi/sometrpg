package com.ziodyne.sometrpg.logic.loader;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.CharacterDatabase;
import com.ziodyne.sometrpg.logic.models.battle.Army;
import com.ziodyne.sometrpg.logic.models.battle.ArmyType;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.TerrainType;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.conditions.ProtectUnits;
import com.ziodyne.sometrpg.logic.models.battle.conditions.Rout;
import com.ziodyne.sometrpg.logic.models.battle.conditions.Sieze;
import com.ziodyne.sometrpg.logic.models.battle.conditions.Survive;
import com.ziodyne.sometrpg.logic.models.battle.conditions.WinCondition;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import org.apache.commons.lang3.StringUtils;

/**
 * Creates battle models from Tiled maps.
 */
public class TiledBattleBuilder {
  private static final String BASE_LAYER_NAME = "Ground";
  private static final String BLOCKING_LAYER_NAME = "Blocking";
  private static final String UNIT_LAYER_NAME = "Units";
  private static final String GOAL_LAYER_NAME = "Goals";

  private static final String ROUT_CONDITION = "rout";
  private static final String PROTECT_CONDITION = "protect";
  private static final String SIEZE_CONDITION = "seize";
  private static final String SURVIVE_CONDITION = "survive";

  private static final String PROTECT_TARGETS_NAME = "targets";
  private static final String SURVIVE_TURNS_NAME = "turns";

  private final float tileSize;
  private final TiledMap map;
  private final CharacterDatabase characterDB;

  public TiledBattleBuilder(TiledMap map, CharacterDatabase characterDB) {

    this.map = map;
    this.characterDB = characterDB;

    TiledMapTileLayer baseLayer = (TiledMapTileLayer)getLayer(BASE_LAYER_NAME);
    float tileWidth = baseLayer.getTileWidth();
    float tileHeight = baseLayer.getTileHeight();

    if (tileWidth != tileHeight) {
      throw new IllegalArgumentException("Non-square tiles in tiled map.");
    }

    this.tileSize = tileWidth;
  }

  private Map<Army, Set<PositionedCharacter>> buildArmyIndex(Collection<PositionedCharacter> allCharacters) {

    return allCharacters.stream()
      .collect(Collectors.groupingBy(character -> {
        Character realCharacter = character.character;
        return characterDB.getArmyForCharacterId(realCharacter.getId());
      }, Collectors.toSet()));

  }

  public SomeTRPGBattle build(EventBus todoFixThis) {

    WinCondition winCondition = buildWinCondition();

    BattleMap battleMap = new BattleMap(buildTiles());

    // Add all characters to the map
    Map<Army, Set<PositionedCharacter>> charactersByArmy = buildCombatants();
    for (Map.Entry<Army, Set<PositionedCharacter>> armyEntry : charactersByArmy.entrySet()) {
      Set<PositionedCharacter> characters = armyEntry.getValue();
      for (PositionedCharacter character : characters) {
        Combatant combatant = new Combatant(character.character);

        // Add the unit to the map on its tile
        battleMap.addUnit(combatant, character.pos);

        // Add the unit to the Army too
        armyEntry.getKey().addCombatant(combatant);
      }
    }

    return new SomeTRPGBattle(battleMap, Lists.newArrayList(charactersByArmy.keySet()), winCondition, todoFixThis);
  }

  private Map<Army, Set<PositionedCharacter>> buildCombatants() {

    MapLayer unitLayer = getLayer(UNIT_LAYER_NAME);
    MapObjects units = unitLayer.getObjects();
    List<PositionedCharacter> allCharacters = Lists.newArrayList(units).stream()

      // Exclude any malformed objects (non-rects, or objects without pointers to units)
      .filter(mapObject -> mapObject instanceof RectangleMapObject && mapObject.getProperties().containsKey("unitId"))

        // Turn them into characters
      .map(object -> getCharacterFromObject((RectangleMapObject) object))
      .collect(Collectors.toList());


    // Collect them into an index.
    return buildArmyIndex(allCharacters);
  }

  private ProtectUnits buildProtectCondition() {
    MapProperties props = map.getProperties();
    String protectTargets = props.get(PROTECT_TARGETS_NAME, String.class);

    Set<Character> charactersToProtect = Arrays.asList(StringUtils.split(protectTargets, ",")).stream()
      // Pull every given character from the database
      .map(id -> characterDB.getById(StringUtils.trim(id)))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toSet());

    if (charactersToProtect.isEmpty()) {
      throw new IllegalArgumentException("Protect condition specified with no characters to protect.");
    }

    return new ProtectUnits(charactersToProtect);
  }

  private Sieze buildSeizeCondition() {

    MapLayer goalLayer = getLayer(GOAL_LAYER_NAME);
    MapObjects objects = goalLayer.getObjects();

    for (MapObject object : objects) {
      if (object instanceof RectangleMapObject) {
        RectangleMapObject rect = (RectangleMapObject)object;

        MapProperties rectProps = rect.getProperties();
        if (rectProps.containsKey("goal")) {
          GridPoint2 tilePosition = getTilePosition(rect.getRectangle());

          return new Sieze(tilePosition.x, tilePosition.y);
        }
      }
    }

    throw new IllegalArgumentException("Seize condition requires a rectangle object on the Goals layer with the 'goal' " +
      "property set.");
  }

  private Survive buildSurviveCondition() {

    MapProperties props = map.getProperties();
    Integer turns = props.get(SURVIVE_TURNS_NAME, Integer.class);
    if (turns == null) {
      throw new IllegalArgumentException("Survive condition requires a 'turns' property.");
    }

    return new Survive(turns);
  }

  private Set<Tile> buildTiles() {

    TiledMapTileLayer baseLayer = (TiledMapTileLayer)getLayer(BASE_LAYER_NAME);
    float tileWidth = baseLayer.getTileWidth();
    float tileHeight = baseLayer.getTileHeight();

    if (tileWidth != tileHeight) {
      throw new IllegalArgumentException("Non-square tiles in tiled map.");
    }

    float tileSize = tileWidth;
    int width = baseLayer.getWidth();
    int height = baseLayer.getHeight();
    Set<Tile> tiles = new HashSet<>(width * height);

    Map<GridPoint2, Tile> tilesByLocation = new HashMap<>(width * height);

    for (int row = 0; row < height; row++) {
      for (int column = 0; column < width; column++) {
        Tile tile = new Tile(TerrainType.GRASS, row, column);
        tiles.add(tile);
        tilesByLocation.put(new GridPoint2(row, column), tile);
      }
    }

    MapLayer blockingLayer = getLayer(BLOCKING_LAYER_NAME);

    // Stupid libgdx doesn't use real collections here.
    StreamSupport.stream(blockingLayer.getObjects().spliterator(), false)
      .filter(object -> object instanceof RectangleMapObject)
      // Figure out what grid points the rects cover
      .map(mapObject -> {

        RectangleMapObject rect = (RectangleMapObject) mapObject;
        Rectangle bounds = rect.getRectangle();

        return tilesByLocation.get(getTilePosition(bounds));
      })
      .filter(Objects::nonNull)

        // Block off any tiles covered by a blocking object
      .forEach(tile -> tile.setPassable(false));

    return tiles;
  }

  private GridPoint2 getTilePosition(Rectangle rectangle) {
    float tileRatio = 1.0f / tileSize;

    // TODO: This isn't going to go super well if the rects aren't snapped to the grid
    return new GridPoint2(
      Math.round(rectangle.x * tileRatio),
      Math.round(rectangle.y * tileRatio)
    );
  }

  private WinCondition buildWinCondition() {

    MapProperties properties = map.getProperties();
    String winCondition = properties.get("win_condition", "rout", String.class);
    switch (winCondition) {
      case ROUT_CONDITION:
        return new Rout();
      case PROTECT_CONDITION:
        return buildProtectCondition();
      case SIEZE_CONDITION:
        return buildSeizeCondition();
      case SURVIVE_CONDITION:
        return buildSurviveCondition();
      default:
        throw new IllegalArgumentException("Unknown win condition in tiled map: " + winCondition);
    }
  }

  private PositionedCharacter getCharacterFromObject(RectangleMapObject unit) {

    float tileRatio = 1.0f / tileSize;
    Rectangle bounds = unit.getRectangle();
    MapProperties props = unit.getProperties();
    String id = props.get("unitId", String.class);
    GridPoint2 position = new GridPoint2(Math.round(bounds.x * tileRatio), Math.round(bounds.y * tileRatio));

    // Add the character + its position to the set and blow up if it cant be found in the db.
    return characterDB.getById(id)
      .map(character -> new PositionedCharacter(position, character))
      .orElseThrow(() -> new IllegalArgumentException("Could not find character by id: " + id));
  }

  private com.badlogic.gdx.maps.MapLayer getLayer(String name) {

    return map.getLayers().get(name);
  }

  private static class PositionedCharacter {
    public final GridPoint2 pos;
    public final Character character;

    private PositionedCharacter(GridPoint2 pos, Character character) {

      this.pos = pos;
      this.character = character;
    }

  }

}
