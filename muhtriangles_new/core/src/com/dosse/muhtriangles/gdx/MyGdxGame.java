package com.dosse.muhtriangles.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import sun.util.calendar.Gregorian;

public abstract class MyGdxGame extends ApplicationAdapter{
	private static final float SPACINGX=0.14f, SPACINGY=0.18f;
    private static final float DAMPEN=3;

    public static final int GRADIENTMODE_OLD=0, GRADIENTMODE_SMOOTH=1;
    public static final int GRADIENTSUBTLE_NO=0, GRADIENTSUBTLE_BARELY=3, GRADIENTSUBTLE_SLIGHTLY=2, GRADIENTSUBTLE_MODERATE=4, GRADIENTSUBTLE_VERY=1, GRADIENTSUBTLE_NOTATALL=5, GRADIENTSUBTLE_EXTREMELY=6;
    public static final int GRADIENTTYPE_RADIAL=0, GRADIENTTYPE_VERTICAL=1, GRADIENTTYPE_HORIZONTAL=2, GRADIENTTYPE_DIAGONAL1=3, GRADIENTTYPE_DIAGONAL2=4, GRADIENTTYPE_RANDOM=5, GRADIENTTYPE_DIAGONAL3=6, GRADIENTTYPE_DIAGONAL4=7;
    public static final int GRADIENTINVERT_NO=0, GRADIENTINVERT_YES=1;
    public static final int HUEMODE_1Y=5, HUEMODE_1W=7, HUEMODE_24H=0, HUEMODE_12H=1, HUEMODE_1H=2, HUEMODE_1M=3, HUEMODE_FIXED=4, HUEMODE_ARLEQUIN=6;
    public static final int SATMODE_NORMAL=0, SATMODE_BATTERY=1, SATMODE_BW=2, SATMODE_LESS=3, SATMODE_MORE=4, SATMODE_SLIGHTLYLESS=5, SATMODE_SLIGHTLYMORE=6;
    public static final int LUMAMODE_NORMAL=0, LUMAMODE_BATTERY=1, LUMAMODE_DARKER=2, LUMAMODE_BRIGHTER=3, LUMAMODE_SLIGHTLYDARKER=4, LUMAMODE_SLIGHTLYBRIGHTER=5, LUMAMODE_BITDARKER=6, LUMAMODE_MUCHBRIGHTER=7, LUMAMODE_MUCHDARKER=8;
    public static final int TOUCHREACT_NOTHING=0, TOUCHREACT_ANIMATE=1;
    public static final int INSTABILITY_LOW=0, INSTABILITY_NORMAL=1, INSTABILITY_HIGH=2, INSTABILITY_GLITCH=3;
    public static final int OUTLINE_OFF=0, OUTLINE_WHITE=1, OUTLINE_BLACK=2;
    public static final int SPEED_SLOWER=5, SPEED_SLOW=0, SPEED_NORMAL=1, SPEED_FAST=2, SPEED_FASTER=3, SPEED_FASTEST=4;
    public static final int DENSITY_LOW=0, DENSITY_NORMAL=1, DENSITY_HIGH=2, DENSITY_HIGHER=3, DENSITY_HIGHEST=4, DENSITY_UBER=5, DENSITY_INSANE=6;
    public static final int OUTLINETHICKNESS_HAIRLINE=3, OUTLINETHICKNESS_THIN=0, OUTLINETHICKNESS_NORMAL=1, OUTLINETHICKNESS_THICK=2, OUTLINETHICKNESS_BOLD=4;
    public static final int ALGORITHM_HEXAGONS=0, ALGORITHM_MESH=1;


    private int GRADIENTMODE=GRADIENTMODE_SMOOTH,
            GRADIENTSUBTLE=GRADIENTSUBTLE_SLIGHTLY,
            GRADIENTTYPE=GRADIENTTYPE_RADIAL,
            GRADIENTINVERT=GRADIENTINVERT_NO,
            HUEMODE=HUEMODE_24H, CUSTOMHUE=0,
            SATMODE=SATMODE_NORMAL,
            LUMAMODE=LUMAMODE_NORMAL,
            TOUCHREACT=TOUCHREACT_ANIMATE,
            SPEED=SPEED_NORMAL,
            DENSITY=DENSITY_NORMAL,
            INSTABILITY=INSTABILITY_NORMAL,
            OUTLINE=OUTLINE_WHITE,
            OUTLINETHICKNESS=OUTLINETHICKNESS_NORMAL,
            ALGORITHM=ALGORITHM_HEXAGONS,
            FPSLIMIT=20;



    /**
     * hsl (css style) -> rgb
     * modified from this article: http://biginteger.blogspot.it/2012/01/convert-rgb-to-hsl-and-vice-versa-in.html
     * @param h hue 0-360
     * @param s saturation 0-100
     * @param l lightness 0-100
     * @return rgb as 3 floats 0-1
     */
    private static float[] hslToRgb(float h, float s, float l){
        s/=100f;
        l/=100f;
        float c = (1 - Math.abs(2.f * l - 1.f)) * s;
        float h_ = h / 60.f;
        float h_mod2 = h_;
        if (h_mod2 >= 4.f) h_mod2 -= 4.f;
        else if (h_mod2 >= 2.f) h_mod2 -= 2.f;

        float hmabs=h_mod2-1;
        float x = c * (1 - (hmabs>0?hmabs:-hmabs));
        float r_, g_, b_;
        if (h_ < 1)      { r_ = c; g_ = x; b_ = 0; }
        else if (h_ < 2) { r_ = x; g_ = c; b_ = 0; }
        else if (h_ < 3) { r_ = 0; g_ = c; b_ = x; }
        else if (h_ < 4) { r_ = 0; g_ = x; b_ = c; }
        else if (h_ < 5) { r_ = x; g_ = 0; b_ = c; }
        else             { r_ = c; g_ = 0; b_ = x; }

        float m = l - (0.5f * c);

        return new float[]{((r_ + m)),((g_ + m)),((b_ + m))};
    }

    private class Point{
        private float x,y, tmul, h,s,l, cX,cY;

        public Point(float x, float y, float h, float s, float l) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.s = s;
            this.l = l;
            tmul=(float)(0.1+Math.random()*0.5);
        }
    }
    private class Triangle{
        private Point p1,p2,p3;
        private float h,s,l;

        public Triangle(Point p1, Point p2, Point p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
            if(GRADIENTMODE==GRADIENTMODE_SMOOTH) {
                h = HUEMODE==HUEMODE_ARLEQUIN?p1.h:(p1.h + p2.h + p3.h) / 3;
                s = (p1.s + p2.s + p3.s) / 3;
                l = (p1.l + p2.l + p3.l) / 3;
            }else if(GRADIENTMODE==GRADIENTMODE_OLD){
                if(Math.random()<0.33){
                    h=p1.h;
                    s=p1.s;
                    l=p1.l;
                }else if(Math.random()<0.5){
                    h=p2.h;
                    s=p2.s;
                    l=p2.l;
                }else{
                    h=p3.h;
                    s=p3.s;
                    l=p3.l;
                }
            }
        }
    }

    private float xoff=0, battery=1;
    private float SX,SY, width=-1,height=-1, dampen;
    private LinkedList<Triangle> triangles;

	private ShapeRenderer tris,outline;
	
	@Override
	public void create () {

	}

    private void init(){

        tris=new ShapeRenderer();
        outline=new ShapeRenderer();

        dampen=DAMPEN*(INSTABILITY==INSTABILITY_LOW?2:INSTABILITY==INSTABILITY_NORMAL?1:INSTABILITY==INSTABILITY_HIGH?0.7f:0.1f);

        float div=DENSITY==DENSITY_LOW?0.7f:DENSITY==DENSITY_HIGH?1.4f:DENSITY==DENSITY_HIGHER?2:DENSITY==DENSITY_HIGHEST?2.5f:DENSITY==DENSITY_UBER?3.5f:DENSITY==DENSITY_INSANE?4.5f:1;
        SX=SPACINGX/div;
        SY=SPACINGY/div;

        int NX, NY;
        if(width<height){
            NX= (int) (1/SX);
            NY= (int) ((height/width)/SY);
        }else{
            NY= (int) (1/SY);
            NX= (int) ((width/height)/SX);
        }
        triangles=new LinkedList<Triangle>();
        Point[][] grid=new Point[NY+7][NX+8];
        float xf,yf,f=0;
        float neutrF=0.7f;
        for(int y=0;y<grid.length;y++){
            for(int x=0;x<grid[0].length;x++){
                xf=(x-3)*SX;
                yf=(y-2)*SY;
                if(GRADIENTTYPE==GRADIENTTYPE_RADIAL) f= (float) (1-Math.sqrt(Math.pow(((NX * SX) / 2) - xf, 2) + Math.pow(((NY * SY) / 2) - yf, 2))); else
                if(GRADIENTTYPE==GRADIENTTYPE_HORIZONTAL) f= (1-xf)*(width<height?width/height:height/width); else
                if(GRADIENTTYPE==GRADIENTTYPE_VERTICAL) f= (1-yf)*(width<height?width/height:height/width);else
                if(GRADIENTTYPE==GRADIENTTYPE_DIAGONAL1) f= (((1-xf)*(width<height?width/height:height/width))+((1-yf)*(width<height?width/height:height/width)))/2; else
                if(GRADIENTTYPE==GRADIENTTYPE_DIAGONAL2) f= ((xf*(width<height?width/height:height/width))+((1-yf)*(width<height?width/height:height/width)))/2; else
                if(GRADIENTTYPE==GRADIENTTYPE_DIAGONAL3) f= (((1-xf)*(width<height?width/height:height/width))+(yf*(width<height?width/height:height/width)))/2; else
                if(GRADIENTTYPE==GRADIENTTYPE_DIAGONAL4) f= ((xf*(width<height?width/height:height/width))+(yf*(width<height?width/height:height/width)))/2; else
                if(GRADIENTTYPE==GRADIENTTYPE_RANDOM) f=(float)(0.3+Math.random()*0.7);

                if(GRADIENTSUBTLE==GRADIENTSUBTLE_VERY) f=neutrF*0.5f+f*0.5f; else
                if(GRADIENTSUBTLE==GRADIENTSUBTLE_SLIGHTLY) f=neutrF*0.2f+f*0.8f; else
                if(GRADIENTSUBTLE==GRADIENTSUBTLE_BARELY) f=neutrF*0.1f+f*0.9f; else
                if(GRADIENTSUBTLE==GRADIENTSUBTLE_MODERATE) f=neutrF*0.35f+f*0.65f; else
                if(GRADIENTSUBTLE==GRADIENTSUBTLE_NOTATALL) f=neutrF*-0.2f+f*1.2f; else
                if(GRADIENTSUBTLE==GRADIENTSUBTLE_EXTREMELY) f=neutrF*0.7f+f*0.3f;

                if(GRADIENTINVERT==GRADIENTINVERT_YES) f=1-f;
                f=f<0?0:f>1?1:f;

                grid[y][x]=new Point((float)((x-3-Math.random()/dampen)*SX),(float)((y-2-Math.random()/dampen)*SY),HUEMODE==HUEMODE_ARLEQUIN?(float)(360*Math.random()):HUEMODE==HUEMODE_FIXED?CUSTOMHUE:0,40+60*f,5+60*f+15*f*f);
            }
        }
        if(ALGORITHM==ALGORITHM_HEXAGONS){
            for(int y=0;y<grid.length-1;y++){
                for(int x=y%2==0?0:1;x<grid[0].length-3;x+=2){
                    triangles.addLast(new Triangle(grid[y][x],grid[y+1][x+1],grid[y][x+2]));
                    triangles.addLast(new Triangle(grid[y+1][x+1],grid[y+1][x+3],grid[y][x+2]));
                }
            }
        }else if(ALGORITHM==ALGORITHM_MESH){
            for(int y=0;y<grid.length-1;y++){
                for(int x=0;x<grid[0].length-2;x+=2){
                    triangles.addLast(new Triangle(grid[y][x],grid[y+1][x],grid[y][x+2]));
                    triangles.addLast(new Triangle(grid[y+1][x],grid[y+1][x+2],grid[y][x+2]));
                }
            }
        }
    }

    @Override
    public void resize(int newW, int newH){
        if(newW==width&&newH==height) return;
        width=newW;
        height=newH;
        init();
    }

    private GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    private long lastRenderT=0, lastUpdateT=0, lastXOffChange=0;
	@Override
	public void render () {
        if(System.nanoTime()-lastUpdateT>=10000000000L){ //update battery every 10 seconds
            readBattery();
            lastUpdateT=System.nanoTime();
        }
        while(FPSLIMIT>0 && System.nanoTime()-lastXOffChange>=1000000000L && System.nanoTime()-lastRenderT<1000000000/FPSLIMIT){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
        lastRenderT=System.nanoTime();

        double time=System.currentTimeMillis()/1000.0, day=0;
        if(HUEMODE==HUEMODE_1W) day=time/604800.0; else
        if(HUEMODE==HUEMODE_24H) day=time/86400.0; else
        if(HUEMODE==HUEMODE_12H) day=time/43200.0; else
        if(HUEMODE==HUEMODE_1H) day=time/3600.0; else
        if(HUEMODE==HUEMODE_1M) day=time/60.0;

        float w,h;
        if(width<height){
            w=width;
            h=w;
        }else{
            h=height;
            w=h;
        }

        if(TOUCHREACT==TOUCHREACT_ANIMATE) time+=xoff*3;
        time*=SPEED==SPEED_SLOWER?0.4f:SPEED==SPEED_SLOW?0.6f:SPEED==SPEED_FAST?1.3f:SPEED==SPEED_FASTER?1.6f:SPEED==SPEED_FASTEST?2:1;
        float hue, sat, luma;
        float[] color;
        tris.begin(ShapeRenderer.ShapeType.Filled);
        float hueOffset=HUEMODE==HUEMODE_1Y?360*((float)calendar.get(GregorianCalendar.DAY_OF_YEAR)/ (calendar.isLeapYear(calendar.get(GregorianCalendar.YEAR))?366f:365f)):(float) (day%1)*360;
        for(Triangle t:triangles){
            t.p1.cX=(float)(((SX/dampen)*Math.cos(time * t.p1.tmul)+t.p1.x)*w);
            t.p1.cY=(float)(((SY/dampen)*Math.sin(time * t.p1.tmul)+t.p1.y)*h);
            t.p2.cX=(float)(((SX/dampen)*Math.cos(time * t.p2.tmul)+t.p2.x)*w);
            t.p2.cY=(float)(((SY/dampen)*Math.sin(time * t.p2.tmul)+t.p2.y)*h);
            t.p3.cX=(float)(((SX/dampen)*Math.cos(time * t.p3.tmul)+t.p3.x)*w);
            t.p3.cY=(float)(((SY/dampen)*Math.sin(time * t.p3.tmul)+t.p3.y)*h);
        }
        for(Triangle t:triangles){
            hue=(t.h+(HUEMODE==HUEMODE_1W||HUEMODE==HUEMODE_12H||HUEMODE==HUEMODE_24H||HUEMODE==HUEMODE_1H||HUEMODE==HUEMODE_1M||HUEMODE==HUEMODE_1Y?hueOffset:0))%360;
            luma=t.l;
            sat=t.s;
            sat*=SATMODE==SATMODE_BW?0:SATMODE==SATMODE_LESS?0.5f:SATMODE==SATMODE_MORE?1.3f:SATMODE==SATMODE_SLIGHTLYLESS?0.7f:SATMODE==SATMODE_SLIGHTLYMORE?1.15f:SATMODE==SATMODE_BATTERY?battery:1;
            luma*=LUMAMODE==LUMAMODE_MUCHDARKER?0.5f:LUMAMODE==LUMAMODE_MUCHBRIGHTER?1.35f:LUMAMODE==LUMAMODE_BITDARKER?0.92f:LUMAMODE==LUMAMODE_DARKER?0.7f:LUMAMODE==LUMAMODE_BRIGHTER?1.2f:LUMAMODE==LUMAMODE_SLIGHTLYDARKER?0.85f:LUMAMODE==LUMAMODE_SLIGHTLYBRIGHTER?1.1f:LUMAMODE==LUMAMODE_BATTERY?(0.35f+0.65f*battery):1;
            color=hslToRgb(hue,sat>100?100:sat<0?0:sat,luma>100?100:luma<0?0:luma);
            tris.setColor(color[0],color[1],color[2],1);
            tris.triangle(t.p1.cX,t.p1.cY,t.p2.cX,t.p2.cY,t.p3.cX,t.p3.cY);
        }
        tris.end();
        if(INSTABILITY!=INSTABILITY_GLITCH&&OUTLINE!=OUTLINE_OFF){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            outline.begin(ShapeRenderer.ShapeType.Line);
            if(OUTLINE==OUTLINE_WHITE)outline.setColor(1, 1, 1, OUTLINETHICKNESS==OUTLINETHICKNESS_HAIRLINE?0.03f:OUTLINETHICKNESS==OUTLINETHICKNESS_THIN?0.07f:OUTLINETHICKNESS==OUTLINETHICKNESS_THICK?0.16f:OUTLINETHICKNESS==OUTLINETHICKNESS_BOLD?0.5f:0.1f); else if(OUTLINE==OUTLINE_BLACK)outline.setColor(0,0,0, OUTLINETHICKNESS==OUTLINETHICKNESS_HAIRLINE?0.02f:OUTLINETHICKNESS==OUTLINETHICKNESS_THIN?0.05f:OUTLINETHICKNESS==OUTLINETHICKNESS_THICK?0.1f:OUTLINETHICKNESS==OUTLINETHICKNESS_BOLD?0.5f:0.08f);
            for(Triangle t:triangles){
                outline.triangle(t.p1.cX,t.p1.cY,t.p2.cX,t.p2.cY,t.p3.cX,t.p3.cY);
            }
            outline.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
	}

	public void setXOffset(float xoff){
		this.xoff=xoff;
        if(TOUCHREACT!=TOUCHREACT_NOTHING) lastXOffChange=System.nanoTime();
	}
    public void setBattery(float batt){battery=batt;}

    public void setPreferences(int gradientMode, int gradientSubtle, int gradientType, int gradientInvert, int hueMode, int customHue, int satMode, int lumaMode, int touchReact, int speed, int density, int instability, int outline, int outlineThickness, int algorithm, int fpsLimit){
        GRADIENTMODE=gradientMode;
        GRADIENTSUBTLE=gradientSubtle;
        GRADIENTTYPE=gradientType;
        GRADIENTINVERT=gradientInvert;
        HUEMODE=hueMode;
        CUSTOMHUE=customHue;
        SATMODE=satMode;
        LUMAMODE=lumaMode;
        TOUCHREACT=touchReact;
        SPEED=speed;
        DENSITY=density;
        INSTABILITY=instability;
        OUTLINE=outline;
        OUTLINETHICKNESS=outlineThickness;
        FPSLIMIT=fpsLimit;
        ALGORITHM=algorithm;
        init();
    }

    public abstract void readBattery();

}

