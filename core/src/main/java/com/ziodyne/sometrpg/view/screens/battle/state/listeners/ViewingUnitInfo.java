package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.StateEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.ziodyne.sometrpg.view.controllers.MenuController;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.InputStealingFlowListener;
import com.ziodyne.sometrpg.view.widgets.UnitInfoMenu;

public class ViewingUnitInfo extends InputStealingFlowListener<BattleContext> implements InputProcessor {
  private final MenuController menuController;
  private UnitInfoMenu infoMenu;
  private BattleContext context;

  public ViewingUnitInfo(StateEnum state, MenuController menuController) {
    super(state);
    this.menuController = menuController;
  }

  @Override
  public void onLeave(BattleContext context) {

    if (infoMenu != null) {
      infoMenu.dispose();
    }

    super.onLeave(context);
  }

  @Override
  public void onEnter(BattleContext context) {

    super.onEnter(context);

    // This must happen in order or else the context may not be available when the input starts coming in
    this.context = context;
    Gdx.input.setInputProcessor(this);

    infoMenu = menuController.showUnitInfo(context.selectedCombatant);
    infoMenu.render();
  }

  @Override
  public boolean keyDown(int keycode) {

    if (Input.Keys.ESCAPE == keycode) {
      context.safeTrigger(BattleEvent.UNIT_DETAILS_CLOSED);
    }
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {

    return false;
  }

  @Override
  public boolean keyTyped(char character) {

    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {

    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {

    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {

    return false;
  }

  @Override
  public boolean scrolled(int amount) {

    return false;
  }
}
