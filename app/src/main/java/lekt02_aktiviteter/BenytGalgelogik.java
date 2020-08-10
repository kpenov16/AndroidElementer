package lekt02_aktiviteter;

public class BenytGalgelogik {

  public static void main(String[] args) {

    Galgelogik spil = new Galgelogik();
    spil.startNytSpil();
    // Kommentér ind for at hente ord fra DR
    /*
    try {
      spil.hentOrdFraDr();
    } catch (Exception e) {
      e.printStackTrace();
    }
    */

    // Kommentér ind for at hente ord fra et online regneark
    try {
      spil.hentOrdFraRegneark("12");
    } catch (Exception e) {
      e.printStackTrace();
    }

    /* Serialisering af objekter som JSON. Kræver at du sætter
    implementation 'com.google.code.gson:gson:2.8.6'
    ind i dependencies { ... } i app/build.gradle

    com.google.gson.Gson gson = new com.google.gson.Gson();
    String spilSomStreng = gson.toJson(spil);
    System.out.println("spilSomStreng = " + spilSomStreng);
    Galgelogik spil2 = gson.fromJson(spilSomStreng, Galgelogik.class);
     */

    spil.logStatus();

    spil.gætBogstav("e");
    spil.logStatus();

    spil.gætBogstav("a");
    spil.logStatus();
    System.out.println("" + spil.getAntalForkerteBogstaver());
    System.out.println("" + spil.getSynligtOrd());
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("i");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("s");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("r");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("l");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("b");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("o");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("t");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("n");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("m");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("y");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("p");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;

    spil.gætBogstav("g");
    spil.logStatus();
    if (spil.erSpilletSlut()) return;
  }
}
