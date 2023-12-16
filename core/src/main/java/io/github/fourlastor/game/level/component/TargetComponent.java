package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TargetComponent implements Component {
    public final Vector2 hitTarget;
    public final Rectangle area;

    public TargetComponent(Vector2 hitTarget) {
        this.hitTarget = hitTarget;
        area = new Rectangle(hitTarget.x - 1, hitTarget.y - 1, 5, 5);
    }
}
