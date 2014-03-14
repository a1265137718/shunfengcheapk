package com.shunfengche.Activity;


import java.io.UnsupportedEncodingException;


import com.util.Connect;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GuestRegisterActivity extends Activity {
	Button submitbtn;
	EditText  edtext_user,  
	edtext_psw, 
	edtext_sure_psw,  
	edtext_phnum,  
	edtext_idnum,

	edit_name;
	TextView
	 edtext_userflag,  
		edtext_pswflag, 
		edtext_sure_pswflag,  
		edtext_phnumflag,  
		edtext_idnumflag,

		edit_nameflag;
	  
	  int flag1=1,flag2=1,flag3=1,flag4=1,flag5=1,flag6=1;
	 
	  private Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO ������Ϣ����ȥ����UI�߳��ϵĿؼ�����
				switch(msg.what ) {
				case 0:
					flag1 = 0;
					edtext_userflag.setVisibility(View.INVISIBLE);
					break;
				case 1:
					flag1 = 1;
					edtext_userflag.setVisibility(View.VISIBLE);
					edtext_userflag.setText("*�Ѵ���");
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.guest_register);
		//�û����ؼ�
		edtext_user=(EditText) findViewById(R.id.edtext_user);
		//����ؼ�
		edtext_psw=(EditText) findViewById(R.id.edtext_psw);
		//ȷ������
		edtext_sure_psw=(EditText) findViewById(R.id.edtext_sure_psw);
		//�绰����
		edtext_phnum=(EditText) findViewById(R.id.edtext_phnum);
		//��ʵ����
		edit_name=(EditText) findViewById(R.id.edit_name);
		edtext_idnum=(EditText) findViewById(R.id.edtext_idnum);
		//�û����ؼ�flag
		edtext_userflag=(TextView) findViewById(R.id.edtext_userflag);
		//����ؼ�flag
		edtext_pswflag=(TextView) findViewById(R.id.edtext_pswflag);
		//ȷ������flag
		edtext_sure_pswflag=(TextView) findViewById(R.id.edtext_sure_pswflag);
		//�绰����flag
		edtext_phnumflag=(TextView) findViewById(R.id.edtext_phnumflag);
		//��ʵ����flag
		edit_nameflag=(TextView) findViewById(R.id.edit_nameflag);
		edtext_idnumflag=(TextView) findViewById(R.id.edtext_idnumflag);
		//�û����޸ļ�����
		edtext_user.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				//���е���
				if(arg0.hasFocus()==false){
					final String username = edtext_user.getText().toString().trim();
					if(!username.equals("")){
					new Thread(new Runnable() {
						public void run() {
							boolean nouser=Connect.hasusername("User[username]="+username);
							if(nouser)
								handler.sendEmptyMessage(0);
							else handler.sendEmptyMessage(1);
						}
					}).start();

					
				}
					else {
						flag1 = 1;
						edtext_userflag.setVisibility(View.VISIBLE);
						edtext_userflag.setText("*");
					}
				}
			}
			
		});
		edtext_user.addTextChangedListener(new TextWatcher() {  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before, int count) {  
                // TODO Auto-generated method stub  
            }  
  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count,  
                    int after) {  
                // TODO Auto-generated method stub  
                  
            }  
              
        
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String username = edtext_user.getText().toString().trim();
				
				if(username.equals("")) {
					flag1=1;
					
					edtext_userflag.setVisibility(View.VISIBLE);
					edtext_userflag.setText("*");
				}
				else{
					flag1=0; 
					edtext_userflag.setVisibility(View.INVISIBLE);
				}
			}  
        });  
		
		//����һ�޸ļ�����
		edtext_psw.addTextChangedListener(new TextWatcher() {  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before, int count) {  
                // TODO Auto-generated method stub  
            }  
  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count,  
                    int after) {  
                // TODO Auto-generated method stub  
                  
            }  
              
        
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String str = edtext_psw.getText().toString().trim();
			
				if(str.equals(""))       {
					flag2=1;
					edtext_pswflag.setVisibility(View.VISIBLE);
					edtext_pswflag.setText("*");
				}
				else{
				  if(str.length()<6){
					flag2 = 1;
					edtext_pswflag.setVisibility(View.VISIBLE);
					edtext_pswflag.setText("*���볤�Ȳ���С��6");
				  }
				  else{
					flag2 = 0;
					edtext_pswflag.setVisibility(View.GONE);
				  }
				}
			
			}  
        });  
		//����2�޸ļ�����
		edtext_sure_psw.addTextChangedListener(new TextWatcher() {  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before, int count) {  
                // TODO Auto-generated method stub  
            }  
  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count,  
                    int after) {  
                // TODO Auto-generated method stub  
                  
            }  
              
        
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String str = edtext_sure_psw.getText().toString().trim();
				String str2 = edtext_psw.getText().toString().trim();
				if(str.equals(""))       {
					flag3=1;
					edtext_sure_pswflag.setVisibility(View.VISIBLE);
					edtext_sure_pswflag.setText("*");
				}
				else{
				  if(!str.equals(str2)){
					flag3 = 1;
					edtext_sure_pswflag.setVisibility(View.VISIBLE);
					edtext_sure_pswflag.setText("*�������벻һ��");
				  }
				  else{
					flag3 = 0;
					edtext_sure_pswflag.setVisibility(View.GONE);
				  }
				}
			
			}  
        });  
//�ֻ������޸ļ�����
		edtext_phnum.addTextChangedListener(new TextWatcher() {  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before, int count) {  
                // TODO Auto-generated method stub  
            }  
  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count,  
                    int after) {  
                // TODO Auto-generated method stub  
                  
            }  
              
        
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String str = edtext_phnum.getText().toString().trim();
			
				if(str.equals(""))       {
					flag4=1;
					edtext_phnumflag.setVisibility(View.VISIBLE);
					edtext_phnumflag.setText("*");
				}
				else{
				  if(str.length()!=11){
					flag4 = 1;
					edtext_phnumflag.setVisibility(View.VISIBLE);
					edtext_phnumflag.setText("*�ֻ����볤�Ȳ���ȷ");
				  }
				  else{
					flag4 = 0;
					edtext_phnumflag.setVisibility(View.GONE);
				  }
				}
			
			}  
        });  
		//���֤�����޸ļ�����
		edtext_idnum.addTextChangedListener(new TextWatcher() {  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before, int count) {  
                // TODO Auto-generated method stub  
            }  
  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count,  
                    int after) {  
                // TODO Auto-generated method stub  
                  
            }  
              
        
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String str = edtext_idnum.getText().toString().trim();
			
			
				if(str.equals(""))       {
					flag5=1;
					edtext_idnumflag.setVisibility(View.VISIBLE);
					edtext_idnumflag.setText("*");
				}
				else{
				  if(str.length()!=18){
					flag5 = 1;
					edtext_idnumflag.setVisibility(View.VISIBLE);
					edtext_idnumflag.setText("*���֤���볤�Ȳ���ȷ");
				  }
				  else{
					flag5 = 0;
					edtext_idnumflag.setVisibility(View.GONE);
				  }
				}
			
			}  
        });  
		//��ʵ�����޸ļ�����
		edit_name.addTextChangedListener(new TextWatcher() {  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before, int count) {  
                // TODO Auto-generated method stub  
            }  
  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count,  
                    int after) {  
                // TODO Auto-generated method stub  
                  
            }  
              
        
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String str = edit_name.getText().toString().trim();
			
				if(str.equals(""))       {
					flag6=1;
					edit_nameflag.setVisibility(View.VISIBLE);
					edit_nameflag.setText("*");
				}
				else{
				  if(!isChinese(str)){
					flag6 = 1;
					edit_nameflag.setVisibility(View.VISIBLE);
					edit_nameflag.setText("*�����뺺��");
				  }
				  else{
					flag6 = 0;
					edit_nameflag.setVisibility(View.GONE);
				  }
				}
			
			}  
        });  
	    //�ύ��ť
	    submitbtn =  (Button) findViewById(R.id.submit_guest);
		submitbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String user = edtext_user.getText().toString().trim();
				String password = edtext_psw.getText().toString().trim();
				String password_sure = edtext_sure_psw.getText().toString().trim();
				String phnum = edtext_phnum.getText().toString().trim();
				String name = edit_name.getText().toString().trim();
				String id=edtext_idnum.getText().toString().trim();
			
				if(flag1==0&&flag2==0&&flag3==0&&flag4==0&&flag5==0&&flag6==0){
					final StringBuffer bf=new StringBuffer();
					bf.append("User[username]=").append(user)
					.append("&User[password]=").append(password)
					.append("&User[phnum]=").append(phnum)
					.append("&User[turename]=").append(name)
					.append("&User[cardid]=").append(id);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(Connect.dogusetregister(bf.toString())){
								handler.sendEmptyMessage(3);
								SharedPreferences prefs=getSharedPreferences("myDataStorage", MODE_PRIVATE);
								Editor mEditor=prefs.edit();
								mEditor.putString("myhead","0");
								mEditor.commit();
							finish();
							}
						}
					}).start();
					
					/*sql="insert into usertb values(null,'" +user+
							"','"+password+
							"','"+phnum+
							"','"+id+
					"',null,null,0,1,'"+name+
					"','��','D://upload/userhead_default.png','ʲôҲû˵')";
					con.doS(sql, 1, "usertb", null, null);
					*/
				}else{Toast.makeText(getApplicationContext(), "�뽫ע����Ϣ����������", Toast.LENGTH_LONG).show();}
				
			}
		});
		
	}
	private boolean isChinese(String stx) {
	  boolean flag;
	  int bl=stx.getBytes().length;
		if(bl==3*stx.length())
			flag=true;
		else flag=false;
		return flag;
    }
}
