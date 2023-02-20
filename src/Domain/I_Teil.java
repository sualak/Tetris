public class I_Teil extends Teile
{
    //construktor
    public I_Teil()
    {
        super(5);
        fillArray();
    }

    //fills
    public void fillArray()
    {
        for (int reihe = 0; reihe < this.getGroese(); reihe++)
        {
            for (int spalte = 0; spalte < this.getGroese(); spalte++)
            {
                if(reihe == 2 && spalte >= 1)
                {
                    fillTile(reihe,spalte);
                    increaseXe();
                }
            }
        }
        fillBlanks();
    }
}
