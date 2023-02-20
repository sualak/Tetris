import java.util.ArrayList;

public abstract class Teile
{
    private char[][] tiles;
    //private final int[] startPunkt = new int[2];
    private int startPunkt = 0;
    private int groese;
    private int anzahlXe;

    //construktor
    public Teile(int groese)
    {
        tiles = new char[groese][groese];
        this.groese = groese;
//        startPunkt[0] = groese/2;
//        startPunkt[1] = groese/2;
        startPunkt = groese/2;
    }

    //getter
    public char getTile(int yPos, int xPos)
    {
        return tiles[yPos][xPos];
    }

    public int getGroese()
    {
        return groese;
    }

    public int getStarpunkt(int pos)
    {
//        return startPunkt[pos];
        return startPunkt;
    }

    public int getAnzahlXe()
    {
        return anzahlXe;
    }

    public char[] getReihe(int reihe)
    {
        return tiles[reihe];
    }

    public void increaseXe()
    {
        anzahlXe++;
    }

    //fills
    public abstract void fillArray();

    public void fillTile(int yPos,int xPos)
    {
        tiles[yPos][xPos] = 'X';
    }

    public void fillBlanks()
    {
        for (int reihe = 0; reihe < groese; reihe++)
        {
            for (int spalte = 0; spalte < groese; spalte++)
            {
                if(tiles[reihe][spalte] != 'X')
                {
                    tiles[reihe][spalte] =' ';
                }
            }
        }
    }

    //rotates
    public void rotateLeft()
    {
        char[][] ret = new char[groese][groese];
        for (int reihe = 0; reihe < groese; reihe++)
        {
            for (int spalte = 0; spalte < groese; spalte++)
            {
                ret[spalte][groese - 1 - reihe] = tiles[reihe][spalte];
            }
        }
        for (int reihe = 0; reihe < groese; reihe++)
        {
            System.arraycopy(ret[reihe], 0, tiles[reihe], 0, groese);
        }
    }

    public void rotateRight()
    {
        char[][] ret = new char[groese][groese];
        for (int reihe = 0; reihe < groese; reihe++)
        {
            for (int spalte = 0; spalte < groese; spalte++)
            {
                ret[groese - 1 - spalte][reihe] = tiles[reihe][spalte];
            }
        }
        for (int reihe = 0; reihe < groese; reihe++)
        {
            System.arraycopy(ret[reihe], 0, tiles[reihe], 0, groese);
        }
    }


    //veraltet
//    public char[][] getTiles()
//    {
//        return tiles;
//    }



//    public void printArray()
//    {
//        for (int reihe = 0; reihe < tiles.length; reihe++)
//        {
//            for (int spalte = 0; spalte < tiles[0].length; spalte++)
//            {
//                if(tiles[reihe][spalte] == 'X')
//                {
//                    System.out.print(tiles[reihe][spalte]);
//                }
//            }
//            System.out.println();
//        }
//    }
}
