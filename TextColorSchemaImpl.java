package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    
    private final char[] SYMBOLS = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        // color от 0 (черный) до 255 (белый)

        
        int index = color * (SYMBOLS.length - 1) / 255;

        return SYMBOLS[index];
    }

}
