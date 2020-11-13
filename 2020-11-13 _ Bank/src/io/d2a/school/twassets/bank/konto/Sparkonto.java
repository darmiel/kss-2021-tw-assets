package io.d2a.school.twassets.bank.konto;

public class Sparkonto extends Konto {

  private double zinssatz = 0; // <-- we don't know this yet

  @Override
  public boolean auszahlen(final double betrag) {
    if (this.kontostand < betrag) {
      return false;
    }
    this.kontostand -= betrag;
    return true;
  }
}
