package Modelo;

import java.io.Serial;
import java.io.Serializable;

public abstract class Estatico extends Personagem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Estatico(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }
}