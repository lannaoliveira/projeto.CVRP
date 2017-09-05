/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufop.br.objetos;

import java.util.ArrayList;

/**
 *
 * @author Lanna
 */
public class Caminho {

    private ArrayList<Integer> caminho;
    private int qualidade;
    private double rank;

    public Caminho(ArrayList<Integer> caminho, int qualidade, double rank) {
        this.caminho = new ArrayList<Integer>(caminho);
        this.qualidade = qualidade;
        this.rank = rank;
    }

    public int getQualidade() {
        return qualidade;
    }

    public ArrayList<Integer> getCaminho() {
        return caminho;
    }

    public void setQualidade(int qualidade) {
        this.qualidade = qualidade;
    }

    public void setCaminho(ArrayList<Integer> caminho) {
        this.caminho = caminho;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }
}
