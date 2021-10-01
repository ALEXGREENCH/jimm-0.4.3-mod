package jimm.util;

import javax.microedition.lcdui.Graphics;

public class GreenchUtils {

    public static void drawGradientAndFrameBG(
            Graphics g,
            int x1,
            int y1,
            int x2,
            int y2,
            int color
    ){
        drawGradient(g, x1 + 1, y1 + 1, x2 - x1, y2 - y1 - 1, color, 16, -32, 0);
        g.setColor(transformColorLight(color, -48));
        g.drawLine(x1 + 1, y1, x2 - 1, y1);
        g.drawLine(x2, y1 + 1, x2, y2 - 1);
        g.drawLine(x2 - 1, y2, x1 + 1, y2);
        g.drawLine(x1, y2 - 1, x1, y1 + 1);
    }

    static void drawGradient(Graphics g, int x, int y, int w, int h, int color, int count, int light1, int light2) {
        for (int i = 0; i < count; i++) {
            g.setColor(transformColorLight(color, (light2 - light1) * i / (count - 1) + light1));
            int y1 = y + (i * h) / count;
            int y2 = y + (i * h + h) / count;
            g.fillRect(x, y1, w, y2 - y1);
        }
    }

    public static int transformColorLight(int color, int light) {
        int r = (color & 0xFF) + light;
        int g = ((color & 0xFF00) >> 8) + light;
        int b = ((color & 0xFF0000) >> 16) + light;
        if (r < 0) r = 0;
        if (r > 255) r = 255;
        if (g < 0) g = 0;
        if (g > 255) g = 255;
        if (b < 0) b = 0;
        if (b > 255) b = 255;
        return r | (g << 8) | (b << 16);
    }
}
