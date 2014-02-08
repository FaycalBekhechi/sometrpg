package com.ziodyne.sometrpg.view.systems;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.view.components.Navigating;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.tween.TweenUtils;

public class BattleUnitNavigationSystem extends EntityProcessingSystem {
  @Mapper
  private ComponentMapper<Position> positionComponentMapper;

  @Mapper
  private ComponentMapper<Navigating> navigatingComponentMapper;

  private final TweenManager tweenManager;

  public BattleUnitNavigationSystem(TweenManager tweenManager) {
    super(Aspect.getAspectForAll(Position.class, Navigating.class));
    this.tweenManager = tweenManager;
  }

  @Override
  protected void process(Entity entity) {
    Navigating navigationComponent = navigatingComponentMapper.get(entity);
    Path<GridPoint2> path = navigationComponent.path;
    Position positionComponent = positionComponentMapper.get(entity);

    Timeline timeline = TweenUtils.sequencePosition(positionComponent, path.getPoints(), 0.1f);
    timeline.start(tweenManager);

    entity.removeComponent(navigationComponent);
  }
}
