import Controller.Tela;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            Tela tTela = new Tela();
            tTela.setVisible(true);
            tTela.createBufferStrategy(2);
            tTela.go();
        });
    }
}