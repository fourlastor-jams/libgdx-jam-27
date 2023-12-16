package io.github.fourlastor.game.level.input.state;

import com.badlogic.ashley.core.Entity;
import io.github.fourlastor.game.level.component.TargetComponent;
import io.github.fourlastor.game.level.component.TurretComponent;
import javax.inject.Inject;

public class TurretDestroyed extends InputState {
    @Inject
    public TurretDestroyed(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void enter(Entity entity) {
        super.enter(entity);
        entity.remove(TargetComponent.class);
        TurretComponent turret = turret(entity);
        turret.animatedImage.setVisible(false);
        turret.towerImage.setVisible(false);
        turret.destroyedImage.setVisible(true);
    }

    @Override
    public void onHit(Entity entity) {}
}
