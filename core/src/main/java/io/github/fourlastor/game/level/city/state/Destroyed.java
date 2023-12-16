package io.github.fourlastor.game.level.city.state;

import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.level.component.TargetComponent;
import javax.inject.Inject;

public class Destroyed extends CityState {

    @Inject
    public Destroyed(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        city(entity).cityImage.setVisible(false);
        city(entity).destroyedImage.setVisible(true);
        entity.remove(TargetComponent.class);
    }

    @Override
    public void onHit(Entity entity) {}
}
