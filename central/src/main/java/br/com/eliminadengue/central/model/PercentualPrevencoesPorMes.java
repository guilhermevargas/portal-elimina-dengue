/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.eliminadengue.central.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Guilherme Alves
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PercentualPrevencoesPorMes extends PercentualPrevencoes {
    private double percentualAtrasada;
    private double percentualEfetuada;

    public void setPercentualAtrasada(double percentAtualrasada) {
        this.percentualAtrasada = percentAtualrasada;
    }

    public void setPercentualEfetuada(double percentualEfetuada) {
        this.percentualEfetuada = percentualEfetuada;
    }

    public double getPercentualAtrasada() {
        return percentualAtrasada;
    }

    public double getPercentualEfetuada() {
        return percentualEfetuada;
    }
    private int mes;
    private String nomeMes;

    public String getNomeMes() {
        String[] meses = {"JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO",
            "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"};
        return meses[mes];
    }

    public void setNomeMes(String nomeMes) {
        this.nomeMes = nomeMes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getMes() {
        return mes;
    }

    @Override
    public String toString() {
        return "Percentual de prevenções por mês";
    }
    
    
}
