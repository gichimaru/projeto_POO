package Modelo;

import java.io.Serial;
import java.io.Serializable;

public class Coracao extends Estatico implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Coracao(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bColetavel = true;
        this.bMortal = false;
    }
}