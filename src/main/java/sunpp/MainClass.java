/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp;

import java.util.HashMap;

/**
 *
 * @author dmitry
 */
public class MainClass {

    public static void main(String[] args) {
        MainClass app = new MainClass();
        
        CCGeneratorProtocolsFromExcel generatorProtocolsFromExcel = new CCGeneratorProtocolsFromExcel("Statement_of_repairs_2019_unit_1.xlsx", "out111");
        generatorProtocolsFromExcel.generate();
        
        System.out.println("App closed");

//        CCMsWordTemplate word = new CCMsWordTemplate("data/templates/МСП.docx");
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("{{1_1}}", "val 1");
//        map.put("{{1_2}}", "val 2");
//        word.fillBodyByTemplate(map);
//        map.put("{{1_1}}", "val > 3");
//        map.put("{{1_2}}", "val < 4");
//        map.put("{{sit_name_1}}", "bspt");
//        word.fillBodyByTemplate(map);
//        word.save("out111/1.docx");
    }
}
