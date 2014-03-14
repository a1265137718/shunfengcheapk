package com.shunfengche.Activity;




import com.android.app.sqlite.SqliteHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class GuideActivity extends Activity {
	  private ViewFlipper mViewFlipper;  
	  @Override  
	    public void onCreate(Bundle savedInstanceState) {   
	        super.onCreate(savedInstanceState);  
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.guiderxml);   
	           
	       
	        mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);  
	        mViewFlipper.setInAnimation(getApplicationContext(), R.anim.guide_inanim);   
      mViewFlipper.setOutAnimation(getApplicationContext(), R.anim.guide_out);   
 
     //  mViewFlipper.setFlipInterval(3000);   
            
        
			// mViewFlipper.showNext();   
			
             //��������ĺ�������ѭ����ʾmViewFlipper�ڵ�����View��   
     // mViewFlipper.startFlipping();   
   
	  }
	//��������key�Ƿ���move�¼���
		private int key,position;
		private float firstx=0,firsty=0,secx=0,secy=0;
		
	  public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			 //��ȡ�ֱ���
	        DisplayMetrics dm = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(dm);        
	        
	        int nowWidth = dm.widthPixels; //��ǰ�ֱ��� ���
	        int nowHeigth = dm.heightPixels; //��ǰ�ֱ��ʸ߶�
	        
			int action=event.getAction();
			System.out.println("====onTouchEvent=====");
			
			if(action==MotionEvent.ACTION_DOWN){
				System.out.println("====ACTION_DOWN=====");
				firstx=event.getX();
				firsty=event.getY();
				key=0;
				//Toast.makeText(getApplicationContext(),"������:"+event.getX()+"   ������:"+event.getY(), Toast.LENGTH_SHORT).show();
			}
			if(action==MotionEvent.ACTION_MOVE){
			System.out.println("====ACTION_MOVE=====");
//				Toast.makeText(getApplicationContext(),"������:"+event.getX()+"   ������:"+event.getY(), Toast.LENGTH_SHORT).show();
			key=1;
			}
		   if(action==MotionEvent.ACTION_UP){
				System.out.println("====ACTION_UP=====");
				//�л����¼�
				if(key==1){
					
					secx=event.getX();
					secy=event.getY();
					//���һ�
					if(Math.abs(secy-firsty)<=130&&secx-firstx>140){
						Toast.makeText(getApplicationContext(),"���һ���", Toast.LENGTH_SHORT).show();
						
					}
					//������
					if(Math.abs(secy-firsty)<=130&&firstx-secx>140){
						 
						  if(position<2)
							  {
							  mViewFlipper.showNext(); 
								position++;
							  }
							else {
								Intent in=new Intent(GuideActivity.this, MainlistActivity.class);
								
								startActivity(in);
								  overridePendingTransition(R.anim.guide_inanim,R.anim.guide_out);
								GuideActivity.this.finish();
							}
						//Toast.makeText(getApplicationContext(),"������", Toast.LENGTH_SHORT).show();
					
					
					}
					//���ϻ���
					if(Math.abs(secx-firstx)<=130&&firsty-secy>140){
						
						Toast.makeText(getApplicationContext(),"���ϻ���", Toast.LENGTH_SHORT).show();
						
					}
					//���»���
					if(Math.abs(secx-firstx)<=130&&secy-firsty>140){
						
						Toast.makeText(getApplicationContext(),"���»���", Toast.LENGTH_SHORT).show();
					   
					}
				}
				//Toast.makeText(getApplicationContext(),"������:"+event.getX()+"   ������:"+event.getY(), Toast.LENGTH_SHORT).show();
			}	
			return super.onTouchEvent(event);
		}
	// �������������������棬����SQLiteHelper�еķ����������ݿ�����ݱ�
		private void initDataBase() {

			SqliteHelper sqLiteHelper = new SqliteHelper(GuideActivity.this);
			sqLiteHelper.getReadableDatabase();
			// ���ԣ�
			Toast.makeText(GuideActivity.this, "���ݿⴴ�����", Toast.LENGTH_SHORT)
					.show();

		}
	    
}

