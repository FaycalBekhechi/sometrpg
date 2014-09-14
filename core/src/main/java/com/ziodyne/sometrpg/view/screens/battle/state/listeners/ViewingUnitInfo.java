package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.StateEnum;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.ziodyne.sometrpg.view.controllers.MenuController;
import com.ziodyne.sometrpg.view.input.InputHandlerStack;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;
import com.ziodyne.sometrpg.view.widgets.UnitInfoMenu;

public class ViewingUnitInfo extends FlowListener<BattleContext> implements InputProcessor {
  private final MenuController menuController;
  private final InputHandlerStack handlerStack;
  private UnitInfoMenu infoMenu;
  private BattleContext context;

  public ViewingUnitInfo(StateEnum state, MenuController menuController, InputHandlerStack handlers) {
    super(state);
    this.menuController = menuController;
    this.handlerStack = handlers;
  }

  @Override
  public void onLeave(BattleContext context) {

    if (infoMenu != null) {
      infoMenu.dispose();
    }
    handlerStack.pop();
  }

  @Override
  public void onEnter(BattleContext context) {

    // This must happen in order or else the context may not be available when the input starts coming in
    this.context = context;
    handlerStack.push(this);

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
