package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;

public class BulletComponent implements Component {
    public final float angle;

    public BulletComponent(float angle) {
        this.angle = angle;
    }
}
