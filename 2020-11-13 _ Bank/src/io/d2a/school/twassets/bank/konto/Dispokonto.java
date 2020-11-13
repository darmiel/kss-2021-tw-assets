package io.d2a.school.twassets.bank.konto;

public class Dispokonto extends Konto {

  private double dispolimit = 200; // <-- we don't know this yet

  @Override
  public boolean auszahlen(final double betrag) {
    if (this.kontostand + this.dispolimit < betrag) {
      return false;
    }
    this.kontostand -= betrag;
    return true;
  }

}