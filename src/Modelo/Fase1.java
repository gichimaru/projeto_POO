package Modelo;

public class Fase1 extends Fase {
    @Override
    public void carregarFase() {
        super.inicializarFaseBase(1);
        this.backgroundImagem = "background_fase1.png";
        super.definirHeroiPrincipal(new WallEHero("walle.png"), 1, 1);

        // Coletáveis
        addPersonagemComPosicao(new Lixo("lixo.png"), 1, 5);
        addPersonagemComPosicao(new Lixo("lixo.png"), 8, 3);
        addPersonagemComPosicao(new Lixo("lixo.png"), 10, 12);
        addPersonagemComPosicao(new Lixo("lixo.png"), 16, 8);
        addPersonagemComPosicao(new Lixo("lixo.png"), 23, 12);
        addPersonagemComPosicao(new Coracao("coracao.png"), 7, 8);

        // Obstáculos
        for (int i = 2; i < 17; i++) {
            addPersonagemComPosicao(new Bloco("bricks.png"), 4, i);
            addPersonagemComPosicao(new Bloco("bricks.png"), 9, i);
        }
        addPersonagemComPosicao(new Bloco("bricks.png"), 1, 3);
        addPersonagemComPosicao(new Bloco("bricks.png"), 3, 0);
        addPersonagemComPosicao(new Bloco("bricks.png"), 2, 3);
        addPersonagemComPosicao(new Bloco("bricks.png"), 3, 3);
        addPersonagemComPosicao(new Bloco("bricks.png"), 3, 2);
        addPersonagemComPosicao(new Bloco("bricks.png"), 3, 1);
        addPersonagemComPosicao(new Bloco("bricks.png"), 5, 5);
        addPersonagemComPosicao(new Bloco("bricks.png"), 5, 6);
        addPersonagemComPosicao(new Bloco("bricks.png"), 5, 7);
        addPersonagemComPosicao(new Bloco("bricks.png"), 6, 7);
        addPersonagemComPosicao(new Bloco("bricks.png"), 7, 7);
        addPersonagemComPosicao(new Bloco("bricks.png"), 10, 10);
        addPersonagemComPosicao(new Bloco("bricks.png"), 11, 10);
        addPersonagemComPosicao(new Bloco("bricks.png"), 9, 10);
        
        for (int i = 2; i < 18; i++) {
            addPersonagemComPosicao(new Bloco("bricks.png"), 14, i);
            addPersonagemComPosicao(new Bloco("bricks.png"), 19, i);
            addPersonagemComPosicao(new Bloco("bricks.png"), 24, i);
        }
        
        addPersonagemComPosicao(new Bloco("bricks.png"), 14, 18);
        addPersonagemComPosicao(new Bloco("bricks.png"), 14, 19);
        addPersonagemComPosicao(new Bloco("bricks.png"), 19, 0);
        addPersonagemComPosicao(new Bloco("bricks.png"), 19, 1);
        addPersonagemComPosicao(new Bloco("bricks.png"), 24, 18);
        addPersonagemComPosicao(new Bloco("bricks.png"), 24, 19);

        addPersonagemComPosicao(new Explosao("explosao.png"), 3, 4);
        addPersonagemComPosicao(new Explosao("explosao.png"), 2, 5);
        addPersonagemComPosicao(new Explosao("explosao.png"), 6, 9);
        addPersonagemComPosicao(new Explosao("explosao.png"), 10, 13);
        addPersonagemComPosicao(new Explosao("explosao.png"), 16, 10);
        addPersonagemComPosicao(new Explosao("explosao.png"), 22, 12);
        addPersonagemComPosicao(new Explosao("explosao.png"), 26, 15);

        // Inimigos
        addPersonagemComPosicao(new ZigueZague("robo.png",7,10), 7, 10);
        addPersonagemComPosicao(new ZigueZague("robo.png",22,10), 22, 10);

        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",2,12), 2, 12);

        // Ponto Final
        addPersonagemComPosicao(new PontoFinal("ponto_final.png"), 28, 19);

        this.coletaveisRestantes = this.totalColetaveisFase;
    }
}
