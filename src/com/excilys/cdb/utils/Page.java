package com.excilys.cdb.utils;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

	public int offset = 0;
	public int nbElem;
	public List<T> elems;
	public final static int NO_LIMIT = -1;

	public Page(int offset, int nbElem) {
		this.offset = offset;
		this.nbElem = nbElem;
		this.elems = new ArrayList<>();
	}
	
	public Page(int nbElem) {
		this.nbElem = nbElem;
		this.elems = new ArrayList<>();
	}

	public void addElem(T m) {
		elems.add(m);
	}
		
	public T getElem(int index) {
		return elems.get(index);
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int val) {
		offset = val;
	}
	
	public void decrOffset() {
		offset--;
	}
}
