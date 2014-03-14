package com.shunfengche.Activity;

import java.util.List;

import com.app.po.Driver;
import com.app.po.User;
import com.util.Connect;
import com.util.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainlistActivity extends Activity {
	
	private ImageView tabuser,tabdriver,tabsetting
	,userdaohang,dealsearchline,myorder,chatroom
	,driverchatroom,dealorder,driverdaohang;
	private RelativeLayout userrl,driverrl,settingrl;
	private  BtnLs  ls=new BtnLs();
	private ImageView myhead;
	private TextView myname,mymood,mymoney;
	User user;
	private boolean isguest=true;
	
	private boolean hasdriver=false;
	 private Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO ������Ϣ����ȥ����UI�߳��ϵĿؼ�����
				switch(msg.what ) {
				case 0:
					hasdriver=true;
					userrl.setVisibility(View.GONE);
		  	    	 driverrl.setVisibility(View.VISIBLE);
		  	    	
		  	    	isguest=true;
					break;
				case 1:
					Intent in = new Intent(MainlistActivity.this,
	  	    				DriverRegisterActivity.class);
					startActivity(in);
					break;
				
			case 3:
				Toast.makeText(getApplicationContext(), "ע��ɹ���", Toast.LENGTH_LONG).show();
				break;
				}
				super.handleMessage(msg);
			}
		};
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.mainlistxml);
       tabuser=(ImageView) findViewById(R.id.tabuser);
        tabdriver=(ImageView) findViewById(R.id.tabdriver);
        tabsetting=(ImageView) findViewById(R.id.tabsetting);
        myhead=(ImageView) findViewById(R.id.myhead);
        MyApplication myapp=(MyApplication) getApplication();
        user=myapp.getUser();
        if(user!=null){
        	SharedPreferences prefs=getSharedPreferences("myDataStorage", MODE_PRIVATE);
			String path=prefs.getString("myhead","");
			if(!path.equals("0")){
				  Bitmap b=BitmapFactory.decodeFile(user.getPhoto());
			        myhead.setImageBitmap(b);
			}
        	
      
       
        myname=(TextView) findViewById(R.id.myname);
        mymood=(TextView) findViewById(R.id.mymood);
        mymoney=(TextView) findViewById(R.id.mymoney);
        mymoney.setText("��"+user.getMoney()+"Ԫ");
       mymood.setText(user.getQianming());
       myname.setText(user.getUsername());
        }
        tabuser.setOnClickListener(ls);
        tabdriver.setOnClickListener(ls);
        tabsetting.setOnClickListener(ls);
        
        userdaohang=(ImageView) findViewById(R.id.userdaohang);
        dealsearchline=(ImageView) findViewById(R.id.dealsearchline);
        myorder=(ImageView) findViewById(R.id.myorder);
        chatroom=(ImageView) findViewById(R.id.chatroom);
        
        driverchatroom=(ImageView) findViewById(R.id.driverchatroom);
        dealorder=(ImageView) findViewById(R.id.dealorder);
        driverdaohang=(ImageView) findViewById(R.id.driverdaohang);
        
        userrl=(RelativeLayout) findViewById(R.id.userrl);
        driverrl=(RelativeLayout) findViewById(R.id.driverrl);
        driverrl.setVisibility(View.GONE);
        userrl.setOnClickListener(ls);
        driverrl.setOnClickListener(ls);
        
        userdaohang.setOnClickListener(ls);
        dealsearchline.setOnClickListener(ls);
        myorder.setOnClickListener(ls);
        chatroom.setOnClickListener(ls);
        
        driverchatroom.setOnClickListener(ls);
        driverdaohang.setOnClickListener(ls);
        dealorder.setOnClickListener(ls);
	}
	   //Ϊ��ť����������
	  	class BtnLs implements OnClickListener{

	  		@Override
	  		public void onClick(View arg0) {
	  			// TODO Auto-generated method stub
	  			switch  (arg0.getId()) {
	  			case R.id.tabuser:
	  				if(!isguest){
	  				 //Toast.makeText(getApplicationContext(), "���� �˳���", Toast.LENGTH_SHORT).show();
	  				driverrl.setVisibility(View.GONE);
	  				 userrl.setVisibility(View.VISIBLE);
	  				}
	  				
	  				break;
	  	       case R.id.tabdriver:
	  	    	 if(isguest&&(!hasdriver)){
	  	    	// Toast.makeText(getApplicationContext(), "����˾��", Toast.LENGTH_SHORT).show();
	  	    	 new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						  MyApplication myapp=(MyApplication) getApplication();
						 String str="Driver[userid]="+user.getUserid();
						 boolean isdriver=Connect.getMydriver(str,myapp);
						 if(isdriver) handler.sendEmptyMessage(0);
						 else handler.sendEmptyMessage(1);
					}
				}).start();
	  	    	
	  	    
	  	    	
	  	    	 }
	  				break;
	  	       case R.id.tabsetting:
	  	    	 Toast.makeText(getApplicationContext(), "����", Toast.LENGTH_SHORT).show();
	  				break;
	  				
	  	       case R.id.userdaohang:
	  	    	 Toast.makeText(getApplicationContext(), "�û�����", Toast.LENGTH_SHORT).show();
	  	 
	  				break;
	  	   
	  	     case R.id.myorder:
	  	   Toast.makeText(getApplicationContext(), "�ҵĶ���", Toast.LENGTH_SHORT).show();
     	Intent in3=new Intent(MainlistActivity.this, MyorderActivity.class);
			
			startActivity(in3);
			  overridePendingTransition(R.anim.guide_inanim,R.anim.guide_out);
	  	    	 break;
	  	   case R.id.chatroom:
		  	   Toast.makeText(getApplicationContext(), "�����ң�", Toast.LENGTH_SHORT).show();
		  	    	 break;
		  	    	 
	  	  case R.id.dealsearchline:
	  	    	 Toast.makeText(getApplicationContext(), "˳�糵ƥ��", Toast.LENGTH_SHORT).show();
	  	   	Intent in=new Intent(MainlistActivity.this, MatchActivity.class);
			
			startActivity(in);
			  overridePendingTransition(R.anim.guide_inanim,R.anim.guide_out);
	  	    	 break;	
	  	  case R.id.driverchatroom:
	  	    	 Toast.makeText(getApplicationContext(), "������", Toast.LENGTH_SHORT).show();
	  	    	 break;	
	  	     case R.id.driverdaohang:
	  	   Toast.makeText(getApplicationContext(), "˾��������", Toast.LENGTH_SHORT).show();
	Intent in2=new Intent(MainlistActivity.this, DriverMapActivity.class);
			
			startActivity(in2);
			  overridePendingTransition(R.anim.guide_inanim,R.anim.guide_out);
	  	    	 break;
	  	   case R.id.dealorder:
		  	   Toast.makeText(getApplicationContext(), "��������", Toast.LENGTH_SHORT).show();
		  	    	 break;
	  			default:
	  				break;
	  			}
	  		}
	  		
	  	}

}
