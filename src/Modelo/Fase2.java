package Modelo;

public class Fase2 extends Fase {
    @Override
    public void carregarFase() {
        super.inicializarFaseBase(2);
        this.backgroundImagem = "background_fase2.png";

        super.definirHeroiPrincipal(new EVAHero("eva.png"),1,1);

        //Coletaveis
        addPersonagemComPosicao(new Planta("planta.png"),  3,  1);
        addPersonagemComPosicao(new Planta("planta.png"), 10, 10);
        addPersonagemComPosicao(new Planta("planta.png"),  2, 18);
        addPersonagemComPosicao(new Planta("planta.png"), 22,  3);
        addPersonagemComPosicao(new Planta("planta.png"), 15, 19);
        addPersonagemComPosicao(new Planta("planta.png"), 28, 12);

        addPersonagemComPosicao(new Coracao("coracao.png"), 15, 7);

        //Obstaculos
         for (int i = 2; i < 17; i++) {
            addPersonagemComPosicao(new Bloco("bricks.png"), 5, i);
            addPersonagemComPosicao(new Bloco("bricks.png"), 12, i);
        }
        addPersonagemComPosicao(new Bloco("bricks.png"), 5, 0);
        addPersonagemComPosicao(new Bloco("bricks.png"), 5, 1);
        
        for (int i = 2; i < 18; i++) {
            addPersonagemComPosicao(new Bloco("bricks.png"), 19, i);
            addPersonagemComPosicao(new Bloco("bricks.png"), 26, i);
        }
        addPersonagemComPosicao(new Bloco("bricks.png"), 19, 18);
        addPersonagemComPosicao(new Bloco("bricks.png"), 19, 19);
        addPersonagemComPosicao(new Bloco("bricks.png"), 26, 0);
        addPersonagemComPosicao(new Bloco("bricks.png"), 26, 1);

        addPersonagemComPosicao(new Explosao("explosao.png"), 11, 8);
        addPersonagemComPosicao(new Explosao("explosao.png"), 18, 10);
        addPersonagemComPosicao(new Explosao("explosao.png"), 27, 12);
        addPersonagemComPosicao(new Explosao("explosao.png"), 22, 12);
        addPersonagemComPosicao(new Explosao("explosao.png"), 25, 17);

        // Inimigos

        addPersonagemComPosicao(new InimigoEspecial("inimigoespecial.png",6,5), 6, 5);
        addPersonagemComPosicao(new InimigoEspecial("inimigoespecial.png",13,5), 13, 5);
        addPersonagemComPosicao(new InimigoEspecial("inimigoespecial.png", 20, 5), 20, 5);

        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",8,6), 8, 6);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",4,10), 4, 10);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",3,15), 3, 15);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",13,6), 13, 6);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",16,5), 16, 5);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",17,6), 17, 6);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",21,10), 21, 10);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",22,5), 22, 5);
        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",28,15), 28, 15);

        // Ponto final
        addPersonagemComPosicao(new PontoFinal("ponto_final.png"), 28, 19);

        this.coletaveisRestantes = this.totalColetaveisFase;
    }
}
