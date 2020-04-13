/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.sqlite.date.ExceptionUtils;
import sunpp.models.protocols.CCMsp;
import sunpp.models.protocols.CCSit;

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

    private ArrayList<CCSit> sits = null;

    private String outDir = null;

    public CCGeneratorProtocols(String outDir) {
        this.outDir = outDir;
    }

    public void setSits(ArrayList<CCSit> sits) {
        this.sits = sits;
    }

    private void gen(int quant_records_per_page,
            final String path_templ,
            HashMap<String, String> filledSits,
            ArrayList<?> equip) {
    }

    private void fillPage(HashMap<String, String> map,
            HashMap<String, String> filledSits,
            StringBuilder strNumUnit,
            StringBuilder strPprYear,
            CCMsWordTemplate doc) {
        map.put("{{Unit # X}}", strNumUnit.toString());
        map.put("{{PPR-XXXX}}", strPprYear.toString());
        map.putAll(filledSits);

        doc.fillBodyByTemplate(map);
        
        strPprYear.setLength(0);
        map.clear();
    }

    public void generateMSP(ArrayList<CCMsp> _equipments) {
        int quant_records_per_page = 4;

        CCMsWordTemplate docUnit_1 = new CCMsWordTemplate(this.m_path_template_msp);
        CCMsWordTemplate docUnit_2 = new CCMsWordTemplate(this.m_path_template_msp);
        CCMsWordTemplate docUnit_3 = new CCMsWordTemplate(this.m_path_template_msp);
        CCMsWordTemplate docSvo_1 = new CCMsWordTemplate(this.m_path_template_msp);
        CCMsWordTemplate docSvo_2 = new CCMsWordTemplate(this.m_path_template_msp);

        HashMap<String, String> filledTemplateUnit_1 = new HashMap<>();
        HashMap<String, String> filledTemplateUnit_2 = new HashMap<>();
        HashMap<String, String> filledTemplateUnit_3 = new HashMap<>();
        HashMap<String, String> filledTemplateSvo_1 = new HashMap<>();
        HashMap<String, String> filledTemplateSvo_2 = new HashMap<>();

        HashMap<String, String> filledSits = new HashMap<>();
        for (int i = 0; i < this.sits.size(); i++) {
            if (this.sits.get(i).name.equals("Мультиметр")) {
                filledSits.put("{{sit_name_1}}", String.format("%s", this.sits.get(i).name));
                filledSits.put("{{sit_type_1}}", String.format("тип: %s", this.sits.get(i).type));
                filledSits.put("{{sit_num_1}}", String.format("зав. №: %s", this.sits.get(i).number));
                filledSits.put("{{sit_date_1}}", String.format("годен до: %s г.;", this.sits.get(i).date));
            }
        }

        HashMap<String, String> curMap;
        int idx = 0;
        StringBuilder strNumUnit = new StringBuilder();
        StringBuilder strPprYear = new StringBuilder();

        int curNumUnit_1 = 0;
        int curNumUnit_2 = 0;
        int curNumUnit_3 = 0;
        int curNumSvo_1 = 0;
        int curNumSvo_2 = 0;

        for (CCMsp equip : _equipments) {
            if (equip.m_pos.startsWith("1")) {
                curMap = filledTemplateUnit_1;
                idx = ++curNumUnit_1;
                strNumUnit = new StringBuilder("Энергоблок № 1");
            } else if (equip.m_pos.startsWith("2")) {
                curMap = filledTemplateUnit_2;
                idx = ++curNumUnit_2;
                strNumUnit = new StringBuilder("Энергоблок № 2");
            } else if (equip.m_pos.startsWith("3")) {
                curMap = filledTemplateUnit_3;
                idx = ++curNumUnit_3;
                strNumUnit = new StringBuilder("Энергоблок № 3");
            } else if (equip.m_pos.startsWith("(1)")) {
                curMap = filledTemplateSvo_1;
                idx = ++curNumSvo_1;
                strNumUnit = new StringBuilder("1СВО");
            } else if (equip.m_pos.startsWith("(2)")) {
                curMap = filledTemplateSvo_2;
                idx = ++curNumSvo_2;
                strNumUnit = new StringBuilder("2СВО");
            } else {
                Logger.getLogger(CCGeneratorProtocols.class.getName()).log(Level.SEVERE, null, "Unknow number unit of power plant");
                continue;
            }

            try {
                if(strPprYear.length() == 0)
                    strPprYear = new StringBuilder("".format("ППР-%s", (equip.m_repair_date != null) ? equip.m_repair_date.split(Pattern.quote("."))[2] : ""));
            } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                StringBuilder msgErr = new StringBuilder();
                msgErr.append("equip.m_repair_date: ");
                msgErr.append(equip.m_repair_date);
                msgErr.append("\n");

                Logger.getLogger(CCGeneratorProtocols.class.getName()).log(Level.SEVERE, msgErr.toString(), ex);
            }


            String[] workers = equip.m_workers != null ? equip.m_workers.split("\n") : new String[] {""};
            
            curMap.put(String.format("{{%d_1}}", idx), equip.m_equip_number);
            curMap.put(String.format("{{%d_2}}", idx), equip.m_equip_year);
            curMap.put(String.format("{{%d_3}}", idx), equip.m_pos);
            curMap.put(String.format("{{%d_4}}", idx), equip.m_type_repair);
            curMap.put(String.format("{{%d_5}}", idx), "Вып.");
            curMap.put(String.format("{{%d_6}}", idx), "Вып.");
            
            curMap.put(String.format("{{%d_15}}", idx), "Вып.");
            curMap.put(String.format("{{%d_16}}", idx), "Годен");
            curMap.put(String.format("{{%d_17}}", idx), equip.m_repair_date != null ? equip.m_repair_date : "");
            curMap.put(String.format("{{%d_18}}", idx), workers[workers.length > 0 ? ThreadLocalRandom.current().nextInt(0, workers.length) : 0]);

            switch (equip.m_type_repair) {
                case "ТО":
                    curMap.put(String.format("{{%d_7}}", idx), "-");
                    curMap.put(String.format("{{%d_8}}", idx), "-");
                    curMap.put(String.format("{{%d_9}}", idx), "-");
                    curMap.put(String.format("{{%d_10}}", idx), "-");
                    curMap.put(String.format("{{%d_11}}", idx), "-");
                    curMap.put(String.format("{{%d_12}}", idx), "-");
                    curMap.put(String.format("{{%d_13}}", idx), "-");
                    curMap.put(String.format("{{%d_14}}", idx), "-");
                    break;

                case "ТР":
                    curMap.put(String.format("{{%d_7}}", idx), "Вып.");
                    curMap.put(String.format("{{%d_8}}", idx), "Вып.");
                    curMap.put(String.format("{{%d_9}}", idx), "Вып.");
                    curMap.put(String.format("{{%d_10}}", idx), equip.m_res_ro_8_7);
                    curMap.put(String.format("{{%d_11}}", idx), equip.m_res_ro_8_9);
                    curMap.put(String.format("{{%d_12}}", idx), equip.m_u);
                    curMap.put(String.format("{{%d_13}}", idx), equip.m_i);
                    curMap.put(String.format("{{%d_14}}", idx), equip.m_p);
                    break;

                case "КР":
                    curMap.put(String.format("{{%d_7}}", idx), "Вып.");
                    curMap.put(String.format("{{%d_8}}", idx), "Вып.");
                    curMap.put(String.format("{{%d_9}}", idx), "Вып.");
                    curMap.put(String.format("{{%d_10}}", idx), equip.m_res_ro_8_7);
                    curMap.put(String.format("{{%d_11}}", idx), equip.m_res_ro_8_9);
                    curMap.put(String.format("{{%d_12}}", idx), equip.m_u);
                    curMap.put(String.format("{{%d_13}}", idx), equip.m_i);
                    curMap.put(String.format("{{%d_14}}", idx), equip.m_p);
                    break;
            }

            if (curNumUnit_1 >= quant_records_per_page) {
                fillPage(curMap, filledSits, strNumUnit, strPprYear, docUnit_1);
                curNumUnit_1 = 0;
            }
            if (curNumUnit_2 >= quant_records_per_page) {
                fillPage(curMap, filledSits, strNumUnit, strPprYear, docUnit_2);
                curNumUnit_2 = 0;
            }
            if (curNumUnit_3 >= quant_records_per_page) {
                fillPage(curMap, filledSits, strNumUnit, strPprYear, docUnit_3);
                curNumUnit_3 = 0;
            }
            if (curNumSvo_1 >= quant_records_per_page) {
                fillPage(curMap, filledSits, strNumUnit, strPprYear, docSvo_1);
                curNumSvo_1 = 0;
            }
            if (curNumSvo_2 >= quant_records_per_page) {
                fillPage(curMap, filledSits, strNumUnit, strPprYear, docSvo_2);
                curNumSvo_2 = 0;
            }
        }

//        write the rest of the dictionary
        if (curNumUnit_1 > 0) fillPage(filledTemplateUnit_1, filledSits, strNumUnit, strPprYear, docUnit_1);
        if (curNumUnit_2 > 0) fillPage(filledTemplateUnit_2, filledSits, strNumUnit, strPprYear, docUnit_2);
        if (curNumUnit_3 > 0) fillPage(filledTemplateUnit_3, filledSits, strNumUnit, strPprYear, docUnit_3);
        if (curNumSvo_1 > 0) fillPage(filledTemplateSvo_1, filledSits, strNumUnit, strPprYear, docSvo_1);
        if (curNumSvo_2 > 0) fillPage(filledTemplateSvo_2, filledSits, strNumUnit, strPprYear, docSvo_2);

        docUnit_1.save(outDir + "/Протоколы_1ЭБ/МСП.docx");
        docUnit_2.save(outDir + "/Протоколы_2ЭБ/МСП.docx");
        docUnit_3.save(outDir + "/Протоколы_3ЭБ/МСП.docx");
        docSvo_1.save(outDir + "/Протоколы_1СВО/МСП.docx");
        docSvo_2.save(outDir + "/Протоколы_2СВО/МСП.docx");
    }
}
