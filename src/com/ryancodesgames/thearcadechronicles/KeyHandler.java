
package com.ryancodesgames.thearcadechronicles;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener
{
    public boolean upPressed, downPressed, leftPressed, rightPressed, rightTurn, leftTurn, front, back;
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            upPressed = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_C)
        {
            downPressed = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            rightPressed = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_A)
        {
            leftPressed = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_Q)
        {
            rightTurn = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_E)
        {
            leftTurn = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_W)
        {
            front = true;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_S)
        {
            back = true;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            upPressed = false;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_C)
        {
            downPressed = false;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_D)
        {
            rightPressed = false;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_A)
        {
            leftPressed = false;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_Q)
        {
            rightTurn = false;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_E)
        {
            leftTurn = false;
        }
        
         if(e.getKeyCode() == KeyEvent.VK_W)
        {
            front = false;
        }
        
        if(e.getKeyCode() == KeyEvent.VK_S)
        {
            back = false;
        }

    }
}
