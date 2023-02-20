public abstract class Regeln
{
    public static void printRegeln()
    {
        System.out.print(
                "\nm = Aufruf der Regeln"+
                "\na = ein move nach links danach ein move nach unten"+
                "\nd = ein move nach rechts danach ein move nach unten"+
                "\ns = ein move nach unten"+
                "\nw = wechseln des aktuellen Teiles mit dem gespeicherten Teil (sollte keines vorhanden sein so wird ein neues generiert)"+
                "\nq = Rotation gegen den Uhrzeigersinn (sofern möglich)"+
                "\ne = Rotation mit dem Uhrzeigersinn (sofern möglich)"+
                "\nX = Spiel beenden\n"
        );
    }
}
