
package com.ryancodesgames.thearcadechronicles.gameobject;

import java.awt.Color;


public class Vec3D 
{
    public double x, y, z;
    
    public double w = 1;
    
    public Vec3D(double x, double y, double z, double w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    
    public Vec3D()
    {
        
    }
    
    public Vec3D addVector(Vec3D in, Vec3D in2)
    {
        Vec3D out = new Vec3D(in.x + in2.x,in.y + in2.y,in.z + in2.z, 1);
        
        return out;
    }
    
    public Vec3D subtractVector(Vec3D in, Vec3D in2)
    {
        Vec3D out = new Vec3D(in.x - in2.x,in.y - in2.y,in.z - in2.z, 1);
        
        return out;
    }
    
    public Vec3D multiplyVector(Vec3D in, double f)
    {
        Vec3D out = new Vec3D(in.x * f,in.y * f,in.z * f, 1);
        
        return out;
    }
    
    public Vec3D divideVector(Vec3D in, double f)
    {
        Vec3D out = new Vec3D(in.x / f,in.y / f,in.z / f, 1);
        
        return out;
    }
    
    public double dotProduct(Vec3D in, Vec3D in2)
    {
        return in.x*in2.x + in.y*in2.y + in.z*in2.z;
    }
    
    public double vectorLength(Vec3D in)
    {
       return Math.sqrt(dotProduct(in, in));
    }
    
    public Vec3D normalize(Vec3D in)
    {
        double l = vectorLength(in);
        
        Vec3D out = new Vec3D(in.x / l, in.y / l, in.z / l,1);
        
        return out;
    }
    
    public Vec3D crossProduct(Vec3D in, Vec3D in2)
    {
        Vec3D out = new Vec3D(0,0,0,1);
        
        out.x = in.y * in2.z - in.z * in2.y;
        out.y = in.z * in2.x - in.x * in2.z;
        out.z = in.x * in2.y - in.y * in2.x;
        
        return out;
    }
    
    public Vec3D vectorIntersectPlane(Vec3D plane_p, Vec3D plane_n, Vec3D lineStart, Vec3D lineEnd, double t)
    {
        plane_n = normalize(plane_n);
	double plane_d = -dotProduct(plane_n, plane_p);
	double ad = dotProduct(lineStart, plane_n);
	double bd = dotProduct(lineEnd, plane_n);
	t = (-plane_d - ad) / (bd - ad);
	Vec3D lineStartToEnd = subtractVector(lineEnd, lineStart);
	Vec3D lineToIntersect = multiplyVector(lineStartToEnd, t);
	return addVector(lineStart, lineToIntersect);
    }
    
    public int triangleClipAgainstPlane(Vec3D plane_p, Vec3D plane_n, Triangle in, Triangle[] out)
    {
        plane_n = normalize(plane_n);
        
        Vec3D[] inside_points = {new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1)};
        int nInsidePointCount = 0;
        
        Vec3D[] outside_points = {new Vec3D(0,0,0,1), new Vec3D(0,0,0,1), new Vec3D(0,0,0,1)};
        int nOutsidePointCount = 0;
        
        Vec2D[] inside_tex = {new Vec2D(0,0), new Vec2D(0,0), new Vec2D(0,0)};
        int nInsideTexCount = 0;
        
        Vec2D[] outside_tex = {new Vec2D(0,0), new Vec2D(0,0), new Vec2D(0,0)};
        int nOutsideTexCount = 0;
        
        double d0 = dist(in.vec3d, plane_n, plane_p);
        double d1 = dist(in.vec3d2, plane_n, plane_p);
        double d2 = dist(in.vec3d3, plane_n, plane_p);
        
        if (d0 >= 0) { inside_points[nInsidePointCount++] = in.vec3d; inside_tex[nInsideTexCount++] = in.vec2d;}
	else { outside_points[nOutsidePointCount++] = in.vec3d; outside_tex[nOutsideTexCount++] = in.vec2d;}
	if (d1 >= 0) { inside_points[nInsidePointCount++] = in.vec3d2; inside_tex[nInsideTexCount++] = in.vec2d2;}
	else { outside_points[nOutsidePointCount++] = in.vec3d2; outside_tex[nOutsideTexCount++] = in.vec2d2; }
	if (d2 >= 0) { inside_points[nInsidePointCount++] = in.vec3d3; inside_tex[nInsideTexCount++] = in.vec2d3;}
	else { outside_points[nOutsidePointCount++] = in.vec3d3; outside_tex[nOutsideTexCount++] = in.vec2d3;}

        
        if (nInsidePointCount == 0)
	{
            // All points lie on the outside of plane, so clip whole triangle
            // It ceases to exist

            return 0; // No returned triangles are valid
	}

	if (nInsidePointCount == 3)
	{
	// All points lie on the inside of plane, so do nothing
	// and allow the triangle to simply pass through
            out[0] = in;

            return 1; // Just the one returned original triangle is valid
	}
        
        if(nInsidePointCount == 1 && nOutsidePointCount == 2)
        {
            out[0].col = in.col;
            out[0].vec3d = inside_points[0];
            out[0].vec2d = inside_tex[0];
            
            double t = 0;

            out[0].vec3d2 = vectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0], t);
            out[0].vec2d2.u = t * (outside_tex[0].u - inside_tex[0].u) + inside_tex[0].u;
            out[0].vec2d2.v = t * (outside_tex[0].v - inside_tex[0].v) + inside_tex[0].v;
            
            out[0].vec3d3 = vectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[1], t);
            out[0].vec2d3.u = t * (outside_tex[1].u - inside_tex[0].u) + inside_tex[0].u;
            out[0].vec2d3.v = t * (outside_tex[1].v - inside_tex[0].v) + inside_tex[0].v;
            return 1;
        }
        
        if(nInsidePointCount == 2 && nOutsidePointCount == 1)
        {
            double t = 0;
            
            out[0].col = in.col;
            out[0].vec3d = inside_points[0];
            out[0].vec3d2 = inside_points[1];
            out[0].vec2d = inside_tex[0];
            out[0].vec2d2 = inside_tex[1];
            
            out[0].vec3d3 = vectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0], t);
            out[0].vec2d3.u = t * (outside_tex[0].u - inside_tex[0].u) + inside_tex[0].u;
            out[0].vec2d3.v = t * (outside_tex[0].v - inside_tex[0].v) + inside_tex[0].v;
            
            out[1].col = in.col;
            out[1].vec3d = inside_points[1];
            out[1].vec2d = inside_tex[1];
            out[1].vec3d2 = out[0].vec3d3; 
            out[1].vec2d2 = out[0].vec2d3;
            out[1].vec3d3 = vectorIntersectPlane(plane_p, plane_n, inside_points[1], outside_points[0], t);
            out[1].vec2d3.u = t * (outside_tex[0].u - inside_tex[1].u) + inside_tex[0].u;
            out[1].vec2d3.v = t * (outside_tex[0].v - inside_tex[1].v) + inside_tex[0].v;
            return 2;
        }
        
        return 0;
    }
    
    public double dist(Vec3D p, Vec3D plane_n, Vec3D plane_p)
    {
        return(plane_n.x * p.x + plane_n.y * p.y + plane_n.z * p.z - dotProduct(plane_n, plane_p));
    }
    
    
}
