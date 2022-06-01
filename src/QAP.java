/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.jfree.ui.RefineryUtilities;
import src.QAP.Cromosoma;

/**
 *
 * @author Usuario
 */
public class QAP {

    int tam = 0;//tamaño del problema
    double[][] flujo;
    double[][] distancia;
    public int Ev = 0;//veces que se llama a la funcion coste

    public ArrayList<double[]> seguimiento = new ArrayList<double[]>();

    /**
     * @param args the command line arguments
     */
    public QAP() {//ESTO VALE SOLO PARA INSTANCIAR LA TABLA
    }

    ;
    public QAP(String nombre) {
        File fichero = new File(nombre);
        String texto = new String("");
        Scanner s = null;

        try {

            s = new Scanner(fichero);

            // Leemos linea a linea el fichero
            while (s.hasNextLine()) {
                String linea = s.nextLine(); 	// Guardamos la linea en un String
                texto = texto + linea;      // Imprimimos la linea
            }

            StringTokenizer parser = new StringTokenizer(texto);

            tam = Integer.parseInt(parser.nextToken());
            flujo = new double[tam][tam];
            distancia = new double[tam][tam];
            for (int i = 0; i < tam; i++) {
                for (int j = 0; j < tam; j++) {

                    distancia[i][j] = Integer.parseInt(parser.nextToken());

                }
            }
            for (int i = 0; i < tam; i++) {
                for (int j = 0; j < tam; j++) {
                    flujo[i][j] = Integer.parseInt(parser.nextToken());
                }
            }

        } catch (Exception ex) {
            System.out.println("Mensaje: " + ex.getMessage());
        } finally {

            try {
                if (s != null) {
                    s.close();
                }
            } catch (Exception ex2) {
                System.out.println("Mensaje 2: " + ex2.getMessage());
            }
        }
    }

    public void probarsolucion(String nombre) {
        File fichero = new File(nombre);
        String texto = new String("");
        Scanner s = null;
        double sol;

        try {

            s = new Scanner(fichero);

            while (s.hasNextLine()) {
                String linea = s.nextLine(); 	// Guardamos la linea en un String
                texto = texto + linea;      // Imprimimos la linea
            }

            StringTokenizer parser = new StringTokenizer(texto);

            tam = Integer.parseInt(parser.nextToken());

            int[] pos = new int[tam];
            sol = Integer.parseInt(parser.nextToken());

            for (int i = 0; i < tam; i++) {

                pos[i] = Integer.parseInt(parser.nextToken());

            }

            System.out.println("El coste calculado es " + this.coste(pos) + " y el real es " + sol);

        } catch (Exception ex) {
            System.out.println("Mensaje: " + ex.getMessage());
        } finally {

            try {
                if (s != null) {
                    s.close();
                }
            } catch (Exception ex2) {
                System.out.println("Mensaje 2: " + ex2.getMessage());
            }
        }
    }

    public double coste(int[] sol) {
        //sol [0 1 2] al lugar 0 se le asigna la unidad 0 etc

        double coste = 0;

        for (int i = 0; i < tam; i++) {
            for (int j = 0; j < tam; j++) {

                coste += flujo[sol[i] - 1][sol[j] - 1] * distancia[i][j];
            }
        }
        Ev++;
        return coste;
    }

    public double coste(int[] sol, int i, int j, double coste_previo) {
        //sol [0 1 2] al lugar 0 se le asigna la unidad 0 etc

        double coste = 0;

        for (int k = 0; k < tam; k++) {
            if (k != i && k != j) {
                coste += distancia[i][k] * (flujo[sol[j] - 1][sol[k] - 1] - flujo[sol[i] - 1][sol[k] - 1])
                        + distancia[j][k] * (flujo[sol[i] - 1][sol[k] - 1] - flujo[sol[j] - 1][sol[k] - 1])
                        + distancia[k][i] * (flujo[sol[k] - 1][sol[j] - 1] - flujo[sol[k] - 1][sol[i] - 1])
                        + distancia[k][j] * (flujo[sol[k] - 1][sol[i] - 1] - flujo[sol[k] - 1][sol[j] - 1]);
            }
        }

        Ev++;
        return coste_previo - coste;
    }

    public int[] Dos_opt(int[] sol, int i, int j) {
        // cambia la posicion i por la posicion j
        int[] sol_aux = sol.clone();

        int aux = sol_aux[i];
        sol_aux[i] = sol_aux[j];
        sol_aux[j] = aux;

        return sol_aux;
    }

    public int[] BusquedaGreedy() {
        int[] sol_final = new int[tam];
        int tam_sol = 0;

        //generacion de candidatos
        List<Integer> lugares_candidatos = new ArrayList<Integer>();
        List<Integer> unidades_candidatos = new ArrayList<Integer>();
        for (int i = 0; i < tam; i++) {
            lugares_candidatos.add(i);
            unidades_candidatos.add(i + 1);
        }
        while (tam_sol < tam) {
            //funcion de seleccion

            //mejor lugar
            double menor_dist = 0;
            int pos_mejor_lugar = -1;
            int mejor_lugar;
            for (int p = 0; p < tam; p++) {
                if (lugares_candidatos.get(p) != -1) {
                    double suma = 0;
                    for (int i = 0; i < tam; i++) {
                        suma += distancia[lugares_candidatos.get(p)][i];
                    }
                    if (pos_mejor_lugar == -1 || suma < menor_dist) {
                        pos_mejor_lugar = p;
                        menor_dist = suma;
                    }

                }

            }

            mejor_lugar = lugares_candidatos.get(pos_mejor_lugar);
            lugares_candidatos.set(pos_mejor_lugar, -1);

            //mejor unidad
            double mayor_flujo = 0;
            int mejor_unidad = -1;
            int pos_mejor_unidad = -1;

            for (int p = 0; p < tam; p++) {
                if (unidades_candidatos.get(p) != -1) {
                    double suma = 0;
                    for (int i = 0; i < tam; i++) {
                        suma += flujo[unidades_candidatos.get(p) - 1][i] + flujo[i][unidades_candidatos.get(p) - 1];
                    }
                    if (pos_mejor_unidad == -1 || suma > mayor_flujo) {
                        pos_mejor_unidad = p;
                        mayor_flujo = suma;
                    }
                }

            }
            mejor_unidad = unidades_candidatos.get(pos_mejor_unidad);
            unidades_candidatos.set(pos_mejor_unidad, -1);

            sol_final[mejor_lugar] = mejor_unidad;
            tam_sol++;
        }
        return sol_final;

    }

    public int[] BusquedaAleatoria(int n, int seed) {
        Random r = new Random(seed);
        int posicion;
        int[] sol_final = new int[tam];
        double costefinal = 0.0;
        for (int i = 0; i < n; i++) {
            int[] sol = new int[tam];
            for (int j = 1; j < tam + 1; j++) {
                do {
                    posicion = r.nextInt(tam);

                } while (sol[posicion] != 0);

                sol[posicion] = j;
            }
            double costeactual = coste(sol);

            if (i == 0 || costeactual < costefinal) {
                sol_final = sol.clone();
                costefinal = costeactual;
            }

        }
        return sol_final;
    }

    public int[] BusquedaLocalMejor(int seed) {
        //Genera solucion
        int[] solucion = BusquedaAleatoria(1, seed);
        int[] mejor_vecino = solucion.clone();
        int[] sol_vecina = solucion.clone();

        double coste_vecino;
        double coste_mejor_vecino;
        double coste_solucion;

        coste_mejor_vecino = coste(mejor_vecino);
        coste_solucion = coste_mejor_vecino;
        coste_vecino = coste_mejor_vecino;

        do {

            solucion = mejor_vecino.clone();
            coste_solucion = coste_mejor_vecino;

            for (int i = 0; i < tam; i++) {
                for (int j = 0; j < tam; j++) {
                    sol_vecina = Dos_opt(solucion, i, j);

                    coste_vecino = coste(sol_vecina, i, j, coste_solucion);

                    if (coste_vecino < coste_mejor_vecino) {
                        mejor_vecino = sol_vecina.clone();
                        coste_mejor_vecino = coste_vecino;
                    }
                }
            }

        } while (coste_mejor_vecino < coste_solucion);
        return (solucion);

    }

    public int[] BusquedaLocalMejor(int seed, int[] inicio) {
        //Genera solucion
        Random r = new Random(seed);
        int limit = (tam * (tam - 1)) / 2;
        int[] solucion = inicio.clone();
        int[] mejor_vecino = solucion.clone();
        int[] sol_vecina = solucion.clone();

        double coste_vecino;
        double coste_mejor_vecino;
        double coste_solucion;

        coste_mejor_vecino = coste(mejor_vecino);
        coste_solucion = coste_mejor_vecino;
        coste_vecino = coste_mejor_vecino;

        do {

            solucion = mejor_vecino.clone();
            coste_solucion = coste_mejor_vecino;

            for (int l = 0; l < limit; l++) {
                int i = r.nextInt(tam);
                int j = r.nextInt(tam);
                sol_vecina = Dos_opt(solucion, i, j);

                coste_vecino = coste(sol_vecina, i, j, coste_solucion);

                if (coste_vecino < coste_mejor_vecino) {
                    mejor_vecino = sol_vecina.clone();
                    coste_mejor_vecino = coste_vecino;
                }
            }

        } while (coste_mejor_vecino < coste_solucion);
        return (solucion);

    }

    public static String atexto(int[] s, int tam) {
        String a = new String("[");
        for (int i = 0; i < tam; i++) {
            a += s[i] + ",";
        }
        a += "]";
        return a;
    }

    int[] sublista(int[] sol, int s, Random r) {
        int[] solucion = sol.clone();

        int s0 = r.nextInt(tam + 1 - s);//mas uno porque la posicion que generamos sirve para guardar
        int s1;

        do {
            s1 = r.nextInt(tam + 1 - s);
        } while (s1 > s0 - s && s1 < s0 + s);

        // tendriamos los dos indices
        List<Integer> sublista = new LinkedList<Integer>();

        for (int j = s0; j < s0 + s; j++) {
            sublista.add(solucion[j]);

        }

        for (int j = s1; j < s1 + s; j++) {
            sublista.add(solucion[j]);

        }

        Collections.shuffle(sublista);

        //Ya estan en una sola lista ambas sublistas y reordenadas, ahora hay que ponerlas en su sitio
        for (int j = s0; j < s0 + s; j++) {

            solucion[j] = sublista.remove(0);

        }

        for (int j = s1; j < s1 + s; j++) {
            solucion[j] = sublista.remove(0);

        }

        return solucion;
    }

    public class Cromosoma {

        int[] sol;
        double coste;

        public Cromosoma(int[] sol) {
            this.sol = sol.clone();
            coste = coste(sol);
        }

        public void recalcular() {
            coste = coste(sol);
        }

        @Override
        public Cromosoma clone() {
            Cromosoma n = new Cromosoma(sol);
            return n;
        }

        @Override
        public boolean equals(Object o) {
            Cromosoma aux = (Cromosoma) o;
            boolean s = true;
            for (int i = 0; i < tam; i++) {
                s = s & (aux.sol[i] == this.sol[i]);
            }
            return s;
        }

        public int distancia_hamming(Cromosoma a) {
            int h = 0;
            for (int i = 0; i < tam; i++) {
                if (this.sol[i] != a.sol[i]) {
                    h++;
                }
            }
            return h;
        }

        public Cromosoma cruceOX_disruptivo(Cromosoma a, Cromosoma b, Random r) {//DEBERIAS SER ESTATICO
            // sublista de a 
            // eliminamos los elementos de la sublista de b (copia)
            // rellenamos los elementos sin marcar con lo que queda en b

            //System.out.println("PADRE "+QAP.atexto(a.sol, tam));
            //System.out.println("MADRE "+QAP.atexto(b.sol, tam));
            int n = tam / 4;

            Cromosoma a_copia = a.clone();
            List<Integer> aux = new LinkedList<Integer>();
            int comienzo;
            do {
                comienzo = r.nextInt(tam);
            } while (comienzo + n > tam);

            //vamos a guardar en aux los elementos de la sublista
            for (int i = 0; i < tam; i++) {
                if (i >= comienzo && i < comienzo + n) {
                    //es de los que necesitamos
                    aux.add(a_copia.sol[i]);
                } else {
                    a_copia.sol[i] = -1;
                }

            }
            //vamos a guardar en aux2 los elementos de b que no estan en aux1
            List<Integer> aux2 = new LinkedList<Integer>();
            for (int i = 0; i < tam; i++) {
                if (!aux.contains(b.sol[i])) {
                    aux2.add(b.sol[i]);
                }
            }
            //construimos finalmente la solucion
            for (int i = 0; i < tam; i++) {
                if (a_copia.sol[i] == -1) {
                    a_copia.sol[i] = aux2.remove(0);
                }
            }

            a_copia.recalcular();
            //System.out.println("HIJO "+QAP.atexto(a_copia.sol, tam));
            return a_copia.clone();

        }

        public Cromosoma cruceOX_suavizado(Cromosoma a, Cromosoma b, Random r) {//DEBERIAS SER ESTATICO
            // sublista de a 
            // eliminamos los elementos de la sublista de b (copia)
            // rellenamos los elementos sin marcar con lo que queda en b

            //System.out.println("PADRE "+QAP.atexto(a.sol, tam));
            //System.out.println("MADRE "+QAP.atexto(b.sol, tam));
            int n = tam / 4;

            Cromosoma a_copia = a.clone();
            List<Integer> aux = new LinkedList<Integer>();
            int comienzo;
            do {
                comienzo = r.nextInt(tam);
            } while (comienzo + n > tam);

            //vamos a guardar en aux los elementos de la sublista
            for (int i = 0; i < tam; i++) {
                if (i >= comienzo && i < comienzo + n) {
                    //es de los que necesitamos
                    aux.add(a_copia.sol[i]);
                } else {
                    a_copia.sol[i] = -1;
                }

            }
            //vamos a guardar en aux2 los elementos de b que no estan en aux1
            List<Integer> aux2 = new LinkedList<Integer>();

            for (int i = 0; i < tam; i++) {
                if (!aux.contains(b.sol[i])) {
                    aux2.add(b.sol[i]);

                }
            }

            //construimos finalmente la solucion
            List<Integer> a_pero_en_lista = new ArrayList<Integer>();
            aux = new ArrayList<Integer>();
            for (int i = 0; i < tam; i++) {
                a_pero_en_lista.add(a.sol[i]);
            }
            int l = aux2.size();

            for (int i = 0; i < l; i++) {
                int elem = aux2.remove(0);

                int pos = a_pero_en_lista.indexOf(elem);

                if (a_copia.sol[pos] == -1) {
                    a_copia.sol[pos] = elem;
                } else {
                    aux.add(elem);
                }
            }

            for (int i = 0; i < tam; i++) {
                if (aux2.size() > 0 && a_copia.sol[i] == -1) {
                    a_copia.sol[i] = aux2.remove(0);
                }
            }

            a_copia.recalcular();
            //System.out.println("HIJO "+QAP.atexto(a_copia.sol, tam));
            return a_copia.clone();

        }

        public Cromosoma cruceHUX(Cromosoma a, Cromosoma b, Random r) {

            double probr = 0.1;
            List<Integer> saco_auxiliar = new ArrayList<Integer>();
            List<Integer> saco_auxiliar2 = new ArrayList<Integer>();
            int[] hux = new int[tam];
            int[] hijo = new int[tam];
            //System.out.println("PADRE "+atexto(a.sol, tam));
            //System.out.println("MADRE "+atexto(b.sol, tam));

            for (int j = 0; j < tam; j++) {
                if (a.sol[j] != b.sol[j]) {
                    saco_auxiliar2.add(a.sol[j]);
                    hux[j] = -1;

                } else {
                    hux[j] = a.sol[j];
                }

                hijo[j] = -1;
            }
            //System.out.println("HUX "+atexto(hux, tam));

            for (int j = 0; j < tam; j++) {

                if (hux[j] == -1) {
                    boolean rellenado = true;
                    double dado = r.nextDouble();
                    double chance_progenitor = (1.0 - probr) / 2;
                    if (dado < chance_progenitor) {
                        if (!saco_auxiliar.contains(a.sol[j])) {
                            hijo[j] = a.sol[j];
                        } else if (!saco_auxiliar.contains(b.sol[j])) {
                            hijo[j] = b.sol[j];
                        } else {

                            rellenado = false;
                        }

                    } else if (dado < chance_progenitor * 2) {
                        if (!saco_auxiliar.contains(b.sol[j])) {
                            hijo[j] = b.sol[j];
                        } else if (!saco_auxiliar.contains(a.sol[j])) {
                            hijo[j] = a.sol[j];
                        } else {
                            rellenado = false;
                        }
                    } else {
                        hijo[j] = saco_auxiliar2.get(r.nextInt(saco_auxiliar2.size()));
                    }

                    if (rellenado) {

                        saco_auxiliar.add(hijo[j]);
                        saco_auxiliar2.remove(saco_auxiliar2.indexOf(hijo[j]));
                    }

                } else {
                    hijo[j] = hux[j];

                }
            }

            for (int i = 0; i < tam; i++) {
                if (hijo[i] == -1) {
                    hijo[i] = saco_auxiliar2.remove(0);
                }
            }

            //System.out.println("HIJO  "+atexto(hijo, tam));
            return new Cromosoma(hijo);

            /*
            Cromosoma h1 = a.clone();
            Cromosoma h2 = a.clone();
            int swaps = 0;
            while(swaps < hamming/2){
                for (int i = 0; i < tam; i++) {
                    if(h1.sol[i]!=h2.sol[i] && h1.sol[i]!=b.sol[i]){
                        if(r.nextBoolean()){
                            
                            int i1=0;
                            int i2=0;
                            for (int j = 0; j < tam; j++) {
                                if(h1.sol[j]==b.sol[i]){
                                    i1=j;
                                }
                                if(h2.sol[j]==a.sol[i]){
                                    i2=j;
                                }
                            }
                            int [] sol1 = Dos_opt(h1.sol, i1, i);
                            h1.sol=sol1;
                            int [] sol2 = Dos_opt(h2.sol, i2, i);
                            h2.sol=sol2;
                           
                            swaps++;
                        }
                    }
                }
            }
            h1.recalcular();
            h2.recalcular();
            ArrayList<Cromosoma> h = new ArrayList<Cromosoma>();
            h.add(h1);
            h.add(h2);
            return h;*/
        }

        public void mutacion(Random r, int t, int stall, double pmax, double pmin) {

            /*if(r.nextDouble()< prob){
                int corte = r.nextInt(tam);
            Queue<Integer> parte1 = new LinkedList<Integer>();
            Queue<Integer> parte2 = new LinkedList<Integer>();
            for (int i = 0; i < tam; i++) {
                if(i<corte){
                    parte1.add(sol[i]);
                }else{
                    parte2.add(sol[i]);
                }
            }
            
            
            
            for (int i = 0; i < tam; i++) {
                if(parte2.size()!=0){
                    sol[i]= parte2.poll();
                }else{
                    sol[i]= parte1.poll();
                }
            }
            
            

            
            }*/
            double prob = t * ((pmin - pmax) / (stall - 1)) + pmax;
            ;
            if (r.nextDouble() < prob) {
                /*int n;
                
                if(t< stall/4){
                     n = 3;
                }else if(t< stall/2){
                     n = 1;
                }else if(t< 3*stall/4){
                     n = 1;
                }else {
                     n = 1;
                }*/

                //for (int i = 0; i < n; i++) {
                sol = Dos_opt(sol, r.nextInt(tam), r.nextInt(tam));
                //}
                /*    
                List<Integer> list = new LinkedList<Integer>();
                for (int i = 0; i < tam; i++) {
                    list.add(sol[i]);
                }
                Collections.reverse(list);
                for (int i = 0; i < tam; i++) {
                    sol[i]=list.get(i);
                }*/
                this.recalcular();
            }

        }

        public void mutacionGEN(Random r, int t, int stall, double pmax, double pmin) {

            double prob = t * ((pmin - pmax) / (stall - 1)) + pmax;
            ;
            for (int i = 0; i < tam; i++) {
                if (r.nextDouble() < prob) {

                    sol = Dos_opt(sol, i, r.nextInt(tam));

                    
                }
            }
            
            this.recalcular();
        }

        public Cromosoma Ruletaproporcional(List<Cromosoma> l, Random r) {
            double[] probabilidades = new double[l.size()];
            double suma = 0.0;
            for (int i = 0; i < l.size(); i++) {
                suma += 1 / l.get(i).coste;

            }

            for (int i = 0; i < l.size(); i++) {
                probabilidades[i] = 1 / (l.get(i).coste * suma);

            }

            double bola = r.nextDouble();

            double flecha = 0.0;
            int i = 0;
            boolean elegido = false;
            while (i < l.size() && !elegido) {
                flecha += probabilidades[i];

                if (bola < flecha) {
                    elegido = true;
                } else {
                    i++;
                }
            }

            return l.get(i).clone();

        }
    }

    public class ordenaporcostes implements Comparator<Cromosoma> {

        @Override
        public int compare(Cromosoma o1, Cromosoma o2) {
            if (o1.coste == 0 && o2.coste == 0) {
                return 0;
            } else if (o1.coste == 0) {
                return 1;
            } else if (o2.coste == 0) {
                return -1;
            } else {
                if (o1.coste < o2.coste) {
                    return -1;
                } else if (o1.coste > o2.coste) {
                    return 1;
                } else {
                    return 0;
                }
            }

        }

    }

    public int[] genetico_simple(int seed) {
        Random r = new Random(seed);
        double prob_mutacion_max = 0.2;//0.05 a 0.2
        double prob_mutacion_min = 0.05;
        int n_torneo = 0;// de 3 a 5
        int torneo_max = tam / 2;
        int torneo_min = tam / 8;

        int stall = 100;
        int n_poblacion = 0;// de 30 a 200
        switch (tam) {
            case 25:
                n_poblacion = 60;

                break;
            case 90:
                n_poblacion = 140;

                break;

            case 150:
                n_poblacion = 200;

                break;

            default:
                break;
        }

        //generar poblacion inicial y evaluarlos
        List<Cromosoma> poblacion = new ArrayList<Cromosoma>();
        List<Cromosoma> camada;
        List<Cromosoma> podio;
        List<Cromosoma> podio_ant;
        poblacion.add(new Cromosoma(this.BusquedaGreedy()));
        for (int i = 1; i < n_poblacion; i++) {
            poblacion.add(new Cromosoma(this.BusquedaAleatoria(1, i * r.nextInt())));
        }
        //ordenados por coste
        Collections.sort(poblacion, new ordenaporcostes());

        double generaciones = 0.0;

        int t = 0;

        while (t < stall) {

            //funcion lineal para medir el torneo
            double torneod = (double) (torneo_max - torneo_min) / (stall - 1);
            n_torneo = (int) (t * torneod + torneo_min);

            podio = new ArrayList<Cromosoma>();
            podio_ant = new ArrayList<Cromosoma>();

            Cromosoma mejor_anterior = poblacion.get(0).clone();

            //reproduccion
            camada = new ArrayList<Cromosoma>();

            for (int i = 0; i < n_poblacion; i++) {
                if (i < 5) {
                    camada.add(poblacion.get(i));
                    podio_ant.add(poblacion.get(i));
                    //los 5 mejores son privilegiados
                } else {
                    if (i % 2 == 0) {

                        //torneo
                        Cromosoma padre, madre;

                        //TORNEO
                        List<Cromosoma> aux = new ArrayList<Cromosoma>();
                        for (int j = 0; j < n_torneo; j++) {
                            int h = r.nextInt(poblacion.size());

                            aux.add(poblacion.get(h));

                        }
                        Collections.sort(aux, new ordenaporcostes());

                        padre = aux.get(0).clone();

                        aux = new ArrayList<Cromosoma>();

                        for (int j = 0; j < n_torneo; j++) {

                            int h = r.nextInt(poblacion.size());

                            aux.add(poblacion.get(h));
                        }
                        Collections.sort(aux, new ordenaporcostes());
                        madre = aux.get(0).clone();

                        //Ruleta
                        //padre = mejor_anterior.Ruletaproporcional(poblacion, r);
                        //madre = mejor_anterior.Ruletaproporcional(poblacion, r);
                        //CRUCE
                        Cromosoma hijo1 = madre.cruceOX_suavizado(madre, padre, r);
                        Cromosoma hijo2 = padre.cruceOX_suavizado(padre, madre, r);

                        //MUTACION
                        hijo1.mutacion(r, t, stall, prob_mutacion_max, prob_mutacion_min);

                        hijo2.mutacion(r, t, stall, prob_mutacion_max, prob_mutacion_min);

                        camada.add(hijo1);
                        camada.add(hijo2);

                    }
                    if (i == n_poblacion - 1 && camada.size() < n_poblacion) {
                        // hay uno cojo
                        //torneo
                        Cromosoma padre, madre;
                        //TORNEO

                        List<Cromosoma> aux = new ArrayList<Cromosoma>();
                        for (int j = 0; j < n_torneo; j++) {
                            aux.add(poblacion.get(r.nextInt(poblacion.size())));

                        }
                        Collections.sort(aux, new ordenaporcostes());

                        padre = aux.get(0).clone();

                        aux = new ArrayList<Cromosoma>();

                        for (int j = 0; j < n_torneo; j++) {

                            aux.add(poblacion.get(r.nextInt(poblacion.size())));
                        }
                        Collections.sort(aux, new ordenaporcostes());
                        madre = aux.get(0).clone();

                        /* 
                        //Ruleta
                        padre = mejor_anterior.Ruletaproporcional(poblacion, r);
                        madre = mejor_anterior.Ruletaproporcional(poblacion, r);
                         */
                        //CRUCE
                        Cromosoma hijo1 = madre.cruceOX_suavizado(madre, padre, r);

                        //MUTACION
                        hijo1.mutacion(r, t, stall, prob_mutacion_max, prob_mutacion_min);

                        camada.add(hijo1);

                    }
                }
            }
            //tenemos una nueva camada plagada de individuos nuevos
            Collections.sort(camada, new ordenaporcostes());

            poblacion = camada;

            for (int i = 0; i < 5; i++) {
                podio.add(camada.get(i));
            }

            if (podio.equals(podio_ant)) {
                t++;
            } else {
                t = 0;

            }

            //SEGUIMIENTO
            generaciones++;
            double[] d = new double[2];
            d[0] = generaciones;
            d[1] = poblacion.get(0).coste;
            seguimiento.add(d);

        }
        return poblacion.get(0).sol;
    }

    public int[] geneticoCHC(int seed) {
        int max_reinicios = 10;
        int n_mejores_reincio = 1;
        int n_poblacion = 0;
        int d = tam / 4;
        switch (tam) {
            case 25:
                n_poblacion = 36;
                break;
            case 90:
                n_poblacion = 110;
                break;

            case 150:
                n_poblacion = 170;
                break;

            default:
                break;
        }

        Random r = new Random(seed);

        //inicializar la poblacion P(t)
        //generar poblacion inicial y evaluarlos
        List<Cromosoma> poblacion = new ArrayList<Cromosoma>();
        List<Cromosoma> camada;
        poblacion.add(new Cromosoma(this.BusquedaGreedy()));
        for (int i = 1; i < n_poblacion; i++) {
            poblacion.add(new Cromosoma(this.BusquedaAleatoria(1, i * r.nextInt())));
        }
        //ordenados por coste
        Collections.sort(poblacion, new ordenaporcostes());

        double generaciones = 0.0;
        int reinicios = 0;

        while (reinicios < max_reinicios) {

            //1.SELECT R
            List<Cromosoma> pasado = new ArrayList<Cromosoma>();//es una mickey herramienta que usaremos mas tarde

            camada = new ArrayList<Cromosoma>();
            //copiar en C(t) desordenados los elementos de P(t-1)
            for (int i = 0; i < n_poblacion; i++) {
                camada.add(poblacion.get(i).clone());
                pasado.add(poblacion.get(i).clone());
            }
            Collections.shuffle(camada, r);

            //2. RECOMBINAR
            List<Cromosoma> camada_aux = new ArrayList<Cromosoma>();

            for (int i = 0; i < camada.size(); i += 2) {
                int hamming = 0;

                for (int j = 0; j < tam; j++) {
                    if (camada.get(i).sol[j] != camada.get(i + 1).sol[j]) {
                        hamming++;

                    }
                }

                if (hamming > d) {// menor o mayor
                    //ArrayList<Cromosoma> hijos=camada.get(i).cruceHUX(camada.get(i), camada.get(i + 1),hamming, r);
                    Cromosoma hijo1 = camada.get(i).cruceOX_disruptivo(camada.get(i), camada.get(i + 1), r);

                    Cromosoma hijo2 = camada.get(i).cruceOX_disruptivo(camada.get(i + 1), camada.get(i), r);

                    camada_aux.add(hijo1);
                    camada_aux.add(hijo2);
                }

            }
            camada = camada_aux;

            //3. SELECT S  
            // reemplaza los peores elementos de P(t-1) con los mejores de C'(t)
            //          hasta que ningun elemento que quede en en C't sea mejor 
            //          que alguno de P(t-1)
            while (camada.size() > 0) {
                poblacion.add(camada.remove(0));
            }
            poblacion.sort(new ordenaporcostes());
            while (poblacion.size() > n_poblacion) {
                poblacion.remove(n_poblacion);
            }

            //4.COMPROBACION Y DIVERGENCIA
            if (poblacion.equals(pasado)) {

                d--;

            }
            if (d < 0) {

                //diverge P(t)
                reinicios++;
                d = tam / 4;
                Cromosoma mejor = poblacion.get(0);
                poblacion = new ArrayList<Cromosoma>();
                int i, j;
                for (i = 0; i < n_mejores_reincio; i++) {
                    poblacion.add(mejor.clone());
                }

                for (j = i; j < n_poblacion; j++) {
                    poblacion.add(new Cromosoma(this.BusquedaAleatoria(1, i * r.nextInt())));
                }
                Collections.sort(poblacion, new ordenaporcostes());
            }
            //SEGUIMIENTO
            generaciones++;
            double[] u = new double[2];
            u[0] = generaciones;
            u[1] = poblacion.get(0).coste;
            seguimiento.add(u);
        }

        return poblacion.get(0).sol;
    }

    public int[] geneticoMultimodal(int seed) {
        Random r = new Random(seed);
        double prob_mutacion_max = 0.05;
        double prob_mutacion_min = 0.01;//0.01 a 0.05
        double prob_cruce = 0.8;
        int stall = 200;
        int n_torneo = 0;// de 3 a 5
        int torneo_max = tam / 2;
        int torneo_min = tam / 8;

        int n_poblacion = 30;// menor que el genetico normal
        switch (tam) {
            case 25:
                n_poblacion = 36;

                break;
            case 90:
                n_poblacion = 116;
                break;

            case 150:
                n_poblacion = 176;
                break;

            default:
                break;
        }
        int radio_de_nicho = (int) (tam*0.75);//Lo que te tienes que diferenciar para ser considerado diferente
        int kappa = 2;// dentro de cada nicho, cuantos se permiten habia una cosa que era kappa = 1

        //generar poblacion inicial y evaluarlos
        List<Cromosoma> poblacion = new ArrayList<Cromosoma>();
        List<Cromosoma> camada;

        Cromosoma mejor_anterior=null;

        poblacion.add(new Cromosoma(this.BusquedaGreedy()));
        for (int i = 1; i < n_poblacion; i++) {
            poblacion.add(new Cromosoma(this.BusquedaAleatoria(1, i * r.nextInt())));
        }
        //ordenados por coste
        Collections.sort(poblacion, new ordenaporcostes());

        int t = 0;
        double generaciones = 0.0;
        while (t < stall) {

            double torneod = (double) (torneo_max - torneo_min) / (stall - 1);
            n_torneo = (int) (t * torneod + torneo_min);
            
            if(mejor_anterior==null || poblacion.get(0).coste<mejor_anterior.coste){
                mejor_anterior = poblacion.get(0).clone();
            }
            

            //reproduccion
            camada = new ArrayList<Cromosoma>();

            //CLEARING, TAMBIEN CONOCIDO COMO LA PURGA
            for (int i = 0; i < poblacion.size(); i++) {
                if (poblacion.get(i).coste > 0) {
                    int numganadores = 1;
                    for (int j = i + 1; j < poblacion.size(); j++) {

                        if (poblacion.get(j).coste > 0 && poblacion.get(i).distancia_hamming(poblacion.get(j)) < radio_de_nicho) {
                            if (numganadores < kappa) {
                                numganadores++;
                            } else {
                                poblacion.get(j).coste = 0;
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < n_poblacion; i++) {

                if (i % 2 == 0) {

                    //torneo
                    Cromosoma padre, madre;
                    List<Cromosoma> aux = new ArrayList<Cromosoma>();
                    for (int j = 0; j < n_torneo; j++) {
                        aux.add(poblacion.get(r.nextInt(poblacion.size())));

                    }
                    Collections.sort(aux, new ordenaporcostes());

                    padre = aux.get(0).clone();

                    aux = new ArrayList<Cromosoma>();

                    for (int j = 0; j < n_torneo; j++) {

                        aux.add(poblacion.get(r.nextInt(poblacion.size())));
                    }
                    Collections.sort(aux, new ordenaporcostes());
                    madre = aux.get(0).clone();

                    //CRUCE
                    Cromosoma hijo1 = madre.cruceOX_suavizado(madre, padre, r);
                    Cromosoma hijo2 = padre.cruceOX_suavizado(padre, madre, r);

                    //MUTACION
                    hijo1.mutacionGEN(r, t, stall, prob_mutacion_max, prob_mutacion_min);
                    hijo2.mutacionGEN(r, t, stall, prob_mutacion_max, prob_mutacion_min);

                    if (r.nextDouble() < prob_cruce) {
                        camada.add(hijo1);
                        camada.add(hijo2);
                    } else {
                        camada.add(madre);
                        camada.add(padre);
                    }

                }
                if (i == n_poblacion - 1 && camada.size() < n_poblacion) {
                    // hay uno cojo
                    //torneo
                    Cromosoma padre, madre;
                    List<Cromosoma> aux = new ArrayList<Cromosoma>();
                    for (int j = 0; j < n_torneo; j++) {
                        aux.add(poblacion.get(r.nextInt(poblacion.size())));

                    }
                    Collections.sort(aux, new ordenaporcostes());

                    padre = aux.get(0).clone();

                    aux = new ArrayList<Cromosoma>();

                    for (int j = 0; j < n_torneo; j++) {

                        aux.add(poblacion.get(r.nextInt(poblacion.size())));
                    }
                    Collections.sort(aux, new ordenaporcostes());
                    madre = aux.get(0).clone();

                    //CRUCE
                    Cromosoma hijo1 = madre.cruceOX_suavizado(madre, padre, r);

                    //MUTACION
                    hijo1.mutacion(r, t, stall, prob_mutacion_max, prob_mutacion_min);

                    camada.add(hijo1);

                }

            }

            //tenemos una nueva camada plagada de individuos nuevos
            Collections.sort(camada, new ordenaporcostes());

            poblacion = camada;

            /* 
            System.out.println("CAMADA "+t);
            for (int i = 0; i < n_poblacion; i++) {
                if(poblacion.get(i).coste==0){
                    System.out.println(atexto(poblacion.get(i).sol, tam)+" PURGADO");
                }else
                System.out.println(atexto(poblacion.get(i).sol, tam));
            }^*/
            if (camada.get(0).coste > mejor_anterior.coste) {
                t++;
            } else {
                t = 0;
            }

            //SEGUIMIENTO
            generaciones++;
            double[] d = new double[2];
            d[0] = generaciones;
            d[1] = mejor_anterior.coste;
            seguimiento.add(d);

        }
        
        //ESTE CLEARING ES PARA SACAR LOS OPTIMOS
        kappa=1;
        
        for (int i = 0; i < poblacion.size(); i++) {
            if (poblacion.get(i).coste > 0) {
                int numganadores = 1;
                for (int j = i + 1; j < poblacion.size(); j++) {

                    if (poblacion.get(j).coste > 0 && poblacion.get(i).distancia_hamming(poblacion.get(j)) < radio_de_nicho) {
                        if (numganadores < kappa) {
                            numganadores++;
                        } else {
                            poblacion.get(j).coste = 0;
                        }
                    }
                }
            }
        }
        int n_soluciones = 3;
        int contador = 0;
        
        
        for (int i = 0; i < poblacion.size(); i++) {
            if(poblacion.get(i).coste>0 && contador < n_soluciones){
                contador++;
                System.out.println((contador)+"º COSTE " + poblacion.get(i).coste + " SOL: " + atexto(poblacion.get(i).sol, tam));
                
            }
        }
        
        
        
        return poblacion.get(0).sol;
    }

    public int[] genetico_multimodalV2(int seed) {
        Random r = new Random(seed);
        double prob_mutacion_max = 0.2;//0.05 a 0.2
        double prob_mutacion_min = 0.05;
        int n_torneo = 0;// de 3 a 5
        int torneo_max = tam / 2;
        int torneo_min = tam / 8;
        
        int stall = 100;
        int n_poblacion = 0;// de 30 a 200
        switch (tam) {
            case 25:
                n_poblacion = 60;

                break;
            case 90:
                n_poblacion = 140;

                break;

            case 150:
                n_poblacion = 200;

                break;

            default:
                break;
        }
        
        int radio_de_nicho = (int) (tam*0.75);//Lo que te tienes que diferenciar para ser considerado diferente
        int kappa = 2;// dentro de cada nicho, cuantos se permiten habia una cosa que era kappa = 1

        //generar poblacion inicial y evaluarlos
        List<Cromosoma> poblacion = new ArrayList<Cromosoma>();
        List<Cromosoma> camada;
        List<Cromosoma> podio;
        List<Cromosoma> podio_ant;
        poblacion.add(new Cromosoma(this.BusquedaGreedy()));
        for (int i = 1; i < n_poblacion; i++) {
            poblacion.add(new Cromosoma(this.BusquedaAleatoria(1, i * r.nextInt())));
        }
        //ordenados por coste
        Collections.sort(poblacion, new ordenaporcostes());

        double generaciones = 0.0;

        int t = 0;

        while (t < stall) {

            //funcion lineal para medir el torneo
            double torneod = (double) (torneo_max - torneo_min) / (stall - 1);
            n_torneo = (int) (t * torneod + torneo_min);

            podio = new ArrayList<Cromosoma>();
            podio_ant = new ArrayList<Cromosoma>();

            for (int i = 0; i < poblacion.size(); i++) {
            if (poblacion.get(i).coste > 0) {
                int numganadores = 1;
                for (int j = i + 1; j < poblacion.size(); j++) {

                    if (poblacion.get(j).coste > 0 && poblacion.get(i).distancia_hamming(poblacion.get(j)) < radio_de_nicho) {
                        if (numganadores < kappa) {
                            numganadores++;
                        } else {
                            poblacion.get(j).coste = 0;
                        }
                    }
                }
            }
        }

            //reproduccion
            camada = new ArrayList<Cromosoma>();

            for (int i = 0; i < n_poblacion; i++) {
                if (i < 5) {
                    camada.add(poblacion.get(i));
                    podio_ant.add(poblacion.get(i));
                    //los 5 mejores son privilegiados
                } else {
                    if (i % 2 == 0) {

                        //torneo
                        Cromosoma padre, madre;

                        //TORNEO
                        List<Cromosoma> aux = new ArrayList<Cromosoma>();
                        for (int j = 0; j < n_torneo; j++) {
                            int h = r.nextInt(poblacion.size());

                            aux.add(poblacion.get(h));

                        }
                        Collections.sort(aux, new ordenaporcostes());

                        padre = aux.get(0).clone();

                        aux = new ArrayList<Cromosoma>();

                        for (int j = 0; j < n_torneo; j++) {

                            int h = r.nextInt(poblacion.size());

                            aux.add(poblacion.get(h));
                        }
                        Collections.sort(aux, new ordenaporcostes());
                        madre = aux.get(0).clone();

                        //Ruleta
                        //padre = mejor_anterior.Ruletaproporcional(poblacion, r);
                        //madre = mejor_anterior.Ruletaproporcional(poblacion, r);
                        //CRUCE
                        Cromosoma hijo1 = madre.cruceOX_suavizado(madre, padre, r);
                        Cromosoma hijo2 = padre.cruceOX_suavizado(padre, madre, r);

                        //MUTACION
                        hijo1.mutacion(r, t, stall, prob_mutacion_max, prob_mutacion_min);

                        hijo2.mutacion(r, t, stall, prob_mutacion_max, prob_mutacion_min);

                        camada.add(hijo1);
                        camada.add(hijo2);

                    }
                    if (i == n_poblacion - 1 && camada.size() < n_poblacion) {
                        // hay uno cojo
                        //torneo
                        Cromosoma padre, madre;
                        //TORNEO

                        List<Cromosoma> aux = new ArrayList<Cromosoma>();
                        for (int j = 0; j < n_torneo; j++) {
                            aux.add(poblacion.get(r.nextInt(poblacion.size())));

                        }
                        Collections.sort(aux, new ordenaporcostes());

                        padre = aux.get(0).clone();

                        aux = new ArrayList<Cromosoma>();

                        for (int j = 0; j < n_torneo; j++) {

                            aux.add(poblacion.get(r.nextInt(poblacion.size())));
                        }
                        Collections.sort(aux, new ordenaporcostes());
                        madre = aux.get(0).clone();

                        /* 
                        //Ruleta
                        padre = mejor_anterior.Ruletaproporcional(poblacion, r);
                        madre = mejor_anterior.Ruletaproporcional(poblacion, r);
                         */
                        //CRUCE
                        Cromosoma hijo1 = madre.cruceOX_suavizado(madre, padre, r);

                        //MUTACION
                        hijo1.mutacion(r, t, stall, prob_mutacion_max, prob_mutacion_min);

                        camada.add(hijo1);

                    }
                }
            }
            //tenemos una nueva camada plagada de individuos nuevos
            Collections.sort(camada, new ordenaporcostes());

            poblacion = camada;

            for (int i = 0; i < 5; i++) {
                podio.add(camada.get(i));
            }

            if (podio.equals(podio_ant)) {
                t++;
            } else {
                t = 0;

            }

            //SEGUIMIENTO
            generaciones++;
            double[] d = new double[2];
            d[0] = generaciones;
            d[1] = poblacion.get(0).coste;
            seguimiento.add(d);

        }
        
        
        
        
        //ESTE CLEARING ES PARA SACAR LOS OPTIMOS
        kappa=1;
        
        for (int i = 0; i < poblacion.size(); i++) {
            if (poblacion.get(i).coste > 0) {
                int numganadores = 1;
                for (int j = i + 1; j < poblacion.size(); j++) {

                    if (poblacion.get(j).coste > 0 && poblacion.get(i).distancia_hamming(poblacion.get(j)) < radio_de_nicho) {
                        if (numganadores < kappa) {
                            numganadores++;
                        } else {
                            poblacion.get(j).coste = 0;
                        }
                    }
                }
            }
        }
        int n_soluciones = 3;
        int contador = 0;
        
        
        for (int i = 0; i < poblacion.size(); i++) {
            if(poblacion.get(i).coste>0 && contador < n_soluciones){
                contador++;
                System.out.println((contador)+"º COSTE " + poblacion.get(i).coste + " SOL: " + atexto(poblacion.get(i).sol, tam));
                
            }
        }
        
        
        
        return poblacion.get(0).sol;
    }
    public static void main(String[] args) {
        QAP datasets[] = new QAP[3];
        datasets[0] = new QAP("tai25b.dat");
        datasets[1] = new QAP("sko90.dat");
        datasets[2] = new QAP("tai150b.dat");

        Scanner reader = new Scanner(System.in);

        int opcion1;
        int opcion2;

        do {

            System.out.println("\tMENU PRINCIPAL");

            System.out.println("Seleccione el dataset a utilizar:");
            System.out.println("1.tai25b");
            System.out.println("2.sko90");
            System.out.println("3.tai150b");
            System.out.println("4. PRUEBA GENERAL");
            System.out.println("5. MODO PRUEBA");
            System.out.println("0. Salir");
            opcion1 = reader.nextInt();
            int dataset = -1;
            int[] sol;
            switch (opcion1) {

                case 1: {
                    dataset = 0;
                    break;
                }
                case 2: {
                    dataset = 1;
                    break;
                }

                case 3: {
                    dataset = 2;
                    break;
                }
                case 4: {
                    for (int i = 0; i < 3; i++) {
                        System.out.println("DATASET " + i);

                        sol = datasets[i].BusquedaGreedy();
                        System.out.println("Greedy " + " Coste:" + fmt(datasets[i].coste(sol)));

                        sol = datasets[i].BusquedaLocalMejor(53, datasets[i].BusquedaAleatoria(1, 53));
                        System.out.println("Busqueda Local mejor " + " Evaluaciones:" + datasets[i].Ev + " Coste:" + fmt(datasets[i].coste(sol)));

                        //sol=datasets[i].GRASP(53);
                        System.out.println("GRASP " + " Evaluaciones:" + datasets[i].Ev + " Coste:" + fmt(datasets[i].coste(sol)));

                        //sol=datasets[i].ILS(53);
                        System.out.println("ILS " + " Evaluaciones:" + datasets[i].Ev + " Coste:" + fmt(datasets[i].coste(sol)));
                        //sol=datasets[i].VNS(53);
                        System.out.println("VNS " + " Evaluaciones:" + datasets[i].Ev + " Coste:" + fmt(datasets[i].coste(sol)));
                    }

                    break;
                }
                case 5: {
                    
                    int x = 2;
                    sol = datasets[x].genetico_multimodalV2(79);
                    System.out.println("Coste: " + datasets[x].coste(sol));

                    final XYSeriesDemo demo = new XYSeriesDemo("XY Series Demo", datasets[x].seguimiento);
                    demo.pack();
                    RefineryUtilities.centerFrameOnScreen(demo);
                    demo.setVisible(true);

                    break;
                }
                default: {
                    break;
                }

            }
            if (dataset != -1) {
                do {

                    System.out.println("\tSeleccione el algoritmo a utilizar:");
                    System.out.println("1.Aleatorio");
                    System.out.println("2.Greedy");
                    System.out.println("3.Genetico simple");
                    System.out.println("4.CHC");
                    System.out.println("5.Genetico Multimodal");

                    System.out.println("0. Atras");

                    opcion2 = reader.nextInt();
                    switch (opcion2) {

                        case 1: {

                            System.out.println("Busqueda aleatoria");
                            for (int i = 0; i < 10; i++) {
                                sol = datasets[dataset].BusquedaAleatoria(1600 * datasets[dataset].tam, i);
                                System.out.println("Prueba " + (i + 1) + " Evaluaciones:" + datasets[dataset].Ev + " Coste:" + fmt(datasets[dataset].coste(sol)));

                                datasets[dataset].Ev = 0;
                            }
                            break;
                        }
                        case 2: {

                            System.out.println("Algoritmo greedy");
                            sol = datasets[dataset].BusquedaGreedy();
                            System.out.println(" Evaluaciones:" + datasets[dataset].Ev + " Coste: " + fmt(datasets[dataset].coste(sol)));

                            datasets[dataset].Ev = 0;
                            break;
                        }

                        case 3: {

                            System.out.println("Genetico simple");
                            for (int i = 0; i < 10; i++) {
                                sol = datasets[dataset].genetico_simple(i);
                                System.out.println(" Evaluaciones:" + datasets[dataset].Ev + " Coste: " + fmt(datasets[dataset].coste(sol)));
                                datasets[dataset].Ev = 0;
                            }

                            break;
                        }
                        case 4: {
                            System.out.println("CHC");
                            for (int i = 0; i < 10; i++) {
                                sol = datasets[dataset].geneticoCHC(i);
                                System.out.println(" Evaluaciones:" + datasets[dataset].Ev + " Coste: " + fmt(datasets[dataset].coste(sol)));
                                datasets[dataset].Ev = 0;
                            }

                            break;
                        }
                        case 5: {
                            System.out.println("Genetico Multimodal");
                            for (int i = 0; i < 10; i++) {
                                sol = datasets[dataset].geneticoMultimodal(i);
                                System.out.println(" Evaluaciones:" + datasets[dataset].Ev + " Coste: " + fmt(datasets[dataset].coste(sol)));
                                datasets[dataset].Ev = 0;
                            }

                            break;
                        }

                        default: {
                            break;
                        }

                    }

                } while (opcion2 != 0);

            }

        } while (opcion1 != 0);

        //BUSQUEDA ALEATORIA
    }

    public static String fmt(double d) {
        if (d == (long) d) {
            return String.format("%d", (long) d);
        } else {
            return String.format("%s", d);
        }
    }
}
