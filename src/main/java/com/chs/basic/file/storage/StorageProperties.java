package com.chs.basic.file.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class StorageProperties {

    @Value("${upload-dir}")
    private String location;

    @Value("${x-upload-dir}")
    private String xLocation;

    @Value("${x-width}")
    private int xWidth;

    @Value("${x-height}")
    private int xHeight;

    @Value("${xx-upload-dir}")
    private String xxLocation;

    @Value("${xx-width}")
    private int xxWidth;

    @Value("${xx-height}")
    private int xxHeight;

}
