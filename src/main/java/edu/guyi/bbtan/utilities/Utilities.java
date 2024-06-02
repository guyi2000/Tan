package edu.guyi.bbtan.utilities;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Utilities {
    public static final Font MonospaceBasic = new Font(Font.MONOSPACED, Font.BOLD, 18);
    public static Font GameOverFont;
    static {
        try {
            GameOverFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    Objects.requireNonNull(
                            Utilities.class.getResourceAsStream("/font/game-over-1.ttf")
                    )
            ).deriveFont(96f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public static final Random rand = new Random();

    public static boolean Property(double x) {
        return rand.nextDouble() < x;
    }
}
