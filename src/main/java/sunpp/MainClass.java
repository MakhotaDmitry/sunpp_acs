/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

/**
 *
 * @author dmitry
 */
public class MainClass {

    private final String driverName = "org.sqlite.JDBC";
    private final String connectionString = "jdbc:sqlite:db.sqlite";

    public void readXLSX(String _nameFile) throws IOException {
        FileInputStream fis = null;
        try {
            File myFile = new File(_nameFile);
            fis = new FileInputStream(myFile);
            // Finds the workbook instance for XLSX file
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            // Return first sheet from the XLSX workbook
//            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
            XSSFSheet mySheet = myWorkBook.getSheet("СИТ");
            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
            // Traversing over each row of XLSX file
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                if (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        String title = cell.getStringCellValue();
                        System.out.println("title: " + title);
                    }
                }

//                if (cellIterator.hasNext()) // drop type
//                    cellIterator.next();
                if (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        String type = cell.getStringCellValue();
                        System.out.println("type: " + type);
                    }
                }

                if (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        String num = cell.getStringCellValue();
                        System.out.println("num: " + num);
                    } else {
                        System.out.println(row.getCell(2).getCellType());
                    }
                }

                if (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        String date = cell.getStringCellValue();
                        System.out.println("date: " + date);
                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        Date date = cell.getDateCellValue();
//                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kiev"));
                        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                        cal.setTime(date);
                        System.out.println("date: " + cal.get(Calendar.MONTH));
                    }
                }
                System.out.println();
            }

            myWorkBook.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    public void generateDOCX(String _nameFile) {
//        XWPFDocument document = new XWPFDocument();
//
//        XWPFParagraph title = document.createParagraph();
//        title.setAlignment(ParagraphAlignment.CENTER);
//
//        XWPFRun titleRun = title.createRun();
//        titleRun.setText("Build Your REST API with Spring");
//        titleRun.setColor("009933");
//        titleRun.setBold(true);
//        titleRun.setFontFamily("Courier");
//        titleRun.setFontSize(20);
//
//        FileOutputStream out = new FileOutputStream(output);
//        document.write(out);
//        out.close();
//        document.close();
//    }
    
    public void getTextFromDocx() throws FileNotFoundException, IOException {
        XWPFDocument docx = new XWPFDocument(new FileInputStream("МСП.docx"));

        XWPFWordExtractor we = new XWPFWordExtractor(docx);
        System.out.println(we.getText());
    }
    
    public void testCopyDOCX() {
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream("data/templates/МСП.docx"));
            XWPFDocument doc_out = new XWPFDocument(new FileInputStream("МСП.docx"));

            Iterator<XWPFParagraph> paras = doc.getParagraphsIterator();
            Iterator<XWPFTable> tables = doc.getTablesIterator();

            Iterator<IBodyElement> bodyIterator = doc.getBodyElementsIterator();
            while (bodyIterator.hasNext()) {
                IBodyElement el = bodyIterator.next();
                Class<?> ss = el.getClass();
                if (ss.getName().equals("org.apache.poi.xwpf.usermodel.XWPFParagraph")) {
                    XWPFParagraph par = paras.next();
                    XWPFParagraph par_out_doc = doc_out.createParagraph();
                    copyAllRunsToAnotherParagraph(par, par_out_doc);
                    
//                    System.out.println(par.getText());
                } /*else if (ss.getName().equals("org.apache.poi.xwpf.usermodel.XWPFTable")) {
                    XWPFTable table = tables.next();

//                    XWPFTable table_1 = doc.createTable();
//                    doc.setTable(1, table_1);
                    for (int i = 0; i < 100; i++) {
                        CTTbl ctTbl = CTTbl.Factory.newInstance(); // Create a new CTTbl for the new table

                        XWPFParagraph paragraph = doc_out.createParagraph();
                        paragraph.setPageBreak(true);
                        XWPFParagraph para = doc_out.createParagraph();
                        XWPFRun run = para.createRun();
                        run.setText("line " + i);
                        doc_out.createTable();

                        XWPFTable table1 = table;
                        ctTbl.set(table1.getCTTbl()); // Copy the template table's CTTbl
                        XWPFTable table2 = new XWPFTable(ctTbl, doc_out); // Create a new table using the CTTbl upon
                        doc_out.setTable(i, table2);
                    }

                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph p : cell.getParagraphs()) {
                                for (XWPFRun r : p.getRuns()) {
                                    String text = r.getText(0);
                                    if (text != null && text.contains("{{1_1}}")) {
                                        text = text.replace("{{1_1}}", "haystack");
                                        r.setText(text, 0);
                                    }
                                }
                            }
                        }
                    }
//                    System.out.println(table.getText());
                }*/
            }
            doc_out.write(new FileOutputStream("output.docx"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void copyAllRunsToAnotherParagraph(XWPFParagraph oldPar, XWPFParagraph newPar) {
        final int DEFAULT_FONT_SIZE = 10;

        for (XWPFRun run : oldPar.getRuns()) {
            String textInRun = run.getText(0);
            if (textInRun == null || textInRun.isEmpty()) {
                continue;
            }

            int fontSize = run.getFontSize();
//            System.out.println("run text = '" + textInRun + "' , fontSize = " + fontSize);

            XWPFRun newRun = newPar.createRun();

            // Copy text
            newRun.setText(textInRun);

            // Apply the same style
            newRun.setFontSize((fontSize == -1) ? DEFAULT_FONT_SIZE : run.getFontSize());
            newRun.setFontFamily(run.getFontFamily());
            newRun.setBold(run.isBold());
            newRun.setItalic(run.isItalic());
            newRun.setStrike(run.isStrike());
            newRun.setColor(run.getColor());
//            newRun.setKerning(1);
        }
    }

    public void testCopyDOCX_old_1() {
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream("data/templates/МСП.docx"));
            XWPFDocument doc_out = new XWPFDocument(new FileInputStream("МСП.docx"));

//            List<XWPFParagraph> paras = doc.getParagraphs();
            Iterator<XWPFParagraph> paras = doc.getParagraphsIterator();
            Iterator<XWPFTable> tables = doc.getTablesIterator();
//            System.out.print(doc.getBodyElements());

            Iterator<IBodyElement> bodyIterator = doc.getBodyElementsIterator();
            while (bodyIterator.hasNext()) {
                IBodyElement el = bodyIterator.next();
                Class<?> ss = el.getClass();
                if (ss.getName().equals("org.apache.poi.xwpf.usermodel.XWPFParagraph")) {
                    XWPFParagraph par = paras.next();
//                    System.out.println(par.getText());
                } else if (ss.getName().equals("org.apache.poi.xwpf.usermodel.XWPFTable")) {
                    XWPFTable table = tables.next();

//                    XWPFTable table_1 = doc.createTable();
//                    doc.setTable(1, table_1);
                    for (int i = 0; i < 100; i++) {
                        CTTbl ctTbl = CTTbl.Factory.newInstance(); // Create a new CTTbl for the new table

                        XWPFParagraph paragraph = doc_out.createParagraph();
                        paragraph.setPageBreak(true);
                        XWPFParagraph para = doc_out.createParagraph();
                        XWPFRun run = para.createRun();
                        run.setText("line " + i);
                        doc_out.createTable();

                        XWPFTable table1 = table;
                        ctTbl.set(table1.getCTTbl()); // Copy the template table's CTTbl
                        XWPFTable table2 = new XWPFTable(ctTbl, doc_out); // Create a new table using the CTTbl upon
                        doc_out.setTable(i, table2);
                    }

                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph p : cell.getParagraphs()) {
                                for (XWPFRun r : p.getRuns()) {
                                    String text = r.getText(0);
                                    if (text != null && text.contains("{{1_1}}")) {
                                        text = text.replace("{{1_1}}", "haystack");
                                        r.setText(text, 0);
                                    }
                                }
                            }
                        }
                    }

//                    for (XWPFTable tbl : doc.getTables()) {
//                        for (XWPFTableRow row : tbl.getRows()) {
//                            for (XWPFTableCell cell : row.getTableCells()) {
//                                for (XWPFParagraph p : cell.getParagraphs()) {
//                                    for (XWPFRun r : p.getRuns()) {
//                                        String text = r.getText(0);
//                                        if (text != null && text.contains("needle")) {
//                                            text = text.replace("needle", "haystack");
//                                            r.setText(text, 0);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                    System.out.println(table.getText());
                }

//                for (XWPFRun run : el.getRuns()) {
//                    String textInRun = run.getText(0);
//                    if (textInRun == null || textInRun.isEmpty()) {
//                        continue;
//                    }
//                }
//                System.out.println(ss.getName());
            }
//            CTTbl ctTbl = CTTbl.Factory.newInstance(); // Create a new CTTbl for the new table
//            XWPFTable table = doc.getTables().get(0);
//            ctTbl.set(table.getCTTbl()); // Copy the template table's CTTbl
//            XWPFTable table2 = new XWPFTable(ctTbl, doc); // Create a new table using the CTTbl upon
//            doc.setTable(1, table2);

//            for (int i = 0; i < 100; i++) {
//                CTTbl ctTbl = CTTbl.Factory.newInstance(); // Create a new CTTbl for the new table
//
//                XWPFParagraph paragraph = doc.createParagraph();
//                paragraph.setPageBreak(true);
//                XWPFParagraph para = doc.createParagraph();
//                XWPFRun run = para.createRun();
//                run.setText("line " + i);
//                XWPFTable table = doc.createTable();
//
//                XWPFTable table1 = doc.getTables().get(0);
//                ctTbl.set(table1.getCTTbl()); // Copy the template table's CTTbl
//                XWPFTable table2 = new XWPFTable(ctTbl, doc); // Create a new table using the CTTbl upon
//                doc.setTable(i + 2, table2);
//            }
            doc_out.write(new FileOutputStream("output.docx"));

//            List<XWPFTable> tables = doc.getTables();
//            System.out.println(tables);
//            XWPFDocument newdoc = new XWPFDocument();
//            for (XWPFParagraph para : paras) {
//
//                if (!para.getParagraphText().isEmpty()) {
//                    XWPFParagraph newpara = newdoc.createParagraph();
//                    copyAllRunsToAnotherParagraph(para, newpara);
//                }
//            }
//            FileOutputStream fos = new FileOutputStream(new File("newJapan.docx"));
//            newdoc.write(fos);
//            fos.flush();
//            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        MainClass app = new MainClass();
//        try {
//            app.readXLSX("1.xlsx");
//        } catch (IOException ex) {
//            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
//        }

        //        app.dbTesting();
//        app.testCopyDOCX();
        CCMsWordTemplate word = new CCMsWordTemplate("data/templates/МСП.docx");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("{{1_1}}", "val 1");
        map.put("{{1_2}}", "val 2");
        word.fillBodyByTemplate(map);
        map.put("{{1_1}}", "val > 3");
        map.put("{{1_2}}", "val < 4");
        map.put("{{sit_name_1}}", "bspt");
        word.fillBodyByTemplate(map);
        word.save("out111/1.docx");
        
//        try {
//            app.getTextFromDocx();
//        } catch (IOException ex) {
//            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
//        }
        System.out.println("end app");
    }
}
