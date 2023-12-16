package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.fourlastor.game.level.component.MovementComponent;
import io.github.fourlastor.game.level.component.PositionComponent;
import javax.inject.Inject;

public class MovementSystem extends IteratingSystem {

    private final ComponentMapper<MovementComponent> movements;
    private final ComponentMapper<PositionComponent> positions;

    @Inject
    public MovementSystem(ComponentMapper<MovementComponent> movements, ComponentMapper<PositionComponent> positions) {
        super(Family.all(MovementComponent.class, PositionComponent.class).get());
        this.movements = movements;
        this.positions = positions;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementComponent movement = movements.get(entity);
        float amount = deltaTime / movement.duration;
        float x = movement.direction.x * amount;
        float y = movement.direction.y * amount;
        positions.get(entity).position.add(x, y);
    }
}
