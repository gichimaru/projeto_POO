package Modelo;

import java.io.Serial;
import java.io.Serializable;

public class Planta extends Estatico implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Planta(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bColetavel = true;
        this.bTransponivel = true;
    }
}