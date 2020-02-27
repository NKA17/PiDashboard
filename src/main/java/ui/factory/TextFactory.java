package ui.factory;

import ui.config.Configuration;

import javax.swing.*;

public class TextFactory {
    public static JLabel createJLabel(String text){
        JLabel label = new JLabel(text);
        label.setBackground(null);
        label.setForeground(Configuration.TEXT_COLOR);
        label.setFont(Configuration.TEXT_FONT);

        return label;
    }
}
