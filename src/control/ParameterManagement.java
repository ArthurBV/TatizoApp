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

public class ParameterManagement {

    private String ruteFile;
    private ArrayList<Parameter> parameter;

    public ParameterManagement(String rute) {
        this.ruteFile = rute;
        this.parameter = new ArrayList();
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
            Logger.getLogger(ParameterManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Parameter> getAll(String pathTraining) {
        ArrayList<Parameter> parameters = new ArrayList();
        FileReader archive;
        BufferedReader br;
        String registry;

        try {
            archive = new FileReader(pathTraining);
            br = new BufferedReader(archive);
            while ((registry = br.readLine()) != null) {
                String[] fields = registry.split("&&");
                Parameter actions = new Parameter(fields[0], fields[1], fields[2],
                        fields[3], fields[4], fields[5]);
                parameters.add(actions);
            }
        } catch (IOException xo) {
            xo.printStackTrace();
            System.exit(1);
        }

        return parameters;
    }

}
