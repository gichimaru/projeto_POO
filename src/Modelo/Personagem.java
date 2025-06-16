package Modelo;

import Auxiliar.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import javax.swing.ImageIcon;

public abstract class Personagem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected transient ImageIcon iImage;
    protected String sNomeImagePNG;
    protected Posicao pPosicao;
    protected boolean bTransponivel;
    protected boolean bMortal;
    protected boolean bColetavel;
    protected boolean bDestrutivel;

    protected Personagem(String sNomeImagePNG) {
        this.sNomeImagePNG = sNomeImagePNG;
        this.pPosicao = new Posicao(0, 0);
        this.bTransponivel = true;
        this.bMortal = false;
        this.bColetavel = false;
        this.bDestrutivel = false;
        loadImage();
    }

    private void loadImage() {
        try {

            if (this.sNomeImagePNG == null || this.sNomeImagePNG.isEmpty()) {
                throw new IOException("Nome do arquivo de imagem (sNomeImagePNG) é nulo ou vazio.");
            }

            String imagePath = new File(".").getCanonicalPath() + Consts.PATH + this.sNomeImagePNG;
            System.out.println("Tentando carregar imagem primária: " + imagePath); // Log para depuração
            ImageIcon tempImage = new ImageIcon(imagePath);

            if (tempImage.getImageLoadStatus() == MediaTracker.ERRORED || tempImage.getIconWidth() == -1 || tempImage.getIconHeight() == -1) {
                throw new IOException("Erro ao carregar dados da imagem primária: " + this.sNomeImagePNG + " (Status: " + tempImage.getImageLoadStatus() + ", Largura: " + tempImage.getIconWidth() + ")");
            }

            Image img = tempImage.getImage();
            BufferedImage bi = new BufferedImage(Consts.CELLSIDE, Consts.CELLSIDE, BufferedImage.TYPE_INT_ARGB);

            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELLSIDE, Consts.CELLSIDE, null);
            this.iImage = new ImageIcon(bi);
            g.dispose();

            System.out.println("Imagem primária '" + this.sNomeImagePNG + "' carregada com sucesso."); // Log para depuração
            return;

        } catch (IOException ex) {
            System.err.println("FALHA ao carregar imagem primária '" + (this.sNomeImagePNG != null ? this.sNomeImagePNG : "NOME_NULO") + "': " + ex.getMessage());
        }

        // Se a imagem primária falhar tentar carregar a imagem de fallback
        try {
            String fallbackImagePath = new File(".").getCanonicalPath() + Consts.PATH + "blackTile.png";
            System.out.println("Tentando carregar imagem de fallback: " + fallbackImagePath);
            ImageIcon tempFallbackImage = new ImageIcon(fallbackImagePath);

            if (tempFallbackImage.getImageLoadStatus() == MediaTracker.ERRORED || tempFallbackImage.getIconWidth() == -1 || tempFallbackImage.getIconHeight() == -1) {
                throw new IOException("Erro ao carregar dados da imagem de fallback: blackTile.png (Status: " + tempFallbackImage.getImageLoadStatus() + ", Largura: " + tempFallbackImage.getIconWidth() + ")");
            }

            Image imgFallback = tempFallbackImage.getImage();
            BufferedImage biFallback = new BufferedImage(Consts.CELLSIDE, Consts.CELLSIDE, BufferedImage.TYPE_INT_ARGB);

            Graphics gFallback = biFallback.createGraphics();
            gFallback.drawImage(imgFallback, 0, 0, Consts.CELLSIDE, Consts.CELLSIDE, null);
            this.iImage = new ImageIcon(biFallback);
            gFallback.dispose();

            System.out.println("SUCESSO: Imagem de fallback 'blackTile.png' carregada para personagem que usaria '" + (this.sNomeImagePNG != null ? this.sNomeImagePNG : "NOME_NULO") + "'.");

        } catch (IOException ex2) {
            System.err.println("FALHA CRÍTICA: Não foi possível carregar nem a imagem primária nem a imagem de fallback 'blackTile.png'. Personagem '" + (this.sNomeImagePNG != null ? this.sNomeImagePNG : "NOME_NULO") + "' ficará sem imagem. Erro: " + ex2.getMessage());
            this.iImage = null;
        }
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        loadImage();
    }

    public Posicao getPosicao() {
        return pPosicao;
    }

    public boolean setPosicao(int linha, int coluna) {
        return pPosicao.setPosicao(linha, coluna);
    }

    public boolean isbTransponivel() {
        return bTransponivel;
    }

    public void setbTransponivel(boolean bTransponivel) {
        this.bTransponivel = bTransponivel;
    }

    public boolean isbMortal() {
        return bMortal;
    }

    public boolean isbColetavel() {
        return bColetavel;
    }

    public void autoDesenho() {

        if (this.iImage != null) {
            Desenho.desenhar(this.iImage, this.pPosicao.getColuna(), this.pPosicao.getLinha());
        } else {
            System.err.println("Tentativa de desenhar " + this.getClass().getSimpleName() + " (imagem: " + sNomeImagePNG + ") mas iImage é NULO. Posição: L" + pPosicao.getLinha() + " C" + pPosicao.getColuna());
        }
    }

    public boolean moveUp() {
        return this.pPosicao.moveUp();
    }

    public boolean moveDown() {
        return this.pPosicao.moveDown();
    }

    public boolean moveRight() {
        return this.pPosicao.moveRight();
    }

    public boolean moveLeft() {
        return this.pPosicao.moveLeft();
    }

    protected boolean moveUpValidando() {
        Posicao proximaPos = new Posicao(pPosicao.getLinha() - 1, pPosicao.getColuna());
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.ehPosicaoValidaMovimento(proximaPos, this)) {
            return this.pPosicao.moveUp();
        }

        return false;
    }

    protected boolean moveDownValidando() {
        Posicao proximaPos = new Posicao(pPosicao.getLinha() + 1, pPosicao.getColuna());
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.ehPosicaoValidaMovimento(proximaPos, this)) {
            return this.pPosicao.moveDown();
        }

        return false;
    }

    protected boolean moveLeftValidando() {
        Posicao proximaPos = new Posicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.ehPosicaoValidaMovimento(proximaPos, this)) {
            return this.pPosicao.moveLeft();
        }

        return false;
    }

    protected boolean moveRightValidando() {
        Posicao proximaPos = new Posicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
        Controller.Tela tela = Desenho.acessoATelaDoJogo();

        if (tela != null && tela.ehPosicaoValidaMovimento(proximaPos, this)) {
            return this.pPosicao.moveRight();
        }

        return false;
    }

    public void respawn() {}
}

