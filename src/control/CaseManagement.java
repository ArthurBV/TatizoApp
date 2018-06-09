/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CaseManagement {

    private String ruteFile;
    private ArrayList<Case> cases;

    public CaseManagement(String rute) {
        this.ruteFile = rute;
        this.cases = new ArrayList();
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
            Logger.getLogger(CaseManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Case> getAll() {

        ArrayList<Case> cases = new ArrayList();
        FileReader archive;
        BufferedReader br;
        String registry;

        try {
            archive = new FileReader(ruteFile);
            br = new BufferedReader(archive);
            while ((registry = br.readLine()) != null) {
                String[] fields = registry.split("&&");
                Case stage = new Case(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), Float.parseFloat(fields[2]),
                        Integer.parseInt(fields[3]), Integer.parseInt(fields[4]), fields[5], fields[6]);
                cases.add(stage);
            }
        } catch (IOException xo) {
            xo.printStackTrace();
            System.exit(1);
        }

        return cases;
    }

}
