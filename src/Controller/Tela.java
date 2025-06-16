package Controller;

import Modelo.*;
import Auxiliar.*;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TimerTask;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {
    private Fase faseAtual;
    private final ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;

    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private int numeroFaseAtual = 1; 
    private int vidas;
    private Image backgroundAtual;

    private boolean isGamePaused = false;
    private boolean gameHasEnded = false;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setSize(Consts.RES * Consts.CELLSIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELLSIDE + getInsets().top + getInsets().bottom + 30);
        this.vidas = Consts.VIDAS_INICIAIS;
        this.faseAtual = FaseFactory.criarFase(numeroFaseAtual);

        carregarFaseEMenu(numeroFaseAtual);

        setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY, new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent dtde) {}
            public void dragOver(DropTargetDragEvent dtde) {}
            public void dropActionChanged(DropTargetDragEvent dtde) {}
            public void dragExit(DropTargetEvent dte) {}

            public void drop(DropTargetDropEvent dtde) {

                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = dtde.getTransferable();

                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

                        List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                        if (!fileList.isEmpty()) {

                            File arquivo = fileList.getFirst();

                            if (arquivo.getName().toLowerCase().endsWith(".zip")) {

                                try (FileInputStream fis = new FileInputStream(arquivo);
                                     GZIPInputStream gzis = new GZIPInputStream(fis);
                                     ObjectInputStream ois = new ObjectInputStream(gzis)) {

                                    Personagem novoPersonagem = (Personagem) ois.readObject();

                                    Point ponto = dtde.getLocation();

                                    int coluna = (ponto.x - getInsets().left) / Consts.CELLSIDE + cameraColuna;
                                    int linha = (ponto.y - getInsets().top) / Consts.CELLSIDE + cameraLinha;
                                    novoPersonagem.setPosicao(linha, coluna);

                                    if (faseAtual != null) {
                                        faseAtual.addPersonagem(novoPersonagem); // addPersonagem faz a sincronização internamente ja
                                    }

                                    repaint();
                                    JOptionPane.showMessageDialog(Tela.this,
                                            "Personagem adicionado na posição: " + linha + ", " + coluna,
                                            "Personagem Carregado via Drag&Drop", JOptionPane.INFORMATION_MESSAGE);
                                } catch (IOException | ClassNotFoundException ex) {

                                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao desserializar personagem do ZIP (Drag&Drop)", ex);
                                    JOptionPane.showMessageDialog(Tela.this,
                                            "Erro ao carregar personagem do ZIP: " + ex.getMessage(), "Erro de Carregamento", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(Tela.this, "Por favor, arraste um arquivo .zip.", "Arquivo Inválido", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                    dtde.dropComplete(true);
                } catch (Exception ex) {
                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro no evento drop", ex);

                    if(dtde.isLocalTransfer()) {
                        dtde.dropComplete(false);
                    }
                }
            }
        }));
        gerarArquivosDemonstracao();
    }

    public boolean isGamePaused() {
        return this.isGamePaused;
    }

    private void gerarArquivosDemonstracao() {
        BaseHero evaHero = new EVAHero("eva.png");
        evaHero.setPosicao(0, 0);
        salvarPersonagemParaZip(evaHero, "EvaDemo.zip");

        BaseHero walleHero = new WallEHero("walle.png");
        walleHero.setPosicao(0, 0);
        salvarPersonagemParaZip(walleHero, "WalleDemo.zip");

        InimigoEspecial AI = new InimigoEspecial("inimigoespecial.png",0,0);
        AI.setPosicao(0, 0);
        salvarPersonagemParaZip(AI, "inimigoespecial.zip");

        System.out.println("Arquivos de demonstração (EvaDemo.zip, WalleDemo.zip, inimigoespecial.zip) gerados na raiz do projeto.");
    }

    public Fase getFaseAtual() {
        return faseAtual;
    }

    public ControleDeJogo getControleDeJogo() {
        return cj;
    }

    public void atualizarTitulo() {
        String titulo = Consts.NOME_DO_JOGO + " - Fase: " + numeroFaseAtual +
                " | Vidas Fase: " + (faseAtual != null ? faseAtual.getVidasRestantesHeroi() : "N/A") +
                " | Tentativas Jogo: " + this.vidas + // Vidas globais da Tela
                " | Coletáveis: " + (faseAtual != null ? faseAtual.getColetaveisRestantes() : "N/A") +
                " | Placar: " + cj.getPlacar();
        setTitle(titulo);
    }

    public void tratarColisaoFatalHeroi() {
        faseAtual.registrarMorteHeroi();

        if (faseAtual.getVidasRestantesHeroi() > 0) {
            // faz respawn do herói e dos chasers
            faseAtual.respawnHeroiNaPosicaoInicial();

            for (Personagem p : faseAtual.getPersonagens()) {
                if (p instanceof Chaser) {
                    p.respawn();
                }
            }

            this.isGamePaused = true;

            JOptionPane.showMessageDialog(this,
                    "Você foi atingido! Vidas restantes nesta fase: " +
                            faseAtual.getVidasRestantesHeroi(),
                    "Cuidado!",
                    JOptionPane.WARNING_MESSAGE);

            this.isGamePaused = false;
        } else {
            this.vidas--;

            if (this.vidas < 0) {

                faseAtual.respawnHeroiNaPosicaoInicial();
                this.isGamePaused = true;

                JOptionPane.showMessageDialog(this,
                        "Você não tem mais tentativas! Game Over.",
                        "Game Over Total",
                        JOptionPane.ERROR_MESSAGE);

                this.dispose();
                System.exit(0);
            } else {
                this.isGamePaused = true;

                int resposta = JOptionPane.showConfirmDialog(this,
                        "Você perdeu todas as vidas desta fase!\n" +
                                "Deseja tentar a fase novamente? (Tentativas restantes: " + this.vidas + ")",
                        "Fim da Tentativa de Fase",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                this.isGamePaused = false;

                if (resposta == JOptionPane.YES_OPTION) {

                    faseAtual.reiniciarFaseCompletamente();
                    this.isGamePaused = true;

                    JOptionPane.showMessageDialog(this,
                            "Fase reiniciada! Boa sorte!",
                            "Nova Tentativa",
                            JOptionPane.INFORMATION_MESSAGE);

                    this.isGamePaused = false;
                } else {
                    this.isGamePaused = true;

                    JOptionPane.showMessageDialog(this,
                            "Você escolheu não continuar.",
                            "Fim de Jogo",
                            JOptionPane.INFORMATION_MESSAGE);

                    this.isGamePaused = false;
                    fimDeJogo(false);
                }
            }
        }
        atualizarTitulo();
    }


    private void carregarFaseEMenu(int numero) {
        
        this.faseAtual = FaseFactory.criarFase(numeroFaseAtual);
        
        if (numero > Consts.TOTAL_FASES) {
            fimDeJogo(true);
            return;
        }

        this.numeroFaseAtual = numero;
        this.faseAtual.carregarFase();

        atualizarBackground();
        atualizaCamera();
        atualizarTitulo();

        System.out.println("Fase " + numeroFaseAtual + " carregada. Herói: " +
                (faseAtual.getHeroi() != null ? faseAtual.getHeroi().getClass().getSimpleName() : "Nenhum"));
        System.out.println("Itens para coletar: " + faseAtual.getColetaveisRestantes());
    }

    public void proximaFase() {
        this.numeroFaseAtual++;

        if (this.numeroFaseAtual > Consts.TOTAL_FASES) {
            fimDeJogo(true);
        } else {
            cj.resetPlacar();
            carregarFaseEMenu(this.numeroFaseAtual);
        }
    }

    private void reiniciarFase() {
        System.out.println("Reiniciando fase " + numeroFaseAtual);
        this.faseAtual.carregarFase();

        atualizarBackground();
        atualizaCamera();
        atualizarTitulo();
    }

    private void fimDeJogo(boolean vitoria) {
        if(gameHasEnded){
            return;
        }

        gameHasEnded = true;
        isGamePaused = true;

        String mensagem;
        if (vitoria) {

            mensagem = "Parabéns! Você completou todas as fases!\n" +
                    "Jogo desenvolvido por: " + Consts.AUTORES + "\n" +
                    "Placar Final: " + cj.getPlacar();
            JOptionPane.showMessageDialog(this,
                    mensagem,
                    "Vitória!",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            mensagem = "Game Over!\n" +
                    "Placar Final: " + cj.getPlacar();
            JOptionPane.showMessageDialog(this,
                    mensagem,
                    "Fim de Jogo",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        this.dispose();
        System.exit(0);
    }

    private void atualizarBackground() {
        try {

            if (faseAtual != null && faseAtual.getBackgroundImagem() != null) {

                String imagePath = new java.io.File(".").getCanonicalPath() +
                        Consts.PATH + faseAtual.getBackgroundImagem();
                backgroundAtual = Toolkit.getDefaultToolkit().getImage(imagePath);
            } else {
                backgroundAtual = null;
            }
        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao carregar background", ex);
            backgroundAtual = null;
        }
    }

    public void addPersonagemNaFase(Personagem p) {
        if (faseAtual != null) {
            faseAtual.addPersonagem(p);
        }
    }

    public void removePersonagemDaFase(Personagem p) {
        if (faseAtual != null) {
            faseAtual.removePersonagem(p);
        }
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    @Override
    public void paint(Graphics gOld) {
        BufferStrategy bufferStrategy = this.getBufferStrategy();

        if (bufferStrategy == null) {

            super.paint(gOld);
            return;
        }

        Graphics g = null;

        try {
            g = bufferStrategy.getDrawGraphics();

            if (g == null) {
                return;
            }

            int mainAreaHeight = getHeight() - getInsets().top - getInsets().bottom - 30;

            this.g2 = g.create(getInsets().left, getInsets().top,
                    getWidth() - getInsets().left - getInsets().right,
                    mainAreaHeight);

            if (this.g2 == null) {
                g.dispose();
                return;
            }

            if (backgroundAtual != null) {
                for (int i = 0; i < Consts.RES; i++) {
                    for (int j = 0; j < Consts.RES; j++) {

                        this.g2.drawImage(backgroundAtual, j * Consts.CELLSIDE, i * Consts.CELLSIDE,
                                Consts.CELLSIDE, Consts.CELLSIDE, null);
                    }
                }
            } else {
                this.g2.setColor(Color.BLACK);
                this.g2.fillRect(0, 0, Consts.RES * Consts.CELLSIDE, Consts.RES * Consts.CELLSIDE);
            }

            if (this.faseAtual != null && this.faseAtual.getPersonagens() != null && !this.faseAtual.getPersonagens().isEmpty()) {
                this.cj.desenhaTudo(faseAtual);

                if (!this.isGamePaused){
                    this.cj.processaTudo(faseAtual, this);
                }
            }

            Graphics hudGraphics = g.create(getInsets().left,
                    getHeight() - getInsets().bottom - 25,
                    getWidth() - getInsets().left - getInsets().right,
                    20);

            hudGraphics.setColor(Color.BLACK);
            hudGraphics.fillRect(0, 0, getWidth() - getInsets().left - getInsets().right, 20);
            hudGraphics.setColor(Color.WHITE);
            hudGraphics.setFont(new Font("Arial", Font.BOLD, 14));

            String hudText = "Fase: " + numeroFaseAtual +
                    " | Vidas: " + vidas +
                    " | Coletáveis: " + (faseAtual != null ? faseAtual.getColetaveisRestantes() : "N/A") +
                    " | Placar: " + cj.getPlacar();

            hudGraphics.drawString(hudText, 10, 15);
            hudGraphics.dispose();

            if (this.g2 != null) {
                this.g2.dispose();
            }

            if (!bufferStrategy.contentsLost()) {
                bufferStrategy.show();
            }
        } catch (Exception e) {
            System.err.println("Exceção em Tela.paint(): " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }


    public void atualizaCamera() {
        if (faseAtual == null || faseAtual.getHeroi() == null) {
            return;
        }

        int linhaHeroi = faseAtual.getHeroi().getPosicao().getLinha();
        int colunaHeroi = faseAtual.getHeroi().getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linhaHeroi - Consts.RES / 2, Consts.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(colunaHeroi - Consts.RES / 2, Consts.MUNDO_LARGURA - Consts.RES));
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public void go() {
        java.util.Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                atualizaCamera();
                repaint();
            }
        };
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
        if (isGamePaused || faseAtual == null || faseAtual.getHeroi() == null) {
            return;
        }

        BaseHero heroi = faseAtual.getHeroi();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    heroi.moveUp();    break;
            case KeyEvent.VK_DOWN:  heroi.moveDown();  break;
            case KeyEvent.VK_LEFT:  heroi.moveLeft();  break;
            case KeyEvent.VK_RIGHT: heroi.moveRight(); break;
            case KeyEvent.VK_S:     salvarJogo();      break;
            case KeyEvent.VK_L:     carregarJogo();    break;
            case KeyEvent.VK_R:     reiniciarFase();   break;
        }
    }

    private void salvarJogo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Estado do Jogo");
        fileChooser.setSelectedFile(new File("fase_salva.zip"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo ZIP de Jogo (*.zip)", "zip"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().toLowerCase().endsWith(".zip")) {

                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".zip");
            }
            try (FileOutputStream fos = new FileOutputStream(fileToSave);
                 GZIPOutputStream gzos = new GZIPOutputStream(fos);
                 ObjectOutputStream oos = new ObjectOutputStream(gzos)) {

                oos.writeObject(this.faseAtual);
                oos.writeInt(this.numeroFaseAtual);
                oos.writeInt(this.vidas);
                oos.writeInt(this.cj.getPlacar());

                JOptionPane.showMessageDialog(this, "Jogo salvo com sucesso em: " + fileToSave.getAbsolutePath(), "Jogo Salvo", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Jogo salvo!");
            } catch (IOException ex) {
                Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao salvar o jogo", ex);
                JOptionPane.showMessageDialog(this, "Erro ao salvar o jogo: " + ex.getMessage(), "Erro de Salvamento", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarJogo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Carregar Estado do Jogo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo ZIP de Jogo (*.zip)", "zip"));
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            File fileToLoad = fileChooser.getSelectedFile();

            try (FileInputStream fis = new FileInputStream(fileToLoad);
                 GZIPInputStream gzis = new GZIPInputStream(fis);
                 ObjectInputStream ois = new ObjectInputStream(gzis)) {

                this.faseAtual = (Fase) ois.readObject();
                this.numeroFaseAtual = ois.readInt();
                this.vidas = ois.readInt();
                int placarCarregado = ois.readInt();
                this.cj.setPlacar(placarCarregado);

                atualizarBackground();
                atualizaCamera();
                atualizarTitulo();
                JOptionPane.showMessageDialog(this, "Jogo carregado de: " + fileToLoad.getAbsolutePath(), "Jogo Carregado", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Jogo carregado!");
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao carregar o jogo", ex);
                JOptionPane.showMessageDialog(this, "Erro ao carregar o jogo: " + ex.getMessage(), "Erro de Carregamento", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Carregar Personagem de Arquivo .zip (via Clique)");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivo ZIP de Personagem (*.zip)", "zip"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

                File selectedFile = fileChooser.getSelectedFile();

                try (FileInputStream fis = new FileInputStream(selectedFile);
                     GZIPInputStream gzis = new GZIPInputStream(fis);
                     ObjectInputStream ois = new ObjectInputStream(gzis)) {

                    Personagem p = (Personagem) ois.readObject();

                    int dropColuna = (e.getX() - getInsets().left) / Consts.CELLSIDE + cameraColuna;
                    int dropLinha = (e.getY() - getInsets().top) / Consts.CELLSIDE + cameraLinha;

                    p.setPosicao(dropLinha, dropColuna);

                    if (faseAtual != null) {
                        faseAtual.addPersonagem(p);
                    }

                    repaint();
                    JOptionPane.showMessageDialog(this, "Personagem adicionado na posição (clique): " + dropLinha + ", " + dropColuna, "Personagem Carregado", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao carregar personagem (clique)", ex);
                    JOptionPane.showMessageDialog(this, "Erro ao carregar personagem: " + ex.getMessage(), "Erro de Carregamento", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void salvarPersonagemParaZip(Personagem personagem, String nomeArquivoBase) {
        String nomeArquivoCompleto = nomeArquivoBase.toLowerCase().endsWith(".zip") ?
                nomeArquivoBase : nomeArquivoBase + ".zip";
        File arquivoZip = new File(nomeArquivoCompleto);

        try (FileOutputStream fos = new FileOutputStream(arquivoZip);
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(gzos)) {

            oos.writeObject(personagem);
            System.out.println("Personagem salvo em: " + arquivoZip.getAbsolutePath());
        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, "Erro ao salvar personagem para ZIP: " + personagem.getClass().getSimpleName(), ex);
            JOptionPane.showMessageDialog(this, "Erro ao salvar personagem '" + nomeArquivoBase + "': " + ex.getMessage(), "Erro de Salvamento", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean ehPosicaoValidaHeroi(Posicao p, BaseHero heroi) {
        if (faseAtual == null || faseAtual.getPersonagens() == null) return false;
        return cj.ehPosicaoValidaHeroi(this.faseAtual.getPersonagens(), p, heroi);
    }

    public boolean ehPosicaoValidaMovimento(Posicao p, Personagem quemMove) {
        if (faseAtual == null || faseAtual.getPersonagens() == null) return false;
        return cj.ehPosicaoValidaMovimento(this.faseAtual.getPersonagens(), p, quemMove);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isGamePaused) {
            return;
        }

        if (faseAtual == null || faseAtual.getHeroi() == null) {
            return;
        }

        if (e.getButton() == MouseEvent.BUTTON1) {

            int colunaClicada = (e.getX() - getInsets().left) / Consts.CELLSIDE + cameraColuna;
            int linhaClicada = (e.getY() - getInsets().top) / Consts.CELLSIDE + cameraLinha;

            BaseHero heroi = faseAtual.getHeroi();
            Posicao novaPosicao = new Posicao(linhaClicada, colunaClicada);

            if (ehPosicaoValidaMovimento(novaPosicao, heroi)) {
                heroi.setPosicao(linhaClicada, colunaClicada);
                atualizaCamera();
                repaint();
                atualizarTitulo();
            } else {
                System.out.println("Movimento para " + linhaClicada + ", " + colunaClicada + " é inválido.");
            }
        }
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Consts.NOME_DO_JOGO);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, Consts.RES * Consts.CELLSIDE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, Consts.RES * Consts.CELLSIDE + 30, Short.MAX_VALUE)
        );

        pack();
    }
}