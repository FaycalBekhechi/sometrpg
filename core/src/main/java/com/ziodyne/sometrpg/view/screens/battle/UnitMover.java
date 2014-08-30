package com.ziodyne.sometrpg.view.screens.battle;

import java.util.List;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.entities.Positioned;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;
import com.ziodyne.sometrpg.view.navigation.PathSegment;
import com.ziodyne.sometrpg.view.navigation.PathUtils;

public class UnitMover {

  private final BattleScreen battleScreen;
  private final TweenManager tweenManager;
  private final float gridSize;

  public UnitMover(BattleScreen battleScreen, TweenManager tweenManager, float gridSize) {

    this.battleScreen = battleScreen;
    this.tweenManager = tweenManager;
    this.gridSize = gridSize;
  }

  public interface MovedCallback {
    public void call();
  }

  public void moveCombatant(Combatant combatant, Path<GridPoint2> path) {
    moveCombatant(combatant, path, null);
  }

  public void moveCombatant(Combatant combatant, Path<GridPoint2> path, final MovedCallback callback) {

    RenderedCombatant renderedCombatant = battleScreen.getRenderedCombatant(combatant);

    List<PathSegment> segmentedPath = PathUtils.segmentPath(path);

    // Set the initial running animation
    PathSegment firstSeg = segmentedPath.get(0);
    renderedCombatant.setAnimationType(getAnimationTypeForSegmentType(firstSeg.getType()));

    Timeline movement = Timeline.createSequence();
    for (int i = 0; i < segmentedPath.size(); i++) {
      PathSegment segment = segmentedPath.get(i);

      GridPoint2 point = segment.getPoint();
      Tween segTween = Tween
        .to(renderedCombatant, 0, 0.3f)
        .ease(TweenEquations.easeNone)
        .target(point.x * gridSize, point.y * gridSize);

      int nextIdx = i + 1;
      if (nextIdx < segmentedPath.size()) {
        PathSegment nextSeg = segmentedPath.get(nextIdx);
        PathSegment.Type segType = nextSeg.getType();
        final AnimationType runningAnim = getAnimationTypeForSegmentType(segType);

        segTween.setCallback((i1, baseTween) -> {

          renderedCombatant.setAnimationType(runningAnim);
        });
      }

      movement = movement.push(segTween);
    }

    movement.setCallback((i, baseTween) -> {

      renderedCombatant.setAnimationType(AnimationType.IDLE);
      if (callback != null) {
        callback.call();
      }
    }).start(tweenManager);
  }

  private static AnimationType getAnimationTypeForSegmentType(PathSegment.Type segType) {
    switch (segType) {
      case N:
      case N2E:
      case N2W:
        return AnimationType.RUN_NORTH;
      case E:
      case E2N:
      case E2S:
        return AnimationType.RUN_EAST;
      case W:
      case W2N:
      case W2S:
        return AnimationType.RUN_WEST;
      case START:
        return AnimationType.IDLE;
      default:
        return AnimationType.RUN_SOUTH;
    }
  }
}
