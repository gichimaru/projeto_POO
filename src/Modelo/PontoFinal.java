package Modelo;

import java.io.Serial;
import java.io.Serializable;

public class PontoFinal extends Estatico implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public PontoFinal(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
    }
}