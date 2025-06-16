// Estrutura de Pastas: src/Modelo/BichinhoVaiVemVertical.java
package Modelo;

import Auxiliar.Desenho;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class BichinhoVaiVemVertical extends Animado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean indoParaBaixo;
    private int passosNoSentidoAtual;
    private int maxPassosSentido;
    private final Random rand = new Random();

    // posição inicial
    private final int initialLinha;
    private final int initialColuna;

    public BichinhoVaiVemVertical(String sNomeImagePNG, int linhaInicial, int colunaInicial) {
        super(sNomeImagePNG);
        this.initialLinha = linhaInicial;
        this.initialColuna = colunaInicial;
        this.setPosicao(this.initialLinha, this.initialColuna);
        this.indoParaBaixo = rand.nextBoolean();
        this.passosNoSentidoAtual = 0;
        this.maxPassosSentido = 3 + rand.nextInt(5);
        this.bTransponivel = true;
        this.bMortal = true;
    }

    @Override
    public void respawn() {
        this.setPosicao(initialLinha, initialColuna);
        this.indoParaBaixo = rand.nextBoolean();    // Nova direção vertical aleatória
        this.passosNoSentidoAtual = 0;              // Zera contador de passos
        this.maxPassosSentido = 3 + rand.nextInt(5); // Novo máximo de passos aleatório
    }

    @Override
    public void autoDesenho() {
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.isGamePaused()) {
            super.autoDesenho();
            return;
        }

        boolean moveu;

        if (indoParaBaixo) {
            moveu = this.moveDownValidando();
        } else {
            moveu = this.moveUpValidando();
        }

        if (!moveu || passosNoSentidoAtual >= maxPassosSentido) {
            indoParaBaixo = !indoParaBaixo;
            passosNoSentidoAtual = 0;
            this.maxPassosSentido = 3 + rand.nextInt(5);
        } else {
            passosNoSentidoAtual++;
        }

        super.autoDesenho();
    }
}