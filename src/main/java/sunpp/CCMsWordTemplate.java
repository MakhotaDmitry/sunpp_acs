/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunpp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author SAR1
 */
public class CCMsWordTemplate {

    // path to file template
    private String fileTemplatePath = null;
    private StringBuilder body = new StringBuilder();
    private StringBuilder bodyFilled = new StringBuilder();
    private String header = null;
    private String footer = null;

    CCMsWordTemplate(String fileTemplatePath) {
        this.fileTemplatePath = fileTemplatePath;
        try {
            ZipFile zipFile = new ZipFile(fileTemplatePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream stream = zipFile.getInputStream(entry);
//                System.out.println(entry);
                if (entry.toString().equals("word/document.xml")) {
//                    InputStreamReader isReader = new InputStreamReader(stream);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//                    StringBuilder sb = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        this.body.append(str);
                    }

                    this.header = this.body.substring(0, this.body.indexOf("<w:body>") + "<w:body>".length());
                    this.footer = this.body.substring(this.body.indexOf("</w:body>"));

                    this.body.delete(0, this.body.indexOf("<w:body>") + "<w:body>".length());
                    this.body.delete(this.body.indexOf("</w:body>"), this.body.length());

                    // @FIXME remove after testing
//                    BufferedWriter writer = new BufferedWriter(new FileWriter("1/1.txt"));
//                    writer.append(this.body);
//                    writer.close();
//                    System.out.println(sb.toString());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CCMsWordTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public StringBuilder getBody() {
//        return null;
//    }
//    private void getXmlStrBodyFromDocx() {
//        this.body = null;
//    }
    public void fillBodyByTemplate(HashMap<String, String> map) {
        StringBuilder bodyFilled = new StringBuilder(this.body);
        String val = null;
        int idxBeg = 0;
        int idxEnd = 0;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            val = entry.getValue().replace("<", "&lt;")
                    .replace(">", "&gt;");
//            val = entry.getValue().replace(">", "&gt;");

            idxBeg = bodyFilled.indexOf(entry.getKey());
            idxEnd = idxBeg + entry.getKey().length();
            bodyFilled.replace(idxBeg, idxEnd, val);
//            System.out.println(entry.getKey() + "/" + entry.getValue());
        }

//        System.out.println(bodyFilled.toString().replaceAll("\\{\\{\\s*\\S+\\s*\\}\\}", ""));
//        Pattern p = Pattern.compile("\\{\\{\\s*\\S+\\s*\\}\\}");
//        Matcher m = p.matcher(bodyFilled);
//        this.bodyFilled.append(new StringBuilder(m.replaceAll("")));
        this.bodyFilled.append(new StringBuilder(bodyFilled.toString().replaceAll("\\{\\{\\s*\\S+\\s*\\}\\}", "")));
    }

    public void save(String fileName) {
        try {
//            Files.copy(Paths.get(this.fileTemplatePath), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
            Files.createDirectories(Paths.get(fileName).getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //        System.out.println();
//        System.out.println(this.bodyFilled);

//        Path myFilePath = Paths.get("1/1.txt");
//        Path zipFilePath = Paths.get("1/МСП.docx");
//        try (FileSystem fs = FileSystems.newFileSystem(zipFilePath, null)) {
//            Path fileInsideZipPath = fs.getPath("/word/1.txt");
//            Files.copy(myFilePath, fileInsideZipPath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
            StringBuilder str_doc = new StringBuilder();
            str_doc.append(this.header);
            str_doc.append(this.bodyFilled);
            str_doc.append(this.footer);
//
//            BufferedWriter writer = new BufferedWriter(new FileWriter("1/document.xml"));
//            writer.append(str_doc);
//            writer.close();

            ZipFile zipFile = new ZipFile(this.fileTemplatePath);
            final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName));
            for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
                ZipEntry entryIn = (ZipEntry) e.nextElement();
                if (!entryIn.getName().equalsIgnoreCase("word/document.xml")) {
                    zos.putNextEntry(entryIn);
                    InputStream is = zipFile.getInputStream(entryIn);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        zos.write(buf, 0, len);
                    }
                } else {
                    zos.putNextEntry(new ZipEntry("word/document.xml"));
                    zos.write(str_doc.toString().getBytes());

//                    InputStream is = zipFile.getInputStream(entryIn);
//                    byte[] buf = new byte[1024];
//                    int len;
//                    while ((len = (is.read(buf))) > 0) {
////                        String s = new String(buf);
////                        buf = str_doc.toString().getBytes();
//                        String s = new String(buf);
//                        if (s.contains("key1=value1")) {
//                            buf = s.replaceAll("key1=value1", "key1=val2").getBytes();
//                        }
//                        zos.write(buf, 0, (len < buf.length) ? len : buf.length);
//                    }
                }
                zos.closeEntry();
            }
            zos.close();
        } catch (IOException ex) {
            Logger.getLogger(CCMsWordTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
