package com.shunfengche.Activity;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoute;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKStep;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.RouteOverlay;

import com.util.Connect;
import com.util.Maputil;
import com.util.MyApplication;


public class DriverMapActivity extends MapActivity {
	/* BMapManager mBMapMan = null; 
	 MapView mMapView;
	 private LocationListener locationListener;
	 GeoPoint pt;
	 MKLocationManager	mLocationManager;
*/
	  private String mMapKey = Maputil.mapkey;
	      
	  
	      private Button btn_location = null,btn_search=null,btn_sound,btn_stepdetail,btn_setline,btn_order;
	 
	      private MapView mapView = null;

	      private BMapManager mMapManager = null;
	      private static final int SHOW_DATAPICK = 0;
	      private static final int SHOW_TIMEPICK = 2;
	      private static final int DATE_DIALOG_ID = 4;  
	     
	      private static final int TIME_DIALOG_ID = 5;
	      private int mYear;  
	      private int mMonth;
	      private int mDay; 
	      private int mHour;
	      private int mMinute;
	      private int userid;
	      private StringBuffer strx=new StringBuffer("");
	      private StringBuffer stry=new StringBuffer("");
	   RouteOverlay routeOverlay;
	     MyOverlay mylay=new MyOverlay();
		StringBuffer stepdetailtxt=new StringBuffer("");
	      //onResumeʱע���listener��onPauseʱ��ҪRemove,ע���listener����Android�Դ��ģ��ǰٶ�API�е�
	
	 private LocationListener locationListener;

	      private MKSearch searchModel;
	
	      GeoPoint pt;
	      public class MyOverlay extends Overlay {
	  	    GeoPoint geoPoint ;
	  	   Paint paint = new Paint();
	  	
	  	   public GeoPoint getGeoPoint() {
	  		return geoPoint;
	  	}

	  	public void setGeoPoint(GeoPoint geoPoint) {
	  		this.geoPoint = geoPoint;
	  	}

	  	@Override
	  	   public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	  		        //���ҵ�λ�û���һ��String
	  	       Point point = mapView.getProjection().toPixels(geoPoint, null);
	  	       
	  	       paint.setColor(Color.RED);
	  	       paint.setTextSize(10);
	  	       paint.setTypeface(Typeface.DEFAULT_BOLD);
	  	           
	  		      
	  		      canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_has),  point.x-15,point.y-13, paint);
	  		    canvas.drawText("��ǰλ��", point.x-15, point.y+10, paint);
  		        
	  	    }
	  			}
	      //Ϊ��ť����������
	  	class BtnLs implements OnClickListener{

	  		@Override
	  		public void onClick(View arg0) {
	  			// TODO Auto-generated method stub
	  			switch  (arg0.getId()) {
	  			case R.id.btn_location:
	  				onPause();
	  				onResume();
	  				Toast.makeText(getApplicationContext(),"�ص��ҵ�λ��", Toast.LENGTH_SHORT).show();
	  		
	  				break;
	  	       case R.id.btn_search:
	  	    	 showDialog(1);
	  				break;
	  	       case R.id.btn_sound:
	  				
	  				break;
	  	       case R.id.btn_stepdetail:
	  	    	 showDialog(2);
	  	 
	  				break;
	  	     case R.id.btn_order:
	  	    	 Toast.makeText(getApplicationContext(), "�鿴����", Toast.LENGTH_SHORT).show();
	  	    	 break;	
	  	     case R.id.btn_setline:
	  	   	if(!stepdetailtxt.toString().equals(""))
	  	    	 showDialog(3);
	  	   	else Toast.makeText(getApplicationContext(), "��ѡ��·�ߣ�", Toast.LENGTH_SHORT).show();
	  	    	 break;
	  	     case R.id.setstarttime:
	  	    	  showDialog(4);
	  	    	 break;
	  	   case R.id.setstarttimedate:
	  		 showDialog(5);
	  	    	 break;
	  			default:
	  				break;
	  			}
	  		}
	  		
	  	}
	  	 ViewHolder viewHolder = new ViewHolder();
	  	  MKPlanNode startNode = new MKPlanNode();
	  	 MKPlanNode endNode = new MKPlanNode();
	  	 //����dialog
	    public Dialog onCreateDialog(int id){
	    	System.out.println("duihuakuang");
	    	 Dialog dialog=null;
	    	 View corvertview=null;
	    	 dialog=new Dialog(DriverMapActivity.this,R.style.dialog);
	    
	     LayoutInflater flater = LayoutInflater.from(this);
	    	switch(id){
	    	//��ѯdialog
	    	case 1:
	    		//����Builder����
	    		 //b=new AlertDialog.Builder(this);
	    		

	    		corvertview= flater.inflate(R.layout.searchdialogxml, null);
	    	
	    		viewHolder.root=(LinearLayout)corvertview. findViewById(R.id.searchdialog);
	    		viewHolder.et_end=(EditText)corvertview. findViewById(R.id.et_end);
	    		viewHolder.et_start=(EditText)corvertview. findViewById(R.id.et_start);
	    		viewHolder.btn_searchDrivingRoute=(Button) corvertview.findViewById(R.id.btn_searchDrivingRoute);
	    		viewHolder.btn_searchDrivingRoute.setOnClickListener(new OnClickListener() {
	    				
		              
	    				
			             @Override
			
			             public void onClick(View v) {
			
			                 String destination = viewHolder.et_end.getText().toString();
			                 String start=viewHolder.et_start.getText().toString();
			                  //Toast.makeText(getApplicationContext(), "chaxun", Toast.LENGTH_SHORT).show();
			                 
			                 //������ʼ�أ���ǰλ�ã�
			
			                endNode = new MKPlanNode();
			                 if(start.equals(""))
			                 startNode.pt = pt;
			            
			                 else startNode.name=start;
			                 //����Ŀ�ĵ�
			 
			                 endNode = new MKPlanNode();
		
			                 endNode.name = destination;
		
			                  
			
			                 //չ�������ĳ���
			
			                 String city = "���";
			                 searchModel.drivingSearch(city, startNode, city, endNode);

		
//			               System.out.println("----"+city+"---"+destination+"---"+pt);
			
			        
			            
			                 //����·��
			
//			               searchModel.walkingSearch(city, startNode, city, endNode);
			
			                 //����·��
			
//			               searchModel.transitSearch(city, startNode, endNode);
			
			             }
			
			         });
	    		//��������������ʧ
	    		 dialog.setContentView(corvertview);
	    		dialog.setCanceledOnTouchOutside(true);
	    	
	    		break;
	    		//������Ϣdialog
	    	case 2:
	    		corvertview= flater.inflate(R.layout.stepdetail, null);
	    		viewHolder.stepdetail=(TextView) corvertview.findViewById(R.id.steptv);
	    		if(!stepdetailtxt.toString().equals("")){
	    		    
	    			viewHolder.stepdetail.setText(stepdetailtxt.toString());
	    			viewHolder.stepdetail.setMovementMethod(ScrollingMovementMethod.getInstance()); 
	    		}
	    		else  viewHolder.stepdetail.setText("û�е�����Ϣ��");
	    		 dialog.setContentView( corvertview);
		    		dialog.setCanceledOnTouchOutside(true);
	    		break;
	    
	    case 3:
    		corvertview= flater.inflate(R.layout.settinglinexml, null);
    		viewHolder.seating=(EditText) corvertview.findViewById(R.id.seating);
    		viewHolder.starttime=(TextView) corvertview.findViewById(R.id.starttime);
    		viewHolder.starttimedate=(TextView) corvertview.findViewById(R.id.starttimedate);
    		
    		viewHolder.speedet=(EditText) corvertview.findViewById(R.id.speedet);
    		viewHolder.setstarttime=(Button) corvertview.findViewById(R.id.setstarttime);
    		viewHolder.setstarttimedate=(Button) corvertview.findViewById(R.id.setstarttimedate);
    		viewHolder.setstarttimedate.setOnClickListener(new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    		           Message msg = new Message(); 
    		           if (viewHolder.setstarttimedate.equals((Button) v)) {  
    		              msg.what = SHOW_DATAPICK;  
    		           }  
    		           dateandtimeHandler.sendMessage(msg); 
    				}
    			});
    	        
    		 viewHolder.setstarttime.setOnClickListener(new View.OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    		           Message msg = new Message(); 
    		           if (viewHolder.setstarttime.equals((Button) v)) {  
    		              msg.what =SHOW_TIMEPICK;  
    		           }  
    		         dateandtimeHandler.sendMessage(msg); 
    				}
    			});
    		viewHolder.aplayline=(Button) corvertview.findViewById(R.id.aplayline);
    		viewHolder.aplayline.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
					final String seating=viewHolder.seating.getText().toString().trim();
					final String starttime=viewHolder.starttimedate.getText().toString()+viewHolder.starttime.getText().toString().trim();
					final String speed=viewHolder.speedet.getText().toString().trim();
					if(seating.equals("")||speed.equals("")||starttime.equals(""))
						Toast.makeText(getApplicationContext(), "����ȷ��д��", Toast.LENGTH_SHORT).show();
					else {
						new Thread( new Runnable() {
							public void run() {
							
								//������·
								String starttxt;
								if(startNode.name==null){
									starttxt=startNode.pt.getLongitudeE6()+"/"+startNode.pt.getLatitudeE6();
								}
								else starttxt=startNode.name;
								
							 String str="Line[userid]="
									+userid+"&Line[seating]="
									+seating+"&Line[starttime]="
									
									+starttime+"&Line[speed]="
									+speed+"&Line[starttxt]="
									+starttxt+"&Line[endtxt]="
									+endNode.name+"&Line[carLongitudeE6]="
									+pt.getLongitudeE6()+"&Line[carLatitudeE6]="
									+pt.getLatitudeE6();
								
								
								
								strx.append("/").append(stry);
								str=str+"&Line[linept]="+strx;
								Connect.dopostdriverline(str);
							}
						}).start();
						/*MyApplication myapp=(MyApplication) getApplication();
						int userid=myapp.getUser().getUserid();
						int state=0;
						String starttxt;
						if(startNode.name==null){
							starttxt=startNode.pt.getLongitudeE6()+"/"+startNode.pt.getLatitudeE6();
						}
						else starttxt=startNode.name;
						String sql="insert into linetb values(null,"
							+userid+","
							+seating+","
							+seating+","
							+state+",'"
							+starttime+"','"
							+speed+"','"
							+starttxt+"','"
							+endNode.name+"',"
							+pt.getLongitudeE6()+","
							+pt.getLatitudeE6()+")";
						Connect con=new Connect();
						if(con.doS(sql, 1, "linetb", null,null).trim().equals("0"))
							Toast.makeText(getApplicationContext(), "����ʧ�ܣ�",Toast.LENGTH_SHORT).show();
						
						else  {
							//�ϴ���ɢ��
							Toast.makeText(getApplicationContext(), "�����ɹ���",Toast.LENGTH_SHORT).show();
						}*/
					}
				}
			});
    		final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);  
            mMonth = c.get(Calendar.MONTH);  
            mDay = c.get(Calendar.DAY_OF_MONTH);
            
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            
            setDateTime(); 
            setTimeOfDay();
    		 dialog.setContentView( corvertview);
	    		dialog.setCanceledOnTouchOutside(true);
    		break;
	    case DATE_DIALOG_ID:  
	           return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,  
	                  mDay);
	       case TIME_DIALOG_ID:
	    	   return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
	    	}

			return dialog;
	    	
	    }
	    static class ViewHolder { 
	       public Button btn_searchDrivingRoute;
	        public EditText et_start;
	        public  EditText et_end;
	       public   LinearLayout root;
	       public  TextView stepdetail;
	       public  EditText seating;
	       public TextView starttime;
	       public TextView starttimedate;
	       public EditText speedet;
	       public Button aplayline;
	       public Button setstarttime;
	       public Button setstarttimedate;
	    }
	    //����
	    public void doudong(){
	    	 Vibrator mVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);//��ȡ����
	    	 mVibrator.vibrate(100);
	    }
	  	BtnLs btnls=new BtnLs();
	  	 /**
	     * ��������
	     */
		private void setDateTime(){
	       final Calendar c = Calendar.getInstance();  
	       
	       mYear = c.get(Calendar.YEAR);  
	       mMonth = c.get(Calendar.MONTH);  
	       mDay = c.get(Calendar.DAY_OF_MONTH); 
	  
	       updateDateDisplay(); 
		}
		
		/**
		 * ����������ʾ
		 */
		private void updateDateDisplay(){
			viewHolder.starttimedate.setText(new StringBuilder().append(mYear).append("-")
		    		   .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
		               .append((mDay < 10) ? "0" + mDay : mDay)); 
		}
		
	    /** 
	     * ���ڿؼ����¼� 
	     */  
	    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {  
	  
	       public void onDateSet(DatePicker view, int year, int monthOfYear,  
	              int dayOfMonth) {  
	           mYear = year;  
	           mMonth = monthOfYear;  
	           mDay = dayOfMonth;  

	           updateDateDisplay();
	       }  
	    }; 
		
		/**
		 * ����ʱ��
		 */
		private void setTimeOfDay(){
		   final Calendar c = Calendar.getInstance(); 
	       mHour = c.get(Calendar.HOUR_OF_DAY);
	       mMinute = c.get(Calendar.MINUTE);
	       updateTimeDisplay();
		}
		
		/**
		 * ����ʱ����ʾ
		 */
		private void updateTimeDisplay(){
			viewHolder.starttime.setText(new StringBuilder().append(mHour).append(":")
	               .append((mMinute < 10) ? "0" + mMinute : mMinute)); 
		}
	    
	    /**
	     * ʱ��ؼ��¼�
	     */
	    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mHour = hourOfDay;
				mMinute = minute;
				
				updateTimeDisplay();
			}
		};
	    
	  
	  //ʱ�������dialog
	    @Override  
	    protected void onPrepareDialog(int id, Dialog dialog) {  
	       switch (id) {  
	       case DATE_DIALOG_ID:  
	           ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);  
	           break;
	       case TIME_DIALOG_ID:
	    	   ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
	    	   break;
	       }
	    }  
	  
	  	/** 
	     * �������ں�ʱ��ؼ���Handler 
	     */  
	    Handler dateandtimeHandler = new Handler() {
	  
	       @Override  
	       public void handleMessage(Message msg) {  
	           switch (msg.what) {  
	           case SHOW_DATAPICK:  
	               showDialog(4);  
	               break; 
	           case SHOW_TIMEPICK:
	        	   showDialog(5);
	        	   break;
	           
	           }  
	       }  
	  
	    }; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	
		         setContentView(R.layout.main);
		         //��ʼ���ؼ�
		         btn_location = (Button) findViewById(R.id.btn_location);
		         btn_search = (Button) findViewById(R.id. btn_search);
		         btn_sound = (Button) findViewById(R.id.btn_sound);
		         btn_stepdetail = (Button) findViewById(R.id.btn_stepdetail);
		         btn_setline=(Button) findViewById(R.id.btn_setline);
		         btn_order=(Button) findViewById(R.id.btn_order);
		         btnls=new BtnLs();
		         btn_location.setOnClickListener(btnls);
		         btn_search.setOnClickListener(btnls);
		         btn_sound.setOnClickListener(btnls);
		         btn_stepdetail.setOnClickListener(btnls);
		         btn_setline.setOnClickListener(btnls);
		         btn_order.setOnClickListener(btnls);
		     mMapManager = new BMapManager(getApplication());
	         mMapManager.init(mMapKey, new MyGeneralListener());
	     	MyApplication myapp=(MyApplication) getApplication();
			 userid=myapp.getUser().getUserid();
		         super.initMapActivity(mMapManager);
	           mapView = (MapView) this.findViewById(R.id.bmapsView);
	
		         //�����������õ����ſؼ�
             
		         //mapView.setBuiltInZoomControls(true); 
	       
		         //���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
		
//		         mapView.setDrawOverlayWhenZooming(true);
		
		         //��ȡ��ǰλ�ò�
		    
		   /*      myLocationOverlay = new MyLocationOverlay(baidumapActivity.this, mapView);
 		         myLocationOverlay.enableCompass(); // ��ָ����

 		         //����ǰλ�õĲ���ӵ���ͼ�ײ���
 	
 		       
 		    mapView.getOverlays().add(myLocationOverlay);*/
 		    
		
		      // ע�ᶨλ�¼�
	
		         locationListener = new LocationListener(){
		     @Override
		
		             public void onLocationChanged(Location location) {
	
		                 if (location != null){
	
		                     //����GEO�������겢�ڵ�ͼ�϶�λ���������ʾ�ĵص�
		
		                      pt = new GeoPoint((int)(location.getLatitude()*1e6),
	
		                             (int)(location.getLongitude()*1e6));
		                    
		     		        mylay.setGeoPoint(pt);
		     		         //����ǰλ�õĲ���ӵ���ͼ�ײ���
		     		    
		     		      if( mapView.getOverlays().size()>=1)
		     		   
		     		      {
		     		    	  mapView.getOverlays().set(0,mylay);
		     		    	 if(mapView.getOverlays().size()>1)
		     		    		 mapView.getOverlays().set(1,routeOverlay);
			                 //����ǰλ�õĲ���ӵ���ͼ�ײ���
		     		      }
		     		      else   mapView.getOverlays().add(mylay);
		     		
//		                   System.out.println("---"+location.getLatitude() +":"+location.getLongitude());
	
		                     mapView.getController().animateTo(pt);
	
		                 }
		
		             }
		
		         };
		  
		         //��ʼ������ģ��
		
		         searchModel = new MKSearch();
	
		         //����·�߲���Ϊ��̾���
	
		         searchModel.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
		 
		         searchModel.init(mMapManager, new MKSearchListener() {
	
		             //��ȡ�ݳ�·�߻ص�����
		
		             @Override
		
		             public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
		
		                 // ����ſɲο�MKEvent�еĶ���
		            	   //Toast.makeText(getApplicationContext(), "onGetDrivingRouteResult", Toast.LENGTH_SHORT).show();
		                 if (error != 0 || res == null) {
		
		                     Toast.makeText(DriverMapActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
		
		                     return;
	
		                 }
		
		           routeOverlay = new RouteOverlay(DriverMapActivity.this, mapView);
	
		                  
	
		                 // �˴���չʾһ��������Ϊʾ��
	
		                 MKRoute route = res.getPlan(0).getRoute(0);
		                 
		                 int distanceM = route.getDistance();

		                 String distanceKm = String.valueOf(distanceM / 1000) +"."+String.valueOf(distanceM % 1000);
	
		                 System.out.println("����:"+distanceKm+"����---�ڵ�����:"+route.getNumSteps());
		
		                 for (int i = 0; i < route.getNumSteps(); i++) {
	
		                     MKStep step = route.getStep(i);
		                     if(i!=0){
		                    	 strx.append(" ");
		                    	 stry.append(" ");
		                     }
		                     strx.append(step.getPoint().getLatitudeE6());
		                     stry.append(step.getPoint().getLongitudeE6());
	                          stepdetailtxt.append("�ڵ���Ϣ��"+step.getContent()+"\n");
		                     System.out.println("�ڵ���Ϣ��"+step.getContent());
	
		                 }
		                // Toast.makeText(DriverMapActivity.this, listp.size(), 2000).show();
		               
		                 routeOverlay.setData(route);
		                 Toast.makeText(DriverMapActivity.this, "----------size:"+route.getNumSteps(), Toast.LENGTH_SHORT).show();
		         		
		             mapView.getOverlays().clear();
		              
		             
		      /*     if(mapView.getOverlays().size()>2){
		        	   mapView.getOverlays().set(2,routeOverlay);
		           mapView.getOverlays().set(0,myLocationOverlay);
		           }
		           else   mapView.getOverlays().add(routeOverlay);*/
		             mapView.getOverlays().add(mylay);
		        	  mapView.getOverlays().add(routeOverlay);
		                 //����ǰλ�õĲ���ӵ���ͼ�ײ���
		             	
		              
		             
		
		               mapView.invalidate();
		
		                 mapView.getController().animateTo(res.getStart().pt);
		                 dismissDialog(1);
	
		             }
		
		              
		
		             //�������ַ�ʽ������ļݳ�����ʵ�ַ���һ��
		
		             @Override
		
		             public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
		
		                 //��ȡ����·��
	
		             }
		
		              
		
		             @Override
	
		             public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		
		                 //��ȡ������·
		
		             }
		
		              
		
		             @Override
		
		             public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
	
		             }
		
		             @Override
	
		             public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
	
		             }
		
		          
		             @Override
		
		             public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
		
		             }
		 
		         });
		
		          
		 
		       
		
		          
		
		     }
	
  
    
	
		     @Override
		
		     protected void onResume() {
		 
		         mMapManager.getLocationManager().requestLocationUpdates(locationListener);
		
		   /*      myLocationOverlay.enableMyLocation();
		 
		   myLocationOverlay.enableCompass(); // ��ָ����
*/
		         mMapManager.start();
		
		         super.onResume();
		 
		     }
		
		      
		
		     @Override
		
		     protected void onPause() {
		
		         mMapManager.getLocationManager().removeUpdates(locationListener);
	
		 /*   myLocationOverlay.disableMyLocation();//��ʾ��ǰλ��
		
		         myLocationOverlay.disableCompass(); // �ر�ָ����
*/
		         mMapManager.stop();
		 
		         super.onPause();
		
		     }
	
		  
		
		     @Override
		 
		     protected boolean isRouteDisplayed() {
		
		         // TODO Auto-generated method stub
	
		         return false;
	
		     }
	
		      
	
		     // �����¼���������������ͨ�������������Ȩ��֤�����
	
		     class MyGeneralListener implements MKGeneralListener {
		 
		             @Override
		
		             public void onGetNetworkState(int iError) {
		
		                 Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);
		
		                 Toast.makeText(DriverMapActivity.this, "���������������",
		 
		                         Toast.LENGTH_LONG).show();
		 
		             }
		
		  
		 
		             @Override
		
		             public void onGetPermissionState(int iError) {
	
		                 Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);
		
		                 if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
		 
		                     // ��ȨKey����
		
		                     Toast.makeText(DriverMapActivity.this,
	
		                             "����BMapApiDemoApp.java�ļ�������ȷ����ȨKey��",
	
		                             Toast.LENGTH_LONG).show();

		                 }
	
		             }
		
		 }
		     // ���ؼ�  
		     private long exitTime = 0;  
		     @Override  
		     public boolean onKeyDown(int keyCode, KeyEvent event) {  
		         if (keyCode == event.KEYCODE_BACK) {  
		             if ((System.currentTimeMillis() - exitTime) > 2000) {  
		                 Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();  
		                 exitTime = System.currentTimeMillis();  
		             } else {  
		                 finish();  
		                 System.exit(0);  
		             }  
		             return true;  
		         }  
		         return super.onKeyDown(keyCode, event);  
		     }  
		 }

		/*mBMapMan = new BMapManager(getApplication());
			mBMapMan.init(Maputil.mapkey, null);
		super.initMapActivity(mBMapMan);
	 
		mMapView = (MapView) findViewById(R.id.bmapsView);
	//mMapView.setBuiltInZoomControls(true);  //�����������õ����ſؼ�
			MapController mMapController = mMapView.getController();  // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
			GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
			      (int) (116.404 * 1E6));  //�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
				mMapController.setCenter(point); 

		//mMapController.setZoom(12);    //���õ�ͼzoom����
		//mMapView.getOverlays().add(new MyOverlay());
		
			// ��ʼ��Locationģ��
	mLocationManager = mBMapMan.getLocationManager();
		// ͨ��enableProvider��disableProvider������ѡ��λ��Provider
	 mLocationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
	 mLocationManager.disableProvider(MKLocationManager.MK_GPS_PROVIDER);
		// ��Ӷ�λͼ��
	 // ע�ᶨλ�¼�

	 

	
		MyLocationOverlay mylocTest = new MyLocationOverlay(this, mMapView);
		mylocTest.enableMyLocation(); // ���ö�λ
		Double geolat =mLocationManager.getLocationInfo().getLatitude() * 1E6;  
        Double geoLng = mLocationManager.getLocationInfo().getLongitude() * 1E6;  
        // Toast.makeText(ShowMap.this, "γ�� ��"+geolat+"����: "+geoLng,  
        // Toast.LENGTH_SHORT ).show();  
        GeoPoint mypoint1 = new GeoPoint(geolat.intValue(), geoLng.intValue()); 
	mylocTest.enableCompass();    // ����ָ����
			mMapView.getOverlays().add(mylocTest);
			//mMapController.setCenter(mypoint1); 
		locationListener = new LocationListener() {

	            @Override
	            public void onLocationChanged(Location location) {
	                    // TODO Auto-generated method stub
	                    if (location != null) {
	                            GeoPoint geoPoint = new GeoPoint(
	                                            (int) (location.getLatitude() * 1e6),
	                                            (int) (location.getLongitude() * 1e6));
	                            MyOverlay mylay=new MyOverlay();
	                            mylay.setGeoPoint(geoPoint);
	                            mMapView.getOverlays().add( mylay);
	                            mMapView.getController().animateTo(geoPoint);
	                            mMapView.getController().setCenter(geoPoint);
	                            mMapView.getController().setZoom(14);// ���õ�ͼzoom����
	                            mLocationManager.removeUpdates(locationListener);  
	                    }
	            }

	    };
	    mLocationManager.enableProvider(MKLocationManager.MK_GPS_PROVIDER);  
	    mLocationManager.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);  
	    mLocationManager.requestLocationUpdates(locationListener);
	    mLocationManager.setNotifyInternal(5, 2); 
	}
	// ��λ�Լ���λ�ã�ֻ��λһ��  
    private LocationListener mLocationListener = new LocationListener() {  
  
        @Override  
        public void onLocationChanged(Location location) {  
            if (location != null) {  
                Double geolat = location.getLatitude() * 1E6;  
                Double geoLng = location.getLongitude() * 1E6;  
                // Toast.makeText(ShowMap.this, "γ�� ��"+geolat+"����: "+geoLng,  
                // Toast.LENGTH_SHORT ).show();  
                GeoPoint mypoint1 = new GeoPoint(geolat.intValue(), geoLng.intValue());  
                app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);  
                onceMyPoint++;  
            }  
        }  
    };  
public class MyOverlay extends Overlay {
	    GeoPoint geoPoint ;
	   Paint paint = new Paint();
		
	   public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	@Override
	   public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		        //���찲�ŵ�λ�û���һ��String
	       Point point = mMapView.getProjection().toPixels(geoPoint, null);
		        canvas.drawText("���������ҵ�λ��", point.x, point.y, paint);
		       
	    }
			}

@Override
protected void onDestroy() {
	   if (mBMapMan != null) {
        mBMapMan.destroy();
        mBMapMan = null;
    }
	    super.onDestroy();
	}
@Override
protected void onPause() {
	    if (mBMapMan != null) {
		        mBMapMan.stop();
	    }
	    super.onPause();
		}
	@Override
protected void onResume() {
	    if (mBMapMan != null) {
	        mBMapMan.start();
	    }
		    super.onResume();
	}


}
*/