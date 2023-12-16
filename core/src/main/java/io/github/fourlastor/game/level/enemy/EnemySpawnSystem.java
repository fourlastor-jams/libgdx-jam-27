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
import io.github.fourlastor.game.level.component.TargetComponent;
import javax.inject.Inject;

public class EnemySpawnSystem extends EntitySystem {
    private static final Family FAMILY_CITIES =
            Family.all(TargetComponent.class).get();

    private final EntitiesFactory entitiesFactory;
    private final EnhancedRandom random;
    private final ComponentMapper<TargetComponent> targets;
    private ImmutableArray<Entity> targetEntities;

    @Inject
    public EnemySpawnSystem(
            EntitiesFactory entitiesFactory, EnhancedRandom random, ComponentMapper<TargetComponent> targets) {
        this.entitiesFactory = entitiesFactory;
        this.random = random;
        this.targets = targets;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        targetEntities = engine.getEntitiesFor(FAMILY_CITIES);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int cityCount = targetEntities.size();
        if (cityCount > 0 && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            int cityIndex = random.nextInt(cityCount);
            TargetComponent target = targets.get(targetEntities.get(cityIndex));
            getEngine().addEntity(entitiesFactory.enemy(target));
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        targetEntities = null;
        super.removedFromEngine(engine);
    }
}
