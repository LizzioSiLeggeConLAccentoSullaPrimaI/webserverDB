package mainpackage;

/**
 * classe con i dati delle persone
 * @author Francesco Lizzio
 */
public class NameList {
    private String name;
    private String surname;

    public NameList(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
}
