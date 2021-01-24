package mainpackage;

import java.util.ArrayList;

/**
 * classe contenitore punti vendita
 * @author Francesco Lizzio
 */
public class ResultList {
    private int size;
    private ArrayList<PuntiVendita> resultlist;

    public void setSize(int size) {
        this.size = size;
    }

    public void setResultList(ArrayList<PuntiVendita> resultlist) {
        this.resultlist = resultlist;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<PuntiVendita> getResultList() {
        return resultlist;
    }
}
