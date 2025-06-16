package Modelo;

import Auxiliar.Desenho;
import java.io.Serial;
import java.io.Serializable;

public class Fogo extends Animado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Fogo(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bMortal = true;
        this.bTransponivel = true;
    }

    @Override
    public void autoDesenho() {
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.isGamePaused()) {
            super.autoDesenho();
            return;
        }

        if (!this.moveRightValidando()) {
            if (Desenho.acessoATelaDoJogo() != null) {
                Desenho.acessoATelaDoJogo().removePersonagemDaFase(this);
                return;
            }
        }
        super.autoDesenho();
    }
}