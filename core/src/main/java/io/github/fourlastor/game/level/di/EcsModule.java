package io.github.fourlastor.game.level.di;

import com.badlogic.ashley.core.ComponentMapper;
import dagger.Module;
import dagger.Provides;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.level.component.CityComponent;
import io.github.fourlastor.game.level.component.EnemyComponent;
import io.github.fourlastor.game.level.component.MovementComponent;
import io.github.fourlastor.game.level.component.PositionComponent;
import io.github.fourlastor.game.level.component.ScoreComponent;
import io.github.fourlastor.game.level.component.TargetComponent;
import io.github.fourlastor.game.level.component.TurretComponent;
import io.github.fourlastor.harlequin.component.ActorComponent;

@Module
public class EcsModule {

    @Provides
    @ScreenScoped
    public ComponentMapper<ActorComponent> imageComponent() {
        return ComponentMapper.getFor(ActorComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<TurretComponent> turretComponent() {
        return ComponentMapper.getFor(TurretComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<CityComponent> cityComponent() {
        return ComponentMapper.getFor(CityComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<PositionComponent> positionComponent() {
        return ComponentMapper.getFor(PositionComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<MovementComponent> movementComponent() {
        return ComponentMapper.getFor(MovementComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<EnemyComponent> enemyComponent() {
        return ComponentMapper.getFor(EnemyComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<TargetComponent> targetComponent() {
        return ComponentMapper.getFor(TargetComponent.class);
    }

    @Provides
    @ScreenScoped
    public ComponentMapper<ScoreComponent> scoreComponent() {
        return ComponentMapper.getFor(ScoreComponent.class);
    }
}
