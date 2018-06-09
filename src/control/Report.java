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
public class Report {
    
    private String dateReport;
    private String userName;
    private String clientReport;
    private String timeReport;
    private String effortReport;

    public Report() {
        this.dateReport = "";
        this.userName = "";
        this.clientReport = "";
        this.timeReport = "";
        this.effortReport = "";
    }
    
    public Report(String dateReport, String userName, String clientReport, String timeReport, String effortReport) {
        this.dateReport = dateReport;
        this.userName = userName;
        this.clientReport = clientReport;
        this.timeReport = timeReport;
        this.effortReport = effortReport;
    }

    public String getDateReport() {
        return dateReport;
    }

    public void setDateReport(String dateReport) {
        this.dateReport = dateReport;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientReport() {
        return clientReport;
    }

    public void setClientReport(String clientReport) {
        this.clientReport = clientReport;
    }

    public String getTimeReport() {
        return timeReport;
    }

    public void setTimeReport(String timeReport) {
        this.timeReport = timeReport;
    }

    public String getEffortReport() {
        return effortReport;
    }

    public void setEffortReport(String effortReport) {
        this.effortReport = effortReport;
    }

    @Override
    public String toString() {
        return dateReport + "&&" 
                + userName + "&&" 
                + clientReport + "&&" 
                + timeReport + "&&" 
                + effortReport;
    }
}
