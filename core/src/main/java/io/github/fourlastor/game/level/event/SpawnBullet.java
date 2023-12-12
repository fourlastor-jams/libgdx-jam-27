package io.github.fourlastor.game.level.event;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class SpawnBullet implements Pool.Poolable {

    public final Vector2 location = new Vector2();
    public float angle = 0f;

    public void set(float x, float y, float angle) {
        location.set(x, y);
        this.angle = angle;
    }

    @Override
    public void reset() {
        set(0, 0, 0);
    }
}
