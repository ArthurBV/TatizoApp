/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportManagement {

    private String ruteFile;
    private ArrayList<Report> report;

    public ReportManagement(String rute) {
        this.ruteFile = rute;
        this.report = new ArrayList();
        this.verifyArchive();
    }

    private void verifyArchive() {
        try {
            File archive = new File(ruteFile);
            if (!archive.exists()) {
                archive.createNewFile();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveReport(Report report, String rute) {
        Report recordUser = report;
        try {
            File archive = new File(rute);
            FileWriter fr = new FileWriter(archive, true);
            PrintWriter ps = new PrintWriter(fr);
            ps.println(recordUser);
            ps.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public ArrayList<Report> getAll(String rute) {

        ArrayList<Report> report = new ArrayList();
        FileReader archive;
        BufferedReader br;
        String registry;

        try {
            archive = new FileReader(rute);
            br = new BufferedReader(archive);
            while ((registry = br.readLine()) != null) {
                String[] fields = registry.split("&&");
                Report recordUser = new Report(fields[0], fields[1], fields[2], fields[3], fields[4]);
                report.add(recordUser);
            }
        } catch (IOException xo) {
            xo.printStackTrace();
            System.exit(1);
        }

        return report;
    }

}
