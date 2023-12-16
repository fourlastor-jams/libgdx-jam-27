package io.github.fourlastor.game.level.city.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import io.github.fourlastor.game.level.component.CityComponent;
import javax.inject.Inject;

public class ShieldDown extends CityState {

    @Inject
    public ShieldDown(Mappers mappers) {
        super(mappers);
    }

    @Override
    public void enter(Entity entity) {
        Image shieldImage = city(entity).shieldImage;
        shieldImage.addAction(Actions.sequence(
                Actions.parallel(Actions.fadeOut(0.4f), Actions.color(Color.RED, 0.4f)),
                Actions.run(() -> shieldImage.setVisible(false))));
    }

    @Override
    public void onHit(Entity entity) {
        CityComponent city = city(entity);
        city.stateMachine.changeState(city.destroyed);
    }
}
