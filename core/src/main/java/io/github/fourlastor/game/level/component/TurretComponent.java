package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Aiming;
import io.github.fourlastor.game.level.input.state.Idle;
import io.github.fourlastor.game.level.input.state.TurretDestroyed;
import io.github.fourlastor.harlequin.ui.AnimatedImage;

/**
 * Bag containing the player state machine, and the possible states it can get into.
 */
public class TurretComponent implements Component {
    public final InputStateMachine stateMachine;

    public final AnimatedImage animatedImage;
    public final Aiming aiming;
    public final Idle idle;
    public final TurretDestroyed destroyed;

    public final float maxLength;
    public final Vector2 fireOrigin;

    public final int left;
    public final int right;

    public float angle = 90f;

    public TurretComponent(
            InputStateMachine stateMachine,
            AnimatedImage animatedImage,
            Aiming aiming,
            Idle idle,
            TurretDestroyed destroyed,
            float maxLength,
            Vector2 fireOrigin,
            int left,
            int right) {
        this.stateMachine = stateMachine;
        this.animatedImage = animatedImage;
        this.aiming = aiming;
        this.idle = idle;
        this.destroyed = destroyed;
        this.maxLength = maxLength;
        this.fireOrigin = fireOrigin;
        this.left = left;
        this.right = right;
    }
}
