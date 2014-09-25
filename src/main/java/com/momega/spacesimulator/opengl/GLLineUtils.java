/**
 * 
 */
package com.momega.spacesimulator.opengl;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

/**
 * @author martin
 *
 */
public class GLLineUtils {

	public static void line( GL2 gl, double x1, double y1, double x2, double y2, //coordinates of the line
			double w,			//width/thickness of the line in pixel
			double Cr, double Cg, double Cb,	//RGB color components
			double Br, double Bg, double Bb,	//color of background when alphablend=false,
							//  Br=alpha of color when alphablend=true
			boolean alphablend)		//use alpha blend or not
		{
			double t=0; double R=0; double f=w-(int)w;
			double A;
			
			if ( alphablend)
				A=Br;
			else
				A=1.0f;
			
			//determine parameters t,R
			/*   */if ( w>=0.0 && w<1.0) {
				t=0.05; R=0.48+0.32*f;
				if ( !alphablend) {
					Cr+=0.88*(1-f);
					Cg+=0.88*(1-f);
					Cb+=0.88*(1-f);
					if ( Cr>1.0) Cr=1.0d;
					if ( Cg>1.0) Cg=1.0d;
					if ( Cb>1.0) Cb=1.0d;
				} else {
					A*=f;
				}
			} else if ( w>=1.0 && w<2.0) {
				t=0.05+f*0.33; R=0.768+0.312*f;
			} else if ( w>=2.0 && w<3.0){
				t=0.38+f*0.58; R=1.08;
			} else if ( w>=3.0 && w<4.0){
				t=0.96+f*0.48; R=1.08;
			} else if ( w>=4.0 && w<5.0){
				t=1.44+f*0.46; R=1.08;
			} else if ( w>=5.0 && w<6.0){
				t=1.9+f*0.6; R=1.08;
			} else if ( w>=6.0){
				double ff=w-6.0;
				t=2.5+ff*0.50; R=1.08;
			}
			//printf( "w=%f, f=%f, C=%.4f\n", w,f,C);
			
			//determine angle of the line to horizontal
			double tx=0,ty=0; //core thinkness of a line
			double Rx=0,Ry=0; //fading edge of a line
			double cx=0,cy=0; //cap of a line
			double ALW=0.01;
			double dx=x2-x1;
			double dy=y2-y1;
			if ( Math.abs(dx) < ALW) {
				//vertical
				tx=t; ty=0;
				Rx=R; Ry=0;
				if ( w>0.0 && w<1.0)
					tx*=8;
				else if ( w==1.0)
					tx*=10;
			} else if ( Math.abs(dy) < ALW) {
				//horizontal
				tx=0; ty=t;
				Rx=0; Ry=R;
				if ( w>0.0 && w<1.0)
					ty*=8;
				else if ( w==1.0)
					ty*=10;
			} else {
				if ( w < 3) { //approximate to make things even faster
					double m=dy/dx;
					//and calculate tx,ty,Rx,Ry
					if ( m>-0.4142 && m<=0.4142) {
						// -22.5< angle <= 22.5, approximate to 0 (degree)
						tx=t*0.1; ty=t;
						Rx=R*0.6; Ry=R;
					} else if ( m>0.4142 && m<=2.4142) {
						// 22.5< angle <= 67.5, approximate to 45 (degree)
						tx=t*-0.7071; ty=t*0.7071;
						Rx=R*-0.7071; Ry=R*0.7071;
					} else if ( m>2.4142 || m<=-2.4142) {
						// 67.5 < angle <=112.5, approximate to 90 (degree)
						tx=t; ty=t*0.1;
						Rx=R; Ry=R*0.6;
					} else if ( m>-2.4142 && m<-0.4142) {
						// 112.5 < angle < 157.5, approximate to 135 (degree)
						tx=t*0.7071; ty=t*0.7071;
						Rx=R*0.7071; Ry=R*0.7071;
					} else {
						// error in determining angle
						//printf( "error in determining angle: m=%.4f\n",m);
					}
				} else { //calculate to exact
					dx=y1-y2;
					dy=x2-x1;
					double L=Math.sqrt(dx*dx+dy*dy);
					dx/=L;
					dy/=L;
					cx=-0.6*dy; cy=0.6*dx;
					tx=t*dx; ty=t*dy;
					Rx=R*dx; Ry=R*dy;
				}
			}

			//draw the line by triangle strip
			double line_vertex[]=
			{
				x1-tx-Rx, y1-ty-Ry,	//fading edge1
				x2-tx-Rx, y2-ty-Ry,
				x1-tx,y1-ty,		//core
				x2-tx,y2-ty,
				x1+tx,y1+ty,
				x2+tx,y2+ty,
				x1+tx+Rx, y1+ty+Ry,	//fading edge2
				x2+tx+Rx, y2+ty+Ry
			};
			gl.glVertexPointer(2, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_vertex));
			
			if ( !alphablend) {
				
				double line_color[]=
				{
					Br,Bg,Bb,			
					Br,Bg,Bb,
					Cr,Cg,Cb,
					Cr,Cg,Cb,
					Cr,Cg,Cb,
					Cr,Cg,Cb,
					Br,Bg,Bb,			
					Br,Bg,Bb
				};
				gl.glColorPointer(3, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_color));
			} else {
				double line_color[]=
				{
					Cr,Cg,Cb,0,
					Cr,Cg,Cb,0,
					Cr,Cg,Cb,A,
					Cr,Cg,Cb,A,
					Cr,Cg,Cb,A,
					Cr,Cg,Cb,A,
					Cr,Cg,Cb,0,
					Cr,Cg,Cb,0
				};
				gl.glColorPointer(4, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_color));
			}
			
			if ( (Math.abs(dx) < ALW || Math.abs(dy) < ALW) && w<=1.0) {
				gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 6);
			} else {
				gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 8);
			}
			
			//cap
			if ( w < 3) {
				//do not draw cap
			} else {
				//draw cap
				double line_vertex2[]=
				{
					x1-Rx+cx, y1-Ry+cy,	//cap1
					x1+Rx+cx, y1+Ry+cy,
					x1-tx-Rx, y1-ty-Ry,
					x1+tx+Rx, y1+ty+Ry,
					x2-Rx-cx, y2-Ry-cy,	//cap2
					x2+Rx-cx, y2+Ry-cy,
					x2-tx-Rx, y2-ty-Ry,
					x2+tx+Rx, y2+ty+Ry
				};
				gl.glVertexPointer(2, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_vertex2));
						
				if ( !alphablend) {
					double line_color[]=
					{
						Br,Bg,Bb,	//cap1
						Br,Bg,Bb,
						Cr,Cg,Cb,
						Cr,Cg,Cb,
						Br,Bg,Bb,	//cap2
						Br,Bg,Bb,
						Cr,Cg,Cb,
						Cr,Cg,Cb
					};
					gl.glColorPointer(3, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_color));
				} else {
					double line_color[]=
					{
						Cr,Cg,Cb,0,	//cap1
						Cr,Cg,Cb,0,
						Cr,Cg,Cb,A,
						Cr,Cg,Cb,A,
						Cr,Cg,Cb,0,	//cap2
						Cr,Cg,Cb,0,
						Cr,Cg,Cb,A,
						Cr,Cg,Cb,A
					};
					gl.glColorPointer(4, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_color));
				}
				
				gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 4);
				gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 4, 4);
			}
		}


		/*a skimmed version of line(); no color, no thickness control
		 * draws near-perfectly a black "hair line" of thickness 1px
		 * when alphablend is false, it assumes drawing on a white surface
		 * when alphablend is true, it draws with alpha */
		public static void hair_line(GL2 gl, double x1, double y1, double x2, double y2, boolean alphablend)
		{
			double t=0.05; double R=0.768;
			double C=0.0;

			//determine angle of the line to horizontal
			double tx=0,ty=0, Rx=0,Ry=0;
			double ALW=0.01;
			double dx=x2-x1;
			double dy=y2-y1;
			if ( Math.abs(dx) < ALW) {
				tx=t*10; ty=0;
				Rx=R; Ry=0;
			} else if ( Math.abs(dy) < ALW) {
				tx=0; ty=t*10;
				Rx=0; Ry=R;
			} else {
				double m=dy/dx;
				if ( m>-0.4142 && m<=0.4142) {
					// -22.5< angle <= 22.5, approximate to 0 (degree)
					tx=t*0.1; ty=t;
					Rx=R*0.6; Ry=R;
				} else if ( m>0.4142 && m<=2.4142) {
					// 22.5< angle <= 67.5, approximate to 45 (degree)
					tx=t*-0.7071; ty=t*0.7071;
					Rx=R*-0.7071; Ry=R*0.7071;
				} else if ( m>2.4142 || m<=-2.4142) {
					// 67.5 < angle <=112.5, approximate to 90 (degree)
					tx=t; ty=t*0.1;
					Rx=R; Ry=R*0.6;
				} else if ( m>-2.4142 && m<-0.4142) {
					// 112.5 < angle < 157.5, approximate to 135 (degree)
					tx=t*0.7071; ty=t*0.7071;
					Rx=R*0.7071; Ry=R*0.7071;
				}
			}
			
			//draw the line by triangle strip
			double line_vertex[]=
			{
				x1-tx-Rx, y1-ty-Ry,	//fading edge1
				x2-tx-Rx, y2-ty-Ry,
				x1-tx,y1-ty,		//core
				x2-tx,y2-ty,
				x1+tx,y1+ty,
				x2+tx,y2+ty,
				x1+tx+Rx, y1+ty+Ry,	//fading edge2
				x2+tx+Rx, y2+ty+Ry
			};
			gl.glVertexPointer(2, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_vertex));
			
			if ( !alphablend) {
				double line_color[]=
				{	1,1,1,
					1,1,1,
					0,0,0,
					0,0,0,
					0,0,0,
					0,0,0,
					1,1,1,
					1,1,1
				};
				gl.glColorPointer(3, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_color));
			} else {
				double line_color[]=
				{	0,0,0,0,
					0,0,0,0,
					0,0,0,1,
					0,0,0,1,
					0,0,0,1,
					0,0,0,1,
					0,0,0,0,
					0,0,0,0
				};
				gl.glColorPointer(4, GL2.GL_DOUBLE, 0, Buffers.newDirectDoubleBuffer(line_color));
			}
			
			if ( (Math.abs(dx) < ALW || Math.abs(dy) < ALW)) {
				gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 6);
			} else {
				gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP, 0, 8);
			}
		}

//		/*as a fall back to line()*/
//		void line_raw( double x1, double y1, double x2, double y2,
//			double w,
//			double Cr, double Cg, double Cb,
//			double,double,double, bool)
//		{
//			glLineWidth(w);
//			float line_vertex[]=
//			{
//				x1,y1,
//				x2,y2
//			};
//			float line_color[]=
//			{
//				Cr,Cg,Cb,
//				Cr,Cg,Cb
//			};
//			glVertexPointer(2, GL_FLOAT, 0, line_vertex);
//			glColorPointer(3, GL_FLOAT, 0, line_color);
//			glDrawArrays(GL_LINES, 0, 2);
//		}
//

}
