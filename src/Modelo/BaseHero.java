package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Posicao;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseHero extends Animado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public BaseHero(String sNomeImagePNG) {
        super(sNomeImagePNG);
        this.bTransponivel = false;
    }

    @Override
    public boolean setPosicao(int linha, int coluna) {
        Posicao novaPos = new Posicao(linha, coluna);

        if (Desenho.acessoATelaDoJogo() != null &&
                Desenho.acessoATelaDoJogo().ehPosicaoValidaHeroi(novaPos, this)) {
            boolean moveu = super.setPosicao(linha, coluna);

            if (moveu) {
                Desenho.acessoATelaDoJogo().getControleDeJogo().verificaColeta(this);
            }
            return moveu;
        }
        return false;
    }

    @Override
    public boolean moveUp() {
        return this.setPosicao(this.getPosicao().getLinha() - 1, this.getPosicao().getColuna());
    }

    @Override
    public boolean moveDown() {
        return this.setPosicao(this.getPosicao().getLinha() + 1, this.getPosicao().getColuna());
    }

    @Override
    public boolean moveRight() {
        return this.setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna() + 1);
    }

    @Override
    public boolean moveLeft() {
        return this.setPosicao(this.getPosicao().getLinha(), this.getPosicao().getColuna() - 1);
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
    }

    public abstract boolean podeColetar(Personagem p);
}