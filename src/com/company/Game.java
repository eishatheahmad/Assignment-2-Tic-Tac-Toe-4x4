package com.company;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Game extends JFrame {

    Cell cell = new Cell();
    JButton[] buttons=new JButton[Cell.SIZE];
    GridBagConstraints gbc;

    String username;
    char turnPlayer;
    char turnComputer;

    public void playSound(String soundName)
    {
        try
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
            Clip clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.start( );
        }
        catch(Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace( );
        }
    }

    public Game()
    {

        setLayout(new GridBagLayout());
        gbc= new GridBagConstraints();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //setLayout(new GridLayout(Position.DIM,Position.DIM));

        //getting input from user in start
        username= JOptionPane.showInputDialog(null, "Enter Your name").toString();
        char temp= JOptionPane.showInputDialog(null, "Enter Your Symbol: X or O").toString().charAt(0);
        cell.playerTurn=temp; //storing the player symbol
        cell.turn=temp;
        if(temp=='x' || temp=='X')
            {
                cell.computerTurn='o';
                //position.
            }
        else
        {
            cell.computerTurn='x';
        }
        //System.out.println(username);



        //indexes for the layout
        int indexY=0;
        int indexX=1;

        //creating the board of cells
        for (int i = 0; i < Cell.SIZE; i++) {
            final JButton newButton= createButtons(indexX,indexY); //returns 1 button
            indexY++;
            if(indexY==4) {indexY=0; indexX++;}
            buttons[i]=newButton;
            final int index= i;


            newButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    playSound("click.wav");
                    newButton.setBackground(Color.cyan);
                    if (cell.board[index] == ' ') {
                        newButton.setText(Character.toString(cell.turn));
                        cell.makeMove(index);

                        //computer play
                        if (!cell.isGameEnd()) {
                            int best = cell.bestMove();
                            buttons[best].setText(Character.toString(cell.turn));
                            cell.makeMove(best);
                        }

                        if (cell.isGameEnd()) {
                            playSound("end.wav");
                            String message = cell.isWinFor(cell.playerTurn) ? "YOU WON!" : cell.isWinFor(cell.computerTurn) ? "COMPUTER WON" : "DRAW";
                            JOptionPane.showMessageDialog(null, message);




                        }
                    }
                    //warning message to user
                    else
                    {
                        String warningMessage= "Wrong Move";
                        playSound("error.wav");
                        JOptionPane.showMessageDialog(null, warningMessage);

                    }
                }
            });

        }
        pack();
        setVisible(true);


    }

    private JButton createButtons(int x, int y) {
        JButton button=new JButton();
        button.setPreferredSize(new Dimension(100,100));
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setFont(new Font(null, Font.PLAIN,50));
        gbc.gridx=x;
        gbc.gridy=y;
        add(button,gbc);
        return button;
    }

    private JButton createButtons() {
        JButton button=new JButton();
        button.setPreferredSize(new Dimension(200,200));
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setFont(new Font(null, Font.PLAIN,70));
        add(button);

        return button;
    }


    public static void main(String[] args){
        //System.out.println("HELLO");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Game();
            }
        });
    }

}
