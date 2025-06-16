package Auxiliar;

import java.io.Serial;
import java.io.Serializable;

public class Posicao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int linha;
    private int coluna;

    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        this.setPosicao(linha, coluna);
    }

    public boolean setPosicao(int linha, int coluna) {

        if (linha < 0 || linha >= Consts.MUNDO_ALTURA || coluna < 0 || coluna >= Consts.MUNDO_LARGURA) {
            return false;
        }

        this.linha = linha;
        this.coluna = coluna;

        return true;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public boolean igual(Posicao posicao) {
        if (posicao == null) return false;
        return (linha == posicao.getLinha() && coluna == posicao.getColuna());
    }

    public boolean moveUp() {
        return this.setPosicao(this.getLinha() - 1, this.getColuna());
    }

    public boolean moveDown() {
        return this.setPosicao(this.getLinha() + 1, this.getColuna());
    }

    public boolean moveRight() {
        return this.setPosicao(this.getLinha(), this.getColuna() + 1);
    }

    public boolean moveLeft() {
        return this.setPosicao(this.getLinha(), this.getColuna() - 1);
    }
}