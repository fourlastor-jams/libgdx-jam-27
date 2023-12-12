package io.github.fourlastor.game.level;

public interface Config {

    interface Turret {
        float AIM_SPEED = 30f;
        float SHOOT_INTERVAL = 0.2f;
    }

    interface Bullet {

        float SPEED = 5f;
    }
}
