package com.sort;

import java.io.File;

public class Main {
    private static Configure fileIO;

    public static void main(String[] args) {
        fileIO = new FileIO();

        File selectedPDF = fileIO.select();
        File saveFolder = fileIO.select();

        fileIO.splitAndSave(selectedPDF, saveFolder);
    }
}
