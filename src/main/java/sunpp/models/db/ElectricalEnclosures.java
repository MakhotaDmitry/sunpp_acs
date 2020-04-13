/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp.models.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 *
 * @author dmitry
 */
@Table("ElectricalEnclosures")
public class ElectricalEnclosures extends Model {

    public ElectricalEnclosures() {

    }

//    public ElectricalEnclosures(int id, String name, String pos) {
//        setInteger("id", id);
//        setString("name", name);
//        setString("pos", pos);
////        set("id", id);
////        set("name", name);
////        set("pos", pos);
//    }

//    public String getName() {
//        return getString("name");
//    }
//    
//    public String getPos() {
//        return getString("pos");
//    }

}
