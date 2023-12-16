package io.github.fourlastor.game.level.city.state;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.fourlastor.game.level.component.CityComponent;

public abstract class CityState implements State<Entity> {

    private final Mappers mappers;

    public CityState(Mappers mappers) {
        this.mappers = mappers;
    }

    protected CityComponent city(Entity entity) {
        return mappers.cities.get(entity);
    }

    @Override
    public void enter(Entity entity) {}

    @Override
    public void update(Entity entity) {}

    @Override
    public void exit(Entity entity) {}

    @Override
    public boolean onMessage(Entity entity, Telegram telegram) {
        return false;
    }

    public abstract void onHit(Entity entity);
}
