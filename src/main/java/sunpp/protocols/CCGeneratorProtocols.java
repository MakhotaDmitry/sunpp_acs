/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp.protocols;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;

/**
 *
 * @author dmitry
 */
public class CCGeneratorProtocols {

    private final String m_path_template_ap_50 = "data/templates/АП-50.docx";
    private final String m_path_template_ar_privod = "data/templates/АР_Привод.docx";
    private final String m_path_template_du_privod = "data/templates/ДУ_Привод.docx";
    private final String m_path_template_msp = "data/templates/МСП.docx";
    private final String m_path_template_meo = "data/templates/МЭО.docx";
    private final String m_path_template_pbr_2 = "data/templates/ПБР-2.docx";
    private final String m_path_template_pbr_3 = "data/templates/ПБР-3.docx";
    private final String m_path_template_res_contr_cabel = "data/templates/Сопротивление_Изоляции_Контр_Кабеля.docx";
    private final String m_path_template_u22 = "data/templates/У22.docx";
    private final String m_path_template_u23 = "data/templates/У23.docx";
    private final String m_path_template_u24 = "data/templates/У24.docx";
    private final String m_path_template_electric_motor = "data/templates/Электродвигатели.docx";
    private final String m_path_template_elch_privod = "data/templates/ЭЧ_Привод.docx";
//    public Options m_options = null;

    public void CCGeneratorProtocols() {
    }

    public void generateMSP(String _out_dir, ArrayList<CCMsp> _equipments, Options _options) {
        int quant_records_per_page = 4;

        HashMap<String, String> templ = new HashMap<String, String>();
        templ.put("{{1_1}}", String.valueOf(10));
        templ.put("{{1_2}}", String.valueOf(123.14));
        System.out.println(templ);
        
        generateDOCX(templ);

//        for (CCMsp equip : _equipments) {
//            System.out.println(equip);
////            equip.printAll();
////            System.out.println(equip.getClass());
////            System.out.println(equip.m_number);
////            System.out.println(equip.m_year);
////            System.out.println(equip.m_pos);
////            System.out.println(equip.m_type_repair);
////            System.out.println(equip.m_res_ro_8_7);
////            System.out.println(equip.m_res_ro_8_9);
////            System.out.println(equip.m_u);
////            System.out.println(equip.m_i);
////            System.out.println(equip.m_p);
////            System.out.println(equip.m_date);
////            System.out.println(equip.m_man);
////            System.out.println();
//        }
    }
    
    public void generateDOCX(HashMap<String, String> _patternFill) {
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(m_path_template_msp));
            XWPFDocument doc_out = new XWPFDocument(new FileInputStream(m_path_template_msp));

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
                    System.out.println(par.getText());
                } else if (ss.getName().equals("org.apache.poi.xwpf.usermodel.XWPFTable")) {
                    XWPFTable table = tables.next();

//                    XWPFTable table_1 = doc.createTable();
//                    doc.setTable(1, table_1);
//                    for (int i = 0; i < 100; i++) {
//                        CTTbl ctTbl = CTTbl.Factory.newInstance(); // Create a new CTTbl for the new table
//
//                        XWPFParagraph paragraph = doc_out.createParagraph();
//                        paragraph.setPageBreak(true);
//                        XWPFParagraph para = doc_out.createParagraph();
//                        XWPFRun run = para.createRun();
//                        run.setText("line " + i);
//                        doc_out.createTable();
//
//                        XWPFTable table1 = table;
//                        ctTbl.set(table1.getCTTbl()); // Copy the template table's CTTbl
//                        XWPFTable table2 = new XWPFTable(ctTbl, doc_out); // Create a new table using the CTTbl upon
//                        doc_out.setTable(i, table2);
//                    }

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
//                    System.out.println(table.getText());
                }

//                for (XWPFRun run : el.getRuns()) {
//                    String textInRun = run.getText(0);
//                    if (textInRun == null || textInRun.isEmpty()) {
//                        continue;
//                    }
//                }
//                System.out.println(ss.getName());
            }
            paras = doc.getParagraphsIterator();
            tables = doc.getTablesIterator();
            bodyIterator = doc.getBodyElementsIterator();
            while (bodyIterator.hasNext()) {
                IBodyElement el = bodyIterator.next();
                Class<?> ss = el.getClass();
                if (ss.getName().equals("org.apache.poi.xwpf.usermodel.XWPFParagraph")) {
                    XWPFParagraph par = paras.next();
                    System.out.println(par.getText());
                } else if (ss.getName().equals("org.apache.poi.xwpf.usermodel.XWPFTable")) {
                    XWPFTable table = tables.next();

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
                }
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

    public class Options {

        public boolean isUnit1 = false;
        public boolean isUnit2 = false;
        public boolean isUnit3 = false;
        public boolean isSVO1 = false;
        public boolean isSVO2 = false;

        public void Options() {
        }

        public void reset() {
            isUnit1 = false;
            isUnit2 = false;
            isUnit3 = false;
            isSVO1 = false;
            isSVO2 = false;
        }
    }
}
