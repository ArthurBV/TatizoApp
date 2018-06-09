/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

/**
 *
 * @author vanis
 */
public class Case {

    private int caseId;
    private int ecuA;
    private float ecuB;
    private int caseMin;
    private int caseMax;
    private String pathTraining;
    private String pathProduction;

    public Case() {
        this.caseId = 0;
        this.ecuA = 0;
        this.ecuB = 0;
        this.caseMin = 0;
        this.caseMax = 0;
        this.pathTraining ="";
        this.pathProduction = "";
    }

    public Case(int caseId, int ecuA, float ecuB, int caseMin, int caseMax, String pathTraining, String pathProduction) {
        this.caseId = caseId;
        this.ecuA = ecuA;
        this.ecuB = ecuB;
        this.caseMin = caseMin;
        this.caseMax = caseMax;
        this.pathTraining = pathTraining;
        this.pathProduction = pathProduction;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseName(int caseId) {
        this.caseId = caseId;
    }

    public int getEcuA() {
        return ecuA;
    }

    public void setEcuA(int ecuA) {
        this.ecuA = ecuA;
    }

    public float getEcuB() {
        return ecuB;
    }

    public void setEcuB(float ecuB) {
        this.ecuB = ecuB;
    }

    public int getCaseMin() {
        return caseMin;
    }

    public void setCaseMin(int caseMin) {
        this.caseMin = caseMin;
    }

    public int getCaseMax() {
        return caseMax;
    }

    public void setCaseMax(int caseMax) {
        this.caseMax = caseMax;
    }

    public String getPathTraining() {
        return pathTraining;
    }

    public void setPathTraining(String pathTraining) {
        this.pathTraining = pathTraining;
    }

    public String getPathProduction() {
        return pathProduction;
    }

    public void setPathProduction(String pathProduction) {
        this.pathProduction = pathProduction;
    }

    @Override
    public String toString() {
        return caseId + "&&" 
                + ecuA + "&&" 
                + ecuB + "&&" 
                + caseMin + "&&" 
                + caseMax + "&&" 
                + pathTraining + "&&" 
                + pathProduction;
    }

    
}
