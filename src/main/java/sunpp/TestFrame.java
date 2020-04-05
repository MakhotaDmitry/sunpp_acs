/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp;

/**
 *
 * @author dmitry
 */
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class TestFrame extends JFrame {

    static int i = 0;

    public TestFrame() {

        super("Тестовое окно");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ArrayList<MyBean> beans = new ArrayList<MyBean>();

        for (int i = 0; i < 30; i++) {
            beans.add(new MyBean("Имя " + i, "Размер " + i, "Описание " + i));
        }

        TableModel model = new MyTableModel(beans);
        JTable table = new JTable(model);

        getContentPane().add(new JScrollPane(table));

        setPreferredSize(new Dimension(260, 220));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                JFrame.setDefaultLookAndFeelDecorated(true);
//                System.setProperty("prism.lcdtext", "false");
//                System.setProperty("-Dswing.aatext", "true");
//                System.setProperty("-Dawt.useSystemAAFontSettings", "on");

                System.setProperty("awt.useSystemAAFontSettings", "on");
                System.setProperty("swing.aatext", "true");
//                System.setProperty("JAVA_OPTS", "-Xmx128m -Xms32m -XX:MaxPermSize=64m");

                try {
                    // Set System L&F
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

                    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        System.out.println(info.getClassName());
                    }
                } catch (UnsupportedLookAndFeelException e) {
                    System.out.println(e.fillInStackTrace());
                } catch (ClassNotFoundException e) {
                    System.out.println(e.fillInStackTrace());
                } catch (InstantiationException e) {
                    System.out.println(e.fillInStackTrace());
                } catch (IllegalAccessException e) {
                    System.out.println(e.fillInStackTrace());
                }
                new TestFrame();
            }
        });
    }

    public class MyTableModel implements TableModel {

        private Set<TableModelListener> listeners = new HashSet<TableModelListener>();

        private List<MyBean> beans;

        public MyTableModel(List<MyBean> beans) {
            this.beans = beans;
        }

        public void addTableModelListener(TableModelListener listener) {
            listeners.add(listener);
        }

        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public int getColumnCount() {
            return 3;
        }

        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return "Имя";
                case 1:
                    return "Размер";
                case 2:
                    return "Описание";
            }
            return "";
        }

        public int getRowCount() {
            return beans.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            MyBean bean = beans.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return bean.getName();
                case 1:
                    return bean.getSize();
                case 2:
                    return bean.getDescription();
            }
            return "";
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void removeTableModelListener(TableModelListener listener) {
            listeners.remove(listener);
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex) {

        }

    }

    public class MyBean {

        private String name;
        private String size;
        private String description;

        public MyBean(String name, String size, String description) {
            this.setName(name);
            this.setSize(size);
            this.setDescription(description);
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getSize() {
            return size;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
