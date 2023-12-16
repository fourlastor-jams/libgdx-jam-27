package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.math.Vector2;
import io.github.fourlastor.game.level.component.CityComponent;
import io.github.fourlastor.game.level.component.EnemyComponent;
import io.github.fourlastor.game.level.component.PositionComponent;
import io.github.fourlastor.game.level.component.TargetComponent;
import io.github.fourlastor.game.level.component.TurretComponent;
import io.github.fourlastor.game.level.event.Message;
import javax.inject.Inject;

public class EnemyRemovalSystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(EnemyComponent.class, PositionComponent.class).get();
    private static final Family FAMILY_CITIES =
            Family.all(CityComponent.class, TargetComponent.class).get();
    private static final Family FAMILY_TURRETS =
            Family.all(TurretComponent.class, TargetComponent.class).get();

    private final ComponentMapper<TargetComponent> targets;
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<EnemyComponent> enemies;
    private final MessageDispatcher messageDispatcher;
    private ImmutableArray<Entity> cityEntities;
    private ImmutableArray<Entity> towerEntities;

    @Inject
    public EnemyRemovalSystem(
            ComponentMapper<TargetComponent> targets,
            ComponentMapper<PositionComponent> positions,
            ComponentMapper<EnemyComponent> enemies,
            MessageDispatcher messageDispatcher) {
        super(FAMILY);
        this.targets = targets;
        this.positions = positions;
        this.enemies = enemies;
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        cityEntities = engine.getEntitiesFor(FAMILY_CITIES);
        towerEntities = engine.getEntitiesFor(FAMILY_TURRETS);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        cityEntities = null;
        towerEntities = null;
        super.removedFromEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        for (Entity city : cityEntities) {
            if (!targets.has(city)) {
                continue;
            }
            Vector2 position = positions.get(entity).position;
            float headX = enemies.get(entity).headX;
            TargetComponent target = targets.get(city);
            if (target.area.contains(position.x + headX, position.y)) {
                getEngine().removeEntity(entity);
                messageDispatcher.dispatchMessage(Message.CITY_HIT.ordinal(), city);
                return;
            }
        }
        for (Entity tower : towerEntities) {
            if (!targets.has(tower)) {
                continue;
            }
            Vector2 position = positions.get(entity).position;
            float headX = enemies.get(entity).headX;
            TargetComponent target = targets.get(tower);
            if (target.area.contains(position.x + headX, position.y)) {
                getEngine().removeEntity(entity);
                messageDispatcher.dispatchMessage(Message.TURRET_HIT.ordinal(), tower);
                return;
            }
        }
    }
}
