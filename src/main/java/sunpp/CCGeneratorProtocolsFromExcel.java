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
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import sunpp.models.db.EquipmentParams;
import sunpp.models.db.Equipments;
import sunpp.models.db.Positions;
import sunpp.models.protocols.CCMsp;
import sunpp.models.protocols.CCSit;

/**
 *
 * @author dmitry
 */
public class CCGeneratorProtocolsFromExcel {

    private ArrayList<CCMsp> msp_equips = new ArrayList<CCMsp>();
    private ArrayList<CCSit> sit_equips = new ArrayList<CCSit>();

    private Iterator<Row> rowExcelIterator_vip_rem = null;
//    private Iterator<Row> rowExcelIterator_ved_got = null;
    private Iterator<Row> rowExcelIterator_sit = null;
    private Iterator<Row> rowExcelIterator_msp = null;
//    private Iterator<Row> rowExcelIterator_meo = null;
//    private Iterator<Row> rowExcelIterator_el_mot = null;
//    private Iterator<Row> rowExcelIterator_u_22 = null;
//    private Iterator<Row> rowExcelIterator_u_23 = null;
//    private Iterator<Row> rowExcelIterator_u_24 = null;
//    private Iterator<Row> rowExcelIterator_pbr_2 = null;
//    private Iterator<Row> rowExcelIterator_pbr_3 = null;
//    private Iterator<Row> rowExcelIterator_ap_50 = null;

    private String excelName = null;
    private String outDir = null;

    /**
     * 
     * @param excelName
     * @param outDir 
     */
    public CCGeneratorProtocolsFromExcel(String excelName, String outDir) {
        this.excelName = excelName;
        this.outDir = outDir;
        
        Base.open("org.sqlite.JDBC", "jdbc:sqlite:data/acs.sqlite", "", "");
    }

    /**
     *
     */
    public void generate() {
        //        ArrayList<Object[]> arr_obj = null;
//        String s1[] = {"sa"};
//        Integer i1[] = {1, 2, 34};
//        arr_obj.add(s1);
//        arr_obj.add(i1);
//        
//        System.out.println(arr_obj);

        loadData();

        CCGeneratorProtocols gen = new CCGeneratorProtocols(this.outDir);
        gen.setSits(this.sit_equips);
        gen.generateMSP(this.msp_equips);

//            Double a = 2223.34500;
//            Double b = 4.00;
//            NumberFormat nf = new DecimalFormat("#.######");
//            System.out.println(nf.format(a));
//            System.out.println(nf.format(b));
//            app.compareFilesByHash("МСП.docx", "МСП1.docx");
    }

    /**
     * 
     * @param _pathFile_1
     * @param _pathFile_2 
     * 
     * @deprecated removed from java 9 and above
     */
    @Deprecated
    private void compareFilesByHash (String _pathFile_1, String _pathFile_2) {
        try {
            byte[] b = Files.readAllBytes(Paths.get(_pathFile_1));
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(b);
            String sHash_1 = DatatypeConverter.printHexBinary(hash);

            b = Files.readAllBytes(Paths.get(_pathFile_2));
            byte[] hash1 = MessageDigest.getInstance("SHA-256").digest(b);
            String sHash_2 = DatatypeConverter.printHexBinary(hash1);

            System.out.println(sHash_1.equalsIgnoreCase(sHash_2) ? "MATCH" : "NO MATCH");
        } catch (IOException ex) {
            Logger.getLogger(CCGeneratorProtocolsFromExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
            Logger.getLogger(CCGeneratorProtocolsFromExcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() {
        FileInputStream fis = null;
        
        try {
            //        loadDataMsp();
            File myFile = new File(this.excelName);
            fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

            rowExcelIterator_vip_rem = myWorkBook.getSheet("Выполненный ремонт").iterator();
//            rowExcelIterator_ved_got = myWorkBook.getSheet("Ведомость готовности").iterator();
            rowExcelIterator_sit = myWorkBook.getSheet("СИТ").iterator();
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
        } catch (IOException ex) {
            Logger.getLogger(CCGeneratorProtocolsFromExcel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        loadFromExcel_Sit();

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
            
            switch (equip.toLowerCase().trim()) {
                case "мсп":
                    CCMsp msp = getMeassureFromExcel_Msp(pos);
                    msp.m_type_repair = type_repair;
                    msp.m_repair_date = date;
                    msp.m_workers = workers;
                    
//                    System.out.println(date);
                    
                    fillMSP (msp);

                    msp_equips.add(msp);
                    break;

//                default:
//                    System.out.println("Unknow type equipment");
            }
            
//            System.out.println();
            rowNum++;
        }
    }
    
    /**
     * @FIXME:  Emulate date from database
     * @param _msp
     */
    private void fillMSP(CCMsp _msp) {
        Equipments equip = Equipments.findFirst("pos = ? and name = 'мсп'", _msp.m_pos);
        LazyList<Model> params = EquipmentParams.find("equipment_id = ?", equip.getInteger("id"));

//        _msp.m_equip_number = "AD34";
//        _msp.m_equip_year = "1993";

        for (Model param : params) {
            switch (param.getString("name")) {
                case "num":
                    _msp.m_equip_number = param.getString("value");
                    break;
                case "year":
                    _msp.m_equip_year = param.getString("value");
                    break;
                default:
                    _msp.m_equip_number = "";
                    _msp.m_equip_year = "";
                    break;
            }
        }
//        
//        System.out.println(_msp.m_equip_number);
//        System.out.println(_msp.m_equip_year);
    }
    
    /**
     * 
     * @param _pos
     * @return
     */
    private CCMsp getMeassureFromExcel_Msp(String _pos) {
        String pos = null;

        while (rowExcelIterator_msp.hasNext()) {
            Row row = rowExcelIterator_msp.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            if (cellIterator.hasNext()) { 
                pos = (String) getValueFromExcelCell(cellIterator.next());
                if(pos.equals(_pos)) {
                    CCMsp msp = new CCMsp();
                    msp.m_pos = pos;
                    if (cellIterator.hasNext()) msp.m_res_ro_8_7 = (String) getValueStringFromExcelCell(cellIterator.next());
                    if (cellIterator.hasNext()) msp.m_res_ro_8_9 = (String) getValueStringFromExcelCell(cellIterator.next());
                    if (cellIterator.hasNext()) msp.m_u = (String) getValueStringFromExcelCell(cellIterator.next());
                    if (cellIterator.hasNext()) msp.m_i = (String) getValueStringFromExcelCell(cellIterator.next());
                    if (cellIterator.hasNext()) msp.m_p = (String) getValueStringFromExcelCell(cellIterator.next());
                    return msp;
                }
            }
        }
        return null;
    }
    
    private void loadFromExcel_Sit() {
        // drop header of table
        if (rowExcelIterator_sit.hasNext()) rowExcelIterator_sit.next();
        
        while (this.rowExcelIterator_sit.hasNext()) {
            Row row = this.rowExcelIterator_sit.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            if (cellIterator.hasNext()) { 
                CCSit sit = new CCSit();
                if (cellIterator.hasNext()) sit.name    = (String) getValueFromExcelCell(cellIterator.next());
                if (cellIterator.hasNext()) sit.type    = (String) getValueFromExcelCell(cellIterator.next());
                if (cellIterator.hasNext()) sit.number  = (String) getValueFromExcelCell(cellIterator.next());
                if (cellIterator.hasNext()) sit.date    = (String) getValueDateFromExcelCell(cellIterator.next());
                
                this.sit_equips.add(sit);
            }
        }
    }

    /**
     * @param cell
     * @return Object from cell or null
     */
    private Object getValueFromExcelCell(Cell cell) {
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
    
    private Object getValueStringFromExcelCell(Cell cell) {
        String val = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case Cell.CELL_TYPE_NUMERIC:
                        NumberFormat nf = new DecimalFormat("#.######");
                        val = nf.format(cell.getNumericCellValue());

//                        val = val.valueOf(cell.getNumericCellValue());
                        break;
                    case Cell.CELL_TYPE_STRING:
                        val = cell.getRichStringCellValue().getString();
                        break;
                }
                break;

            case Cell.CELL_TYPE_NUMERIC:
                val = val.valueOf(cell.getNumericCellValue());
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
    private String getValueDateFromExcelCell(Cell cell) {
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
                String yearStr = String.format("%d", cal.get(Calendar.YEAR));
//                String yearStr = String.format("%d", cal.get(Calendar.YEAR)).substring(2);

                val = dayStr + "." + monthStr + "." + yearStr;
                break;
        }
        
        return val;
    }

//    private void loadData_FromExcel(String _nameFile) throws IOException {
//        FileInputStream fis = null;
//        try {
//            File myFile = new File(_nameFile);
//            fis = new FileInputStream(myFile);
//            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
//
//            rowExcelIterator_vip_rem = myWorkBook.getSheet("Выполненный ремонт").iterator();
////            rowExcelIterator_ved_got = myWorkBook.getSheet("Ведомость готовности").iterator();
//            rowExcelIterator_sit = myWorkBook.getSheet("СИТ").iterator();
//            rowExcelIterator_msp = myWorkBook.getSheet("Измерения МСП").iterator();
////            rowExcelIterator_meo = myWorkBook.getSheet("Измерения МЭО").iterator();
////            rowExcelIterator_el_mot = myWorkBook.getSheet("Измерения Электромоторы").iterator();
////            rowExcelIterator_u_22 = myWorkBook.getSheet("Измерения У-22").iterator();
////            rowExcelIterator_u_23 = myWorkBook.getSheet("Измерения У-23").iterator();
////            rowExcelIterator_u_24 = myWorkBook.getSheet("Измерения У-24").iterator();
////            rowExcelIterator_pbr_2 = myWorkBook.getSheet("Измерения ПБР-2").iterator();
////            rowExcelIterator_pbr_3 = myWorkBook.getSheet("Измерения ПБР-3").iterator();
////            rowExcelIterator_ap_50 = myWorkBook.getSheet("Измерения АП-50").iterator();
//
//            myWorkBook.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                fis.close();
//            } catch (IOException ex) {
//                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }

//    public void readXLSX(String _nameFile) throws IOException {
//        FileInputStream fis = null;
//        try {
//            File myFile = new File(_nameFile);
//            fis = new FileInputStream(myFile);
//            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
//            XSSFSheet mySheet = myWorkBook.getSheet("СИТ");
//
//            Iterator<Row> rowIterator = mySheet.iterator();
//
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                Iterator<Cell> cellIterator = row.cellIterator();
//
//                if (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                        String title = cell.getStringCellValue();
//                        System.out.println("title: " + title);
//                    }
//                }
//
////                if (cellIterator.hasNext()) // drop type
////                    cellIterator.next();
//                if (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                        String type = cell.getStringCellValue();
//                        System.out.println("type: " + type);
//                    }
//                }
//
//                if (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                        String num = cell.getStringCellValue();
//                        System.out.println("num: " + num);
//                    } else {
//                        System.out.println(row.getCell(2).getCellType());
//                    }
//                }
//
//                if (cellIterator.hasNext()) {
//                    Cell cell = cellIterator.next();
//                    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
//                        String date = cell.getStringCellValue();
//                        System.out.println("date: " + date);
//                    } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//                        Date date = cell.getDateCellValue();
////                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kiev"));
//                        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
//                        cal.setTime(date);
//                        System.out.println("date: " + cal.get(Calendar.MONTH));
//                    }
//                }
//                System.out.println();
//            }
//
//            myWorkBook.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                fis.close();
//            } catch (IOException ex) {
//                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
}
