package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    // Символы от самых темных к самым светлым
    private final char[] SYMBOLS = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        // color от 0 (черный) до 255 (белый)

        // Вычисляем индекс в массиве символов
        // Для черного (0): 0 * 8 / 255 = 0 -> SYMBOLS[0] = '▇'
        // Для белого (255): 255 * 8 / 255 = 8 -> SYMBOLS[8] = '-'
        int index = color * (SYMBOLS.length - 1) / 255;

        return SYMBOLS[index];
    }
}