package io.github.fourlastor.game.level.city;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.city.state.CityState;

public class CityStateMachine extends DefaultStateMachine<Entity, CityState> {

    @AssistedInject
    public CityStateMachine(@Assisted Entity entity, @Assisted CityState initialState) {
        super(entity, initialState);
    }

    public void onHit() {
        currentState.onHit(owner);
    }

    @AssistedFactory
    public interface Factory {
        CityStateMachine create(Entity entity, CityState initialState);
    }
}
