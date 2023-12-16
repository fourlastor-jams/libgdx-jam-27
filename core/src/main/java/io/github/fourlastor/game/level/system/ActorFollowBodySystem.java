package io.github.fourlastor.game.level.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.fourlastor.game.level.component.PositionComponent;
import io.github.fourlastor.harlequin.component.ActorComponent;
import javax.inject.Inject;

/**
 * Coordinates the movement between each pair of scene2d actor and box2d body.
 * Actors follow the bodies.
 */
public class ActorFollowBodySystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(PositionComponent.class, ActorComponent.class).get();
    private final ComponentMapper<PositionComponent> bodies;
    private final ComponentMapper<ActorComponent> actors;

    @Inject
    public ActorFollowBodySystem(ComponentMapper<PositionComponent> bodies, ComponentMapper<ActorComponent> actors) {
        super(FAMILY);
        this.bodies = bodies;
        this.actors = actors;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Vector2 position = bodies.get(entity).position;
        Actor actor = actors.get(entity).actor;
        actor.setPosition(position.x, position.y);
    }
}
