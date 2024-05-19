package com.sort;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Splitting a PDF in to many using Java 
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * (Update from John)
 * This SplitPDF is extra file that wont used anymore, but i just keep it. 
 */
public class SplitPDF {
    public Map<String, String> splitPDF(File pdfFile)
    {
        // Hash map to store new split PDFs (each sub-list represents a section)
        Map<String, String> splitDocuments = new HashMap<>();

        try ( PDDocument document = PDDocument.load(pdfFile) ) {
            String idPattern = "([0-9]{8})";
            String splitText = "End of Record";

            // Temporary document to hold pages for current section
            PDDocument currentSection = new PDDocument();
            String currentID = null;

            PDFTextStripper stripper = new PDFTextStripper();

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                PDPage currentPage = document.getPage(i);
                stripper.setStartPage(i+1);
                stripper.setEndPage(i+1);
                
                String pageText = stripper.getText(document);
                Pattern idRegex = Pattern.compile(idPattern);
                Matcher idMatcher = idRegex.matcher(pageText);

                // Check if the id is found on the current page
                if (idMatcher.find()) {
                    currentID = idMatcher.group(0);

                    /*if ( !splitDocuments.containsKey(currentID) ) {
                        splitDocuments.put(currentID, new PDDocument());
                    }*/
                    
                    // Add the current page to the temporary section document
                    currentSection.addPage(currentPage);

                    //currentID = matchString;
                    
                    // Ensure we have a document for the current ID in the map
                    //splitDocuments.putIfAbsent(currentID, currentSection);
                }

                // Check if splitting criteria is found on the current page
                if ( pageText.contains(splitText) ) {
                    File newPdfFile = new File(pdfFile.getParentFile(), currentID + ".pdf");

                    currentSection.save(newPdfFile);
                    splitDocuments.put(currentID, newPdfFile.getAbsolutePath());

                    // Close the current section document
                    //currentSection.close();

                    // Create a new document for the next section
                    currentSection = new PDDocument();
                }
            }

            // Add any remaining pages to the last section
            /*if (currentID != null && !currentSection.isEmpty()) {
                splitDocuments.put(currentID, new ArrayList<>(currentSection));
            }*/

            //currentSection.close();

            System.out.println("Splitted Pdf based on text content (Improved).");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return splitDocuments;
    }
}