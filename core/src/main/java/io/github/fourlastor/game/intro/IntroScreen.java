package io.github.fourlastor.game.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TypingLabel;

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

    @Inject
    public IntroScreen(
            InputMultiplexer inputMultiplexer,
            AssetManager assetManager,
            TextureAtlas textureAtlas
    ) {
        this.inputMultiplexer = inputMultiplexer;
        this.textureAtlas = textureAtlas;
        this.assetManager = assetManager;

        viewport = new FitViewport(160f, 90f);
        stage = new Stage(viewport);
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
        BitmapFont font = assetManager.get("fonts/play-16.fnt");
        label = new TypingLabel("lorem ipsum", new Font(font));
        Table table = new Table();
        table.add(label);
        table.setFillParent(true);
        stage.addActor(table);

        stars = new Image(textureAtlas.findRegion("background/stars"));
        stars.setY(-20f);
        stage.addActor(stars);
        planets = new Image(textureAtlas.findRegion("background/planets"));
        planets.setY(-30f);
        stage.addActor(planets);

        ground = new Array<>();
        for (int i = 5; i >= 1; i--) {
            Image image = new Image(textureAtlas.findRegion("ground/layer" + i));
            image.setName("layer " + (i - 1));
            ground.add(image);
        }

        ground.get(4).setY(-30f); // foreground
        ground.get(3).setY(-17f);
        ground.get(2).setY(-8f);
        ground.get(1).setY(0f);
        ground.get(0).setY(10f); // background

        for (Image layer : ground)
            layer.moveBy(0f, -66f);

        for (Image image : ground)
            stage.addActor(image);

        /*
                cityShields
        cities
                turrets*/
    }

    private void startAnimation() {
        /*for (int i = 0; i < ground.size; i++) {
            float amountY = 66f - 19 * i;
            float duration = 10f - .2f * i;
            System.out.println(i + ", name: " + ground.get(i).getName() + ", amountY: " + amountY + ", duration: " + duration);
            ground.get(i).addAction(
                    Actions.moveBy(0f, amountY, duration, Interpolation.circleOut)
            );
        }*/

        float totalDuration = 13f;

        ground.get(0).addAction(Actions.moveBy(0f, 31f, 5f, Interpolation.fastSlow));
        ground.get(1).addAction(Actions.moveBy(0f, 41f, 7f, Interpolation.fastSlow));
        ground.get(2).addAction(Actions.moveBy(0f, 49f, 9f, Interpolation.fastSlow));
        ground.get(3).addAction(Actions.moveBy(0f, 58f, 11f, Interpolation.fastSlow));
        ground.get(4).addAction(Actions.moveBy(0f, 70f, 13f, Interpolation.fastSlow));

        planets.addAction(Actions.moveBy(0f, 5f, 9f, Interpolation.fastSlow));
        stars.addAction(Actions.moveBy(0f, 2f, 9f, Interpolation.fastSlow));
    }
}
