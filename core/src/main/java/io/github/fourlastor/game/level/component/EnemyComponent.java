package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;

public class EnemyComponent implements Component {
    public final float headX;

    public EnemyComponent(float headX) {
        this.headX = headX;
    }
}
