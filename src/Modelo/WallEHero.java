package Modelo;

import java.io.Serial;
import java.io.Serializable;

public class WallEHero extends BaseHero implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public WallEHero(String sNomeImagePNG) {
        super(sNomeImagePNG);
    }

    @Override
    public boolean podeColetar(Personagem p) {
        return p instanceof Lixo;
    }
}