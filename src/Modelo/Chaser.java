package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Posicao;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class Chaser extends Animado implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean moveDirectionV;
    private boolean moveDirectionH;
    private final Random random = new Random();
    private final int initialLinha, initialColuna; //para respawnar
    private int moveTick = 0;

    public Chaser(String sNomeImagePNG,int linhaInicial, int colunaInicial) {
        super(sNomeImagePNG);
        this.bTransponivel = true;
        this.bMortal = true;
        this.initialLinha = linhaInicial;
        this.initialColuna = colunaInicial;
        this.setPosicao(linhaInicial, colunaInicial);
    }

    @Override
    public void respawn() {
        this.setPosicao(initialLinha, initialColuna);
        this.moveTick = 0;
    }

    public void computeDirection(Posicao heroPos) {
        if (heroPos == null || this.getPosicao() == null) return;

        if (heroPos.getColuna() < this.getPosicao().getColuna()) {
            moveDirectionH = true;
        } else if (heroPos.getColuna() > this.getPosicao().getColuna()) {
            moveDirectionH = false;
        }

        if (heroPos.getLinha() < this.getPosicao().getLinha()) {
            moveDirectionV = true;
        } else if (heroPos.getLinha() > this.getPosicao().getLinha()) {
            moveDirectionV = false;
        }
    }

    @Override
    public void autoDesenho() {
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.isGamePaused()) {
            super.autoDesenho();
            return;
        }

        moveTick++;
        int ticksParaMover = 3;

        if (moveTick < ticksParaMover) {
            super.autoDesenho();
            return;
        }

        moveTick = 0;

        Posicao atualPos = this.getPosicao();

        if (Desenho.acessoATelaDoJogo() == null
                || Desenho.acessoATelaDoJogo().getFaseAtual() == null
                || Desenho.acessoATelaDoJogo().getFaseAtual().getHeroi() == null) {
            super.autoDesenho();
            return;
        }

        Posicao heroPos = Desenho.acessoATelaDoJogo()
                .getFaseAtual()
                .getHeroi()
                .getPosicao();

        boolean preferHorizontal;
        int deltaCol = Math.abs(atualPos.getColuna() - heroPos.getColuna());
        int deltaLin = Math.abs(atualPos.getLinha() - heroPos.getLinha());

        if (deltaCol > deltaLin) {
            preferHorizontal = true;
        } else if (deltaCol < deltaLin) {
            preferHorizontal = false;
        } else {
            preferHorizontal = random.nextBoolean();
        }

        if (preferHorizontal) {
            if (moveDirectionH && atualPos.getColuna() > heroPos.getColuna()) {
                if (!this.moveLeftValidando()) {
                    tentarMovimentoVertical(heroPos);
                }
            } else if (!moveDirectionH && atualPos.getColuna() < heroPos.getColuna()) {
                if (!this.moveRightValidando()) {
                    tentarMovimentoVertical(heroPos);
                }
            } else {
                tentarMovimentoVertical(heroPos);
            }
        } else {
            if (moveDirectionV && atualPos.getLinha() > heroPos.getLinha()) {
                if (!this.moveUpValidando()) {
                    tentarMovimentoHorizontal(heroPos);
                }
            } else if (!moveDirectionV && atualPos.getLinha() < heroPos.getLinha()) {
                if (!this.moveDownValidando()) {
                    tentarMovimentoHorizontal(heroPos);
                }
            } else {
                tentarMovimentoHorizontal(heroPos);
            }
        }
        super.autoDesenho();
    }


    private void tentarMovimentoVertical(Posicao heroPos) {
        Posicao atualPos = this.getPosicao();

        if (moveDirectionV && atualPos.getLinha() > heroPos.getLinha()) {
            this.moveUpValidando();
        } else if (!moveDirectionV && atualPos.getLinha() < heroPos.getLinha()) {
            this.moveDownValidando();
        }
    }

    private void tentarMovimentoHorizontal(Posicao heroPos) {
        Posicao atualPos = this.getPosicao();

        if (moveDirectionH && atualPos.getColuna() > heroPos.getColuna()) {
            this.moveLeftValidando();
        } else if (!moveDirectionH && atualPos.getColuna() < heroPos.getColuna()) {
            this.moveRightValidando();
        }
    }
}