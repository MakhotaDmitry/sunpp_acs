/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and openDb the template in the editor.
 */
package code_sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author dmitry
 */
public class SqlLearn implements AutoCloseable {

    Connection m_db_r_connection = null;
    Connection m_db_w_connection = null;

    public static void main(String[] args) {
        System.out.println("hello");

        SqlLearn app = new SqlLearn();
//        app.removeFileDb();
        
        app.openDb();
        app.insertRecordPos("RA1", null, "СВБ");
        app.close();
//        app.createNewTable();

    }

    private void removeFileDb() {
        File file = new File("data/acs.sqlite");
        if (file.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }
    }

    public void SqlLearn() {
    }

    public void openDb() {
    m_db_r_connection = null;
        try {
            m_db_r_connection = DriverManager.getConnection("jdbc:sqlite:data/acs.sqlite");
//            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void SqlTest() {
    }

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS employees (\n"
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL,\n"
                + " capacity real\n"
                + ");";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:data/acs.sqlite");
//            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
//            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertRecordPos(String pos, Integer system_security_num, String system_security_type) {
        String sql = "INSERT INTO `Positions` (pos, system_security_num, system_security_type) VALUES (?, ?, ?)";

        try {
//            Connection conn = this.connect();  
            PreparedStatement pstmt = m_db_r_connection.prepareStatement(sql);
            pstmt.setString(1, pos);
            if (system_security_num != null)    pstmt.setInt(2, system_security_num);
            if (system_security_type != null)   pstmt.setString(3, system_security_type);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void close() {
        System.out.println("closing...");
        try {
            if (m_db_r_connection != null) {
                m_db_r_connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
