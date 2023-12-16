package io.github.fourlastor.game.level.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import io.github.fourlastor.game.level.component.TurretComponent;
import io.github.fourlastor.game.level.event.Message;
import javax.inject.Inject;

public class PlayerInputSystem extends IteratingSystem implements Telegraph {

    private static final Family FAMILY = Family.all(TurretComponent.class).get();

    private final MessageDispatcher messageDispatcher;
    private final ComponentMapper<TurretComponent> turrets;

    @Inject
    public PlayerInputSystem(MessageDispatcher messageDispatcher, ComponentMapper<TurretComponent> turrets) {
        super(FAMILY);
        this.messageDispatcher = messageDispatcher;
        this.turrets = turrets;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        turrets.get(entity).stateMachine.update(deltaTime);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        messageDispatcher.addListener(this, Message.TURRET_HIT.ordinal());
    }

    @Override
    public void removedFromEngine(Engine engine) {
        messageDispatcher.removeListener(this, Message.TURRET_HIT.ordinal());
        super.removedFromEngine(engine);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        Entity entity = (Entity) msg.extraInfo;
        turrets.get(entity).stateMachine.onHit();
        return true;
    }
}
