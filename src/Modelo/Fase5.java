package Modelo;

public class Fase5 extends Fase {
    @Override
    public void carregarFase() {
        super.inicializarFaseBase(5);
        this.backgroundImagem = "background_fase5.png";
        super.definirHeroiPrincipal(new WallEHero("walle.png"),1,1);

        // Coletáveis
        addPersonagemComPosicao(new Lixo("lixo.png"), 2, 10);
        addPersonagemComPosicao(new Lixo("lixo.png"), 10, 2);
        addPersonagemComPosicao(new Lixo("lixo.png"), 10, 5);
        addPersonagemComPosicao(new Lixo("lixo.png"), 5, 10);
        addPersonagemComPosicao(new Lixo("lixo.png"), 18, 8);
        addPersonagemComPosicao(new Lixo("lixo.png"), 22, 22);

        addPersonagemComPosicao(new Coracao("coracao.png"), 8, 5);
        addPersonagemComPosicao(new Coracao("coracao.png"), 15, 14);
        addPersonagemComPosicao(new Coracao("coracao.png"), 25, 16);

        for (int x = 3; x <= 24; x++) {
            // Linha superior (y = 4), com entrada no meio
            if (x != 12) addPersonagemComPosicao(new Bloco("bricks.png"), x, 3);
            // Linha inferior (y = 20), com entrada no meio
            if (x != 12) addPersonagemComPosicao(new Bloco("bricks.png"), x, 15);
        }

        for (int y = 3; y <=15 ; y++) {
            // Coluna esquerda (x = 4), com entrada no meio
            if (y != 12) addPersonagemComPosicao(new Bloco("bricks.png"), 3, y);
            // Coluna direita (x = 20), com entrada no meio
            if (y != 12) addPersonagemComPosicao(new Bloco("bricks.png"), 24, y);
        }
        
        // Obstáculos formando um quadrado com uma entrada em cada lado
        for (int x = 7; x <= 20; x++) {
            // Linha superior (y = 4), com entrada no meio
            if (x != 12) addPersonagemComPosicao(new Bloco("bricks.png"), x, 7);
            // Linha inferior (y = 20), com entrada no meio
            if (x != 12) addPersonagemComPosicao(new Bloco("bricks.png"), x, 11);
        }

        for (int y = 3; y <=15 ; y++) {
            // Coluna esquerda (x = 4), com entrada no meio
            if (y != 12) addPersonagemComPosicao(new Bloco("bricks.png"), 3, y);
            // Coluna direita (x = 20), com entrada no meio
            if (y != 12) addPersonagemComPosicao(new Bloco("bricks.png"), 24, y);
        }

        //Inimigos
        addPersonagemComPosicao(new InimigoEspecial("inimigoespecial.png",6,4), 6, 4);

        Chaser c = new Chaser("Chaser.png", 13, 13);
        addPersonagemComPosicao(c, 13, 13);

        addPersonagemComPosicao(new ZigueZague("roboPink.png",5,9), 5, 9);
        addPersonagemComPosicao(new ZigueZague("roboPink.png",5,5), 5, 5);
        addPersonagemComPosicao(new ZigueZague("roboPink.png",5,12), 5, 12);
        addPersonagemComPosicao(new ZigueZague("roboPink.png",20,9), 20, 9);
        addPersonagemComPosicao(new ZigueZague("roboPink.png",20,5), 20, 5);
        addPersonagemComPosicao(new ZigueZague("roboPink.png",20,12), 20, 12);

        addPersonagemComPosicao(new BichinhoVaiVemHorizontal("roboPink.png",1,9), 1, 9);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",9,1), 9, 1);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",26,4), 26, 4);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",26,14), 26, 14);
        addPersonagemComPosicao(new BichinhoVaiVemVertical("robo.png",5,16), 5, 16);

        // Ponto final
        addPersonagemComPosicao(new PontoFinal("ponto_final.png"), 28, 19);

        this.coletaveisRestantes = this.totalColetaveisFase;
    }
}
