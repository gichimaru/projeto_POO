package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Posicao;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class InimigoEspecial extends Animado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean movendoParaCima;
    private int contadorMovimento;
    private final Random rand = new Random();
    private static final int DELAY_MOVIMENTO = 3;
    // para respawnar
    private final int initialLinha;
    private final int initialColuna;
    private final boolean initialMovendoParaCima;
    private int contadorTiro; //para o fogo
    private static final int INTERVALO_TIRO = 3;

    public InimigoEspecial(String sNomeImagePNG, int linhaInicial, int colunaInicial) {
        super(sNomeImagePNG);
        this.bMortal = true;
        this.bTransponivel = true;

        this.initialLinha = linhaInicial;
        this.initialColuna = colunaInicial;
        this.initialMovendoParaCima = rand.nextBoolean();
        this.movendoParaCima = this.initialMovendoParaCima;

        this.contadorMovimento = 0;
        this.contadorTiro = 0;
    }

    @Override
    public void respawn() {
        this.setPosicao(initialLinha, initialColuna);
        this.contadorMovimento = 0;
        this.movendoParaCima = initialMovendoParaCima;
        this.contadorTiro = 0;
    }

    @Override
    public void autoDesenho() {
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.isGamePaused()) {
            super.autoDesenho();
            return;
        }

        contadorMovimento++;

        if (contadorMovimento % DELAY_MOVIMENTO != 0) {
            super.autoDesenho();
            return;
        }

        boolean moveu;

        if (movendoParaCima) {
            moveu = this.moveUpValidando();
        } else {
            moveu = this.moveDownValidando();
        }

        if (!moveu || contadorMovimento > (10 + rand.nextInt(10))) {
            movendoParaCima = !movendoParaCima;
            contadorMovimento = 0;
        }

        this.contadorTiro++;

        if (this.contadorTiro >= INTERVALO_TIRO) {
            this.contadorTiro = 0;

            if (Desenho.acessoATelaDoJogo() != null) {
                Fogo fogoDireita = new Fogo("fire.png");
                Posicao posFogoDireita = new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna() + 1);

                if (Desenho.acessoATelaDoJogo().ehPosicaoValidaMovimento(posFogoDireita, fogoDireita)) {
                    fogoDireita.setPosicao(posFogoDireita.getLinha(), posFogoDireita.getColuna());
                    Desenho.acessoATelaDoJogo().addPersonagemNaFase(fogoDireita);
                }

                Fogo fogoEsquerda = new Fogo("fire.png");
                Posicao posFogoEsquerda = new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna() - 1);

                if (Desenho.acessoATelaDoJogo().ehPosicaoValidaMovimento(posFogoEsquerda, fogoEsquerda)) {
                    fogoEsquerda.setPosicao(posFogoEsquerda.getLinha(), posFogoEsquerda.getColuna());
                    Desenho.acessoATelaDoJogo().addPersonagemNaFase(fogoEsquerda);
                }
            }
        }
        super.autoDesenho();
    }
}