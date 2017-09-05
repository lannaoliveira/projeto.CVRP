/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufop.br.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import ufop.br.desenvolvimento.Desenvolvimento;
import ufop.br.objetos.Caminho;
import ufop.br.objetos.Individuo;
import ufop.br.objetos.ComparaQualidade;
import ufop.br.objetos.ComparaRank;

/**
 * Classe responsável pela estrutura do problema, chamando os métodos
 * necessários na ordem correta para tentar solucioná-lo.
 *
 * @author Lanna
 */
public class ProjetoVRP {

    ArrayList<ArrayList<Integer>> crossover, mutacao;
    ArrayList<String> arquivo, individuos, demandas;
    ArrayList<Individuo> individuo;
    ArrayList<Caminho> c, caminho, caminhoI, caminhoII, melhor;
    int dimensao, capacidade;
    Random aleatorio;
    int[][] matriz;
    Caminho caminhoIII, melhores;

    /**
     * Construtor para inicialização das variáveis globais.
     */
    public ProjetoVRP() {
        this.aleatorio = new Random();
        this.capacidade = 0;
        this.dimensao = 0;
        this.crossover = new ArrayList<ArrayList<Integer>>();
        this.mutacao = new ArrayList<ArrayList<Integer>>();
        this.caminho = new ArrayList<Caminho>();
        this.c = new ArrayList<Caminho>();
        this.caminhoI = new ArrayList<Caminho>();
        this.caminhoII = new ArrayList<Caminho>();
        this.melhor = new ArrayList<Caminho>();
        this.arquivo = new ArrayList<String>();
        this.individuos = new ArrayList<String>();
        this.demandas = new ArrayList<String>();
        this.individuo = new ArrayList<Individuo>();
    }

    /**
     * Nesse método o programa é estruturado chamando os outros métodos da
     * classe Desenvolvimento na ordem que devem ser executados.
     *
     * @throws java.io.FileNotFoundException utilizada se o arquivo for
     * inválido.
     */
    public void inicio() throws FileNotFoundException, IOException {

        Caminho cam;
        Desenvolvimento d;
        int contadorA = 0, contadorB = 0, qtdCaminhos = 100, posicaoBest = 0;
        int mutacao, best, qualidade, aleatorioA, aleatorioB;
        long criterio = 300;
        double percentC = 0.8, percentM = 0.05;
        double percentCi = 0, percentMi = 0;
        String auxiliar;

        /**
         * Leitura do arquivo para testes.
         */
        FileInputStream entrada = new FileInputStream("C:/Users/olive/Documents/Testes_VPRM/A/A-n33-k5.vrp");
        try (InputStreamReader leitura = new InputStreamReader(entrada)) {
            BufferedReader le = new BufferedReader(leitura);
            String linha = le.readLine();

            while (linha != null) {
                linha = le.readLine();
                this.arquivo.add(linha);
                contadorA++;
                if (contadorA == 3) {
                    auxiliar = linha.replaceAll("DIMENSION : ", "");
                    this.dimensao = Integer.parseInt(auxiliar);
                }
                if (contadorA == 5) {
                    auxiliar = linha.replaceAll("CAPACITY : ", "");
                    this.capacidade = Integer.parseInt(auxiliar);
                }
            }

            d = new Desenvolvimento(dimensao);
            this.individuos = d.trataIndividuo(this.arquivo);
            this.demandas = d.trataDemanda(this.arquivo, this.dimensao);
            this.individuo = d.trataCoordenadas(this.demandas, this.individuos, this.dimensao);
            this.matriz = d.preencheMatriz(this.individuo);

             /*
            TESTE BEST SOLUTION            
            */
            // Melhor solução
            int [] solution = {1, 15, 17, 9, 3, 16, 29, 1, 12, 5, 26, 7, 8, 13, 32, 2, 1, 20, 4, 27, 25, 30, 10, 1, 23, 28, 18, 22, 1, 24, 6, 19, 14, 21, 1, 31, 11, 1};            
            ArrayList<Integer> bestCaminho = new ArrayList<>();
            for(int s : solution) {
                bestCaminho.add(s);
            }

            int bestCost = d.avaliaQualidade(bestCaminho, this.matriz);
            System.out.println(bestCaminho);
            System.out.println(bestCost); // O correto é 661
            
            d.imprimeMatriz();
           
            if (true) // Forçar saída e evitar erro de compilação no Netbeans
                return;
            
            /**
             * Laço responsável por gerar novas rotas. Só é acessado no início
             * do programa e gera a quantidade de acordo com a escolha do
             * usuário.
             */
            for (int r = 0; r < qtdCaminhos; r++) {
                cam = new Caminho(d.geraCaminho(this.capacidade, this.individuo), 0, 0);
                while (this.caminho.contains(cam)) {
                    cam = new Caminho(d.geraCaminho(this.capacidade, this.individuo), 0, 0);
                }

                this.caminho.add(cam);
                this.caminho.get(r).setQualidade(d.avaliaQualidade(caminho.get(r).getCaminho(), this.matriz));
            }

            /**
             * Laço responsável por guardar a melhor rota inicial. Essa é
             * definida pela qualidade que conter o menor valor. Esse laço só
             * será executado no início do programa.
             */
            best = this.caminho.get(0).getQualidade();
            for (int h = 0; h < this.caminho.size(); h++) {
                if (best > this.caminho.get(h).getQualidade()) {
                    best = this.caminho.get(h).getQualidade();
                    posicaoBest = h;
                }
            }

            /**
             * A lista de melhores é uma instância da classe Caminho, e servirá
             * de auxílio para a lista melhor, que irá conter todos as melhores
             * rotas encontradas durante o desenvolvimento. Inicialmente
             * armazena a melhor rota encontrada.
             */
            this.melhores = new Caminho(this.caminho.get(posicaoBest).getCaminho(), best, 0);
            this.melhor.add(melhores);

            /**
             * Esse laço é responsável por fazer a quantidade de iterações
             * escolhida pelo usuário para procurar a melhor solução perto da
             * solução ótima.
             */
            do {
                /**
                 * Laço auxiliar utilizado para guardar os primeiros individuos
                 * criados.
                 */
                for (int i = 0; i < this.caminho.size(); i++) {
                    qualidade = d.avaliaQualidade(this.caminho.get(i).getCaminho(), matriz);
                    cam = new Caminho(this.caminho.get(i).getCaminho(), qualidade, 0);
                    this.c.add(cam);
                }

                /**
                 * Laço responsável por realizar o crossover. Nesse laço são
                 * gerados números para escolher dois indivíduos de forma
                 * aleatória. Esse processo é feito até que a porcentagem
                 * definida seja alcançada.
                 */
                for (int i = 0; i < qtdCaminhos; i++) {
                    aleatorioA = this.aleatorio.nextInt(this.caminho.size() - 1);
                    aleatorioB = this.aleatorio.nextInt(this.caminho.size() - 1);
                    if (percentCi <= percentC) {
                        while (aleatorioA == aleatorioB) {
                            aleatorioB = this.aleatorio.nextInt(this.caminho.size() - 1);
                        }
                        d.fazCrossover(this.caminho.get(aleatorioA).getCaminho(), this.caminho.get(aleatorioB).getCaminho(), this.capacidade);
                    }
                    percentCi = i * 0.01;
                }

                /**
                 * Laço responsável por realizar a mutação. Nesse laço é gerado
                 * um número para realizar a mutação em um indivíduo escolhido
                 * aleatoriamente. Novos indivíduos são escolhidos até alcançar
                 * a porcentagem definida.
                 */
                for (int k = 0; k < qtdCaminhos; k++) {
                    mutacao = aleatorio.nextInt(qtdCaminhos - 1);
                    if (percentMi <= percentM) {
                        d.fazMutacao(mutacao);
                    }
                    percentMi = k * 0.01;
                }

                /**
                 * Laço responsável por realizar a busca local. Essa busca é
                 * realizada em todos os indivíduos da lista.
                 */
                for (int b = 0; b < d.caminho.size(); b++) {
                    d.buscaLocal(this.capacidade, b);
                }

                /**
                 * Laço responsável por adicionar todos os caminhos criados
                 * antes das operações com os que sofreram algum tipo de
                 * modificação.
                 */
                for (int i = 0; i < this.c.size(); i++) {
                    qualidade = d.avaliaQualidade(this.c.get(i).getCaminho(), matriz);
                    caminhoIII = new Caminho(this.c.get(i).getCaminho(), qualidade, 0);
                    this.caminhoI.add(caminhoIII);
                }

                /**
                 * Laço responsável por modificar a qualidade de cada novo
                 * caminho criado e adicioná-lo a lista de caminhos;
                 */
                for (int j = 0; j < d.caminho.size(); j++) {
                    qualidade = d.avaliaQualidade(d.caminho.get(j), matriz);
                    caminhoIII = new Caminho(d.caminho.get(j), qualidade, 0);
                    this.caminhoI.add(caminhoIII);
                }

                this.caminho.clear();
                this.c.clear();
                d.caminho.clear();
                mergeSort(1);

                for (int w = 0; w < this.caminhoI.size(); w++) {
                    rankingLinear(w);
                }

                seleciona();
                mergeSort(3);
                cortamelhores(qtdCaminhos);
                this.melhores = new Caminho(this.caminho.get(0).getCaminho(), this.caminho.get(0).getQualidade(), 0);
                this.melhor.add(melhores);
                contadorB++;
                this.caminhoI.clear();
                this.caminhoII.clear();
            } while (contadorB != criterio);
            mergeSort(2);
            System.out.println("Melhor solução inicial: " + best);
            System.out.println("Melhor solução encontrada: " + this.melhor.get(0).getQualidade());
            d.gap(this.melhor.get(0).getQualidade());
            System.out.println("\nCaminho: " + this.melhor.get(0).getCaminho());
            entrada.close();
            System.out.println("\n");
        }
    }

    /**
     * Método responsável por ordenar os indivíduos.
     *
     * @param indice utilizado para definir qual lista será ordenada.
     */
    public void mergeSort(int indice) {
        switch (indice) {
            case 1:
                Collections.sort(this.caminhoI, new ComparaQualidade());
                break;
            case 2:
                Collections.sort(this.melhor, new ComparaQualidade());
                break;
            default:
                Collections.sort(this.caminhoII, new ComparaRank());
                break;
        }
    }

    public void rankingLinear(int posicao) {
        double rand = 2.0;
        this.caminhoI.get(posicao).setRank(((2 - rand) + 2 * (rand - 1)) * ((posicao - 1) / this.caminhoI.size() - 1));
    }

    public void seleciona() {
        Caminho ce;
        for (int i = 0; i < this.caminhoI.size(); i++) {
            if (i == 0) {
                ce = new Caminho(this.caminhoI.get(i).getCaminho(), this.caminhoI.get(i).getQualidade(),
                        this.caminhoI.get(i).getRank());
                this.caminhoII.add(ce);
            } else if (i == (this.caminhoI.size() - 1)) {
                ce = new Caminho(this.caminhoI.get(i).getCaminho(), this.caminhoI.get(i).getQualidade(),
                        this.caminhoI.get(i).getRank());
                this.caminhoII.add(ce);
            } else {
                if (this.caminhoI.get(i) != this.caminhoI.get(i + 1)) {
                    ce = new Caminho(this.caminhoI.get(i).getCaminho(), this.caminhoI.get(i).getQualidade(),
                            this.caminhoI.get(i).getRank());
                    this.caminhoII.add(ce);
                }
            }
        }
    }

    /**
     * Se a nova população tiver mais caminhos que a quantidade estabelecida
     * inicialmente ela é reduzida.
     *
     * @param qtdCaminhos é usado para delimitar o tamanho da nova população.
     */
    private void cortamelhores(int qtdCaminhos) {
        Caminho ca;
        if (this.caminhoII.size() > qtdCaminhos) {
            for (int i = 0; i < qtdCaminhos; i++) {
                ca = new Caminho(this.caminhoII.get(i).getCaminho(), this.caminhoII.get(i).getQualidade(),
                        this.caminhoII.get(i).getRank());
                this.caminho.add(ca);
            }
        }
    }
}
