package com.ccbooks.fullscreen.content;


import java.util.ArrayList;

import android.util.Log;

public class MoveControler {
	static final String DEBUG_TAG = "jf";
	long tStart;
	long tEnd;
	float s0;
	float sStart;
	float sEnd;
	double vStart;
	double vEnd;
	double a;
	
	public MoveControler(float s) {
		s0 = s;
		tStart = tEnd = 0;
		sStart = sEnd = 0;
		vStart = vEnd = 0;
	}

	public MoveControler(float s,double a) {
		s0 = s;
		this.a = a;
		tStart = tEnd = 0;
		sStart = sEnd = 0;
		vStart = vEnd = 0;
	}
	
	public void setAcceleration(double a) {
		this.a = a;
	}
	
	public void setReference(float s) {
		s0 = s;
	}
	
	public void resetMove() {
		tStart = tEnd = 0;
		sStart = sEnd = 0;
		vStart = vEnd = 0;
	}
	
	public void addNewMovePoint(long t, float s) {
		tStart = tEnd;
		sStart = sEnd;
		vStart = vEnd;
		
		tEnd = t;
		sEnd = s;
		vEnd = (sEnd-sStart)/(tEnd-tStart);
		if(vEnd <-5)vEnd = -3;
		else if(vEnd >5)vEnd = 3;
		System.out.println("==addNewMovePoint==vEnd======"+vEnd);
	}
	public float getPosBy(long tAdd, boolean isTouching) {
		float sNow = 0;
		if(isTouching == false){
			if(vEnd != 0) {
				tStart = tEnd;
				sStart = sEnd;
				vStart = vEnd;
				tEnd = tStart+tAdd;
				if(vStart>0)
					vEnd = vStart - a*(tEnd - tStart);
				else
					vEnd = vStart + a*(tEnd - tStart);
				System.out.println("==getPos==vEnd======"+vEnd);
				if((vStart>0 && vEnd<0) ||(vStart<0 && vEnd>0)) {
					vEnd = 0;
					sEnd = sEnd;
				}
				else {
					sEnd = (float)(sStart + (vEnd + vStart)*(tEnd - tStart)/2);
				}
			}
		}
		sNow = sEnd - s0;
		Log.d(DEBUG_TAG, "vend="+vEnd+"......sEnd="+sEnd+".......sNow=="+sNow);
		return sNow;	
	}

	public double getCurrentVelocity() {
		return vEnd;
	}
	
	public float getPosIn(long t, boolean isTouching) {
		float sNow = 0;
		if(isTouching == false){
			if(vEnd != 0) {
				tStart = tEnd;
				sStart = sEnd;
				vStart = vEnd;
				tEnd = t;
				if(vStart>0)
					vEnd = vStart - a*(tEnd - tStart);
				else
					vEnd = vStart + a*(tEnd - tStart);
				System.out.println("==getPos==vEnd======"+vEnd);
				if((vStart>0 && vEnd<0) ||(vStart<0 && vEnd>0)) {
					vEnd = 0;
					sEnd = sEnd;
				}
				else {
					sEnd = (float)(sStart + (vEnd + vStart)*(tEnd - tStart)/2);
				}
			}
		}
		sNow = sEnd - s0;
		Log.d(DEBUG_TAG, "vend="+vEnd+"......sEnd="+sEnd+".......sNow=="+sNow);
		return sNow;	
	}
	
	
	public long getTime(float sAdd, boolean isTouching) {
		if(isTouching == false){
			if(vEnd != 0) {
				tStart = tEnd;
				sStart = sEnd;
				vStart = vEnd;
				
				sEnd = sStart+sAdd;
				if(vStart >0)
					tEnd = tStart+(long)(sAdd/vStart);
				else
					tEnd = tStart-(long)(sAdd/vStart);
				
				if(vStart>0)
					vEnd = vStart - a*(tEnd - tStart);
				else
					vEnd = vStart + a*(tEnd - tStart);
				if((vStart>0 && vEnd<0) ||(vStart<0 && vEnd>0)) {
					vEnd = 0;
				}
			}
			else
				return 0;
		}
		else
			return 0;
		Log.d(DEBUG_TAG,"==vEnd="+vEnd+"==tEnd="+tEnd+"==sEnd="+sEnd+"time:::"+(tEnd - tStart));
		return tEnd - tStart;
	}
}
