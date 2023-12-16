package io.github.fourlastor.game.level.city.state;

import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.level.component.CityComponent;
import javax.inject.Inject;

public class ShieldUp extends CityState {

    @Inject
    public ShieldUp(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void onHit(Entity entity) {
        CityComponent city = city(entity);
        city.stateMachine.changeState(city.shieldDown);
    }
}
