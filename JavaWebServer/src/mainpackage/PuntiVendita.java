package mainpackage;


/**
 * classe punti vendita
 * @author Francesco Lizzio
 */
public class PuntiVendita {
    private int idPuntoVendita;
    private String denominazione;
    private String indirizzo;
    private String cap;
    private String comune;
    private String codProvincia;
    private String urlSito;
    private String telefonoPrincipale;
    private String telefonoSecondario;
    private String email;
    private String latitudine;
    private String longitudine;
    private boolean flagFisicoOnline;
    private int idEsercente;
    private String ragioneSociale;

    public void setIdPuntoVendita(int idPuntoVendita) {
        this.idPuntoVendita = idPuntoVendita;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    public void setUrlSito(String urlSito) {
        this.urlSito = urlSito;
    }

    public void setTelefonoPrincipale(String telefonoPrincipale) {
        this.telefonoPrincipale = telefonoPrincipale;
    }

    public void setTelefonoSecondario(String telefonoSecondario) {
        this.telefonoSecondario = telefonoSecondario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public void setFlagFisicoOnline(boolean flagFisicoOnline) {
        this.flagFisicoOnline = flagFisicoOnline;
    }

    public void setIdEsercente(int idEsercente) {
        this.idEsercente = idEsercente;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public int getIdPuntoVendita() {
        return idPuntoVendita;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public String getCap() {
        return cap;
    }

    public String getComune() {
        return comune;
    }

    public String getCodProvincia() {
        return codProvincia;
    }

    public String getUrlSito() {
        return urlSito;
    }

    public String getTelefonoPrincipale() {
        return telefonoPrincipale;
    }

    public String getTelefonoSecondario() {
        return telefonoSecondario;
    }

    public String getEmail() {
        return email;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public boolean isFlagFisicoOnline() {
        return flagFisicoOnline;
    }

    public int getIdEsercente() {
        return idEsercente;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }
}
