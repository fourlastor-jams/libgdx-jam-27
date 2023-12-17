package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.inject.Inject;

import io.github.fourlastor.game.SoundController;

public class LevelScreen extends ScreenAdapter {

    private final Engine engine;
    private final Viewport viewport;
    private final EntitiesFactory entitiesFactory;

    @Inject
    public LevelScreen(Engine engine, Viewport viewport, EntitiesFactory entitiesFactory, SoundController soundController, AssetManager assetManager) {
        this.engine = engine;
        this.viewport = viewport;
        this.entitiesFactory = entitiesFactory;

        Music music = assetManager.get("audio/music/612631__szegvari__techno-retro-trance-sample-short-cinematic-120bpm-music-surround.ogg");
        soundController.play(music, 1f, true);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void show() {
        engine.addEntity(entitiesFactory.fade());
        engine.addEntity(entitiesFactory.background());
        for (Entity turret : entitiesFactory.turrets()) {
            engine.addEntity(turret);
        }
        for (Entity city : entitiesFactory.cities()) {
            engine.addEntity(city);
        }
    }

    @Override
    public void hide() {
        engine.removeAllEntities();
        engine.removeAllSystems();
    }
}
