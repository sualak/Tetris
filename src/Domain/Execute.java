import java.util.Scanner;

public class Execute
{
    public static void main(String[] args)
    {
        Spielfeld s = new Spielfeld();
        s.fillArrayWithBlank();
        s.setAktTeil();
        s.print();


        Scanner sc = new Scanner(System.in);
        while (!s.spielVorbei())
        {
            char c = sc.next().charAt(0);
            if (c == 'a')
            {
                s.moveLeft();
            }
            if (c == 'd')
            {
                s.moveRight();
            }
            if (c == 'q')
            {
                s.getAktTeil().rotateLeft();
                s.teilInSpielfeldRotate(s.getAktTeil());
                if(s.ensureRotation() != s.getAnzahlXe())
                {
                    s.getAktTeil().rotateRight();
                    s.teilInSpielfeldRotate(s.getAktTeil());
                }
            }
            if (c == 'e')
            {
                s.getAktTeil().rotateRight();
                s.teilInSpielfeldRotate(s.getAktTeil());
                if(s.ensureRotation() != s.getAnzahlXe())
                {
                    s.getAktTeil().rotateLeft();
                    s.teilInSpielfeldRotate(s.getAktTeil());
                }
            }
            if (c == 's')
            {
                s.moveDown();
            }
            if(c == 'w')
            {
                s.saveTeil();
            }
            if(c == 'm')
            {
                Regeln.printRegeln();
            }
            if(c == 'X')
            {
                break;
            }
            s.print();
        }
        sc.close();
    }
}
