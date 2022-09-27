
package com.ryancodesgames.thearcadechronicles.gameobject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mesh 
{
    public List<Triangle> triangles = new ArrayList<>();
    
    public Mesh(List<Triangle> tris) 
    {
        this.triangles = tris;
    }
    
    public void loadFromObjectFile(String fileName, String fileName2, List<Vec3D> verts, List<Triangle> tris)
    {
         //LOCAL CACHE OF VERTICES
        verts = new ArrayList<>();
        
        tris = new ArrayList<>();
        
        try
        {
            File file = new File(fileName);
            File file2 = new File(fileName2);
            
            Scanner scan = new Scanner(file);
            Scanner scan2 = new Scanner(file2);
           
            while(scan.hasNextLine())
            {    
                Vec3D vec3d = new Vec3D(0,0,0,1);
                
                double xs = scan.nextDouble();
                double ys = scan.nextDouble();
                double zs = scan.nextDouble();
                
                vec3d.x = xs;
                vec3d.y = ys;
                vec3d.z = zs;
                
                verts.add(vec3d); 
            }
            
            while(scan2.hasNextLine())
            {
                int[] f = new int[3];
                
                f[0] = scan2.nextInt();
                f[1] = scan2.nextInt();
                f[2] = scan2.nextInt();
                
               tris.add(new Triangle(verts.get(f[0]-1), verts.get(f[1]-1), verts.get(f[2]-1)));
            }
 
            scan.close();
            scan2.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
