package Classes;

public class Menu {

    private int id;
    private String titre;
    private int nombreRepas;
    private String type;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Menu() {
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

    public int getNombreRepas() {
        return nombreRepas;
    }

    public void setNombreRepas(int nombreRepas) {
        this.nombreRepas = nombreRepas;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
