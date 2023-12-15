package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TypingConfig;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.fourlastor.game.SoundController;

import javax.inject.Inject;

public class IntroScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = new Color(0x333333ff);

    private final InputMultiplexer inputMultiplexer;
    private final Stage stage;
    private final Viewport viewport;
    private final TextureAtlas textureAtlas;
    private final AssetManager assetManager;

    private Array<Image> ground;
    private Array<Image> cityShields;
    private Array<Image> cities;
    private Array<Image> turrets;
    private Image stars;
    private Image planets;

    private TypingLabel label;

    private Music introMusic;

    @Inject
    public IntroScreen(
            InputMultiplexer inputMultiplexer,
            AssetManager assetManager,
            TextureAtlas textureAtlas,
            SoundController soundController
    ) {
        this.inputMultiplexer = inputMultiplexer;
        this.textureAtlas = textureAtlas;
        this.assetManager = assetManager;

        viewport = new FitViewport(160f, 90f);
        stage = new Stage(viewport);

        TypingConfig.registerEffect("SHAKEFADE", ShakeFadeEffect::new);
        introMusic = assetManager.get("audio/music/241618__zagi2__dark-pulsing-intro.ogg", Music.class);
        soundController.play(introMusic, 1f, false);
    }

    private final InputProcessor processor = new InputAdapter() {
        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transitionToLevelScreen();
            return true;
        }

        @Override
        public boolean keyDown(int keycode) {
            transitionToLevelScreen();
            if (keycode == Input.Keys.Q)
                Gdx.app.exit();
            return super.keyDown(keycode);
        }
    };

    @Override
    public void show() {
        setup();
        startAnimation();


        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(processor);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
        inputMultiplexer.removeProcessor(processor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.071f, 0.024f, 0.071f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    private void transitionToLevelScreen() {
        // TODO
    }

    private void setup() { // TODO
        stars = new Image(textureAtlas.findRegion("background/stars"));
        stars.setY(-20f);
        stage.addActor(stars);

        float offset = -30f;
        planets = new Image(textureAtlas.findRegion("background/planets"));
        planets.setY(-31f + offset);
        stage.addActor(planets);

//        BitmapFont font = assetManager.get("fonts/play-16.fnt");

        label = new TypingLabel("{FADE}{SLOWER}From the cosmic abyss\n{WAIT=0.75}emerged an [dark yellow 3 black 2 apricot 1]ancient {SHAKEFADE=1.5;1;4.5}terror",
                new Font("fonts/grenade-64.fnt", "fonts/grenade-64.png").setTextureFilter().scale(.125f, .125f));
        label.setAlignment(Align.center);
        Table table = new Table();
        table.add(label).padBottom(Gdx.graphics.getHeight() * .025f);
        table.setFillParent(true);
        stage.addActor(table);


        ground = new Array<>();
        ground.add(new Image(textureAtlas.findRegion("ground/layer5")));
        ground.add(new Image(textureAtlas.findRegion("ground/layer4")));
        ground.add(new Image(textureAtlas.findRegion("ground/layer3")));
        ground.add(new Image(textureAtlas.findRegion("intro/layer2")));
        ground.add(new Image(textureAtlas.findRegion("intro/layer1")));

        ground.get(4).setY(-30f + offset); // foreground
        ground.get(3).setY(-17f + offset);
        ground.get(2).setY(-8f + offset);
        ground.get(1).setY(0f + offset);
        ground.get(0).setY(10f + offset); // background

        for (Image layer : ground)
            layer.moveBy(0f, -66f);

        for (Image image : ground)
            stage.addActor(image);
    }

    private void startAnimation() {
        {/*for (int i = 0; i < ground.size; i++) {
            float amountY = 66f - 19 * i;
            float duration = 10f - .2f * i;
            System.out.println(i + ", name: " + ground.get(i).getName() + ", amountY: " + amountY + ", duration: " + duration);
            ground.get(i).addAction(
                    Actions.moveBy(0f, amountY, duration, Interpolation.circleOut)
            );
        }*/
        }

        float durationOffset = 5f;

        float offset = 30f;
        float delay = 5f;
        ground.get(0).addAction(Actions.sequence(
                Actions.delay(delay),
                Actions.moveBy(0f, 31f + offset, 5f + durationOffset, Interpolation.fastSlow)
        ));

        ground.get(1).addAction(Actions.sequence(
                Actions.delay(delay),
                Actions.moveBy(0f, 41f + offset, 7f + durationOffset, Interpolation.fastSlow))
        );
        ground.get(2).addAction(Actions.sequence(
                Actions.delay(delay),
                Actions.moveBy(0f, 49f + offset, 9f + durationOffset, Interpolation.fastSlow))
        );
        ground.get(3).addAction(Actions.sequence(
                Actions.delay(delay),
                Actions.moveBy(0f, 58f + offset, 11f + durationOffset, Interpolation.fastSlow))
        );
        ground.get(4).addAction(Actions.sequence(
                Actions.delay(delay),
                Actions.moveBy(0f, 70f + offset, 13f + durationOffset, Interpolation.fastSlow))
        );

        planets.addAction(Actions.moveBy(0f, 5f + offset, 13f + durationOffset, Interpolation.fastSlow));
        stars.addAction(Actions.moveBy(0f, 2f + offset, 13f + durationOffset, Interpolation.fastSlow));

        label.addAction(Actions.sequence(
                Actions.delay(8f),
                Actions.run(() -> {
                    label.setText("{FADE}{SLOWER}Our world was forcibly reshaped\n in [dark yellow 3 black 2 apricot 1]their image[]. " +
                            "{WAIT=0.75}{HANG}Abolishing{ENDHANG} sapient life.");
                    label.restart();
                })
        ));
        label.addAction(Actions.sequence(
                Actions.delay(17f),
                Actions.run(() -> {
                    label.setText("{FADE}{SLOWER}{SHAKEFADE=1.2}we're all that's left{WAIT=0.75}\n" +
                            "and this{WAIT=0.75} is our {ENDSHAKEFADE}[dark dull red]last {WAIT=0.75}stand[].");
                    label.restart();
                })
        ));
    }
}
