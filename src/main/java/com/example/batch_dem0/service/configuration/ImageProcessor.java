package com.example.batch_dem0.service.configuration;

import com.example.batch_dem0.model.Author;
import com.example.batch_dem0.model.Image;
import org.springframework.batch.item.ItemProcessor;

public class ImageProcessor implements ItemProcessor<Image, Image> {

    @Override
    public Image process(Image image) throws Exception {
        image.setImageStatus("Empty");
        return image;
    }
}
