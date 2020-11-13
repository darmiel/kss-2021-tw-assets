package io.d2a.school.twassets.bank.kunde;

import io.d2a.school.twassets.bank.konto.Konto;
import java.util.ArrayList;
import java.util.List;

public class Kunde {

  private static int autonr = 0;

  private final int kundennummer;
  private final String name;
  private final String vorname;

  public final List<Konto> konten = new ArrayList<>();

  public Kunde(final String name, final String vorname) {
    this.kundennummer = ++autonr; // auto gen kundennr

    this.name = name;
    this.vorname = vorname;
  }

}
