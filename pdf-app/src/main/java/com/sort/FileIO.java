package com.sort;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileIO implements Configure {

    @Override
    public File select() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    @Override
    public void splitAndSave(File excelFile, File pdfFile, File saveFolder) {
        if (!pdfFile.exists() || !pdfFile.isFile()) {
            System.err.println("Error: The provided PDF file does not exist or is not a file.");
            return;
        }

        if (!saveFolder.exists() && !saveFolder.mkdirs()) {
            System.err.println("Error: Could not create the save folder.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis);
             PDDocument document = PDDocument.load(pdfFile)) {

            Sheet sheet = workbook.getSheetAt(0);

            String idPattern = "\\d{8}";
            String splitText = "End of Record";

            PDDocument currentSection = new PDDocument();
            String currentID = null;
            PDFTextStripper stripper = new PDFTextStripper();

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                PDPage currentPage = document.getPage(i);
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);

                String pageText = stripper.getText(document);
                Pattern idRegex = Pattern.compile(idPattern);
                Matcher idMatcher = idRegex.matcher(pageText);

                if (idMatcher.find()) {
                    currentID = idMatcher.group(0);
                    currentSection.addPage(currentPage);
                }

                if (pageText.contains(splitText) && currentID != null) {
                    saveCurrentSection(currentSection, saveFolder, currentID);
                    updateExcelWithLink(sheet, currentID, saveFolder);
                    currentSection = new PDDocument();
                    currentID = null;
                }
            }

            try (FileOutputStream fos = new FileOutputStream(excelFile)) {
                workbook.write(fos);
                System.out.println("Excel file updated with PDF links.");
            }

        } catch (IOException e) {
            System.err.println("Error processing the files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveCurrentSection(PDDocument currentSection, File saveFolder, String currentID) throws IOException {
        File newPdfFile = new File(saveFolder, currentID + ".pdf");
        currentSection.save(newPdfFile);
        currentSection.close();
    }

    private void updateExcelWithLink(Sheet sheet, String studentID, File saveFolder) throws IOException {
        for (Row row : sheet) {
            Cell idCell = row.getCell(3); // Assuming the student ID is in the 4th column (Perth ID)
            if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
                double idValue = idCell.getNumericCellValue();
                if (String.valueOf((int) idValue).equals(studentID)) {
                    Cell linkCell = row.createCell(6); // Assuming the link will be in the 7th column
                    CreationHelper createHelper = sheet.getWorkbook().getCreationHelper();
                    Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.FILE);
                    // Convert the file path to a URI
                    String filePath = new File(saveFolder, studentID + ".pdf").toURI().toString();
                    hyperlink.setAddress(filePath);
                    linkCell.setHyperlink(hyperlink);
                    linkCell.setCellValue("Link to PDF");
                }
            }
        }
    }
}
