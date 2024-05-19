package com.sort;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

public class FileIO implements Configure{

    @Override
    public File select() {
        // Create a JFileChooser instance
        JFileChooser fileChooser = new JFileChooser();
        
        // Set the file chooser to select File or Folder
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // Show the file chooser dialog
        int result = fileChooser.showOpenDialog(null);

        // Check if a folder was selected
        if (result == JFileChooser.APPROVE_OPTION) {
            // Return the selected file or folder
            return fileChooser.getSelectedFile();
        } else {
            // Return null if no file or folder was selected
            return null;
        }
    }

    @Override
    public void splitAndSave(File pdfFile, File saveFolder) {
        // Check if the provided PDF file exists and is a valid file
        if (!pdfFile.exists() || !pdfFile.isFile()) {
            System.err.println("Error: The provided PDF file does not exist or is not a file.");
            return;
        }

        // Check if the save folder exists, if not, try to create it
        if (!saveFolder.exists() && !saveFolder.mkdirs()) {
            System.err.println("Error: Could not create the save folder.");
            return;
        }

        try ( PDDocument document = PDDocument.load(pdfFile) ) {
            String idPattern = "([0-9]{8})";        // Regular expression to match an 8-digit ID
            String splitText = "End of Record";     // Text to indicate the end of a section

            PDDocument currentSection = new PDDocument();   // Temporary document to hold pages for current section
            String currentID = null;

            PDFTextStripper stripper = new PDFTextStripper();   // PDFTextStripper to extract text from the PDF

            // Iterate over all pages in the document
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                PDPage currentPage = document.getPage(i);
                stripper.setStartPage(i+1);
                stripper.setEndPage(i+1);
                
                String pageText = stripper.getText(document);   // Extract text from the current page
                Pattern idRegex = Pattern.compile(idPattern);   // Compile the regular expression
                Matcher idMatcher = idRegex.matcher(pageText);  // Match the regular expression against the page text

                // Check if the id is found on the current page
                if (idMatcher.find()) {
                    currentID = idMatcher.group(0);
                    
                    // Add the current page to the temporary section document
                    currentSection.addPage(currentPage);
                }

                // Check if splitting criteria is found on the current page
                if ( pageText.contains(splitText) && currentID != null ) {
                    saveCurrentSection(currentSection, saveFolder, currentID);

                    // Create a new document for the next section
                    currentSection = new PDDocument();
                    currentID = null;
                }
            }

            System.out.println("Successfully splitted and saveed PDF file into following folder based on text content.");
        }
        catch (IOException e) {
            System.err.println("Error processing the PDF document: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCurrentSection(PDDocument currentSection, File saveFolder, String currentID) throws IOException {
        // Create a new directory within the save folder using the current ID
        File newPDFDirectory = new File(saveFolder, currentID);

        if (!newPDFDirectory.exists() && !newPDFDirectory.mkdirs()) {
            throw new IOException("Could not create directory: " + newPDFDirectory.getAbsolutePath());
        }
        else {
            File newPdfFile = new File(newPDFDirectory, currentID + ".pdf");
            currentSection.save(newPdfFile);
            currentSection.close();
        }
    }
}
