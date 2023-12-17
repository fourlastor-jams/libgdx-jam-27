package io.github.fourlastor.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import java.awt.Dimension;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    private static final float PERCENT_OF_SCREEN_SIZE = 0.7f;
    private static final float ASPECT_RATIO = 160f / 90f;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration.getDisplayMode();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Chain Gun Command");
        config.setHdpiMode(HdpiMode.Pixels);
        setWindowedMode(config);
        new Lwjgl3Application(GdxGame.createGame(), config);
    }

    private static void setWindowedMode(Lwjgl3ApplicationConfiguration config) {
        Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) (dimension.height * PERCENT_OF_SCREEN_SIZE);

        int width = (int) (height * ASPECT_RATIO);
        config.setWindowedMode(width, height);
    }
}
