/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;
import java.sql.SQLException; 
import javax.xml.bind.DatatypeConverter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sunpp.protocols.CCGeneratorProtocols;
//import sunpp.protocols.CCGeneratorProtocols.Options;
import sunpp.protocols.CCMsp;

/**
 *
 * @author dmitry
 */
public class RunApp {

    private ArrayList<CCMsp> m_msp_equips = new ArrayList<CCMsp>();
    private ArrayList<CCMsp> m_msp_equips_unit1 = new ArrayList<CCMsp>();
    private ArrayList<CCMsp> m_msp_equips_unit2 = new ArrayList<CCMsp>();
    private ArrayList<CCMsp> m_msp_equips_unit3 = new ArrayList<CCMsp>();
    private ArrayList<CCMsp> m_msp_equips_svo1 = new ArrayList<CCMsp>();
    private ArrayList<CCMsp> m_msp_equips_svo2 = new ArrayList<CCMsp>();
    private Iterator<Row> rowExcelIterator_vip_rem = null;
    private Iterator<Row> rowExcelIterator_msp = null;

    public static void main(String[] args) {
//        ArrayList<Object[]> arr_obj = null;
//        String s1[] = {"sa"};
//        Integer i1[] = {1, 2, 34};
//        arr_obj.add(s1);
//        arr_obj.add(i1);
//        
//        System.out.println(arr_obj);

        RunApp app = new RunApp();
        try {
            app.loadData_FromExcel("1.xlsx");
            app.loadData();
//            app.generateProtocols();

//            Double a = 2223.34500;
//            Double b = 4.00;
//            NumberFormat nf = new DecimalFormat("#.######");
//            System.out.println(nf.format(a));
//            System.out.println(nf.format(b));
            
//            app.compareFilesByHash("МСП.docx", "МСП1.docx");

        } catch (IOException ex) {
            Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param _pathFile_1
     * @param _pathFile_2 
     * 
     * @deprecated removed from java 9 and above
     */
    @Deprecated
    public void compareFilesByHash (String _pathFile_1, String _pathFile_2) {
        try {
            byte[] b = Files.readAllBytes(Paths.get(_pathFile_1));
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(b);
            String sHash_1 = DatatypeConverter.printHexBinary(hash);

            b = Files.readAllBytes(Paths.get(_pathFile_2));
            byte[] hash1 = MessageDigest.getInstance("SHA-256").digest(b);
            String sHash_2 = DatatypeConverter.printHexBinary(hash1);

            System.out.println(sHash_1.equalsIgnoreCase(sHash_2) ? "MATCH" : "NO MATCH");
        } catch (IOException ex) {
            Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
            Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     */
    public void RunApp() {
    }

    public void loadData() {
//        loadDataMsp();

        // drop header of table
        if (rowExcelIterator_vip_rem.hasNext()) rowExcelIterator_vip_rem.next();

//        ArrayList<CCMsp> pArrLstClMsp = null;
        
        int rowNum = 2;
        String pos           = null;
        String equip          = null;
        String type_repair   = null;
        String date          = null;
        String workers       = null;

        while (rowExcelIterator_vip_rem.hasNext()) {
            Row row = rowExcelIterator_vip_rem.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            pos         = null;
            equip       = null;
            type_repair = null;
            date        = null;
            workers     = null;

            if (cellIterator.hasNext()) pos         = (String) getValueFromExcelCell(cellIterator.next());
            if (cellIterator.hasNext()) equip       = (String) getValueFromExcelCell(cellIterator.next());
            if (cellIterator.hasNext()) type_repair = (String) getValueFromExcelCell(cellIterator.next());
            if (cellIterator.hasNext()) date        = (String) getValueDateFromExcelCell(cellIterator.next());
            if (cellIterator.hasNext()) workers     = (String) getValueFromExcelCell(cellIterator.next());
            
            switch (equip.toLowerCase()) {
                case "мсп":
                    CCMsp msp = getMeassureFromExcel_Msp(pos);
                    msp.m_type_repair = type_repair;
                    msp.m_repair_date = date;
                    msp.m_workers = workers;
                    
                    fillMSP (msp);

                    m_msp_equips.add(msp);
                    break;
            }
            
//            System.out.println();
            rowNum++;
        }
    }
    
    private void fillMSP (CCMsp _msp) {
        _msp.m_equip_number = "AD34";
        _msp.m_equip_year = 1993;
    }
    
    /**
     * 
     * @param _pos
     * @return
     */
    private CCMsp getMeassureFromExcel_Msp(String _pos) {
        CCMsp msp = new CCMsp();
        String pos = null;

        while (rowExcelIterator_msp.hasNext()) {
            Row row = rowExcelIterator_msp.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            if (cellIterator.hasNext()) { 
                pos = (String) getValueFromExcelCell(cellIterator.next());
                if(pos.equals(_pos)) {
                    msp.m_pos = pos.toString();
                    if (cellIterator.hasNext()) msp.m_res_ro_8_7 = (Double) getValueFromExcelCell(cellIterator.next());
                    if (cellIterator.hasNext()) msp.m_res_ro_8_9 = (Double) getValueFromExcelCell(cellIterator.next());
                    if (cellIterator.hasNext()) msp.m_u = (Double) getValueFromExcelCell(cellIterator.next());
                    if (cellIterator.hasNext()) msp.m_i = (Double) getValueFromExcelCell(cellIterator.next());
                    if (cellIterator.hasNext()) msp.m_p = (Double) getValueFromExcelCell(cellIterator.next());
                    return msp;
                }
            }
        }
        return null;
    }

    private void loadDataMsp() {
        // drop header of table
        if (rowExcelIterator_msp.hasNext()) rowExcelIterator_msp.next();
        
        ArrayList<CCMsp> pArrLstClMsp = null;

        while (rowExcelIterator_msp.hasNext()) {
            Row row = rowExcelIterator_msp.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            CCMsp msp = new CCMsp();

            pArrLstClMsp = null;

            if (cellIterator.hasNext()) {
                msp.m_pos = (String) getValueFromExcelCell(cellIterator.next());
                if (msp.m_pos.startsWith("1"))          pArrLstClMsp = m_msp_equips_unit1;
                else if (msp.m_pos.startsWith("2"))     pArrLstClMsp = m_msp_equips_unit2;
                else if (msp.m_pos.startsWith("3"))     pArrLstClMsp = m_msp_equips_unit3;
                else if (msp.m_pos.startsWith("(1)"))   pArrLstClMsp = m_msp_equips_svo1;
                else if (msp.m_pos.startsWith("(2)"))   pArrLstClMsp = m_msp_equips_svo2;
            }
            if (cellIterator.hasNext()) msp.m_res_ro_8_7 = (Double) getValueFromExcelCell(cellIterator.next());
            if (cellIterator.hasNext()) msp.m_res_ro_8_9 = (Double) getValueFromExcelCell(cellIterator.next());
            if (cellIterator.hasNext()) msp.m_u = (Double) getValueFromExcelCell(cellIterator.next());
            if (cellIterator.hasNext()) msp.m_i = (Double) getValueFromExcelCell(cellIterator.next());
            if (cellIterator.hasNext()) msp.m_p = (Double) getValueFromExcelCell(cellIterator.next());

//            m_msp_equips_unit1.add(msp);
//
//            Class yourClass = YourClass.class
//            Field[] fields = msp.getFields();
//            for (Field f : msp.getClass().getFields()) {
//                System.out.println(f.getName());
//            }
//
            pArrLstClMsp.add(msp);
        }
    }

    /**
     * @param cell
     * @return Object from cell or null
     */
    public Object getValueFromExcelCell(Cell cell) {
        Object val = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        val = cell.getNumericCellValue();
                        break;
                    case Cell.CELL_TYPE_STRING:
                        val = cell.getRichStringCellValue();
                        break;
                }
                break;

            case Cell.CELL_TYPE_NUMERIC:
                val = cell.getNumericCellValue();
                break;

            case Cell.CELL_TYPE_STRING:
                val = cell.getStringCellValue();
                break;
        }

        return val;
    }
    
    /**
     * 
     * @param cell
     * @return String-value formatted from cell or null
     */
    public String getValueDateFromExcelCell(Cell cell) {
        String val = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                val = cell.getStringCellValue();
                break;
                
            case Cell.CELL_TYPE_NUMERIC:
                Date date = cell.getDateCellValue();
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                cal.setTime(date);

                String dayStr = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
                String monthStr = String.format("%02d", (cal.get(Calendar.MONTH) + 1));
                String yearStr = String.format("%d", cal.get(Calendar.YEAR)).substring(2);

                val = dayStr + "." + monthStr + "." + yearStr;
                break;
        }
        
        return val;
    }

    public void loadData_FromExcel(String _nameFile) throws IOException {
        FileInputStream fis = null;
        try {
            File myFile = new File(_nameFile);
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

            rowExcelIterator_vip_rem = myWorkBook.getSheet("Выполненный ремонт").iterator();
//            rowExcelIterator_ved_got = myWorkBook.getSheet("Ведомость готовности").iterator();
//            rowExcelIterator_sit = myWorkBook.getSheet("СИТ").iterator();
            rowExcelIterator_msp = myWorkBook.getSheet("Измерения МСП").iterator();
//            rowExcelIterator_meo = myWorkBook.getSheet("Измерения МЭО").iterator();
//            rowExcelIterator_el_mot = myWorkBook.getSheet("Измерения Электромоторы").iterator();
//            rowExcelIterator_u_22 = myWorkBook.getSheet("Измерения У-22").iterator();
//            rowExcelIterator_u_23 = myWorkBook.getSheet("Измерения У-23").iterator();
//            rowExcelIterator_u_24 = myWorkBook.getSheet("Измерения У-24").iterator();
//            rowExcelIterator_pbr_2 = myWorkBook.getSheet("Измерения ПБР-2").iterator();
//            rowExcelIterator_pbr_3 = myWorkBook.getSheet("Измерения ПБР-3").iterator();
//            rowExcelIterator_ap_50 = myWorkBook.getSheet("Измерения АП-50").iterator();

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

    public void generateProtocols() {
        CCGeneratorProtocols gen = new CCGeneratorProtocols();
        CCGeneratorProtocols.Options opt = new CCGeneratorProtocols().new Options();

        opt.isUnit1 = true;
        gen.generateMSP("out/", m_msp_equips, opt);
//        gen.generateMSP("out/unit_1", m_msp_equips_unit1, opt);
//        gen.generateMSP("out/unit_2", m_msp_equips_unit2, opt);
        opt.reset();
    }

    public void readXLSX(String _nameFile) throws IOException {
        FileInputStream fis = null;
        try {
            File myFile = new File(_nameFile);
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
            XSSFSheet mySheet = myWorkBook.getSheet("СИТ");

            Iterator<Row> rowIterator = mySheet.iterator();

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
}
