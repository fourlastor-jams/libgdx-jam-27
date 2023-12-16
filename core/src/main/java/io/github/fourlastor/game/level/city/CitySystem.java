package io.github.fourlastor.game.level.city;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import io.github.fourlastor.game.level.component.CityComponent;
import io.github.fourlastor.game.level.event.Message;
import javax.inject.Inject;

public class CitySystem extends EntitySystem implements Telegraph {

    private final MessageDispatcher messageDispatcher;
    private final ComponentMapper<CityComponent> cities;

    @Inject
    public CitySystem(MessageDispatcher messageDispatcher, ComponentMapper<CityComponent> cities) {
        this.messageDispatcher = messageDispatcher;
        this.cities = cities;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        messageDispatcher.addListener(this, Message.CITY_HIT.ordinal());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        Entity entity = (Entity) msg.extraInfo;
        cities.get(entity).stateMachine.onHit();
        return true;
    }
}
