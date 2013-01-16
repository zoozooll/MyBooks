package com.chinachip.ccbooks.core;


public class TextSizeUtil {

	public static int fontSize = 22;
	public static int lineCount = 24;
	
	
	public TextSizeUtil() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**获得一页行数*/
	public static int regetLineCount(int  fontSize,int portraint) {
		if (portraint == 1) {
			float temp = 400/fontSize;
			float temp2 = temp/0.75f;
			int temp3 = Math.round(temp2);
			lineCount = temp3;
		} else if (portraint == 2) {
			return 0;
		}
		return lineCount;
		
		
	}

	
	public static int getLineCount(int  fontSize,int portraint) {
		
		/*if (portraint == 1) {
			switch (fontSize) { 
			 case 10 : 
				 	lineCount = 55;
					break; 
			 case 14 : 
				 	lineCount = 41;
					break; 
			 case 18 : 
				 	lineCount = 33;
					break; 
			 
			 case 26 : 
				 	lineCount = 24;
					break; 
			 
			 case 30 : 
				 	lineCount = 20;
					break; 
			 case 34 : 
				 	lineCount = 19;
					break; 
			 case 38 : 
				 	lineCount = 17;
					break; 
					
			 default : 
				 	lineCount = 27;
				 	break; 
			}
			
		} else if (portraint == 2) {
			switch (fontSize) { 
			 case 10 : 
				 	lineCount = 52;
					break; 
			 case 14 : 
				 	lineCount = 40;
					break; 
			 case 18 : 
				 	lineCount = 32;
					break; 
			 
			 case 26 : 
				 	lineCount = 22;
					break; 
			 
			 case 30 : 
				 	lineCount = 20;
					break; 
			 case 34 : 
				 	lineCount = 18;
					break; 
			 case 38 : 
				 	lineCount = 16;
					break; 
					
			 default : 
				 	lineCount = 30;
				 	break; 
			}
			
		}*/
		switch (portraint) {
		case 1:
			lineCount= 674/(fontSize+2);
			break;
		case 2:
			lineCount= ((int)(360/(fontSize+2)))*2;
			break;
		default:
			break;
		}
		return lineCount;
		
		
	}

}
