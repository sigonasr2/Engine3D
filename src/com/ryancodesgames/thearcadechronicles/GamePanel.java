
package com.ryancodesgames.thearcadechronicles;

import com.ryancodesgames.thearcadechronicles.gameobject.Matrix;
import com.ryancodesgames.thearcadechronicles.gameobject.Mesh;
import com.ryancodesgames.thearcadechronicles.gameobject.Triangle;
import com.ryancodesgames.thearcadechronicles.gameobject.Vec2D;
import com.ryancodesgames.thearcadechronicles.gameobject.Vec3D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable
{
    public Thread gameThread;
    
    //Frames per second
    public double fps = 60;
    
    //Class that handles keyboard input
    KeyHandler keyH = new KeyHandler();
    //CLASS THAT REPRESENTS ARRAY OF TRIANGLES THAT FORMS AN OBJECT
    public Mesh meshCube;
    
    //PROJECTION MATRIX DATA
    double fNear = 0.10;
    double fFar = 1000.00;
    double fov = 90.00; //FIELD OF VIEW
    double a = 600.00/800.00; //ASPECT RATIO OF HEIGHT/WIDHT
    double fovRad = 1.00/Math.tan(fov*0.50/180.00*Math.PI); // RELATIONVAL FOV
    
    //Projection Matrix
    public Matrix matProj = new Matrix();
    public Matrix matProjected = matProj.projectionMatrix(fov, a, fNear, fFar);
    //ROTATION MATRIX AROUND Z-AXIS
    public Matrix matZ = new Matrix();
    //ROTATION MATRIX AROUND X-AXIS AFTER Z TRANSFORMATION
    public Matrix matZX = new Matrix();
    //VARIABLE THAT TRAVERSES A CIRCLE AS AN ANGLE
    double fTheta;
    //STATIONARY POSITION OF CAMERA IN VECTOR SPACE THAT FILTERS OBJECTS AS THE CAMERA PROJECTS TO THE SURFACE OF THE OBJECT
    Vec3D vCamera = new Vec3D(0,0,0,1);
    //ENHANCENET OF VCAMERA THAT WILL BE A UNIT VECTOR THAT TRAVELS ALONG THE CAMERA FROM POINT TO POINT
    Vec3D vLookDir = new Vec3D(0,0,1,1);
    //DETERMINES WHERE THE PLAYER IS FACING
    double fYaw;
    
    int[] pixels = new int[477601];
    
    public GamePanel()
    {
        this.addKeyListener(keyH);
        this.setFocusable(true);
        this.setDoubleBuffered(true); 
        intializeMesh();
        getRGB();
    }
    
    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
     public void getRGB()
    {
        try
        {
            File file = new File("cobble.txt");
            
            Scanner scan = new Scanner(file);
            
            int i = 0;
            
            while(scan.hasNextLine())
            {
                pixels[i] = scan.nextInt();
                
                i++;
            }
            
            scan.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
   
    }
    
    @Override
    public void run()
    {
        double drawInterval = 1000000000/fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        int timer = 0;
        int drawCount = 0;
        
        while(gameThread != null)
        {
            currentTime = System.nanoTime();
            
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            
            lastTime = currentTime;
            
            if(delta >= 1)
            {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000)
            {
                drawCount = 0;
                timer = 0;
            }
        }
    }
    
    public void intializeMesh()
    {
//         //LOCAL CACHE OF VERTICES
//        List<Vec3D> verts = new ArrayList<>();
//        
//        List<Triangle> tris = new ArrayList<>();
//        
//        try
//        {
//            File file = new File("bowser.txt");
//            File file2 = new File("fBow.txt");
//            
//            Scanner scan = new Scanner(file);
//            Scanner scan2 = new Scanner(file2);
//           
//            while(scan.hasNextLine())
//            {    
//                Vec3D vec3d = new Vec3D(0,0,0,1);
//                
//                double xs = scan.nextDouble();
//                double ys = scan.nextDouble();
//                double zs = scan.nextDouble();
//                
//                vec3d.x = -xs;
//                vec3d.y = -ys;
//                vec3d.z = zs;
//                
//                verts.add(vec3d); 
//            }
//            scan.close();
//             
//            while(scan2.hasNextLine())
//            {
//                int[] f = new int[3];
//                
//                f[0] = scan2.nextInt();
//                f[1] = scan2.nextInt();
//                f[2] = scan2.nextInt();
//                
//               tris.add(new Triangle(verts.get(f[0]-1), verts.get(f[1]-1), verts.get(f[2]-1)));
//            }
// 
//           
//           scan2.close();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        meshCube = new Mesh(tris);
        meshCube = new Mesh(Arrays.asList(
                new Triangle[]{
                    //SOUTH
                    new Triangle(new Vec3D(0,0,0,1), new Vec3D(0,1,0,1), new Vec3D(1,1,0,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    new Triangle(new Vec3D(0,0,0,1), new Vec3D(1,1,0,1), new Vec3D(1,0,0,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    //EAST
                    new Triangle(new Vec3D(1,0,0,1), new Vec3D(1,1,0,1), new Vec3D(1,1,1,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    new Triangle(new Vec3D(1,0,0,1), new Vec3D(1,1,1,1), new Vec3D(1,0,1,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    //NORTH
                    new Triangle(new Vec3D(1,0,1,1), new Vec3D(1,1,1,1), new Vec3D(0,1,1,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    new Triangle(new Vec3D(1,0,1,1), new Vec3D(0,1,1,1), new Vec3D(0,0,1,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    //WEST
                    new Triangle(new Vec3D(0,0,1,1), new Vec3D(0,1,1,1), new Vec3D(0,1,0,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    new Triangle(new Vec3D(0,0,1,1), new Vec3D(0,1,0,1), new Vec3D(0,0,0,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    //TOP
                    new Triangle(new Vec3D(0,1,0,1), new Vec3D(0,1,1,1), new Vec3D(1,1,1,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    new Triangle(new Vec3D(0,1,0,1), new Vec3D(1,1,1,1), new Vec3D(1,1,0,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    //BOTTOM
                    new Triangle(new Vec3D(1,0,1,1), new Vec3D(0,0,1,1), new Vec3D(0,0,0,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0)),
                    new Triangle(new Vec3D(1,0,1,1), new Vec3D(0,0,0,1), new Vec3D(1,0,0,1), new Vec2D(0,1), new Vec2D(0,0), new Vec2D(1,0))
                }));
    }
            
    
    public void update()
    {
        //USER INPUT TO ALLOW USER TO MOVE AROUND WORLD
        if(keyH.upPressed)
        {
            vCamera.y -= 0.25;
        }
        
        if(keyH.downPressed)
        {
            vCamera.y += 0.25;
        }
        
        if(keyH.rightPressed)
        {
            vCamera.x += 0.25;
        }
        
        if(keyH.leftPressed)
        {
            vCamera.x -= 0.25;
        }
        
        Vec3D vForward = new Vec3D(0,0,0,1);
        vForward = vForward.multiplyVector(vLookDir, 1);
        
        if(keyH.front)
        {
            vCamera = vForward.addVector(vCamera, vForward);
        }
        
        if(keyH.back)
        {
            vCamera = vForward.subtractVector(vCamera, vForward);
        }
        
        if(keyH.rightTurn)
        {
            fYaw += 0.008;
        }
        
        if(keyH.leftTurn)
        {
            fYaw -= 0.008;
        }     
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        
        //FILL SCREEN BLACK
        g2.setColor(Color.black);
        g2.fillRect(0, 0, 800, 600);
        
       // fTheta += 0.02;
        
        matZ = matZ.rotationMatrixZ(fTheta * 0.5);
        matZX = matZX.rotationMatrixX(fTheta);
        
        Matrix matTrans = new Matrix(new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}});
        matTrans = matTrans.translationMatrix(0, 0, 16.0);
        
        Matrix matWorld = new Matrix();
        matWorld = matWorld.identityMatrix();
        matWorld = matWorld.matrixMatrixMultiplication(matZ, matZX);
        matWorld = matWorld.matrixMatrixMultiplication(matWorld, matTrans);
        
        Vec3D vUp = new Vec3D(0,1,0,1);
        Vec3D vTarget = new Vec3D(0,0,1,1);
        Matrix matCameraRotated = new Matrix(new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}});
        matCameraRotated = matTrans.rotationMatrixY(fYaw);
        vLookDir = matTrans.multiplyMatrixVector(vTarget, matCameraRotated);
        vTarget = vTarget.addVector(vCamera, vLookDir);
        
        //USING THE INFORMATION PROVIDED ABOVE TO DEFIEN A CAMERA MATRIX
        Matrix matCamera = new Matrix(new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}});
        matCamera = matCamera.pointAtMatrix(vCamera, vTarget, vUp);
        
        Matrix matView = new Matrix(new double[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}});
        matView = matView.inverseMatrix(matCamera);
        
        List<Triangle> vecTrianglesToRaster = new ArrayList<>();
        
        for(Triangle tri: meshCube.triangles)
        {
            Triangle triProjected = new Triangle(new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1));
            Triangle triTrans = new Triangle(new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1));
            Triangle triViewed = new Triangle(new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1));
            
            triTrans.vec3d = matWorld.multiplyMatrixVector(tri.vec3d, matWorld);
            triTrans.vec3d2 = matWorld.multiplyMatrixVector(tri.vec3d2, matWorld);
            triTrans.vec3d3 = matWorld.multiplyMatrixVector(tri.vec3d3, matWorld);
            triTrans.vec2d = tri.vec2d;
            triTrans.vec2d2 = tri.vec2d2;
            triTrans.vec2d3 = tri.vec2d3;

            Vec3D normal = new Vec3D(0,0,0,1);
            Vec3D line1 = new Vec3D(0,0,0,1);
            Vec3D line2 = new Vec3D(0,0,0,1);
            
            line1 = normal.subtractVector(triTrans.vec3d2, triTrans.vec3d);
            line2 = normal.subtractVector(triTrans.vec3d3, triTrans.vec3d);
            
            normal = normal.crossProduct(line1, line2);
            normal = normal.normalize(normal);
            
            Vec3D vCameraRay = new Vec3D(0,0,0,1);
            vCameraRay = normal.subtractVector(triTrans.vec3d, vCamera);
            
            //if(normal.z < 0)
            if(normal.dotProduct(normal, vCameraRay) < 0.0)
            {
                //ILLUMINATION
                Vec3D light_direction = new Vec3D(0,0,-1,1);
                
                light_direction = light_direction.normalize(light_direction);
                
                double dp = Math.max(0.1, light_direction.dotProduct(light_direction, normal));
                
                //CONVERT WORLD SPACE TO VIEW SPACE
                triViewed.vec3d = matView.multiplyMatrixVector(triTrans.vec3d, matView);
                triViewed.vec3d2 = matView.multiplyMatrixVector(triTrans.vec3d2, matView);
                triViewed.vec3d3 = matView.multiplyMatrixVector(triTrans.vec3d3, matView);
                triViewed.vec2d = triTrans.vec2d;
                triViewed.vec2d2 = triTrans.vec2d2;
                triViewed.vec2d3 = triTrans.vec2d3;
                
                //CLIP VIEWED TRIANGLE AGAINST NEAR PLANE, PROJECTION MATRIX FNEAR, NOTE THAT THIS
                //COULD FORM TWO ADDITIONAL TRIANGLES.
                
                int nClippedTriangles = 0;
                Triangle[] clipped = new Triangle[]{new Triangle(new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec2D(0,0), new Vec2D(0,0), new Vec2D(0,0))
                        ,new Triangle(new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec2D(0,0), new Vec2D(0,0), new Vec2D(0,0))};

                
                nClippedTriangles = line1.triangleClipAgainstPlane(new Vec3D(0,0,0.1,1), new Vec3D(0,0,1,1
                ),triViewed, clipped);
                
                for(int i = 0; i < nClippedTriangles; i++)
                {
                triProjected.vec3d = matWorld.multiplyMatrixVector(clipped[i].vec3d, matProjected);
                triProjected.vec3d2 = matWorld.multiplyMatrixVector(clipped[i].vec3d2, matProjected);
                triProjected.vec3d3 = matWorld.multiplyMatrixVector(clipped[i].vec3d3, matProjected);
                
                triProjected.vec2d = clipped[i].vec2d;
                triProjected.vec2d2 = clipped[i].vec2d2;
                triProjected.vec2d3 = clipped[i].vec2d3;
                //SCALE INTO VIEW
                // Scale into view, we moved the normalising into cartesian space
		// out of the matrix.vector function from the previous videos, so
		// do this manually
                //INSTEAD OF DIVIDING BY 4TH DIMENSION TO CAST SHADOW BACK INTO 3D PLANE BY
                //USING CONDITIONALS IN PROJECTION MATRIX METHOD, WE DO IT HERE MANUALLY
                triProjected.vec3d = normal.divideVector(triProjected.vec3d, triProjected.vec3d.w);
                triProjected.vec3d2 = normal.divideVector(triProjected.vec3d2, triProjected.vec3d2.w);
                triProjected.vec3d3 = normal.divideVector(triProjected.vec3d3, triProjected.vec3d3.w);
                
                Vec3D vOffset = new Vec3D(1,1,0,1);
                
                triProjected.vec3d = normal.addVector(triProjected.vec3d, vOffset);
                triProjected.vec3d2 = normal.addVector(triProjected.vec3d2, vOffset);
                triProjected.vec3d3 = normal.addVector(triProjected.vec3d3, vOffset);
            
                triProjected.vec3d.x *= 0.5 * 800;
                triProjected.vec3d.y *= 0.5 * 600;
                triProjected.vec3d2.x *= 0.5 * 800;
                triProjected.vec3d2.y *= 0.5 * 600;
                triProjected.vec3d3.x *= 0.5 * 800;
                triProjected.vec3d3.y *= 0.5 * 600;
                
                triProjected.color((int)Math.abs(dp*255), (int)Math.abs(dp*255), (int)Math.abs(dp*255));
                
                vecTrianglesToRaster.add(triProjected);
                }
            }
        }
        
       Collections.sort((ArrayList<Triangle>)vecTrianglesToRaster, new Comparator<Triangle>() {
                @Override
                public int compare(Triangle t1, Triangle t2) {
                    double z1=(t1.vec3d.z+t1.vec3d2.z+t1.vec3d3.z)/3.0;
                    double z2=(t2.vec3d.z+t2.vec3d2.z+t2.vec3d3.z)/3.0;
                    return (z1<z2)?1:(z1==z2)?0:-1;
                }
            });
        for(Triangle t: vecTrianglesToRaster)
        {
            Triangle[] clipped = new Triangle[]{new Triangle(new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec2D(0,0), new Vec2D(0,0), new Vec2D(0,0)),
                new Triangle(new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec2D(0,0), new Vec2D(0,0), new Vec2D(0,0))};;
            
            LinkedList<Triangle> listTriangles = new LinkedList<>();
            listTriangles.add(t);
            int nNewTriangles = 1;
            
            for(int p = 0; p < 4; p++)
            {
                int trisToAdd = 0;
                
                while(nNewTriangles > 0)
                {
                    Triangle test = new Triangle(new Vec3D(0,0,0,1),new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec2D(0,0), new Vec2D(0,0), new Vec2D(0,0));
                    test = listTriangles.peek();
                    listTriangles.pollFirst();
                    nNewTriangles--;
                    
                    Vec3D vec = new Vec3D(0,0,0,1);  
                    
                    switch(p)
                    {
                        case 0:{trisToAdd = vec.triangleClipAgainstPlane(new Vec3D(0,0,0,1),new Vec3D(0,1,0,1),test,clipped);}break;
                        case 1:{trisToAdd = vec.triangleClipAgainstPlane(new Vec3D(0,600-1,0,1),new Vec3D(0,-1,0,1),test,clipped);}break;
                        case 2:{trisToAdd = vec.triangleClipAgainstPlane(new Vec3D(0,0,0,1),new Vec3D(1,0,0,1),test,clipped);}break;
                        case 3:{trisToAdd = vec.triangleClipAgainstPlane(new Vec3D(800-1,0,0,1),new Vec3D(-1,0,0,1),test,clipped);}break;
                    }
                      
                    for (int w = 0; w < trisToAdd; w++)
                    {
                        listTriangles.add(clipped[w]);
                    }
                }
                 nNewTriangles = listTriangles.size();
            }

            for(Triangle tt: listTriangles)
            {
                texturedTriangle(g2, (int)tt.vec3d.x,(int)tt.vec3d.y, tt.vec2d.u, tt.vec2d.v,(int)tt.vec3d2.x,(int)tt.vec3d2.y,
                tt.vec2d2.u, tt.vec2d2.v,(int)tt.vec3d3.x,(int)tt.vec3d3.y, tt.vec2d3.u, tt.vec2d3.v, pixels);

                g2.setColor(Color.white); 
                drawTriangle(g2, tt.vec3d.x, tt.vec3d.y, tt.vec3d2.x,
                tt.vec3d2.y, tt.vec3d3.x, tt.vec3d3.y
                );
                
//                //TURN 3D VECTOR X AND Y COORDINATES INTO A POLYGON THAT WILL FILL EACH SURFACE
//                Polygon triangle = new Polygon();
//                triangle.addPoint((int)tt.vec3d.x,(int)tt.vec3d.y);
//                triangle.addPoint((int)tt.vec3d2.x,(int)tt.vec3d2.y);
//                triangle.addPoint((int)tt.vec3d3.x,(int)tt.vec3d3.y);
//
//                g.setColor(tt.col);
               // g.fillPolygon(triangle);
  

            }
        }
           
        g.dispose();
    }
    
    public void drawTriangle(Graphics2D g2, double x1, double y1, double x2, double y2, double x3, double y3)
    {
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Line2D.Double(x1, y1, x2, y2));
        g2.draw(new Line2D.Double(x2, y2, x3, y3));
        g2.draw(new Line2D.Double(x3, y3, x1, y1));
    }
    
    public void texturedTriangle(Graphics2D g2, int x1, int y1, double u1, double v1, int x2, int y2, double
    u2, double v2, int x3, int y3, double u3, double v3, int[] pixels)
    {
        if(y2 < y1)
        {
            int temp = y1;
            y1 = y2;
            y2 = temp;
            
            int tempx = x1;
            x1 = x2;
            x2 = tempx;
            
            double tempu = u1;
            u1 = u2;
            u2 = tempu;
            
            double tempv = v1;
            v1 = v2;
            v2 = tempv;
        }
        
        if(y3 < y1)
        {
            int temp = y1;
            y1 = y3;
            y3 = temp;
            
            int tempx = x1;
            x1 = x3;
            x3 = tempx;
            
            double tempu = u1;
            u1 = u3;
            u3 = tempu;
            
            double tempv = v1;
            v1 = v3;
            v3 = tempv;
        }
        
        if(y3 < y2)
        {
            int temp = y2;
            y2 = y3;
            y3 = temp;
            
            int tempx = x2;
            x2 = x3;
            x3 = tempx;
            
            double tempu = u2;
            u2 = u3;
            u3 = tempu;
            
            double tempv = v2;
            v2 = v3;
            v3 = tempv;
        }
        
        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        double dv1 = v2 - v1;
        double du1 = u2 - u1;
        
        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        double dv2 = v3 - v1;
        double du2 = u3 - u1;
        
        double tex_u, tex_v;
        
        double dax_step = 0, dbx_step = 0, du1_step = 0, dv1_step = 0, du2_step = 0, dv2_step = 0;
        
         if (dy1 != 0) dax_step = dx1 / (float)Math.abs(dy1);
         if (dy2 != 0) dbx_step = dx2 / (float)Math.abs(dy2);

	 if (dy1 != 0) du1_step = du1 / (float)Math.abs(dy1);
	 if (dy1 != 0) dv1_step = dv1 / (float)Math.abs(dy1);
 
	 if (dy2 != 0) du2_step = du2 / (float)Math.abs(dy2);
	 if (dy2 != 0) dv2_step = dv2 / (float)Math.abs(dy2);
         
         if(dy1 != 0)
         {
             for(int i = y1; i <= y2; i++)
             {
                 int ax = (int)(x1 + (double)(i - y1) * dax_step);
		 int bx = (int)(x1 + (double)(i - y1) * dbx_step);
                 
                 double tex_su = u1 + (float)(i - y1) * du1_step;
		 double tex_sv = v1 + (float)(i - y1) * dv1_step;
                 
                 double tex_eu = u1 + (float)(i - y1) * du2_step;
		 double tex_ev = v1 + (float)(i - y1) * dv2_step;
                 
                 if(ax > bx)
                 {
                     int temp = ax;
                     ax = bx;
                     bx = temp;
                     
                     double temps = tex_su;
                     tex_su = tex_eu;
                     tex_eu = temps;
                     
                     double tempv = tex_sv;
                     tex_sv = tex_ev;
                     tex_ev = tempv;
                 }
                 
                 tex_u = tex_su;
                 tex_v = tex_sv;
                 
                 double tstep = 1.0 / (double)(bx-ax);
                 double t = 0.0;
                 
                 for(int j = ax; j < bx; j++)
                 {
                     tex_u = (1.0 - t) * tex_su + t * tex_eu;
                     tex_v = (1.0 - t) * tex_sv + t * tex_ev;
                     
                     int pixel = (int)((tex_v*pixels.length/1200)*400+(tex_u*400))*3;
                     
                     if (pixel>pixels.length-3) {
                        pixel=pixels.length-3;
                     }

                     int red = pixels[pixel+0];
                     int green = pixels[pixel+1];
                     int blue = pixels[pixel+2];
                     
                     g2.setColor(new Color(red, green, blue));
                     g2.drawLine(j, i, j+1, i+1);
                     
                     t += tstep;
                 }
             }

         }
         
         dy1 = y3 - y2;
             dx1 = x3 - x2;
             dv1 = v3 - v2;
             du1 = u3 - u2;
             
             if (dy1 != 0) dax_step = dx1 / (float)Math.abs(dy1);
	     if (dy2 != 0) dbx_step = dx2 / (float)Math.abs(dy2);

             du1_step = 0; dv1_step = 0;
             
             if (dy1 != 0) du1_step = du1 / (float)Math.abs(dy1);
             if (dy1 != 0) dv1_step = dv1 / (float)Math.abs(dy1);
             
             if(dy1 != 0)
         {
             for(int i = y2; i <= y3; i++)
             {
                 int ax = (int)(x2 + (double)(i - y2) * dax_step);
		 int bx = (int)(x1 + (double)(i - y1) * dbx_step);
                 
                 double tex_su = u2 + (float)(i - y2) * du1_step;
		 double tex_sv = v2 + (float)(i - y2) * dv1_step;
                 
                 double tex_eu = u1 + (float)(i - y1) * du2_step;
		 double tex_ev = v1 + (float)(i - y1) * dv2_step;
                 
                 if(ax > bx)
                 {
                     int temp = ax;
                     ax = bx;
                     bx = temp;
                     
                     double temps = tex_su;
                     tex_su = tex_eu;
                     tex_eu = temps;
                     
                     double tempv = tex_sv;
                     tex_sv = tex_ev;
                     tex_ev = tempv;
                 }
                 
                 tex_u = tex_su;
                 tex_v = tex_sv;
                 
                 double tstep = 1.0 / (double)(bx-ax);
                 double t = 0.0;
                 
                 for(int j = ax; j < bx; j++)
                 {
                     tex_u = (1.0 - t) * tex_su + t * tex_eu;
                     tex_v = (1.0 - t) * tex_sv + t * tex_ev;
                     
                     int pixel = (int)((tex_v*pixels.length/1200)*400+(tex_u*400))*3;

                     if (pixel>pixels.length-3) {
                        pixel=pixels.length-3;
                     }

                     int red = pixels[pixel+0];
                     int green = pixels[pixel+1];
                     int blue = pixels[pixel+2];
                     
                     g2.setColor(new Color(red, green, blue));
                     g2.drawLine(j, i, j+1, i+1);
                     
                     t += tstep;
                 }
             }

         }
             
    }
 
}
