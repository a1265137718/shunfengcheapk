package com.shunfengche.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.app.po.Line;
import com.app.po.User;
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
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.RouteOverlay;


import com.util.Connect;
import com.util.MyApplication;
import com.util.uploadFileClass;


import com.util.Maputil;

import android.R.integer;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MatchActivity extends MapActivity {

	
	private Button searchmatch;
	private EditText mystartet,myendet;
	private Line resultline;
	private ImageView driverhead;
	private TextView lineid,matchstarttime,drivername,lineseinfo,linemoney;
	private RelativeLayout  lineinfo;
	private BMapManager mMapManager = null;
	private List<Line> listline;
	private MKSearch searchModel;
	private String startname,endname;
	private  MKRoute route;
	private  MKRoute routeresult
	;
	private GeoPoint dispts,dispte,disptsresult,dispteresult,disptsresult1,dispteresult1;

	private float startdisresult,enddisresult,mindisresult,startdisresult1,enddisresult1;

	private int type;
	private int flag;
	private float startdis,enddis;
	private boolean isaddress=false;
	private MapView mapView = null;
	 GeoPoint userstartpt,userendpt;
	 MKPlanNode endNode= new MKPlanNode();
     MKPlanNode startNode= new MKPlanNode();
  
    MyOverlay mylay=new MyOverlay();
    RouteOverlay routeOverlay;
    int addresstype=0;
	 private LocationListener locationListener;
	
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
 		    canvas.drawText("��ǰλ��", point.x-15, point.y+25, paint);
		        
 	    }
 			}
     public class LineOverlay extends Overlay {
 	    GeoPoint[] geoPoints ;
 	   Paint paint = new Paint();
 		
 	   public GeoPoint[] getGeoPoint() {
 		return geoPoints;
 	}

 	public void setGeoPoint(GeoPoint[] geoPoints) {
 		this.geoPoints = geoPoints;
 	}

 	@Override
 	   public void draw(Canvas canvas, MapView mapView, boolean shadow) {
 		        //���찲�ŵ�λ�û���һ��String
 		String[] txts={"A","B","C","D"};
 	   paint.setColor(Color.BLUE);
	       paint.setTextSize(30);
	       paint.setTypeface(Typeface.DEFAULT_BOLD);
 		  for(int i=0;i<4;i++){
 			  GeoPoint pt=geoPoints[i];
 			  String txt=txts[i];
 	       Point point = mapView.getProjection().toPixels(pt, null);
 		        canvas.drawText(txt, point.x-15, point.y, paint);
 		  } 
 	    }
 			}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	///i��routeѭ��������f:startrouteѭ������ t:endrouteѭ������
	Line line;
	private int i=0,f=0,t=0,d=0;
	MyApplication myapp;
	 Geocoder gc;
	 Boolean isend=false;
	 Boolean isstart=false;
	 String bystart=null,byend=null;
	//������·����
	private boolean changeroute=false,changestartroute=false,changeendroute=false;
	private  MKSearchListener mymksearch=new MKSearchListener() {
		   @Override

	        public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			    //�˳��յ�
			 
	 			       
	        }

        //��ȡ�ݳ�·�߻ص�����

        @Override

        public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {

            // ����ſɲο�MKEvent�еĶ���
       	// Toast.makeText(MatchActivity.this, "onGetDrivingRouteResult", Toast.LENGTH_SHORT).show();
          System.out.println("onGetDrivingRouteResult");
          if (error != 0 || res == null) {

             Toast.makeText(getApplicationContext(), "�����յ�����", Toast.LENGTH_SHORT).show();
        	  
        	  System.out.println("��Ǹ��δ�ҵ����");
              return;

          }
          if(isstart){
        	  isstart=false;
        	  route=res.getPlan(0).getRoute(0);
        	  userstartpt=route.getStart();
        	  userendpt=route.getEnd();
        	  initdate();
     		 if(listline.isEmpty())   Toast.makeText(getApplicationContext(), "��ǰû����·!", Toast.LENGTH_SHORT).show();
     		 else {
     			showDialog(2);
     			 line=listline.get(0);
     		     f=0;
     		  fileSize=listline.size();
     		 downLoadFileSize = 1;  
             sendMsg(0); 
             sendMsg(1); 
             
					    //������ʼ�أ���ǰλ�ã�
						  System.out.println("for lineid:"+line.getLineid());
						String start=line.getOrigin();
						String end=line.getDestination();
					 endNode = new MKPlanNode();
						 startNode = new MKPlanNode();
				         if(start.contains("/")){
				        	 GeoPoint startpt=new GeoPoint(
				        			 Integer.parseInt(start.substring(0, start.lastIndexOf("/")-1))
				        			 , Integer.parseInt(start.substring(start.lastIndexOf("/")+1))); 	 
				         startNode.pt =startpt;
				         }
				         else startNode.name=start;
				         //����Ŀ�ĵ�
				      
				         endNode = new MKPlanNode();

				         endNode.name = end;
				         //������· route
				    
				         searchModel.drivingSearch("���", startNode, "���", endNode);
     			}
          }
          if(isend){
        	  dismissDialog(2);
        	  route=res.getPlan(0).getRoute(0);
        	int   distant=route.getDistance();
        		List< Map<String,?>>list=new ArrayList<Map<String,?>>();
        		if(mindisresult<=Maputil.mindis){
        		/*	Map<String,GeoPoint> mappt=new HashMap<String, GeoPoint>();
        			mappt.put("disptsresult", disptsresult1);
        			mappt.put("dispteresult",dispteresult1);
        			list.add(mappt);*/
        			Map<String,String> mappt=new HashMap<String, String>();
        			mappt.put("bystart", bystart);
        			mappt.put("byend",byend);
        			list.add(mappt);
        			Map<String,MKRoute> maproute=new HashMap<String, MKRoute>();
        			maproute.put("routeresult", routeresult);
        			//maproute.put("startrouteresult", startrouteresult1);
        			//maproute.put("endrouteresult1", endrouteresult1);
        			list.add(maproute);
        			Map<String,Line> mapline=new HashMap<String, Line>();
        			mapline.put("resultline", resultline);
        			list.add(mapline);
        			//�����Ǯ��
        			Connect con=new Connect();
        			String money=con.getoilMoney(distant, resultline.getSeating());
        			if(money==null) Toast.makeText(getApplicationContext(), "�����쳣���Ժ�����", Toast.LENGTH_SHORT).show();
        			Map<String,String> dis=new HashMap<String,String>();
        			dis.put("money", money+"");
        			list.add(dis);
        		
        			myapp.setListpipei(list);
        			routeOverlay= new RouteOverlay(MatchActivity.this, mapView);
        			   mapView.getOverlays().clear();
        			   mapView.getOverlays().add(mylay);
        			    
        			    routeOverlay.setData(routeresult);
        			    mapView.getOverlays().add(routeOverlay);
        			    LineOverlay lineoverlay=new LineOverlay();
        			    lineoverlay.setGeoPoint(new GeoPoint[]{
        			    		userstartpt,disptsresult1,dispteresult1,userendpt
        			    });
        			    mapView.getOverlays().add(lineoverlay);
        			  /*  routeOverlay.setData(startrouteresult1);
        			    mapView.getOverlays().add(routeOverlay);
        			    routeOverlay.setData(endrouteresult1);
        			    mapView.getOverlays().add(routeOverlay);*/
        		//	resultline=(Line)( list.get(2).get("resultline"));
        			System.out.println("resultline");
        			lineid.setText("��·"+resultline.getLineid());
        			matchstarttime.setText(resultline.getTime());
        			linemoney.setText("��"+money+"Ԫ");
        			String sql="select * from usertb where userid="+resultline.getUserid();
        			
        			if(!con.getlist(0, "usertb", sql).isEmpty()){
        				User driveruser=(User) (con.getlist(0, "usertb", sql).get(0));
        			uploadFileClass ulf=new uploadFileClass();
        			String result=ulf.downloadfile(driveruser.getPhoto());
        			String photo=null;
        			if(result.equals("���سɹ�")){
        				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        				photo=ulf.getdownloadBitmappath(driveruser.getPhoto());
        				driverhead.setImageBitmap(ulf.getdownloadBitmap(driveruser.getPhoto()));
        			}
        			
        			driveruser.setPhoto(photo);
        		
        			myapp.setDriver(driveruser);
        		
        			
        			drivername.setText(driveruser.getUsername());
        			}
        			lineseinfo.setText(resultline.getOrigin()+"��"+resultline.getDestination());
        			 lineinfo.setVisibility(View.VISIBLE);
        			 lineinfo.setOnClickListener(new OnClickListener() {
        				
        				@Override
        				public void onClick(View v) {
        					// TODO Auto-generated method stub
        					Intent in=new Intent(MatchActivity.this, DriverinfoActivity.class);
        					MyApplication.myLatitudeE6=pt.getLatitudeE6();
        					MyApplication.myLongitudeE6=pt.getLongitudeE6();
        					startActivity(in);
        					  overridePendingTransition(R.anim.guide_inanim,R.anim.guide_out);
        					
        				}
        			});
        			//
        	
        		}
        	  isend=false;
        	  return;
          }
         route = res.getPlan(0).getRoute(0);
         
			System.out.println("---�ڵ�����:"+route.getNumSteps());
			  
		    System.out.println("dealroute���ѭ��");
		    //�ڶ���ѭ����ʼ
			int start2=0;
		
			for(int st=0;st<route.getNumSteps();st++){
			     MKStep step = route.getStep(st);
			   
			     dispts=step.getPoint();
			   
			  startdis=(float) getDistanceFromXtoY(userstartpt.getLatitudeE6(),userstartpt.getLongitudeE6(),dispts.getLatitudeE6(),dispts.getLongitudeE6());
		     if(st==0)  startdisresult=startdis;
			  if(startdis<=startdisresult){
		        	startdisresult=startdis;
		        
		        	disptsresult=dispts;
		        	start2=st;
		        
		        }
			
			}
			
		startdis=(float) getDistanceFromXtoY(userstartpt.getLatitudeE6(),userstartpt.getLongitudeE6(),route.getStart().getLatitudeE6(),route.getStart().getLongitudeE6());
			 if(startdis<=startdisresult){
		        	startdisresult=startdis;
		        
		        	disptsresult=route.getStart();
		        	start2=0;
		        
		        }
			for(int st=start2;st<route.getNumSteps();st++){
			     MKStep step = route.getStep(st);
			    
			     dispte=step.getPoint();
			    
			  enddis=(float) getDistanceFromXtoY(userendpt.getLatitudeE6(),userendpt.getLongitudeE6(),dispte.getLatitudeE6(),dispte.getLongitudeE6());
			 if(st==0) enddisresult=enddis;
			  if(enddis<=enddisresult){
		        	enddisresult=enddis;
		        
		        	dispteresult=dispte;
		        
		        
		        }
			
			}
		enddis=(float) getDistanceFromXtoY(userendpt.getLatitudeE6(),userendpt.getLongitudeE6(),route.getEnd().getLatitudeE6(),route.getEnd().getLongitudeE6());
			  if(enddis<=enddisresult){
		        	enddisresult=enddis;
		        
		        	dispteresult=route.getEnd();
		        
		        
		        }
			 //�����··��
			 if(f==0) {
				 mindisresult=startdisresult+enddisresult;
				
			 }
			 if(f==1) {
				 mindisresult=mindisresult;
				
			 }
			 if(f==2) {
				 mindisresult=mindisresult;
				
			 }
			 if(startdisresult+enddisresult<=mindisresult){
				 mindisresult=startdisresult+enddisresult;
				 routeresult=route;
				  resultline=line;
				startdisresult1=startdisresult;
	             enddisresult1=enddisresult;
	             disptsresult1=disptsresult;
	             dispteresult1=dispteresult;
	           
			 }
			 f++;
			
		    if(f<listline.size()){
	        	  line=listline.get(f);
	        	  downLoadFileSize =f+ 1;  
	              sendMsg(1); 
				
					    //������ʼ�أ���ǰλ�ã�
						  System.out.println("for lineid:"+line.getLineid());
						String start=line.getOrigin();
						String end=line.getDestination();
				
						 startNode = new MKPlanNode();
				         if(start.contains("/")){
				        	 GeoPoint startpt=new GeoPoint(
				        			 Integer.parseInt(start.substring(0, start.lastIndexOf("/")-1))
				        			 , Integer.parseInt(start.substring(start.lastIndexOf("/")+1))); 	 
				         startNode.pt =startpt;
				         }
				         else startNode.name=start;
				         //����Ŀ�ĵ�
				      
				         endNode = new MKPlanNode();

				         endNode.name = end;
				         searchModel.drivingSearch("���", startNode, "���", endNode);
	          }
		    else{
		    	//����˳����롣
		     bystart= Maputil.GetAddrname(disptsresult1);
		     byend=Maputil.GetAddrname(dispteresult1);
	    	 startNode = new MKPlanNode();
		       	 
		         startNode.pt =disptsresult1;
		       
		      
		         endNode = new MKPlanNode();

		         endNode.pt= dispteresult1;
		         isend=true;
		       
		         
		         searchModel.drivingSearch("���", startNode, "���", endNode);
			
	}
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

        public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {

        }

    };

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		
		
		
		
		
		super.onCreate(arg0);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.matchxml);
		searchmatch=(Button) findViewById(R.id.searchmatch);
		mystartet=(EditText) findViewById(R.id.mystartet);
		myendet=(EditText) findViewById(R.id.myendet);
		lineid=(TextView) findViewById(R.id.lineid);
		matchstarttime=(TextView) findViewById(R.id.matchstarttime);
		drivername=(TextView) findViewById(R.id.drivername);
		lineseinfo=(TextView) findViewById(R.id.lineseinfo);
		linemoney=(TextView) findViewById(R.id.linemoney);
		driverhead=(ImageView)findViewById(R.id.driverhead);
		 lineinfo=(RelativeLayout) findViewById(R.id.lineinfo);
		 
		 lineinfo.setVisibility(View.GONE);
		  mapView = (MapView) this.findViewById(R.id.bmapsView);
		gc = new Geocoder(this,Locale.CHINA);
	      locationListener = new LocationListener(){
	  		
			  
	  		
	             @Override
	
	             public void onLocationChanged(Location location) {

	                 if (location != null){

	                     //����GEO�������겢�ڵ�ͼ�϶�λ���������ʾ�ĵص�
	
	                      pt = new GeoPoint((int)(location.getLatitude()*1e6),

	                             (int)(location.getLongitude()*1e6));
	                    
	     		        mylay.setGeoPoint(pt);
	     		         //����ǰλ�õĲ���ӵ���ͼ�ײ���
	     		mapView.getOverlays().add(mylay);
	     		
//	                   System.out.println("---"+location.getLatitude() +":"+location.getLongitude());

	                     mapView.getController().animateTo(pt);

	                 }
	
	             }
	
	         };
	  
		  mMapManager = new BMapManager(getApplicationContext());
	      mMapManager.init(Maputil.mapkey, new MyGeneralListener());
	
	   super.initMapActivity(mMapManager);
	   mMapManager.start();
	   searchModel = new MKSearch();
     
	      //����·�߲���Ϊ��̾���

	      searchModel.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
	      searchModel.init(mMapManager, mymksearch);
	
		searchmatch.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			startname=mystartet.getText().toString().trim();
			 endname=myendet.getText().toString().trim();
			  // ��ѯ�þ�γ��ֵ����Ӧ�ĵ�ַλ����Ϣ  
		
			//getGeoPointBystr(startname);
			if(startname.equals("")||endname.equals(""))
				Toast.makeText(getApplicationContext(),"��ַ����Ϊ��", Toast.LENGTH_SHORT).show();
			else{	
		/*	String city="���";
			addresstype=0;
			searchModel.geocode(startname, city);*/
			//Maputil.GetAddrname(startname);
				startNode = new MKPlanNode();
				 startNode.name=startname;
		         //����Ŀ�ĵ�
		      
		         endNode = new MKPlanNode();

		         endNode.name = endname;
		        isstart=true;
		         
		         searchModel.drivingSearch("���", startNode, "���", endNode);
		
	
			}
			}
		});
	}
	ProgressBar pb;  
    TextView tv;
    int   fileSize;  
    int   downLoadFileSize;  
    String fileEx,fileNa,filename;  
    
    public Dialog onCreateDialog(int id){
    	System.out.println("duihuakuang");
    	 Dialog dialog=null;
    	 View corvertview=null;
    	 dialog=new Dialog(MatchActivity.this,R.style.dialog);
    	 
    
     LayoutInflater flater = LayoutInflater.from(this);
     corvertview= flater.inflate(R.layout.matchproxml, null);
		pb=(ProgressBar) corvertview.findViewById(R.id.match_pb);
		tv=(TextView) corvertview.findViewById(R.id.matchprotxt);
	
		 dialog.setContentView( corvertview);
 		dialog.setCanceledOnTouchOutside(false);
 		return dialog;
    }
    private Handler handler = new Handler()  
      {  
        @Override  
        public void handleMessage(Message msg)  
        {//����һ��Handler�����ڴ��������߳���UI��ͨѶ  
          if (!Thread.currentThread().isInterrupted())  
          {  
            switch (msg.what)  
            {  
              case 0:  
                pb.setMax(fileSize);  
              case 1:  
                pb.setProgress(downLoadFileSize);  
               
                tv.setText(downLoadFileSize +" /"+ fileSize);  
                break;  
            
            }  
          }  
          super.handleMessage(msg);  
        }  
      };  
      private void sendMsg(int flag)  
      {  
          Message msg = new Message();  
          msg.what = flag;  
          handler.sendMessage(msg);  
      }      
	public static double getDistanceFromXtoY(double lat_a, double lng_a,
			   double lat_b, double lng_b)
			 {
			  double pk = (double) (180 / 3.14159);
			 
			  double a1 = lat_a/1e6 / pk;
			  double a2 = lng_a /1e6/ pk;
			  double b1 = lat_b /1e6/ pk;
			  double b2 = lng_b/1e6 / pk;
			 
			  double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
			  double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
			  double t3 = Math.sin(a1) * Math.sin(b1);
			  double tt = Math.acos(t1 + t2 + t3);
			 
			  return  tt*6366000;
			 }
	/*public GeoPoint getGeoPointBystr(String str) {
		GeoPoint gpGeoPoint = null;
		if (str!=null) {
		
		List<Address> addressList = null;
		try {
		addressList = gc.getFromLocationName(str, 1);
		if (!addressList.isEmpty()) {
		Address address_temp = addressList.get(0);
		//���㾭γ��
		double Latitude=address_temp.getLatitude()*1E6;
		double Longitude=address_temp.getLongitude()*1E6;
		//����GeoPoint
		gpGeoPoint = new GeoPoint((int)Latitude, (int)Longitude);
		}
		else {
			addressList = gc.getFromLocationName("���"+str, 1);
			Address address_temp = addressList.get(0);
			//���㾭γ��
			double Latitude=address_temp.getLatitude()*1E6;
			double Longitude=address_temp.getLongitude()*1E6;
			//����GeoPoint
			gpGeoPoint = new GeoPoint((int)Latitude, (int)Longitude);
		}
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
	
		
		return gpGeoPoint;
		}*/

		
		public void initdate(){
			Connect con=new Connect();
			myapp=(MyApplication) MatchActivity.this.getApplication();
			//δ���ĺ��ѿ���
			String sql="select * from linetb where state!=-1 and seatleft>0 and userid!="+myapp.getUser().getUserid();
			//�õ���·����
			listline=(List<Line>) con.getlist(0, "linetb", sql);
			 //��ʼ������ģ��
		  	
		   
		   
		}
	
		
		
		// �����¼���������������ͨ�������������Ȩ��֤�����


		

	class MyGeneralListener implements MKGeneralListener {

        @Override

        public void onGetNetworkState(int iError) {

            Log.d("MyGeneralListener", "onGetNetworkState error is "+ iError);

            Toast.makeText(getApplicationContext(), "���������������",

                    Toast.LENGTH_LONG).show();

        }



        @Override

        public void onGetPermissionState(int iError) {

            Log.d("MyGeneralListener", "onGetPermissionState error is "+ iError);

            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {

                // ��ȨKey����

                Toast.makeText(getApplicationContext(),

                        "����BMapApiDemoApp.java�ļ�������ȷ����ȨKey��",

                        Toast.LENGTH_LONG).show();

            }

        }
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

 
	   // ���ؼ�  
    private long exitTime = 0;  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == event.KEYCODE_BACK) {  
           
               
                finish();  
               
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
}
	


