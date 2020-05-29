package pgdp.color;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class DisplayColor {
    public static void displayColor(RgbColor8Bit c) {
        JFrame jf = new JFrame("Farbtest") {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue()));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        jf.setLocation(300, 300);
        jf.setSize(400, 350);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        RgbColor rgbColor = new RgbColor(10, 0b00_1111_1111, 0b00_0000_0000, 0b00_1010_0001);
        displayColor(rgbColor.toRgbColor8Bit());
    }

}
