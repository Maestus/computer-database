package main.java.com.excilys.cdb.utils;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

    public long offset = 0;
    public long nbElem;
    public List<T> elems;
    public static final int NO_LIMIT = -1;

    /**
     * Constructeur de Page.
     * @param cURRENT_INIT_ElEM nombre d'element à ignorer.
     * @param nB_ElEM nombre d'element à stocker dans la liste.
     */
    public Page(long CURRENT_INIT_ElEM, long nB_ElEM) {
        this.offset = CURRENT_INIT_ElEM;
        this.nbElem = nB_ElEM;
        this.elems = new ArrayList<>();
    }

    /**
     * Constructeur de Page.
     * @param nbElem nombre d'element à stocker dans la liste.
     */
    public Page(int nbElem) {
        this.nbElem = nbElem;
        this.elems = new ArrayList<>();
    }

    /**
     * Ajouter un element à la liste.
     * @param m Un objet.
     */
    public void addElem(T m) {
        elems.add(m);
    }

    /**
     * Retourne un element de la liste.
     * @param index position de l'element dans la liste.
     * @return L'objet à la position 'index'.
     */
    public T getElem(int index) {
        return elems.get(index);
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(int val) {
        offset = val;
    }

    /**
     * Décremente l'offset de 1.
     */
    public void decrOffset() {
        offset--;
    }
}
