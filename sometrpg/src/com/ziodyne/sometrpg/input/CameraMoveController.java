package com.ziodyne.sometrpg.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraMoveController implements InputProcessor {
    private final OrthographicCamera camera;
    private boolean ignoreNextTouchUp = false;

    public CameraMoveController(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
      if (button == Input.Buttons.LEFT && !ignoreNextTouchUp) {

        // Get the touch position in world space.
        Vector3 touchCoords3d = new Vector3(screenX, screenY, 1);
        camera.unproject(touchCoords3d);

        // Compute the offset from the current camera position
        touchCoords3d = touchCoords3d.sub(camera.position);

        camera.translate(touchCoords3d.x, touchCoords3d.y);
      }
      ignoreNextTouchUp = false;
      return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        camera.translate(new Vector3(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY(), 0));
        ignoreNextTouchUp = true;

        return true;
    }

    @Override
    public boolean mouseMoved(int i, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.zoom += amount*0.1;

        return false;
    }
}
