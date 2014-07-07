Map TMX Specification
---

Map Properties
===
- win_condition: Describes the goal condition of the map by name. Different goals may necessitate additional data.
  - rout
  - protect
    - Additional data
      - targets: a comma-delimited list of unit ids that must be protected
  - sieze
    - Additional data
      - Must have a goal tile on the Goals layer
  - survive
    - Additional data
      - turns: the number of turns the player must survive in order to win the map

Required Layers
===

All layers must use exactly the following names to be detected by the engine.

- Blocking
  - An object layer that describes what tiles are impassable.
  - Each object in this layer should be a rectangle with no properties.
  - The rectangle can cover any number of tiles, BUT it must be grid-aligned

- Units
  - An object layer that describes which units start where
  - Each object in this layer should be a rectangle.
  - Properties
    - 'unitId': The identifier of the unit in the characters file

Optional Layers
===

- Goals
  - A rectangular object layer that contains stuff like 'sieze' goal tiles, only if applicable
  - A goal object just needs the property 'goal' set to true.
