package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import io.github.fourlastor.game.level.component.TurretComponent;
import javax.inject.Inject;

public class Mappers {

    public final ComponentMapper<TurretComponent> turrets;

    @Inject
    public Mappers(ComponentMapper<TurretComponent> turrets) {
        this.turrets = turrets;
    }
}
