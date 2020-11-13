package io.d2a.school.twassets.bank;

import io.d2a.school.twassets.bank.konto.Dispokonto;
import io.d2a.school.twassets.bank.konto.Konto;
import io.d2a.school.twassets.bank.konto.Sparkonto;
import io.d2a.school.twassets.bank.kunde.Kunde;
import java.util.ArrayList;
import java.util.List;

public class BankManagement {

  public final List<Kunde> kunden = new ArrayList<>();

  public Kunde anlegenKunde(final String name, final String vorname) {
    final Kunde kunde = new Kunde(name, vorname);
    this.kunden.add(kunde);
    return kunde;
  }

  public Konto eroeffneKonto(final Kunde kunde, final char art) {
    final Konto konto = switch (Character.toUpperCase(art)) {
      case 'S' -> new Sparkonto();
      case 'D' -> new Dispokonto();
      default -> null;
    };
    // Keine gültige Kontenart angegeben
    if (konto == null) {
      return null;
    }
    kunde.konten.add(konto);
    return konto;
  }

  public void einzahlen (final Konto konto, final double betrag) {
    konto.einzahlen(betrag);
  }

  public boolean auszahlen (final Konto konto, final double betrag) {
    return konto.auszahlen(betrag);
  }

}