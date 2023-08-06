package Classes;

import java.util.Date;

public class Event {

    private int id;
    private String titre;
    private String debut;
    private String fin;
    private String responsable;
    private String adress;

    public Event(int id, String titre, String debut, String fin, String responsable, String adress) {
        this.id = id;
        this.titre = titre;
        this.debut = debut;
        this.fin = fin;
        this.responsable = responsable;
        this.adress = adress;
    }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

}
