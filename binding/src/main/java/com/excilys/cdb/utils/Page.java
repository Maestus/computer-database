package main.java.com.excilys.cdb.utils;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

    public int offset = 0;
    public int nbElem;
    public List<T> elems;
    public static final int NO_LIMIT = -1;

    /**
     * Constructeur de Page.
     * @param offset nombre d'element à ignorer.
     * @param nbElem nombre d'element à stocker dans la liste.
     */
    public Page(int offset, int nbElem) {
        this.offset = offset;
        this.nbElem = nbElem;
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

    public int getOffset() {
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
