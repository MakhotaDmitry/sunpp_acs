/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp.models.protocols;

/**
 *
 * @author dmitry
 */
public class CCAP50 {

    public String m_pos = null;
    public String m_el_enclosure = null;
    public String m_type = null;
    public String m_nom_curent = null;
    public String m_kr_ots = null;
    public String m_type_repair = null;

    public Float m_i_nesr_a = null;
    public Float m_i_nesr_b = null;
    public Float m_i_nesr_c = null;

    public Float m_i_srab_a = null;
    public Float m_i_srab_b = null;
    public Float m_i_srab_c = null;

    public Float m_time_a = null;
    public Float m_time_b = null;
    public Float m_time_c = null;

    public String m_date = null;
    public String m_man = null;

    public void printAll() {
        System.out.println("Class: " + this.getClass());
        System.out.println("m_pos: " + this.m_pos);
        System.out.println("m_el_enclosure: " + this.m_el_enclosure);
        System.out.println("m_type: " + this.m_type);
        System.out.println("m_nom_curent: " + this.m_nom_curent);
        System.out.println("m_kr_ots: " + this.m_kr_ots);
        System.out.println("m_type_repair: " + this.m_type_repair);

        System.out.println("m_i_nesr_a:" + this.m_i_nesr_a);
        System.out.println("m_i_nesr_b: " + this.m_i_nesr_b);
        System.out.println("m_i_nesr_c: " + this.m_i_nesr_c);

        System.out.println("m_i_srab_a: " + this.m_i_srab_a);
        System.out.println("m_i_srab_b: " + this.m_i_srab_b);
        System.out.println("m_i_srab_c: " + this.m_i_srab_c);

        System.out.println("m_time_a: " + this.m_time_a);
        System.out.println("m_time_b: " + this.m_time_b);
        System.out.println("m_time_c: " + this.m_time_c);

        System.out.println("m_date: " + this.m_date);
        System.out.println("m_man: " + this.m_man);

        System.out.println();
    }
}
