package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import io.github.fourlastor.game.level.component.Turret;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;

public class Aiming extends InputState {
    @Inject
    public Aiming(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void update(Entity entity) {
        int direction = 0;
        Turret turret = turret(entity);
        if (Gdx.input.isKeyPressed(turret.left)) {
            direction -= 1;
        }
        if (Gdx.input.isKeyPressed(turret.right)) {
            direction += 1;
        }

        AnimatedImage animatedImage = turret.animatedImage;

        if (direction == 0) {
            turret.stateMachine.changeState(turret.idle);
            return;
        }

        float delta = delta() * direction;
        float progress = MathUtils.clamp(animatedImage.playTime + delta, 0f, turret.maxLength);
        animatedImage.setProgress(progress);
    }
}
