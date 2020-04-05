/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp.protocols;

import java.util.ArrayList;
import java.util.HashMap;

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

        for (CCMsp equip : _equipments) {
            System.out.println(equip);
//            equip.printAll();
//            System.out.println(equip.getClass());
//            System.out.println(equip.m_number);
//            System.out.println(equip.m_year);
//            System.out.println(equip.m_pos);
//            System.out.println(equip.m_type_repair);
//            System.out.println(equip.m_res_ro_8_7);
//            System.out.println(equip.m_res_ro_8_9);
//            System.out.println(equip.m_u);
//            System.out.println(equip.m_i);
//            System.out.println(equip.m_p);
//            System.out.println(equip.m_date);
//            System.out.println(equip.m_man);
//            System.out.println();
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
