package com.sort;

import java.io.File;

public class Main {
    private static Configure fileIO;

    public static void main(String[] args) {
        fileIO = new FileIO();

        // Prompt the user to select the Excel file
        System.out.println("Please select the Excel file:");
        File excelFile = fileIO.select();

        // Prompt the user to select the PDF file
        System.out.println("Please select the PDF file:");
        File pdfFile = fileIO.select();

        // Prompt the user to select the save folder
        System.out.println("Please select the folder to save the split PDFs:");
        File saveFolder = fileIO.select();

        // Check if all selections are valid
        if (excelFile != null && pdfFile != null && saveFolder != null) {
            fileIO.splitAndSave(excelFile, pdfFile, saveFolder);
        } else {
            System.err.println("Error: You must select all three files/folders (Excel, PDF, and save folder).");
        }
    }
}
