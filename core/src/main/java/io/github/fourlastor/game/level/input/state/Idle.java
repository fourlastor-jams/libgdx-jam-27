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
        if (Gdx.input.isKeyPressed(turret.left) || Gdx.input.isKeyPressed(turret.right)) {
            turret.stateMachine.changeState(turret.aiming);
        }
    }
}
