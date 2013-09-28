package com.ziodyne.sometrpg.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Sprite extends Component {
    public Texture texture;
    public Color color = new Color(1, 1, 1, 1);

    public Sprite(String path) {
        texture = new Texture(path);
    }
}
