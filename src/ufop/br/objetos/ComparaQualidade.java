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
public class ComparaQualidade implements Comparator<Caminho> {

    @Override
    public int compare(Caminho c1, Caminho c2) {
        if (c1.getQualidade() < c2.getQualidade()) {
            return -1;
        } else if (c1.getQualidade() > c2.getQualidade()) {
            return +1;
        } else {
            return 0;
        }
    }
}
