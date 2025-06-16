package Modelo;

import Auxiliar.Desenho;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class ZigueZague extends Animado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Random rand = new Random();
    private final int initialLinha;
    private final int initialColuna;

    public ZigueZague(String sNomeImagePNG, int linhaInicial, int colunaInicial) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = true;
        this.initialLinha = linhaInicial;
        this.initialColuna = colunaInicial;
    }

    @Override
    public void autoDesenho() {
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.isGamePaused()) {
            super.autoDesenho();
            return;
        }

        int iDirecao = rand.nextInt(4);

        switch (iDirecao) {
            case 0: this.moveRightValidando(); break;
            case 1: this.moveDownValidando();  break;
            case 2: this.moveLeftValidando();  break;
            case 3: this.moveUpValidando();    break;
        }

        super.autoDesenho();
    }

    @Override
    public void respawn() {
        this.setPosicao(initialLinha, initialColuna);
    }
}