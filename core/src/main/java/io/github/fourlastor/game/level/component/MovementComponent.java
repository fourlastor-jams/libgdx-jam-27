package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component {
    public final Vector2 direction;
    public final float duration;

    public MovementComponent(Vector2 direction, float duration) {
        this.direction = direction;
        this.duration = duration;
    }
}
