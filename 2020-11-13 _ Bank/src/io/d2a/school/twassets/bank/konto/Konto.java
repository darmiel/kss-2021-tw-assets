package io.d2a.school.twassets.bank.konto;

public abstract class Konto {

  private static int autonr = 0;
  private final int kontonummer;

  protected double kontostand;

  public Konto() {
    this.kontonummer = ++autonr; // auto gen kontonr
  }

  public void einzahlen(final double betrag) {
    this.kontostand += betrag;
  }

  public abstract boolean auszahlen(final double betrag);

  public static void datenAusgeben(final Konto konto) {
    System.out.printf(
        "Konto {kontonr = %d, kontostand = %e}\n",
        konto.kontonummer, konto.kontostand
    );
  }

}