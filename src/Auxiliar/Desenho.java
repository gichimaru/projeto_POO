package Auxiliar;

import java.awt.Graphics;
import java.io.Serial;
import java.io.Serializable;
import javax.swing.ImageIcon;
import Controller.Tela;

public class Desenho implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    static Tela jCenario;

    public static void setCenario(Tela umJCenario) {
        jCenario = umJCenario;
    }

    public static Tela acessoATelaDoJogo() {
        return jCenario;
    }

    public static Graphics getGraphicsDaTela() {
        if (jCenario != null) {
            return jCenario.getGraphicsBuffer();
        }
        return null;
    }

    public static void desenhar(ImageIcon iImage, int iColuna, int iLinha) {
        Graphics gParaDesenhar = getGraphicsDaTela();

        if (jCenario == null || iImage == null || gParaDesenhar == null) {
            return;
        }

        int telaX = (iColuna - jCenario.getCameraColuna()) * Consts.CELLSIDE;
        int telaY = (iLinha - jCenario.getCameraLinha()) * Consts.CELLSIDE;

        if (telaX + Consts.CELLSIDE > 0 && telaX < Consts.RES * Consts.CELLSIDE &&
                telaY + Consts.CELLSIDE > 0 && telaY < Consts.RES * Consts.CELLSIDE) {
            iImage.paintIcon(jCenario, gParaDesenhar, telaX, telaY);
        }
    }
}