package com.ziodyne.sometrpg.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraMoveController implements InputProcessor {
    private final OrthographicCamera camera;

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
    public boolean touchDown(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i2, int i3, int i4) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i2, int i3) {
        camera.translate(new Vector3(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY(), 0));

        return false;
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
