package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private double maxRatio;
    private int maxWidth;
    private int maxHeight;
    private TextColorSchema schema;

    public TextGraphicsConverterImpl() {
   
        this.schema = new TextColorSchemaImpl();
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url));

        if (img == null) {
            throw new IOException("Не удалось загрузить изображение");
        }

  
        if (maxRatio > 0) {
            double ratio = (double) Math.max(img.getWidth(), img.getHeight()) /
                    Math.min(img.getWidth(), img.getHeight());
            if (ratio > maxRatio) {
                throw new BadImageSizeException(ratio, maxRatio);
            }
        }

  
        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();

        int newWidth = originalWidth;
        int newHeight = originalHeight;

        if (maxWidth > 0 && maxHeight > 0) {
   
            double widthRatio = (double) maxWidth / originalWidth;
            double heightRatio = (double) maxHeight / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            if (ratio < 1.0) {
                newWidth = (int) (originalWidth * ratio);
                newHeight = (int) (originalHeight * ratio);
            }
        } else {

            if (maxWidth > 0 && originalWidth > maxWidth) {
                newWidth = maxWidth;
                newHeight = (int) Math.round(originalHeight * ((double) maxWidth / originalWidth));
            }

            if (maxHeight > 0 && newHeight > maxHeight) {
                newHeight = maxHeight;
                newWidth = (int) Math.round(newWidth * ((double) maxHeight / newHeight));
            }
        }


        newWidth = Math.max(newWidth, 1);
        newHeight = Math.max(newHeight, 1);

   
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);


        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();


        WritableRaster bwRaster = bwImg.getRaster();


        StringBuilder result = new StringBuilder();
        int[] pixel = new int[3];

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, pixel)[0];
                char c = schema.convert(color);
  
                result.append(c).append(c);
            }
            result.append("\n");
        }

        return result.toString();
    }

}
