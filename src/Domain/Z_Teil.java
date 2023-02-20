public class Z_Teil extends Teile
{
    //construktor
    public Z_Teil()
    {
        super(3);
        fillArray();
    }

    //fills
    public void fillArray()
    {
        for (int reihe = 0; reihe < this.getGroese(); reihe++)
        {
            for (int spalte = 0; spalte < this.getGroese(); spalte++)
            {
                if(reihe == 0 && (spalte == 0 || spalte == 1))
                {
                    fillTile(reihe,spalte);
                    increaseXe();
                }
                if(reihe == 1 && (spalte == 1 || spalte == 2))
                {
                    fillTile(reihe,spalte);
                    increaseXe();
                }
            }
        }
        fillBlanks();
    }
}
