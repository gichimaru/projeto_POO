package Modelo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Fase implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final ArrayList<Personagem> personagens;
    protected BaseHero heroi;
    protected String backgroundImagem;
    protected int numeroFase;
    protected int totalColetaveisFase;
    protected int coletaveisRestantes;
    // controle de vidas e posição inicial do herói
    public static final int VIDAS_INICIAIS_PADRAO = 3;
    protected int vidasRestantesHeroi;
    protected int linhaInicialHeroi;
    protected int colunaInicialHeroi;

    public Fase() {
        this.personagens = new ArrayList<>();
        this.totalColetaveisFase = 0;
        this.coletaveisRestantes = 0;
        this.vidasRestantesHeroi = VIDAS_INICIAIS_PADRAO;
    }

    public abstract void carregarFase();

    protected void inicializarFaseBase(int numeroFase) {
        this.numeroFase = numeroFase;

        synchronized (this.personagens) {
            this.personagens.clear();
        }

        this.heroi = null;
        this.totalColetaveisFase = 0;
        this.coletaveisRestantes = 0;
        this.vidasRestantesHeroi = VIDAS_INICIAIS_PADRAO;
    }

    public void ganharVidaNaFase() {

        if (this.vidasRestantesHeroi < VIDAS_INICIAIS_PADRAO) {
        this.vidasRestantesHeroi++;
        }
    }

    protected void definirHeroiPrincipal(BaseHero heroi, int linha, int coluna) {
        this.heroi = heroi;
        this.linhaInicialHeroi = linha;
        this.colunaInicialHeroi = coluna;
        this.adicionarPersonagemNaLista(this.heroi);
        this.heroi.setPosicao(linha, coluna);
    }

    private void adicionarPersonagemNaLista(Personagem p) {
        synchronized (this.personagens) {

            if (!this.personagens.contains(p)) {
                this.personagens.add(p);
            }
        }
    }

    protected void addPersonagemComPosicao(Personagem p, int linha, int coluna) {
        if (p == null) return;

        p.setPosicao(linha, coluna);
        this.adicionarPersonagemNaLista(p);

        if (p.isbColetavel() && !(p instanceof Coracao) && p != this.heroi) {
            if (this.heroi != null && this.heroi.podeColetar(p)) {
                this.totalColetaveisFase++;
            }
        }
    }

    public void addPersonagem(Personagem p) {
        if (p == null) return;

        this.adicionarPersonagemNaLista(p);

        if (p.isbColetavel() && !(p instanceof Coracao) && p != this.heroi) {
            if (this.heroi != null && this.heroi.podeColetar(p)) {
                this.totalColetaveisFase++;
                this.coletaveisRestantes++;
            }
        }
    }

    public void registrarMorteHeroi() {

        if (this.vidasRestantesHeroi > 0) {
            this.vidasRestantesHeroi--;
        }
    }

    public void respawnHeroiNaPosicaoInicial() {
        if (this.heroi != null) {
            this.heroi.setPosicao(this.linhaInicialHeroi, this.colunaInicialHeroi);

            synchronized (this.personagens) {
                for (Personagem p : this.personagens) {
                    if (p != this.heroi) {
                        p.respawn();
                    }
                }
            }
        }
    }

    public void reiniciarFaseCompletamente() {
        this.carregarFase();
    }

     public void heroiColetouItemLogicOnly(String itemClassName, boolean isCountableCollectible) {
        if (isCountableCollectible) {
            this.coletaveisRestantes--;
        }

        System.out.println("Item " + itemClassName + " coletado! Coletáveis restantes para o herói: " + this.coletaveisRestantes);
    }

    public boolean todosItensColetados() {
        return this.coletaveisRestantes <= 0;
    }

    public ArrayList<Personagem> getPersonagens() {
        return this.personagens;
    }

    public BaseHero getHeroi() {
        return this.heroi;
    }

    public String getBackgroundImagem() {
        return this.backgroundImagem;
    }

    public void removePersonagem(Personagem p) {
        synchronized (this.personagens) {
            this.personagens.remove(p);
        }
    }

    public int getColetaveisRestantes() {
        return coletaveisRestantes;
    }

    public int getVidasRestantesHeroi() {
        return vidasRestantesHeroi;
    }
}
