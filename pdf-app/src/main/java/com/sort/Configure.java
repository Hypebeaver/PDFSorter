package com.sort;
import java.io.File;

public interface Configure {
    File select();
    void splitAndSave(File pdfFile, File saveFolder);
}
