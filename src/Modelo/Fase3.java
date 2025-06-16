package Modelo;

public class Fase3 extends Fase {
    @Override
    public void carregarFase() {
        super.inicializarFaseBase(3);
        this.backgroundImagem = "background_fase3.png";
        super.definirHeroiPrincipal(new WallEHero("walle.png"),1,1);

        // Coletáveis
        addPersonagemComPosicao(new Lixo("lixo.png"), 2, 2);
        addPersonagemComPosicao(new Lixo("lixo.png"), 2, 14);
        addPersonagemComPosicao(new Lixo("lixo.png"), 12, 2);
        addPersonagemComPosicao(new Lixo("lixo.png"), 12, 14);
        addPersonagemComPosicao(new Lixo("lixo.png"), 24, 2);
        addPersonagemComPosicao(new Lixo("lixo.png"), 24, 14);
        addPersonagemComPosicao(new Lixo("lixo.png"), 6, 2);
        addPersonagemComPosicao(new Lixo("lixo.png"), 6, 14);
        addPersonagemComPosicao(new Lixo("lixo.png"), 18, 2);
        addPersonagemComPosicao(new Lixo("lixo.png"), 18, 14);

        addPersonagemComPosicao(new Coracao("coracao.png"), 1, 8);
        addPersonagemComPosicao(new Coracao("coracao.png"), 20, 15);

        // Obstáculos
        for (int i = 0; i < 29; i += 2) {
            if (i != 6) addPersonagemComPosicao(new Bloco("bricks.png"), i, 4);
            if (i != 6) addPersonagemComPosicao(new Bloco("bricks.png"), i, 10);
            if (i != 6 && i < 18) addPersonagemComPosicao(new Bloco("bricks.png"), 4, i + 1);
            if (i != 6 && i < 18) addPersonagemComPosicao(new Bloco("bricks.png"), 10, i + 1);
            if (i != 6 && i < 18) addPersonagemComPosicao(new Bloco("bricks.png"), 16, i + 1);
            if (i != 6 && i < 18) addPersonagemComPosicao(new Bloco("bricks.png"), 22, i + 1);
        }

        // Inimigos
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",5,6), 5, 6);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",11,6), 11, 6);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",17,6), 17, 6);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",23,6), 23, 6);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",29,6), 29, 6);

        Chaser c = new Chaser("Chaser.png", 5, 7);
        addPersonagemComPosicao(c, 5, 7);

        // Ponto final
        addPersonagemComPosicao(new PontoFinal("ponto_final.png"), 28, 19);

        this.coletaveisRestantes = this.totalColetaveisFase;
    }
}

