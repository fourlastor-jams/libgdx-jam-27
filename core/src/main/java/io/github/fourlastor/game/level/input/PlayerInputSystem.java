package io.github.fourlastor.game.level.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.fourlastor.game.level.component.Turret;
import javax.inject.Inject;

public class PlayerInputSystem extends IteratingSystem {

    private static final Family FAMILY = Family.all(Turret.class).get();

    private final ComponentMapper<Turret> players;

    @Inject
    public PlayerInputSystem(ComponentMapper<Turret> players) {
        super(FAMILY);
        this.players = players;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        players.get(entity).stateMachine.update(deltaTime);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }
}
