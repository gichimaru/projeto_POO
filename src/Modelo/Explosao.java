package Modelo;

import java.io.Serial;
import java.io.Serializable;

public class Explosao extends Estatico implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Explosao(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = true;//para o heroi pisar nela e morrer
        this.bMortal = true;
        this.bColetavel = false;
    }
}
