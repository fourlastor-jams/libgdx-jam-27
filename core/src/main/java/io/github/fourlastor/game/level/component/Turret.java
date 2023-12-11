package io.github.fourlastor.game.level.component;

import com.badlogic.ashley.core.Component;
import io.github.fourlastor.game.level.input.InputStateMachine;
import io.github.fourlastor.game.level.input.state.Aiming;
import io.github.fourlastor.game.level.input.state.Idle;
import io.github.fourlastor.harlequin.ui.AnimatedImage;

/**
 * Bag containing the player state machine, and the possible states it can get into.
 */
public class Turret implements Component {
    public final InputStateMachine stateMachine;

    public final AnimatedImage animatedImage;
    public final Aiming aiming;
    public final Idle idle;

    public final float maxLength;

    public final int left;
    public final int right;

    public Turret(
            InputStateMachine stateMachine,
            AnimatedImage animatedImage,
            Aiming aiming,
            Idle idle,
            float maxLength,
            int left,
            int right) {
        this.stateMachine = stateMachine;
        this.animatedImage = animatedImage;
        this.aiming = aiming;
        this.idle = idle;
        this.maxLength = maxLength;
        this.left = left;
        this.right = right;
    }
}
