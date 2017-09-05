/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufop.br.objetos;

import java.util.Comparator;

/**
 *
 * @author Lanna
 */
public class ComparaRank implements Comparator<Caminho> {

    @Override
    public int compare(Caminho c1, Caminho c2) {
        if (c1.getRank() < c2.getRank()) {
            return -1;
        } else if (c1.getRank() > c2.getRank()) {
            return +1;
        } else {
            return 0;
        }
    }
}
