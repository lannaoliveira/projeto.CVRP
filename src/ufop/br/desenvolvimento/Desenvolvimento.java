/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufop.br.desenvolvimento;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import ufop.br.objetos.Individuo;

/**
 * Classe em que métodos para encontrar a melhor solução são declarados.
 *
 * @author Lanna
 */
public class Desenvolvimento {

    ArrayList<Individuo> individuos;
    ArrayList<String> individuo, demandas;
    ArrayList<Integer> camA, camB, camAA, camBB, caminhos, caminhoI, caminhoII, caminhoX, one;
    public ArrayList<ArrayList<Integer>> caminho;
    String[] separa;
    int[][] matriz;
    Random aleatorio;

    /**
     * Construtor para inicialização das variáveis globais.
     *
     * @param tam utilizado para definir o tamanho da matriz que irá conter os
     * valores das distâncias entre os indivíduos.
     */
    public Desenvolvimento(int tam) {
        this.matriz = new int[tam][tam];
        this.aleatorio = new Random();
        this.caminho = new ArrayList<ArrayList<Integer>>();
        this.individuos = new ArrayList<Individuo>();
        this.caminhos = new ArrayList<Integer>();
        this.camA = new ArrayList<Integer>();
        this.camB = new ArrayList<Integer>();
        this.camAA = new ArrayList<Integer>();
        this.camBB = new ArrayList<Integer>();
    }

    /**
     * Método que recebe uma lista sem formatação obtida através da leitura do
     * arquivo de testes, seleciona, guarda e retorna uma lista com os
     * indivíduos para realizar as operações necessárias.
     *
     * @param principal lista com diversas informações do arquivo de teste, sem
     * formatação.
     * @return lista formatada apenas com os indivíduos.
     */
    public ArrayList<String> trataIndividuo(ArrayList<String> principal) {
        individuo = new ArrayList<String>();
        for (int i = 6; i < 39; i++) {
            individuo.add(principal.get(i));
        }
        return individuo;
    }

    /**
     * Método que recebe uma lista sem formatação obtida através da leitura do
     * arquivo de testes, seleciona, guarda e retorna uma lista com a demanda de
     * cada indivíduo para posteriores operações.
     *
     * @param principal lista com diversas informações do arquivo de teste, sem
     * formatação.
     * @param tamanho quantidade de demandas que devem ser extraídas da lista
     * sem formatação.
     * @return lista formatada apenas com a demanda de cada indivíduo.
     */
    public ArrayList<String> trataDemanda(ArrayList<String> principal, int tamanho) {
        separa = new String[tamanho];
        demandas = new ArrayList<String>();
        String aux;
        for (int i = 40; i < 73; i++) {
            aux = principal.get(i);
            separa = aux.split(" ");
            demandas.add(separa[1]);
        }
        return demandas;
    }

    /**
     * Método que separa as informações de uma lista não formata de indivíduos e
     * cria uma lista com indivíduos com informações separadas.
     *
     * @param demandaX lista com demanda de cada individuo.
     * @param individuoX lista de indivíduos.
     * @param tamanho quantidade de elementos a serem estraídos da lista de
     * indivíduos para criação do indivíduo com demanda, coordenadas e
     * identificador.
     * @return lista de indvíduos formatada.
     */
    public ArrayList<Individuo> trataCoordenadas(ArrayList<String> demandaX, ArrayList<String> individuoX, int tamanho) {
        String aux;
        ArrayList<Individuo> ind = new ArrayList<Individuo>();
        separa = new String[tamanho];
        for (int i = 0; i < individuoX.size(); i++) {
            aux = individuoX.get(i);
            separa = aux.split(" ");
            Individuo individuo = new Individuo(Integer.parseInt(separa[1]), Integer.parseInt(separa[2]),
                    Integer.parseInt(separa[3]), Integer.parseInt(demandaX.get(i)));
            ind.add(individuo);
            this.individuos.add(individuo);
        }
        return ind;
    }

    /**
     * Método que através das coordenadas dos indivíduos preenche uma matriz de
     * distâncias que será utilizada para definir o melhor caminho a se seguir.
     *
     * @param individuoX lista com indivíduos de onde serão extraídas as
     * coordenadas necessárias para o preenchimento da matriz.
     * @return matriz preenchida com coordenadas e valor entre distâncias.
     */
    public int[][] preencheMatriz(ArrayList<Individuo> individuoX) {
        int raiz;
        int px, py, qx, qy;
        for (int i = 0; i < individuoX.size(); i++) {
            for (int j = 0; j < individuoX.size(); j++) {
                px = individuoX.get(i).getCoX();
                py = individuoX.get(i).getCoY();
                qx = individuoX.get(j).getCoX();
                qy = individuoX.get(j).getCoY();
                raiz = (int) Math.sqrt((Math.pow(px - qx, 2)) + Math.pow(py - qy, 2));
                matriz[i][j] = raiz;
            }
        }
        return matriz;
    }

    /**
     * Método responsável por gerar rota a partir da demanda de cada cliente de
     * da capacidade de cada veículo. É onde será definido quantas vezes será
     * necessário retornar ao depósito.
     *
     * @param capacidade utilizado para definir quando é necessário voltar ao
     * depósito caso a capacidade do veículo seja alcançada.
     * @param individuoX lista com indivíduos utilziada para gerar as rotas a
     * partir da demanda de cada um.
     * @return caminho ao qual deve ser seguido.
     */
    public ArrayList<Integer> geraCaminho(int capacidade, ArrayList<Individuo> individuoX) {
        int aux = 0;
        caminhoX = new ArrayList<Integer>();
        Collections.shuffle(individuoX);
        caminhoX.add(1);
        for (int i = 0; i < individuoX.size(); i++) {
            if (individuoX.get(i).getId() != 1) {
                aux += individuoX.get(i).getDemanda();
                if (aux <= capacidade) {
                    caminhoX.add(individuoX.get(i).getId());
                } else {
                    caminhoX.add(1);
                    aux = 0;
                    aux += individuoX.get(i).getDemanda();
                    caminhoX.add(individuoX.get(i).getId());
                }
            }
        }
        caminhoX.add(1);
        return caminhoX;
    }

    /**
     * Método que avalia a qualidade de cada caminho gerado. Essa qualidade é
     * utilizada para conseguir encontrar o melhor caminho.
     *
     * @param caminhoY caminho ao qual deve-se avaliar a qualidade.
     * @param matriz matriz com distâncias entre cada indivíduo.
     * @return qualidade do caminhoX.
     */
    public int avaliaQualidade(ArrayList<Integer> caminhoY, int[][] matriz) {
        int qualidade = 0;
        for (int i = 0; i < caminhoY.size(); i++) {
            if (i == caminhoY.size() - 1) {
                return qualidade;
            } else {
                qualidade += matriz[caminhoY.get(i) - 1][caminhoY.get(i + 1) - 1];
            }
        }
        return qualidade;
    }

    /**
     * Método que retira os depósitos do caminho em questão para realizar
     * operações de crossover, mutação e busca local. Metódo utilizado para
     * facilitar as operações e impedir que o depósitos atrapalhem as operações.
     *
     * @param caminhoY caminho ao qual deve-se retirar os depósitos.
     * @return caminho sem depósitos.
     */
    public ArrayList<Integer> retiraDepositos(ArrayList<Integer> caminhoY) {
        one = new ArrayList<Integer>();
        one.add(1);
        caminhoY.removeAll(one);

        return caminhoY;
    }

    /**
     * Método que irá realizar a mutação de um indivíduo em duas posições
     * aleatórias.
     *
     * @param posicao posição do indivíduo que será mutado na lista de caminhos.
     */
    public void fazMutacao(int posicao) {
        int e1, e2;
        e1 = this.aleatorio.nextInt(this.caminho.get(posicao).size() - 1);
        e2 = this.aleatorio.nextInt(this.caminho.get(posicao).size() - 1);
        while (e1 == e2) {
            e2 = this.aleatorio.nextInt(this.caminho.get(posicao).size() - 1);
        }
        Collections.swap(this.caminho.get(posicao), e1, e2);
    }

    /**
     * Método responsável por cortar os dois caminhos selecionados. Nesse método
     * são criados dois novos caminhos a partir do corte feito.
     *
     * @param caminhoA caminho para fazer crossover.
     * @param caminhoB caminho para fazer crossover.
     * @param capacidade utilizada para o método posterior.
     */
    public void fazCrossover(ArrayList<Integer> caminhoA,
            ArrayList<Integer> caminhoB, int capacidade) {
        int corteAleatorio;
        caminhoI = new ArrayList<>();
        caminhoII = new ArrayList<>();
        caminhoI = retiraDepositos(caminhoA);
        caminhoII = retiraDepositos(caminhoB);
        corteAleatorio = this.aleatorio.nextInt(caminhoI.size());
        for (int i = corteAleatorio; i < caminhoI.size(); i++) {
            camA.add(caminhoI.get(i));
        }
        for (int j = 0; j < corteAleatorio; j++) {
            camA.add(caminhoI.get(j));
        }
        for (int l = corteAleatorio; l < caminhoII.size(); l++) {
            camB.add(caminhoII.get(l));
        }
        for (int m = 0; m < corteAleatorio; m++) {
            camB.add(caminhoII.get(m));
        }
        excluiRepetidos(corteAleatorio, caminhoI, caminhoII, capacidade);
        camA.clear();
        camB.clear();
    }

    /**
     * Método utilizado para retirar indivíduos repetidos de um caminho. Esse
     * método é feito a partir de uma lista de visitação.
     *
     * @param corteAleatorio onde o caminho foi cortado para geração de novos
     * caminhos.
     * @param caminhoA caminho gerado anteriormente de onde serão tirados os
     * individuos repetidos.
     * @param caminhoB caminho gerado anteriormente de onde serão tirados os
     * individuos repetidos.
     * @param capacidade utilizada para o método posterior.
     */
    public void excluiRepetidos(int corteAleatorio, ArrayList<Integer> caminhoA,
            ArrayList<Integer> caminhoB, int capacidade) {
        for (int i = 0; i < corteAleatorio; i++) {
            this.camAA.add(caminhoA.get(i));
        }

        for (int k = 0; k < corteAleatorio; k++) {
            for (int n = corteAleatorio; n < caminhoB.size(); n++) {
                if (Objects.equals(caminhoA.get(k), caminhoB.get(n))) {
                    for (int i = 0; i < this.camB.size(); i++) {
                        if (Objects.equals(this.camB.get(i), caminhoA.get(k))) {
                            this.camB.remove(i);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < this.camAA.size(); i++) {
            for (int j = 0; j < this.camB.size(); j++) {
                if (Objects.equals(this.camAA.get(i), this.camB.get(j))) {
                    this.camB.remove(j);
                }
            }
        }
        for (int i = 0; i < this.camB.size(); i++) {
            this.camAA.add(this.camB.get(i));
        }

        for (int j = 0; j < corteAleatorio; j++) {
            this.camBB.add(caminhoB.get(j));
        }
        for (int k = 0; k < corteAleatorio; k++) {
            for (int n = corteAleatorio; n < caminhoA.size(); n++) {
                if (Objects.equals(caminhoB.get(k), caminhoA.get(n))) {
                    for (int i = 0; i < this.camA.size(); i++) {
                        if (Objects.equals(this.camA.get(i), caminhoA.get(n))) {
                            this.camA.remove(i);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < this.camBB.size(); i++) {
            for (int j = 0; j < this.camA.size(); j++) {
                if (Objects.equals(this.camBB.get(i), this.camA.get(j))) {
                    this.camA.remove(j);
                }
            }
        }
        for (int i = 0; i < this.camA.size(); i++) {
            this.camBB.add(this.camA.get(i));
        }

        this.caminho.add(colocaDepositos(camBB, capacidade));
        this.caminho.add(colocaDepositos(camAA, capacidade));
        this.camBB.clear();
        this.camAA.clear();
    }

    /**
     * Método para inserir os depósitos no caminho após as operações aplicadas a
     * ele.
     *
     * @param caminhoY caminho ao qual os depósitos serão inseridos.
     * @param capacidade capacidade de cada veículo para definir quando o
     * veículo deve voltar ao depósito e assim o inserir no caminho.
     * @return caminhoY com depósitos inseridos.
     */
    public ArrayList<Integer> colocaDepositos(ArrayList<Integer> caminhoY, int capacidade) {
        int aux = 0;
        this.caminhos = new ArrayList<>();
        this.caminhos.add(1);

        for (int i = 0; i < this.individuos.size() - 1; i++) {
            if ((aux + this.individuos.get(caminhoY.get(i) - 1).getDemanda()) <= capacidade) {
                this.caminhos.add(caminhoY.get(i));
                aux += this.individuos.get(caminhoY.get(i) - 1).getDemanda();
            } else {
                this.caminhos.add(1);
                this.caminhos.add(caminhoY.get(i));
                aux = 0;
                aux += this.individuos.get(caminhoY.get(i) - 1).getDemanda();
            }
        }

        if ((this.caminhos.size() - 1) != 1) {
            this.caminhos.add(1);
        }
        return this.caminhos;
    }

    /**
     * Método que faz a busca local e modifica a posicação de quatro indivíduos
     * no caminho. Dois indivíduos serão aleatóriamente escolhidos e o primeiro
     * sucessor de cada um deles será um dos outros dois indivíduos.
     *
     * @param capacidade utilizada para inserir os depósitos após a operação.
     * @param posicao caminho selecionado para realizar a mutacao.
     */
    public void buscaLocal(int capacidade, int posicao) {
        int posI, posII;
        this.caminho.set(posicao, retiraDepositos(this.caminho.get(posicao)));

        posI = this.aleatorio.nextInt(this.caminho.get(posicao).size() - 1);
        posII = this.aleatorio.nextInt(this.caminho.get(posicao).size() - 1);
        while (posI == posII) {
            posII = this.aleatorio.nextInt(this.caminho.get(posicao).size() - 1);
        }
        if ((posI + 1) == this.caminho.get(posicao).size() - 1) {
            Collections.swap(this.caminho.get(posicao), posI, 0);
        } else {
            Collections.swap(this.caminho.get(posicao), posI, posI + 1);
        }

        if ((posII + 1) == this.caminho.get(posicao).size() - 1) {
            Collections.swap(this.caminho.get(posicao), posII, 0);
        } else {
            Collections.swap(this.caminho.get(posicao), posII, posII + 1);
        }
        this.caminho.set(posicao, colocaDepositos(this.caminho.get(posicao), capacidade));
    }

    public void gap(int melhorQualidade) throws FileNotFoundException, IOException {
        ArrayList<String> arquivo;
        arquivo = new ArrayList<String>();
        int contador = 0;
        double qualidadeEsperada, diferenca;
        qualidadeEsperada = 0.0;
        try (FileInputStream entrada = new FileInputStream("C:/Users/olive/Documents/Testes_VPRM/A/A-n33-k5.opt"); InputStreamReader leitura = new InputStreamReader(entrada)) {
            BufferedReader le = new BufferedReader(leitura);
            String linha = le.readLine();

            while (linha != null) {
                linha = le.readLine();
                arquivo.add(linha);
                contador++;
                if (contador == 5) {
                    String auxiliar = linha.replaceAll("cost ", "");
                    qualidadeEsperada = Double.parseDouble(auxiliar);
                }
            }
        }

        System.out.println("Solução desejada: " + (int) qualidadeEsperada);
        diferenca = ((Double.parseDouble(Integer.toString(melhorQualidade)) - qualidadeEsperada) / qualidadeEsperada) * 100;
        System.out.printf("Diferença da solução encontrada para a melhor solução: %.2f %%", diferenca);
    }
    
    public void imprimeMatriz(){
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println(" ");
        }
    }
}
