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
import io.github.fourlastor.game.level.event.Message;
import javax.inject.Inject;

public class EnemyRemovalSystem extends IteratingSystem {

    private static final Family FAMILY =
            Family.all(EnemyComponent.class, PositionComponent.class).get();
    private static final Family FAMILY_CITIES = Family.all(CityComponent.class).get();

    private final ComponentMapper<CityComponent> cities;
    private final ComponentMapper<PositionComponent> positions;
    private final ComponentMapper<EnemyComponent> enemies;
    private final MessageDispatcher messageDispatcher;
    private ImmutableArray<Entity> cityEntities;

    @Inject
    public EnemyRemovalSystem(
            ComponentMapper<CityComponent> cities,
            ComponentMapper<PositionComponent> positions,
            ComponentMapper<EnemyComponent> enemies,
            MessageDispatcher messageDispatcher) {
        super(FAMILY);
        this.cities = cities;
        this.positions = positions;
        this.enemies = enemies;
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        cityEntities = engine.getEntitiesFor(FAMILY_CITIES);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        cityEntities = null;
        super.removedFromEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        for (Entity city : cityEntities) {
            Vector2 position = positions.get(entity).position;
            float headX = enemies.get(entity).headX;
            CityComponent cityComponent = cities.get(city);
            if (cityComponent.area.contains(position.x + headX, position.y)) {
                getEngine().removeEntity(entity);
                messageDispatcher.dispatchMessage(Message.CITY_HIT.ordinal(), city);
            }
        }
    }
}
