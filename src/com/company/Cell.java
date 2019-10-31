package com.company;

import java.util.*;

public class Cell {

    public static final int DIM=4;
    public static final int SIZE=DIM*DIM;
    public char turn;
    public char playerTurn;
    public char computerTurn;
    public char[] board;
    private Map<Integer,Integer> cache= new HashMap<>();

    public void setTurn(char x)
    {
        turn=x;

    }

    public Cell()
    {
        //turn='x';
        turn=playerTurn;
        board= new char[SIZE];
        for(int i=0;i<SIZE;i++)
        {
            board[i]=' ';
        }
    }

    public Cell(String s, char turn) {
        board=s.toCharArray();
        this.turn=turn;

    }

    @Override
    public String toString() {

        return new String(board);
    }

    public Cell makeMove(int idx) {
        board[idx]=turn;
        //turn= turn=='x'?'o':'x';
        turn=turn==playerTurn?computerTurn:playerTurn;
        return this;
    }

    public Cell undoMove(int i) {
        board[i]=' ';
       // turn= turn=='x'?'o':'x';
        turn=turn==playerTurn?computerTurn:playerTurn;
        return this;

    }

    //bunch of moves(cell positions)
    public List<Integer> listOfPossibleMoves() {
        List<Integer> l= new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            if(board[i] == ' ') {
                l.add(i);
            }
        }
        return l;
    }

    //checks if there are SIZE entries in cell for same symbol, every possible win situation
    public boolean isWinFor(char turn) {
        boolean flagWin=false;
        //1 3 6
        //each column
        for (int i = 0; i <SIZE ; i+=DIM) {
            flagWin=flagWin || symbolsMatch(turn, i, i+DIM,1);
        }

        //vertical
        for (int i = 0; i <DIM ; i++) {
            flagWin=flagWin || symbolsMatch(turn, i, SIZE,DIM); //all the way to end SIZE, each row

        }

        //primary diagonal
        flagWin = flagWin || symbolsMatch(turn,0,SIZE,DIM+1);
        //secondary diagonal
        flagWin = flagWin || symbolsMatch(turn,DIM-1,SIZE-1,DIM-1); //SIZE would include lower hand corner, so size-1

        return flagWin;

    }

    //used as utility function in isWinFor to make traversal easy
    private boolean symbolsMatch(char turn, int start, int end, int step) {
        for (int i = start; i <end ; i+=step) {
            if(board[i] != turn)
                return false;

        }
        return true;

    }

    //return how many blank spaces there are
    public int miniMax() {

        Integer key= code();
        Integer value= cache.get(key);

        if(value!=null)
            return value;
        //if(isWinFor('x'))
        if(isWinFor(playerTurn))
            return blanks();
        //if(isWinFor('o'))
        if(isWinFor(computerTurn))
            return -blanks();
        if(blanks()==0)
            return 0;

        //computer making move
        List<Integer> list= new ArrayList<>();
        for(Integer idx: listOfPossibleMoves())
        {
            list.add(makeMove(idx).miniMax());
            undoMove(idx);
        }
        //value =turn=='x'? Collections.max(list): Collections.min(list); //if x turn find and return max otherwise return min
        value =turn==playerTurn? Collections.max(list): Collections.min(list);
        cache.put(key,value);
        return value;

        //creating map, then checked for value and set the value

    }

    public int code() {
        //number based on 3/hashing
        int val=0;
        for (int i = 0; i <SIZE ; i++) {
            val=val*3;
            //if(board[i] =='x')
            if(board[i] ==playerTurn)
            {
                val+=1;
            }
           // else if(board[i]=='o')
            else if(board[i]==computerTurn)
            {
                val+=2;
            }
        }
        return val;
    }

    //returns blanks to be used in minimax
    private int blanks() {
        int totalBlanks=0;
        for (int i = 0; i <SIZE ; i++) {
            if(board[i]==' ')
                totalBlanks++;

        }

        return totalBlanks;
    }

    //to make move for computer, recursively
    public int bestMove() {
        Comparator<Integer> cmp= new Comparator<Integer>() {
            @Override
            //indexes
            public int compare(Integer first, Integer second) {
                int a= makeMove(first).miniMax();
                undoMove(first);
                int b= makeMove(second).miniMax();
                undoMove(second);
                return a-b;
            }
        };

        List<Integer> list= listOfPossibleMoves();
        //return turn=='x'? Collections.max(list, cmp):Collections.min(list,cmp);
        return turn==playerTurn? Collections.max(list, cmp):Collections.min(list,cmp);
    }

    public boolean isGameEnd() {
        //return isWinFor('x') || isWinFor('o') || blanks() == 0;
        return isWinFor(playerTurn) || isWinFor(computerTurn) || blanks() == 0;
    }
}
