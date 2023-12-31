package io.github.fourlastor.game.level;

import com.badlogic.gdx.graphics.Color;

public interface Config {

    interface Screen {
        Color CLEAR_COLOR = new Color(0.071f, 0.024f, 0.071f, 1f);
    }

    interface Turret {
        float AIM_SPEED = 90f;
        float SHOOT_INTERVAL = 0.15f;
    }

    interface Bullet {

        float SPEED = 7f;
    }
}
