package io.github.fourlastor.game.level.city.state;

import com.badlogic.ashley.core.ComponentMapper;
import io.github.fourlastor.game.level.component.CityComponent;
import javax.inject.Inject;

public class Mappers {

    public final ComponentMapper<CityComponent> cities;

    @Inject
    public Mappers(ComponentMapper<CityComponent> cities) {
        this.cities = cities;
    }
}
