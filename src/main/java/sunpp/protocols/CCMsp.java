/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp.protocols;

import java.lang.reflect.Field;

/**
 *
 * @author dmitry
 */
public class CCMsp {

    public String m_equip_number = null;
    public Integer m_equip_year = null;
    public String m_pos = null;
    public String m_type_repair = null;
    public Double m_res_ro_8_7 = null;
    public Double m_res_ro_8_9 = null;
    public Double m_u = null;
    public Double m_i = null;
    public Double m_p = null;
    public String m_repair_date = null;
    public String m_workers = null;

    public String toString() {
//        String text = "";
//        text += "Class: " + this.getClass() + ", ";
//        text += "m_equip_number: " + this.m_equip_number + ", ";
//        text += "m_equip_year: " + this.m_equip_year + ", ";
//        text += "m_pos: " + this.m_pos + ", ";
//        text += "m_type_repair: " + this.m_type_repair + ", ";
//        text += "m_res_ro_8_7: " + this.m_res_ro_8_7 + ", ";
//        text += "m_res_ro_8_9: " + this.m_res_ro_8_9 + ", ";
//        text += "m_u: " + this.m_u + ", ";
//        text += "m_i: " + this.m_i + ", ";
//        text += "m_p: " + this.m_p + ", ";
//        text += "m_repair_date: " + this.m_repair_date + ", ";
//        text += "m_workers: " + this.m_workers;
//        return text;

        StringBuilder text = new StringBuilder("");
        text.append("Class: ").append(this.getClass()).append (", ");
        text.append("m_equip_number: ").append(this.m_equip_number).append (", ");
        text.append("m_equip_year: ").append(this.m_equip_year).append (", ");
        text.append("m_pos: ").append(this.m_pos).append (", ");
        text.append("m_type_repair: ").append(this.m_type_repair).append (", ");
        text.append("m_res_ro_8_7: ").append(this.m_res_ro_8_7).append (", ");
        text.append("m_res_ro_8_9: ").append(this.m_res_ro_8_9).append (", ");
        text.append("m_u: ").append(this.m_u).append (", ");
        text.append("m_i: ").append(this.m_i).append (", ");
        text.append("m_p: ").append(this.m_p).append (", ");
        text.append("m_repair_date: ").append(this.m_repair_date).append (", ");
        text.append("m_workers: ").append (this.m_workers);
        return text.toString();

//        for (Field f : this.getClass().getFields()) {
//            text += f.getName() + ": " + f + "\n";
//        }
//        return text;

//        System.out.println("Class: " + this.getClass());
//        System.out.println("m_equip_number: " + this.m_equip_number);
//        System.out.println("m_equip_year: " + this.m_equip_year);
//        System.out.println("m_pos: " + this.m_pos);
//        System.out.println("m_type_repair: " + this.m_type_repair);
//        System.out.println("m_res_ro_8_7: " + this.m_res_ro_8_7);
//        System.out.println("m_res_ro_8_9: " + this.m_res_ro_8_9);
//        System.out.println("m_u: " + this.m_u);
//        System.out.println("m_i: " + this.m_i);
//        System.out.println("m_p: " + this.m_p);
//        System.out.println("m_repair_date: " + this.m_repair_date);
//        System.out.println("m_workers: " + this.m_workers);
//        System.out.println();
    }
}
