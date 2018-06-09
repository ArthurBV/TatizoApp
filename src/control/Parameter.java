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
public class Parameter {

    private String idParameter;
    private String accionParameter;
    private String timeParameter;
    private String eaParameter;
    private String ecParameter;
    private String tParameter;

    public Parameter() {
        this.idParameter = "";
        this.accionParameter = "";
        this.timeParameter = "";
        this.eaParameter = "";
        this.ecParameter = "";
        this.tParameter = "";
    }

    public Parameter(String idParameter, String accionParameter, String timeParameter, String eaParameter, String ecParameter, String tParameter) {
        this.idParameter = idParameter;
        this.accionParameter = accionParameter;
        this.timeParameter = timeParameter;
        this.eaParameter = eaParameter;
        this.ecParameter = ecParameter;
        this.tParameter = tParameter;
    }

    @Override
    public String toString() {
        return idParameter + "&&"
                + accionParameter + "&&"
                + timeParameter + "&&"
                + eaParameter + "&&"
                + ecParameter + "&&"
                + tParameter;
    }

}
