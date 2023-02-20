import javax.swing.*;
import java.util.ArrayList;

public class Spielfeld
{
    private final int hoehe = 20;
    private final int breite = 11;
    private int movesDown = 0;
    private int movesSide = 0;
    private int score = 0;
    private int anzahlXe = 0;
    private Teile aktTeil;
    private Teile savedTeil = null;
    private final char[][] spielFeld = new char[hoehe][breite];
    private final int[] aktStartPunkt = new int[2];
    private final boolean[][] besucht = new boolean[hoehe][breite];
    private ArrayList<Integer> toMove;


    //construktor
    public Spielfeld()
    {

    }


    //fills
    public void fillArrayWithBlank()
    {
        for (int reihe = 0; reihe < hoehe; reihe++)
        {
            for (int spalte = 0; spalte < breite; spalte++)
            {
                spielFeld[reihe][spalte] = ' ';
            }
        }
    }


    //getter
    public Teile getAktTeil()
    {
        return aktTeil;
    }

    public int getAnzahlXe()
    {
        return anzahlXe;
    }


    //setter
    public void setSavedTeil(Teile aktTeil)
    {
        clearXe();
        aktStartPunkt[0] = 0;
        aktStartPunkt[1] = breite / 2;
        movesDown = 0;
        movesSide = 0;
        teilInSpielfeld(aktTeil);
    }

    public void setAktTeil()
    {
        clearXe();
        aktStartPunkt[0] = 0;
        aktStartPunkt[1] = breite / 2;
        movesDown = 0;
        movesSide = 0;
        aktTeil = generateTeil();
        anzahlXe = aktTeil.getAnzahlXe();
        for (int i = 0; i < Math.round(Math.random() * 5); i++)
        {
            aktTeil.rotateLeft();
        }
        teilInSpielfeld(aktTeil);
    }

    public Teile generateTeil()
    {
        double random = Math.random();
        if (random >= 0.875)
        {
            return new L_Teil();
        }
        if (random >= 0.75)
        {
            return new S_Teil();
        }
        if (random >= 0.625)
        {
            return new J_Teil();
        }
        if (random >= 0.375)
        {
            return new T_Teil();
        }
        if (random >= 0.25)
        {
            return new Z_Teil();
        }
        if (random >= 0.125)
        {
            return new I_Teil();
        }
        return new O_Teil();
    }


    //moves
    public void saveTeil()
    {
        initalMove();
        removeTeil();
        if(savedTeil == null)
        {
            savedTeil = aktTeil;
            setAktTeil();
        }
        else
        {
            Teile temp = aktTeil;
            aktTeil = savedTeil;
            savedTeil = temp;
            setSavedTeil(aktTeil);
        }
        anzahlXe = aktTeil.getAnzahlXe();
    }

    public void moveDown()
    {
        initalMove();
        movesDown++;
        removeTeil();
        if(!toMove.contains(hoehe-1) && !sucheFertig(toMove))
        {
            for (int i = 0; i < toMove.size(); i += 2)
            {
                int yPos = toMove.get(i);
                int xPos = toMove.get(i + 1);
                spielFeld[yPos+1][xPos] = 'X';
            }
        } else
        {
            xToO();
        }
    }

    public void moveLeft()
    {
        initalMove();
        if (ensureBoundLeft(toMove) && ensureHitBoxLinks(toMove))
        {
            movesSide--;
            removeTeil();
                for (int i = 0; i < toMove.size(); i += 2)
                {
                    int yPos = toMove.get(i);
                    int xPos = toMove.get(i + 1);
                    spielFeld[yPos][xPos - 1] = 'X';
                }
            if (!toMove.contains(hoehe - 1))
            {
                moveDown();
            } else
            {
                initalMove();
                xToO();
            }
        } else
        {
            clearBesucht();
            moveDown();
        }
    }

    public void moveRight()
    {
        initalMove();
        if(ensureBoundRight(toMove) && ensureHitBoxRechts(toMove))
        {
            movesSide++;
            removeTeil();

                for (int i = 0; i < toMove.size(); i += 2)
                {
                    int yPos = toMove.get(i);
                    int xPos = toMove.get(i + 1);
                    spielFeld[yPos][xPos + 1] = 'X';
                }
            if (!toMove.contains(hoehe - 1))
            {
                moveDown();
            } else
            {
                initalMove();
                xToO();
            }
        }
        else
        {
            clearBesucht();
            moveDown();
        }
    }


    //shifts
    public boolean shiftRight()
    {
        initalMove();
        if (ensureBoundLeft(toMove) && ensureHitBoxLinks(toMove))
        {
            movesSide--;
            removeTeil();
            if (!toMove.contains(hoehe - 1))
            {
                for (int i = 0; i < toMove.size(); i += 2)
                {
                    int yPos = toMove.get(i);
                    int xPos = toMove.get(i + 1);
                    spielFeld[yPos][xPos - 1] = 'X';
                }
            }
        } else
        {
            clearBesucht();
            return true;
        }
        return false;
    }

    public boolean shiftLeft()
    {
        initalMove();
        if(ensureBoundRight(toMove) && ensureHitBoxRechts(toMove))
        {
            movesSide++;
            removeTeil();
            if (!toMove.contains(hoehe - 1))
            {
                for (int i = 0; i < toMove.size(); i += 2)
                {
                    int yPos = toMove.get(i);
                    int xPos = toMove.get(i + 1);
                    spielFeld[yPos][xPos + 1] = 'X';
                }
            }
        }
        else
        {
            clearBesucht();
            return true;
        }
        return false;
    }


    //teil ins Spiel
    public void teilInSpielfeld(Teile aktTeil)
    {
        int teilReihe = 0;
        int teilSpalte = 0;
        int groese = aktTeil.getGroese();
        aktStartPunkt[0] = aktTeil.getStarpunkt(0);
        int bound1 = 2;
        int bound2 = 1;

        if (aktTeil instanceof I_Teil)
        {
            bound1 = 3;
            bound2 = 2;
        }

            for (int reihe = 0; reihe < groese; reihe++)
            {
                if (teilReihe < groese)
                {
                    teilSpalte = 0;
                    if (arrayContainsX(aktTeil.getReihe(teilReihe)))
                    {
                        for (int spalte = aktStartPunkt[1] - bound2; spalte < aktStartPunkt[1] + bound1; spalte++)
                        {
                            if (spielFeld[reihe][spalte] != 'O')
                            {
                                spielFeld[reihe][spalte] = aktTeil.getTile(teilReihe, teilSpalte);
                            }
                            teilSpalte++;
                        }
                    } else
                    {
                        reihe--;
                        aktStartPunkt[0]--;
                    }
                    teilReihe++;
                }
            }
        if(aktStartPunkt[0] < 0)
        {
            aktStartPunkt[0] = 0;
        }
    }

    public void teilInSpielfeldRotate(Teile aktTeil)
    {
        int teilReihe = 0;
        int teilSpalte = 0;
        int startYPos = aktStartPunkt[0] + movesDown;
        int startXPos = aktStartPunkt[1] + movesSide;
        int bound1 = 2;
        int bound2 = 1;

        if (aktTeil instanceof I_Teil)
        {
            bound1 = 3;
            bound2 = 2;
        }
            if (startXPos <= breite - bound1)
            {
                if (startXPos >= bound2)
                {
                    if (startYPos >= bound2)
                    {
                        if (startYPos <= hoehe - bound1)
                        {
                            for (int reihe = startYPos - bound2; reihe < startYPos + bound1; reihe++)
                            {
                                teilSpalte = 0;
                                for (int spalte = startXPos - bound2; spalte < startXPos + bound1; spalte++)
                                {
                                    if (spielFeld[reihe][spalte] != 'O')
                                    {
                                        spielFeld[reihe][spalte] = aktTeil.getTile(teilReihe, teilSpalte);
                                    }
                                    teilSpalte++;
                                }
                                teilReihe++;
                            }
                        } else
                        {
                            movesDown--;
                            teilInSpielfeldRotate(aktTeil);
                        }
                    } else
                    {
                        movesDown++;
                        teilInSpielfeldRotate(aktTeil);
                    }
                } else
                {
                    movesSide++;
                    teilInSpielfeldRotate(aktTeil);
                    shiftRight();
                }
            } else
            {
                movesSide--;
                teilInSpielfeldRotate(aktTeil);
                shiftLeft();
            }
    }


    //ensure
    public boolean sucheFertig(ArrayList<Integer> belegt)
    {
        for (int i = 0; i < belegt.size(); i+=2)
        {
            int yPos = belegt.get(i);
            int xPos = belegt.get(i+1);
            //        suche runter
            if (yPos < hoehe - 1 && !besucht[yPos + 1][xPos] && spielFeld[yPos + 1][xPos] == 'O')
            {
                besucht[yPos + 1][xPos] = true;
                suche(yPos + 1, xPos);
                return true;
            }
        }
        return false;
    }

    public boolean spielVorbei()
    {
        for (int spalte = 0; spalte < breite-1; spalte++)
        {
            if(spielFeld[0][spalte] == 'O')
            {
                System.out.println("Das Spiel ist vorbei du hast verloren");
                System.out.println("Dein finaler Score war: " + score);
                return true;
            }
        }
        return false;
    }

    public int ensureRotation()
    {
        initalMove();
        clearBesucht();
        return toMove.size()/2;
    }

    private boolean ensureBoundRight(ArrayList<Integer> breite)
    {
        for (int i = 1; i < breite.size(); i+=2)
        {
            if(breite.get(i) == this.breite-1)
            {
                return false;
            }
        }
        return true;
    }

    private boolean ensureBoundLeft(ArrayList<Integer> breite)
    {
        for (int i = 1; i < breite.size(); i+=2)
        {
            if(breite.get(i) == 0)
            {
                return false;
            }
        }
        return true;
    }

    private boolean ensureHitBoxRechts(ArrayList<Integer> breite)
    {
        for (int i = 0; i < breite.size(); i+=2)
        {
            if(spielFeld[breite.get(i)][breite.get(i+1)+1] == 'O')
            {
                return false;
            }
        }
        return true;
    }

    private boolean ensureHitBoxLinks(ArrayList<Integer> breite)
    {
        for (int i = 0; i < breite.size(); i+=2)
        {
            if(spielFeld[breite.get(i)][breite.get(i+1)-1] == 'O')
            {
                return false;
            }
        }
        return true;
    }


    //private
    private boolean arrayContainsX(char[] toCeck)
    {
        for (char c : toCeck)
        {
            if(c == 'X')
            {
                return true;
            }
        }
        return false;
    }

    private void clearXe()
    {
        for (int reihe = 0; reihe < hoehe; reihe++)
        {
            for (int spalte = 0; spalte < breite; spalte++)
            {
                if(spielFeld[reihe][spalte] == 'X')
                {
                    spielFeld[reihe][spalte] = ' ';
                }
            }
        }
    }

    private void copyRow(int reihe)
    {
        while (reihe > 0)
        {
            System.arraycopy(spielFeld[reihe - 1], 0, spielFeld[reihe], 0, breite);
            reihe--;
        }
    }

    private void addScore(int anz)
    {
        score+=anz;
    }

    private void findeVolleReihe()
    {
        int sumOfCleardRows = 0;
        for (int reihe = hoehe-1; reihe >= 0; reihe--)
        {
            int anz = 0;
            for (int spalte = 0; spalte < breite; spalte++)
            {
                if(spielFeld[reihe][spalte] == 'O')
                {
                    anz++;
                }
                if(anz == breite)
                {
                    sumOfCleardRows++;
                    copyRow(reihe);
                    reihe++;
                    addScore(anz*sumOfCleardRows);
                }
            }
            if(anz == 0)
            {
                return;
            }
        }
    }

    private void clearBesucht()
    {
        for (int i = 0; i < toMove.size(); i += 2)
        {
            int yPos = toMove.get(i);
            int xPos = toMove.get(i + 1);
            besucht[yPos][xPos] = false;
        }
    }

    private void initalMove()
    {
        toMove = new ArrayList<>();
        int reiheStart = aktStartPunkt[0]+movesDown;
        int spalteStart = aktStartPunkt[1]+movesSide;
        suche(reiheStart, spalteStart);
    }

    private void removeTeil()
    {
        for (int i = 0; i < toMove.size(); i += 2)
        {
            int yPos = toMove.get(i);
            int xPos = toMove.get(i + 1);
            besucht[yPos][xPos] = false;
            if(spielFeld[yPos][xPos] == 'X')
            {
                spielFeld[yPos][xPos] = ' ';
            }
        }
    }

    private void xToO()
    {
        for (int i = 0; i < toMove.size(); i += 2)
        {
            int yPos = toMove.get(i);
            int xPos = toMove.get(i + 1);
            spielFeld[yPos][xPos] = 'O';
        }
        findeVolleReihe();
        setAktTeil();
    }


    //tiefensuche
    private void suche(int yPos, int xPos)
    {
//         suche rauf
            if (yPos > 0  && !besucht[yPos - 1][xPos] && spielFeld[yPos - 1][xPos] == 'X')
            {
                besucht[yPos - 1][xPos] = true;
                suche(yPos - 1, xPos);
                toMove.add(yPos - 1);
                toMove.add(xPos);
            }
//         suche runter
            if (yPos < hoehe - 1   && !besucht[yPos + 1][xPos] && spielFeld[yPos + 1][xPos] == 'X')
            {
                besucht[yPos + 1][xPos] = true;
                suche(yPos + 1, xPos);
                toMove.add(yPos + 1);
                toMove.add(xPos);
            }
//         suche links
            if (xPos > 0 && !besucht[yPos][xPos - 1] && spielFeld[yPos][xPos - 1] == 'X')
            {
                besucht[yPos][xPos - 1] = true;
                suche(yPos, xPos - 1);
                toMove.add(yPos);
                toMove.add(xPos - 1);
            }
//         suche Rechts
            if (xPos < breite - 1 && !besucht[yPos][xPos + 1] && spielFeld[yPos][xPos + 1] == 'X')
            {
                besucht[yPos][xPos + 1] = true;
                suche(yPos, xPos + 1);
                toMove.add(yPos);
                toMove.add(xPos + 1);
            }
    }


    //print
    public void print()
    {
        for (int reihen = 0; reihen < hoehe; reihen++)
        {
            for (int spalten = 0; spalten < breite; spalten++)
            {
                if(spalten == 0)
                {
                    System.out.print('|');
                }
                System.out.print(spielFeld[reihen][spalten]);
                if(spalten == breite-1)
                {
                    System.out.print('|');
                }
            }
            if(reihen == 0)
            {
                System.out.println("    Dein Score ist: "+score);
            } else if(reihen == 2)
            {
                System.out.println("    Saved Teil: ");
            } else if(savedTeil != null && reihen > 3 && reihen <= 3+savedTeil.getGroese())
            {
                System.out.print("    ");
                for (int spalte = 0; spalte < savedTeil.getGroese(); spalte++)
                {

                    System.out.print(savedTeil.getTile(reihen-4, spalte));
                }
                System.out.println();
            } else
            {
                System.out.println();
            }
        }
        System.out.print("-------------");
        System.out.println();
    }


    //veraltet
    /*public boolean moveUp()
    {

        initalMove();
        boolean isFertigUp = true;
        if (!toMove.contains(0))
        {
            isFertigUp = false;
            movesDown--;
            removeTeil();
            for(int i = 0; i<toMove.size();i+=2)
            {
                int yPos = toMove.get(i);
                int xPos = toMove.get(i+1);
                spielFeld[yPos - 1][xPos] = 'X';
            }
        } else
        {
            for(int i = 0; i<toMove.size();i+=2)
            {
                int yPos = toMove.get(i);
                int xPos = toMove.get(i + 1);
                besucht[yPos][xPos] = false;
            }
        }
        return isFertigUp;
    }*/

    /*public void teilInSpielfeld(Teile aktTeil)
    {
        int teilReihe = 0;
        int teilSpalte = 0;
        int groese = aktTeil.getGroese();
        aktStartPunkt[0] = aktTeil.getStarpunkt(0);

        if (aktTeil instanceof I_Teil)
        {
            for (int reihe = 0; reihe < groese; reihe++)
            {
                if (teilReihe < groese)
                {
                    teilSpalte = 0;
                    if (arrayContainsX(aktTeil.getReihe(teilReihe)))
                    {
                        for (int spalte = aktStartPunkt[1] - 2; spalte < aktStartPunkt[1] + 3; spalte++)
                        {
                            if (spielFeld[reihe][spalte] != 'O')
                            {
                                spielFeld[reihe][spalte] = aktTeil.getTile(teilReihe, teilSpalte);
                            }
                            teilSpalte++;
                        }
                    } else
                    {
                            reihe--;
                            aktStartPunkt[0]--;
                    }
                    teilReihe++;
                }
            }
        }
        else
        {
            for (int reihe = 0; reihe < groese; reihe++)
            {
                if (teilReihe < groese)
                {
                    teilSpalte = 0;
                    if (arrayContainsX(aktTeil.getReihe(teilReihe)))
                    {
                        for (int spalte = aktStartPunkt[1] - 1; spalte < aktStartPunkt[1] + 2; spalte++)
                        {
                            if (spielFeld[reihe][spalte] != 'O')
                            {
                                spielFeld[reihe][spalte] = aktTeil.getTile(teilReihe, teilSpalte);
                            }
                            teilSpalte++;
                        }
                    } else
                    {
                        reihe--;
                        aktStartPunkt[0]--;
                    }
                    teilReihe++;
                }
            }
        }
        if(aktStartPunkt[0] < 0)
        {
            aktStartPunkt[0] = 0;
        }
    }*/

    /*public void teilInSpielfeldRotate(Teile aktTeil)
    {
        int teilReihe = 0;
        int teilSpalte = 0;
        int startYPos = aktStartPunkt[0]+movesDown;
        int startXPos = aktStartPunkt[1]+movesSide;


        if (aktTeil instanceof I_Teil)
        {
            if(startXPos <= breite-3)
            {
                if(startXPos >= 2)
                {
                    if (startYPos >= 2)
                    {
                        if (startYPos <= hoehe-3)
                        {
                            for (int reihe = startYPos - 2; reihe < startYPos + 3; reihe++)
                            {
                                teilSpalte = 0;
                                for (int spalte = startXPos - 2; spalte < startXPos + 3; spalte++)
                                {
                                    if (spielFeld[reihe][spalte] != 'O')
                                    {
                                        spielFeld[reihe][spalte] = aktTeil.getTile(teilReihe, teilSpalte);
                                    }
                                    teilSpalte++;
                                }
                                teilReihe++;
                            }
                        }
                        else
                        {
                            movesDown--;
                            teilInSpielfeldRotate(aktTeil);
                        }
                    }
                    else
                    {
                        movesDown++;
                        teilInSpielfeldRotate(aktTeil);
                    }
                }
                else
                {
                    movesSide++;
                    teilInSpielfeldRotate(aktTeil);
                    shiftRight();
                }
            }
            else
            {
                movesSide--;
                teilInSpielfeldRotate(aktTeil);
                shiftLeft();
            }
        } else
        {
            if(startXPos <= breite-2)
            {
                if(startXPos >= 1)
                {
                    if (startYPos >= 1)
                    {
                        if (startYPos <= hoehe-2)
                        {
                            for (int reihe = startYPos - 1; reihe < startYPos + 2; reihe++)
                            {
                                teilSpalte = 0;
                                for (int spalte = startXPos - 1; spalte < startXPos + 2; spalte++)
                                {
                                    if (spielFeld[reihe][spalte] != 'O')
                                    {
                                        spielFeld[reihe][spalte] = aktTeil.getTile(teilReihe, teilSpalte);
                                    }
                                    teilSpalte++;
                                }
                                teilReihe++;
                            }
                        }
                        else
                        {
                            movesDown--;
                            teilInSpielfeldRotate(aktTeil);
                        }
                    }
                    else
                    {
                        movesDown++;
                        teilInSpielfeldRotate(aktTeil);
                    }
                }
                else
                {
                    movesSide++;
                    teilInSpielfeldRotate(aktTeil);
                    shiftRight();
                }
            }
            else
            {
                movesSide--;
                teilInSpielfeldRotate(aktTeil);
                shiftLeft();
            }
        }
    }*/
}
