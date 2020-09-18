package ch.unibe.scg.phd.utils;

import java.nio.CharBuffer;

import org.openqa.selenium.Keys;

public class CharUtil {

	public static CharSequence translateKeyCode(int keyCode) {
		
		switch(keyCode) {
		    case 8:
	    		return Keys.BACK_SPACE;
	    	case 9:
	    		return Keys.TAB;
	    	case 13:
	    		return Keys.ENTER;
	    	case 19:
	    		return Keys.CANCEL;
	    	case 27: 
	    		return Keys.ESCAPE;
	    	case 33:
	    		return Keys.PAGE_UP;
	    	case 34:
	    		return Keys.PAGE_DOWN;
	    	case 35:
	    		return Keys.END;
	    	case 36: 
	    		return Keys.HOME;
	    	case 37:
	    		return Keys.ARROW_LEFT;
	    	case 38:
	    		return Keys.ARROW_UP;
	    	case 39: 
	    		return Keys.ARROW_RIGHT;
	    	case 40:
	    		return Keys.ARROW_DOWN;
	    	case 45:
	    		return Keys.INSERT;
	    	case 46:
	    		return Keys.DELETE;
//	    		
//	    	case 48:
//	    		return "0";
//	    	case 49:
//	    		return "1";
//	    	case 50:
//	    		return "2";
//	    	case 51:
//	    		return "3";
//	    	case 52:
//	    		return "4";
//	    	case 53:
//	    		return "5";
//	    	case 54:
//	    		return "6";
//	    	case 55:
//	    		return "7";
//	    	case 56:
//	    		return "8";
//	    	case 57:
//	    		return "9";
//	    		
//	    	case 65:
//	    		return "a";
//	    	case 66:
//	    		return "b";
//	    	case 67:
//	    		return "c";
//	    	case 68:
//	    		return "d";
//	    	case 69:
//	    		return "e";
//	    	case 70:
//	    		return "f";
//	    	case 71:
//	    		return "g";
//	    	case 72:
//	    		return "h";
//	    	case 73:
//	    		return "i";
//	    	case 74:
//	    		return "j";
//	    	case 75:
//	    		return "k";
//	    	case 76:
//	    		return "l";
//	    	case 77:
//	    		return "m";
//	    	case 78:
//	    		return "n";
//	    	case 79:
//	    		return "o";
//	    	case 80:
//	    		return "p";
//	    	case 81:
//	    		return "q";
//	    	case 82:
//	    		return "r";
//	    	case 83:
//	    		return "s";
//	    	case 84:
//	    		return "t";
//	    	case 85:
//	    		return "u";
//	    	case 86:
//	    		return "v";
//	    	case 87:
//	    		return "w";
//	    	case 88:
//	    		return "x";
//	    	case 89:
//	    		return "y";
//	    	case 90:
//	    		return "z";
//	    	
//	    	case 96:
//	    		return Keys.NUMPAD0;
//	    	case 97:
//	    		return Keys.NUMPAD1;
//	    	case 98:
//	    		return Keys.NUMPAD2;
//	    	case 99:
//	    		return Keys.NUMPAD3;
//	    	case 100:
//	    		return Keys.NUMPAD4;
//	    	case 101:
//	    		return Keys.NUMPAD5;
//	    	case 102:
//	    		return Keys.NUMPAD6;
//	    	case 103:
//	    		return Keys.NUMPAD7;
//	    	case 104:
//	    		return Keys.NUMPAD8;
//	    	case 105:
//	    		return Keys.NUMPAD9;
//	    	case 106:
//	    		return Keys.MULTIPLY;
//	    	case 107:
//	    		return Keys.ADD;
//	    	case 109:
//	    		return Keys.SUBTRACT;
//	    	case 110:
//	    		return Keys.DECIMAL;
//	    	case 111:
//	    		return Keys.DIVIDE;
//	    	case 112:
//	    		return Keys.F1;
//	    	case 113:
//	    		return Keys.F2;
//	    	case 114:
//	    		return Keys.F3;
//	    	case 115:
//	    		return Keys.F4;
//	    	case 116:
//	    		return Keys.F5;
//	    	case 117:
//	    		return Keys.F6;
//	    	case 118:
//	    		return Keys.F7;
//	    	case 119:
//	    		return Keys.F8;
//	    	case 120:
//	    		return Keys.F9;
//	    	case 121:
//	    		return Keys.F10;
//	    	case 122:
//	    		return Keys.F11;
//	    	case 123:
//	    		return Keys.F12;
	    	default:
	    		return CharBuffer.wrap(new char[]{(char) keyCode});
	    		//return new String(new int[] {keyCode}, 0, 1); // converts remaining printable chars
	    		
//	    	default:
//	    		if (keyCode >= 32) {
//	    			return CharBuffer.wrap(new char[]{(char) keyCode});
	    }
			
//	    	case "Delete":
//	    		return Keys.DELETE;
//		    case "ArrowUp":
//		        return Keys.ARROW_UP;
//		    case "ArrowRight":
//		        return Keys.ARROW_RIGHT;
//		    case "ArrowLeft":
//		        return Keys.ARROW_LEFT;
//		    case "ArrowDown":
//		        return Keys.ARROW_LEFT;
//		    case "Escape":
//		        return Keys.ESCAPE;
//		    case "Enter":
//		        return Keys.ENTER;
//		    case "Backspace":
//		        return Keys.BACK_SPACE;
//		    case "Tab":
//		        return Keys.TAB;
//		    default:
//		        return CharBuffer.wrap(new char[]{(char) keyCode});
	}
	
}
