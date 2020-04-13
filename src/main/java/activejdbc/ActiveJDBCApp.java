/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activejdbc;

import sunpp.models.db.Positions;
import sunpp.models.db.ElectricalEnclosures;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

/**
 *
 * @author dmitry
 */
public class ActiveJDBCApp {

    public static void main(String[] args) {
        try {
            Base.open("org.sqlite.JDBC", "jdbc:sqlite:data/acs.sqlite", "", "");
            ActiveJDBCApp app = new ActiveJDBCApp();
//            app.read();
            app.create();
//            app.update();
//            app.delete();
//            app.deleteCascade();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Base.close();
        }
    }

    protected void read() {
        LazyList<Model> list = Positions.find("system_security_num = ?", 2);
        System.out.println("Size: " + list.size());

        if (list.size() == 0) {
            System.out.println("No such Position found!");
        } else {
            for (Model item : list) {
                System.out.println(item.getString("pos") + " " + item.getInteger("system_security_num") + " " + item.getString("system_security_type") + " " + item.getString("status"));
            }
        }
    }

    protected void create() {
//        ElectricalEnclosures electricalEnclosures = new ElectricalEnclosures(1, "ШДУ-1", "1RN15S02");
        ElectricalEnclosures electricalEnclosures = new ElectricalEnclosures();
//        electricalEnclosures.set("id", 1);
//        electricalEnclosures.setInteger("id", 1);
        electricalEnclosures.set("name", "ШДУ-1");
        electricalEnclosures.set("pos", "1UA53S02");
        electricalEnclosures.saveIt();
//        employee.add(new Role("Java Developer", "BN"));
        LazyList<Model> all = ElectricalEnclosures.findAll();
        System.out.println(all.size());
    }

    protected void update() {
        ElectricalEnclosures electricalEnclosures = ElectricalEnclosures.findFirst("name = ?", "ШДУ-1");
        electricalEnclosures.set("name", "ШДУ-22").saveIt();
        electricalEnclosures = ElectricalEnclosures.findFirst("name = ?", "ШДУ-22");
        System.out.println(electricalEnclosures.getString("name") + " " + electricalEnclosures.getString("pos"));
    }

    protected void delete() {
        ElectricalEnclosures electricalEnclosures = ElectricalEnclosures.findFirst("name = ?", "ШДУ-22");
        electricalEnclosures.delete();
        electricalEnclosures = ElectricalEnclosures.findFirst("pos = ?", "ШДУ-1");
        if (null == electricalEnclosures) {
            System.out.println("No such ElectricalEnclosures found!");
        }
    }

    protected void deleteCascade() {
        create();
        ElectricalEnclosures electricalEnclosures = ElectricalEnclosures.findFirst("name = ?", "ШДУ-1");
        electricalEnclosures.deleteCascade();
        electricalEnclosures = ElectricalEnclosures.findFirst("pos = ?", "Ш");
        if (null == electricalEnclosures) {
            System.out.println("No such ElectricalEnclosures found!");
        }
    }

}
