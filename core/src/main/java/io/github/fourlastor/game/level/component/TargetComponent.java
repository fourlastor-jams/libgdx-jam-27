package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TargetComponent implements Component {
    public final Vector2 hitTarget;
    public final Rectangle area;

    public final int minEnemyIndex;
    public final int maxEnemyIndex;

    public TargetComponent(Vector2 hitTarget, int minEnemyIndex, int maxEnemyIndex) {
        this.hitTarget = hitTarget;
        area = new Rectangle(hitTarget.x - 1, hitTarget.y - 1, 5, 5);
        this.minEnemyIndex = minEnemyIndex;
        this.maxEnemyIndex = maxEnemyIndex;
    }
}
