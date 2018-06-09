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

public class UserManagement {

    private String ruteFile;
    private ArrayList<User> user;

    public UserManagement(String rute) {
        this.ruteFile = rute;
        this.user = new ArrayList();
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
            Logger.getLogger(UserManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveUser(User user, String rute) {
        User employee = user;
        try {
            File archive = new File(rute);
            FileWriter fr = new FileWriter(archive, true);
            PrintWriter ps = new PrintWriter(fr);
            ps.println(employee);
            ps.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public ArrayList<User> getAll() {

        ArrayList<User> user = new ArrayList();
        FileReader archive;
        BufferedReader br;
        String registry;

        try {
            archive = new FileReader(ruteFile);
            br = new BufferedReader(archive);
            while ((registry = br.readLine()) != null) {
                String[] fields = registry.split("&&");
                User employee = new User(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
                user.add(employee);
            }
        } catch (IOException xo) {
            xo.printStackTrace();
            System.exit(1);
        }

        return user;
    }

}
