/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.eliminadengue.central.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Guilherme Alves
 */
@XmlRootElement(name = "prevencoes")
public interface PercentualPrevencao {
    public void setPercentualAtrasada(double percentAtualrasada);

    public void setPercentualEmDia(double percentualEmDia);

    public double getPercentualAtrasada();

    public double getPercentualEmDia();
    
    public String getNomeMes();
    
    public void setNomeMes(String nomeFoco);
    
    public String getNomeFoco();
    
    public void setNomeFoco(String nomeFoco);
}
