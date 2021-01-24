package mainpackage;

import java.util.ArrayList;

/**
 * classe lista
 * @author Francesco Lizzio
 */
public class List {
    private ArrayList<NameList> namelist;

    public List(ArrayList<NameList> name) {
        namelist=name;
    }

    public ArrayList<NameList> getNames() {
        return namelist;
    }

    public void setNames(ArrayList<NameList> names) {
        this.namelist = names;
    }
    
}
