package Modelo;

import java.io.Serial;
import java.io.Serializable;

public class Lixo extends Estatico implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Lixo(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bColetavel = true;
        this.bTransponivel = true;
        this.bDestrutivel = true;
    }
}