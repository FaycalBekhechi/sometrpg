package com.ziodyne.sometrpg.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.components.Player;

public class CameraControlSystem extends EntitySystem implements InputProcessor {
    private final OrthographicCamera camera;

    private Vector3 currentMousePos;

    public CameraControlSystem(OrthographicCamera camera) {
        super(Aspect.getAspectForAll(Player.class));
        this.camera = camera;
    }

    @Override
    protected void initialize() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        currentMousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(currentMousePos);
    }

    @Override
    protected boolean checkProcessing() {
        return true;
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
