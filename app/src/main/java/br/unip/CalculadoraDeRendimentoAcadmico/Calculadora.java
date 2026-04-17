package br.unip.CalculadoraDeRendimentoAcadmico;

public class Calculadora {
    public static double MS(double notaNP1, double notaNP2) {
        return (notaNP1 + notaNP2) / 2;
    }

    public static double MF(double ms, double exame) {
        return (ms + exame) / 2;
    }
}