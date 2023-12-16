package io.github.fourlastor.game.level.enemy;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.level.EntitiesFactory;
import io.github.fourlastor.game.level.component.CityComponent;
import io.github.fourlastor.game.level.component.DestroyedComponent;
import javax.inject.Inject;

public class EnemySpawnSystem extends EntitySystem {
    private static final Family FAMILY_CITIES =
            Family.all(CityComponent.class).exclude(DestroyedComponent.class).get();

    private final EntitiesFactory entitiesFactory;
    private final EnhancedRandom random;
    private final ComponentMapper<CityComponent> cities;
    private ImmutableArray<Entity> cityEntities;

    @Inject
    public EnemySpawnSystem(
            EntitiesFactory entitiesFactory, EnhancedRandom random, ComponentMapper<CityComponent> cities) {
        this.entitiesFactory = entitiesFactory;
        this.random = random;
        this.cities = cities;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        cityEntities = engine.getEntitiesFor(FAMILY_CITIES);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int cityCount = cityEntities.size();
        if (cityCount > 0 && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            int cityIndex = random.nextInt(cityCount);
            CityComponent cityComponent = cities.get(cityEntities.get(cityIndex));
            getEngine().addEntity(entitiesFactory.enemy(cityComponent));
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        cityEntities = null;
        super.removedFromEngine(engine);
    }
}
