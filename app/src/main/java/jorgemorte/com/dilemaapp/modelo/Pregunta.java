package jorgemorte.com.dilemaapp.modelo;

public class Pregunta {
    String texto;
    String palabrasTaboo;
    public Pregunta(String texto, String palabrasTaboo) {
        this.texto = texto;
        this.palabrasTaboo = palabrasTaboo;

    }

    public String getPalabrasTaboo() {
        return palabrasTaboo;
    }

    public void setPalabrasTaboo(String palabrasTaboo) {
        this.palabrasTaboo = palabrasTaboo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
