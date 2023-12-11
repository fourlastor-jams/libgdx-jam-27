package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.ComponentMapper;
import io.github.fourlastor.game.level.component.Turret;
import javax.inject.Inject;

public class Mappers {

    public final ComponentMapper<Turret> turrets;

    @Inject
    public Mappers(ComponentMapper<Turret> turrets) {
        this.turrets = turrets;
    }
}
