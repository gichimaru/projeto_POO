package Modelo;

import java.io.Serial;
import java.io.Serializable;

public abstract class Animado extends Personagem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Animado(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

}