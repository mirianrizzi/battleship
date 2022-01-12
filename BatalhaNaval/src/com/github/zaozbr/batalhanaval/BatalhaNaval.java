package com.github.zaozbr.batalhanaval;

import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {

  private static final int TAMANHO_GRID = 10;
  private static final int NUMERO_NAVIOS = 10;
  private static final String CHARS_COORDENADAS = "ABCDEFGHIJ";

  private final char[][] gridJogador;
  private final char[][] gridComputador;

  Scanner scanner;

  public BatalhaNaval() {
    this.scanner = new Scanner(System.in);

    this.gridJogador = new char[TAMANHO_GRID][TAMANHO_GRID];
    this.resetarGrid(gridJogador);

    this.gridComputador = new char[TAMANHO_GRID][TAMANHO_GRID];
    this.resetarGrid(gridComputador);
  }

  public void play() {
    String vencedor;

    this.distribuirNaviosManualmente(gridJogador);
    this.distribuirNaviosAleatoriamente(gridComputador);

    while (true) {
      ScreenUtil.clearScreen();
      this.imprimirAviso("Digite as coordenadas do seu tiro!");

      this.imprimirGrid(gridJogador, "JOGADOR");

      this.lerTiroJogador();
      if (this.obterNumeroDeNaviosPosicionados(gridComputador) == 0) {
        vencedor = "JOGADOR";
        break;
      }

      this.lerTiroComputador();
      if (this.obterNumeroDeNaviosPosicionados(gridJogador) == 0) {
        vencedor = "COMPUTADOR";
        break;
      }
    }

    ScreenUtil.clearScreen();

    this.imprimirAviso(
        String.format("Vencedor: %s!", vencedor));

    ScreenUtil.printTextLine("");
    this.imprimirGrid(gridJogador, "JOGADOR");

    ScreenUtil.printTextLine("");
    this.imprimirGrid(gridComputador, "COMPUTADOR");
  }

  private void resetarGrid(char[][] grid) {

    for (int linha = 0; linha < TAMANHO_GRID; linha++) {
      for (int coluna = 0; coluna < TAMANHO_GRID; coluna++) {
        grid[linha][coluna] = ' ';
      }
    }
  }

  private void distribuirNaviosAleatoriamente(char[][] grid) {

    for (int navio = 0; navio < NUMERO_NAVIOS; navio++) {

      while (true) {

        int randomLinha = this.obterCoordenadaAleatoria();
        int randomColuna = this.obterCoordenadaAleatoria();

        if (grid[randomLinha][randomColuna] == ' ') {
          grid[randomLinha][randomColuna] = 'N';
          break;
        }
      }
    }
  }

  private void distribuirNaviosManualmente(char[][] grid) {

    for (int navio = 0; navio < NUMERO_NAVIOS; navio++) {

      ScreenUtil.clearScreen();
      this.imprimirAviso(
          String.format(
              "Escolha a posição dos seus %d navios na grade!",
              NUMERO_NAVIOS));

      this.imprimirGrid(grid, "JOGADOR");

      System.out.printf("Navio %d!%n", navio + 1);

      while (true) {

        int[] coordenadas = this.lerCoordenadas();

        int linha = coordenadas[0];
        int coluna = coordenadas[1];

        if (grid[linha][coluna] == ' ') {
          grid[linha][coluna] = 'N';
          break;
        } else {
          System.out.println("Posição já ocupada.");
        }
      }
    }
  }

  private void lerTiroJogador() {
    int[] tiroCoordenadas = this.lerCoordenadas();

    this.atirar(
        tiroCoordenadas[0],
        tiroCoordenadas[1],
        this.gridJogador,
        this.gridComputador);
  }

  private void lerTiroComputador() {
    int tiroLinha, tiroColuna;

    while (true) {
      tiroLinha = this.obterCoordenadaAleatoria();
      tiroColuna = this.obterCoordenadaAleatoria();

      char posicaoNoTiro = this.gridComputador[tiroLinha][tiroColuna];

      if (posicaoNoTiro == ' ' || posicaoNoTiro == 'N') {
        break;
      }
    }

    this.atirar(
        tiroLinha,
        tiroColuna,
        this.gridComputador,
        this.gridJogador);
  }

  private int[] lerCoordenadas() {

    int[] coordenadas = new int[2];
    int linha, coluna;

    while (true) {
      System.out.print("Coordenadas [linha][coluna]: ");

      String coordenadasStr = this.scanner.nextLine().toUpperCase();

      if (coordenadasStr.isBlank() || coordenadasStr.length() < 2) {
        System.out.println("Coordenadas inválidas.");
        continue;
      }

      char linhaChar = coordenadasStr.charAt(0);
      linha = CHARS_COORDENADAS.indexOf(linhaChar);

      if (linha == -1) {
        System.out.println("Linha inválida.");
        continue;
      }

      String colunaStr = coordenadasStr.substring(1);
      try {
        coluna = Integer.parseInt(colunaStr);
      } catch (NumberFormatException e) {
        System.out.println("Coluna inválida.");
        continue;
      }

      if (coluna < 0 || coluna >= TAMANHO_GRID) {
        System.out.println("Coluna inválida.");
        continue;
      }

      break;
    }

    coordenadas[0] = linha;
    coordenadas[1] = coluna;

    return coordenadas;
  }

  private void atirar(int linha, int coluna, char[][] gridOrigem, char[][] gridAlvo) {

    char posicaoOrigem = gridOrigem[linha][coluna];
    char posicaoAlvo = gridAlvo[linha][coluna];

    boolean acertouTiro = false;

    if (posicaoAlvo == 'N') {
      acertouTiro = true;
      gridAlvo[linha][coluna] = ' ';
    }

    if (posicaoAlvo == 'X') {
      acertouTiro = true;
      gridAlvo[linha][coluna] = '*';
    }

    if (posicaoAlvo == 'n') {
      acertouTiro = true;
      gridAlvo[linha][coluna] = '-';
    }

    if (acertouTiro) {
      if (posicaoOrigem == ' ') {
        gridOrigem[linha][coluna] = '*';
      }

      if (posicaoOrigem == 'N') {
        gridOrigem[linha][coluna] = 'X';
      }
    } else {
      if (posicaoOrigem == ' ') {
        gridOrigem[linha][coluna] = '-';
      }

      if (posicaoOrigem == 'N') {
        gridOrigem[linha][coluna] = 'n';
      }
    }
  }

  private int obterCoordenadaAleatoria() {
    return new Random().nextInt(TAMANHO_GRID);
  }

  private int obterNumeroDeNaviosPosicionados(char[][] grid) {
    int numeroNavios = 0;

    for (int linha = 0; linha < TAMANHO_GRID; linha++) {
      for (int coluna = 0; coluna < TAMANHO_GRID; coluna++) {

        char posicao = grid[linha][coluna];

        if (posicao == 'N' || posicao == 'X' || posicao == 'n') {
          numeroNavios++;
        }
      }
    }

    return numeroNavios;
  }

  private void imprimirGrid(char[][] grid, String nomeJogador) {

    ScreenUtil.printTextLine("---------------------------------------------", 80, true);
    ScreenUtil.printTextLine(nomeJogador, 80, true);
    ScreenUtil.printTextLine("---------------------------------------------", 80, true);

    StringBuilder cabecalhoColuna = new StringBuilder("|   |");
    for (int colunaIndex = 0; colunaIndex < TAMANHO_GRID; colunaIndex++) {
      cabecalhoColuna.append(String.format(" %d |", colunaIndex));
    }

    ScreenUtil.printTextLine(cabecalhoColuna.toString(), 80, true);
    ScreenUtil.printTextLine("---------------------------------------------", 80, true);

    for (int linhaIndex = 0; linhaIndex < CHARS_COORDENADAS.length(); linhaIndex++) {

      char linhaChar = CHARS_COORDENADAS.charAt(linhaIndex);

      StringBuilder linhaString = new StringBuilder(String.format("| %c |", linhaChar));
      for (int colunaIndex = 0; colunaIndex < TAMANHO_GRID; colunaIndex++) {
        linhaString.append(String.format(" %c |", grid[linhaIndex][colunaIndex]));
      }

      ScreenUtil.printTextLine(linhaString.toString(), 80, true);
      ScreenUtil.printTextLine("---------------------------------------------", 80, true);
    }

    ScreenUtil.printTextLine("");
  }

  private void imprimirAviso(String texto) {

    ScreenUtil.printTextLine("");
    ScreenUtil.printTextLine(
        texto.toUpperCase(),
        80,
        true);
    ScreenUtil.printTextLine("");
  }
}
