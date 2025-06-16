package Controller;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.Personagem;
import Modelo.BaseHero;
import Modelo.Chaser;
import Modelo.Coracao;
import Modelo.Fase;
import Modelo.PontoFinal;
import Auxiliar.Posicao;

import java.util.ArrayList;
import java.util.Iterator;

public class ControleDeJogo {

    private int placar = 0;

    public void desenhaTudo(Fase fase) {

        if (fase == null || fase.getPersonagens() == null) {
            return;
        }

        ArrayList<Personagem> personagensCpy;

        synchronized (fase.getPersonagens()) {
            personagensCpy = new ArrayList<>(fase.getPersonagens());
        }

        for (Personagem p : personagensCpy) {
            p.autoDesenho();
        }
    }

    public void processaTudo(Fase fase, Tela tela) {

        if (fase == null || fase.getHeroi() == null || fase.getPersonagens() == null) {
            return;
        }

        BaseHero heroi = fase.getHeroi();

        synchronized (fase.getPersonagens()) {
            Iterator<Personagem> iter = fase.getPersonagens().iterator();

            while (iter.hasNext()) {
                Personagem p = iter.next();

                if (p == heroi) continue;

                if (p instanceof Chaser) {
                    ((Chaser) p).computeDirection(heroi.getPosicao());
                }

                if (heroi.getPosicao().igual(p.getPosicao())) {
                    if (p.isbMortal()) {

                        System.out.println("Herói colidiu com objeto mortal: " + p.getClass().getSimpleName());
                        tela.tratarColisaoFatalHeroi();
                        return;
                    }

                    if (p instanceof Coracao) {

                        if(fase.getVidasRestantesHeroi() < Fase.VIDAS_INICIAIS_PADRAO){
                            fase.ganharVidaNaFase();
                            iter.remove();
                            System.out.println("Vida extra para a fase coletada! Vidas na fase: " + fase.getVidasRestantesHeroi());
                            tela.atualizarTitulo();
                        }else{
                            System.out.println("Vidas da fase no máximo (" + fase.getVidasRestantesHeroi() + "). Coração não coletado.");
                        }
                    } else if (heroi.podeColetar(p) && p.isbColetavel()) {

                        String itemClass = p.getClass().getSimpleName();
                        iter.remove();
                        fase.heroiColetouItemLogicOnly(itemClass, p.isbColetavel() && heroi.podeColetar(p));

                        placar += 10;
                        System.out.println("Item coletado! Placar: " + placar);
                        tela.atualizarTitulo();

                    } else if (p instanceof PontoFinal) {

                        if (fase.todosItensColetados()) {

                            System.out.println("Todos os itens coletados! Indo para o Ponto Final!");
                            tela.proximaFase();
                            return;
                        } else {
                            System.out.println("Ainda faltam " + fase.getColetaveisRestantes() + " itens para coletar!");
                        }
                    }
                }
            }
        }
    }

    public boolean ehPosicaoValidaHeroi(ArrayList<Personagem> personagensDaFase, Posicao pPos, BaseHero heroi) {

        if (pPos.getLinha() < 0 || pPos.getLinha() >= Consts.MUNDO_ALTURA ||
                pPos.getColuna() < 0 || pPos.getColuna() >= Consts.MUNDO_LARGURA) {

            return false;
        }

        ArrayList<Personagem> personagensDaFaseCpy;

        synchronized(personagensDaFase) {
            personagensDaFaseCpy = new ArrayList<>(personagensDaFase);
        }

        for (Personagem personagemNaPosicao : personagensDaFaseCpy) {

            if (personagemNaPosicao == heroi) continue;

            if (personagemNaPosicao.getPosicao().igual(pPos)) {

                if (!personagemNaPosicao.isbTransponivel() &&
                        !(heroi.podeColetar(personagemNaPosicao) && personagemNaPosicao.isbColetavel()) &&
                        !(personagemNaPosicao instanceof Coracao && personagemNaPosicao.isbColetavel())) {

                    return false;
                }
            }
        }
        return true;
    }

    public boolean ehPosicaoValidaMovimento(ArrayList<Personagem> personagensDaFase, Posicao pPos, Personagem quemMove) {

        if (pPos.getLinha() < 0 || pPos.getLinha() >= Consts.MUNDO_ALTURA ||
                pPos.getColuna() < 0 || pPos.getColuna() >= Consts.MUNDO_LARGURA) {
            return false;
        }

        ArrayList<Personagem> personagensDaFaseCpy;

        synchronized(personagensDaFase) {
            personagensDaFaseCpy = new ArrayList<>(personagensDaFase);
        }

        for (Personagem outro : personagensDaFaseCpy) {

            if (outro == quemMove) continue;

            if (outro.getPosicao().igual(pPos)) {
                if (!outro.isbTransponivel()) {
                    if (!(outro instanceof BaseHero)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void verificaColeta(BaseHero heroi) {
        Fase faseAtual = Desenho.acessoATelaDoJogo().getFaseAtual();

        if (faseAtual == null || faseAtual.getPersonagens() == null) return;

        synchronized (faseAtual.getPersonagens()) {
            Iterator<Personagem> iter = faseAtual.getPersonagens().iterator();

            while (iter.hasNext()) {
                Personagem p = iter.next();

                if (p == heroi) continue;

                if (heroi.getPosicao().igual(p.getPosicao())) {

                    if (p instanceof Coracao && p.isbColetavel()) {

                        if (faseAtual.getVidasRestantesHeroi() < Modelo.Fase.VIDAS_INICIAIS_PADRAO) {

                            faseAtual.ganharVidaNaFase();
                            iter.remove();
                            System.out.println("Vida extra para a fase coletada (verificaColeta)! Vidas na fase: " + faseAtual.getVidasRestantesHeroi());
                            Desenho.acessoATelaDoJogo().atualizarTitulo();
                        } else {
                            System.out.println("Vidas da fase no máximo (" + faseAtual.getVidasRestantesHeroi() + ") em verificaColeta. Coração não coletado.");
                            // Não remove o item, ele continua visível e coletável
                        }
                    } else if (heroi.podeColetar(p) && p.isbColetavel()) {

                        String itemClass = p.getClass().getSimpleName();
                        iter.remove(); // Remove using iterator first
                        faseAtual.heroiColetouItemLogicOnly(itemClass, p.isbColetavel() && !(p instanceof Coracao) && heroi.podeColetar(p));

                        placar += 10;
                        System.out.println("Item coletado (verificaColeta)! Placar: " + placar + ", Restantes: " + faseAtual.getColetaveisRestantes());
                        Desenho.acessoATelaDoJogo().atualizarTitulo();
                    }
                }
            }
        }
    }

    public int getPlacar() {
        return placar;
    }

    public void resetPlacar() {
        this.placar = 0;
    }

    public void setPlacar(int placar) {
        this.placar = placar;
    }
}