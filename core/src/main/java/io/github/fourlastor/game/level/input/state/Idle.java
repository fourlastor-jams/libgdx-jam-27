package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import io.github.fourlastor.game.level.component.TurretComponent;
import javax.inject.Inject;

public class Idle extends InputState {
    @Inject
    public Idle(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void update(Entity entity) {
        TurretComponent turret = turret(entity);
        int aimDirection = 0;

        if (Gdx.input.isKeyPressed(turret.left)) {
            aimDirection -= 1;
        }
        if (Gdx.input.isKeyPressed(turret.right)) {
            aimDirection += 1;
        }
        if (aimDirection != 0) {
            turret.stateMachine.changeState(turret.aiming);
        }
    }
}
