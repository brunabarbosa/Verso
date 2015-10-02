package com.projetoles.verso;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

public class ClickableString extends ClickableSpan {

	private View.OnClickListener mListener;         

	public ClickableString(View.OnClickListener listener) {              
		mListener = listener;  
	}          
	
	@Override  
	public void onClick(View v) {  
		mListener.onClick(v);  
	}      

	public static SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
	    SpannableString link = new SpannableString(text);
	    link.setSpan(new ClickableString(listener), 0, text.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
	    link.setSpan(new ForegroundColorSpan(Color.rgb(100, 60, 40)), 0, text.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
	    return link;
	}
	
	public static void makeLinksFocusable(TextView tv) {
	    MovementMethod m = tv.getMovementMethod();  
	    if ((m == null) || !(m instanceof LinkMovementMethod)) {  
	        if (tv.getLinksClickable()) {  
	            tv.setMovementMethod(LinkMovementMethod.getInstance());  
	        }  
	    }  
	}

}
