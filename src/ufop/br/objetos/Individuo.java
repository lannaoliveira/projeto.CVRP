/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufop.br.objetos;

/**
 *
 * @author Lanna
 */
public class Individuo {

    private int id, demanda, coX, coY;

    public Individuo(int id, int coX, int coY, int demanda) {
        this.id = id;
        this.demanda = demanda;
        this.coX = coX;
        this.coY = coY;
    }

    public int getId() {
        return id;
    }

    public int getCoX() {
        return coX;
    }

    public int getCoY() {
        return coY;
    }

    public int getDemanda() {
        return demanda;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCoX(int coX) {
        this.coX = coX;
    }

    public void setCoY(int coY) {
        this.coY = coY;
    }

    public void setDemanda(int demanda) {
        this.demanda = demanda;
    }
}
