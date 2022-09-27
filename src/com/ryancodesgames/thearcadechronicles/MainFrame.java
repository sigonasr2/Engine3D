
package com.ryancodesgames.thearcadechronicles;

import javax.swing.JFrame;

public class MainFrame {
    
    private static final int WIDTH = 800;
    
    private static final int HEIGHT = 600;
    
    private static final String TITLE = "The Arcade Chronicles: Retrovirus";

    public static void main(String[] args) 
    {
        GamePanel gp = new GamePanel();
        
        JFrame jframe = new JFrame();
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setTitle(TITLE);
        jframe.setLocationRelativeTo(null);
        jframe.add(gp);
        jframe.setVisible(true);
        
        gp.startGameThread();
        
        
    }
    
}
