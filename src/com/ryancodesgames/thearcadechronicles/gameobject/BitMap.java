
package com.ryancodesgames.thearcadechronicles.gameobject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BitMap 
{
   public List<Integer> bitMap = new ArrayList<>();
   
   public BitMap(List<Integer> bitMap)
   {
       this.bitMap = bitMap;
   }
   
   public void getBitMap(String fileName)
   {
       try
       {
           File file = new File(fileName);
           Scanner scan = new Scanner(file);
           
           while(scan.hasNextInt())
           {
              bitMap.add(scan.nextInt());
           }

       }
       catch(Exception e)
       {
           e.printStackTrace();
       }
      
   }
}
