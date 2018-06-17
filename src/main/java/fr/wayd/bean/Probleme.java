package fr.wayd.bean;

public class Probleme {
    private String iduser;
    private String text;
    private String timestamp;

    public Probleme(String iduser, String text, String timestamp) {
        this.iduser = iduser;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Probleme() {

    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
