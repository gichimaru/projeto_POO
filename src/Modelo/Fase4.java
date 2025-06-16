package Modelo;

public class Fase4 extends Fase {
    @Override
    public void carregarFase() {
        super.inicializarFaseBase(4);
        this.backgroundImagem = "background_fase4.png";
        super.definirHeroiPrincipal(new EVAHero("eva.png"),1,1);

        // Coletáveis
        addPersonagemComPosicao(new Planta("planta.png"), 2, 2);
        addPersonagemComPosicao(new Planta("planta.png"), 2, 14);
        addPersonagemComPosicao(new Planta("planta.png"), 2, 7);
        addPersonagemComPosicao(new Planta("planta.png"), 12, 2);
        addPersonagemComPosicao(new Planta("planta.png"), 12, 14);
        addPersonagemComPosicao(new Planta("planta.png"), 12, 7);
        addPersonagemComPosicao(new Planta("planta.png"), 24, 2);
        addPersonagemComPosicao(new Planta("planta.png"), 24, 14);
        addPersonagemComPosicao(new Planta("planta.png"), 24, 7);
        addPersonagemComPosicao(new Planta("planta.png"), 6, 2);
        addPersonagemComPosicao(new Planta("planta.png"), 6, 14);
        addPersonagemComPosicao(new Planta("planta.png"), 6, 7);
        addPersonagemComPosicao(new Planta("planta.png"), 18, 2);
        addPersonagemComPosicao(new Planta("planta.png"), 18, 14);
        addPersonagemComPosicao(new Planta("planta.png"), 18, 7);

        addPersonagemComPosicao(new Coracao("coracao.png"), 8, 7);
        addPersonagemComPosicao(new Coracao("coracao.png"), 26, 15);

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
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",3,5), 3, 5);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",9,5), 9, 5);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",15,5), 15, 5);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",21,5), 21, 5);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",27,5), 27, 5);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",3,11), 3, 11);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",9,11), 9, 11);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",15,11), 15, 11);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",21,11), 21, 11);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",27,11), 27, 11);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",11,6), 11, 6);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",17,6), 17, 6);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",23,6), 23, 6);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",29,6), 29, 6);

        // Ponto final
        addPersonagemComPosicao(new PontoFinal("ponto_final.png"), 28, 19);

        this.coletaveisRestantes = this.totalColetaveisFase;
    }
}
