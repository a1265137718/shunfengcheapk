package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.GeoPoint;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.format.Time;

  public final class Maputil {
  public static String mapkey="32256FB8D1D708008D8DF2FD296D5AD3E0DECE75";
  public static float mindis=5000;
 // public static String url="http://192.168.56.1:8080/shenfengcheweb/";
  public static String url="http://192.168.148.1:8080/shenfengcheweb/";
  public static String getnowTime(){
	 Date time=new Date();  
     
  
     return String.format("%tF", time)+" "+String.format("%tT", time);
  }
  //��ͼƬԲ�ǻ�
  public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) { 
	  Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888); 
	  Canvas canvas = new Canvas(output); 
	  final int color = 0xff424242; 
	  final Paint paint = new Paint(); 
	  final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
	  final RectF rectF = new RectF(rect); 
	  final float roundPx = pixels; 
	  paint.setAntiAlias(true); 
	  canvas.drawARGB(0, 0, 0, 0); 
	  paint.setColor(color); 
	  canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
	  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
	  canvas.drawBitmap(bitmap, rect, rect, paint); 
	  return output; 
	  } 
  /*��ַ���������ݵ�ַ��ȡ����
	http://api.map.baidu.com/geocoder?address=��ַ&output=�����ʽ����&key=�û���Կ&city=������
    ���ؽ������ַ�����Ľ����

 {status: '�ַ���״̬����', ȡֵ���£�
 //OK �ɹ�
 INVILID_KEY �Ƿ���Կ 
 INVALID_PARAMETERS �Ƿ���������������ʱ�������
 result: {    
 location: {
 lat: γ�ȣ���ֵ��
 lng: ���ȣ���ֵ
 },
 precise:��λ�õĸ�����Ϣ���Ƿ�ȷ���ҡ���1Ϊ��ȷ���ң�0Ϊ����ȷ���ң�,
 confidence: ���Ŷ�,
 level:'����'
 },
 }
	���ַ���������������ȡ��ַ
	http://api.map.baidu.com/geocoder?location=γ��,����&output=�����ʽ����&key=�û���Կ
	���ؽ��������ַ�����Ľ����

 {status: '�ַ���״̬����', ȡֵ���£�
 //OK �ɹ�
 INVILID_KEY �Ƿ���Կ   
 INVALID_PARAMETERS �Ƿ���������������ʱ�������
 result: {    
 location: {
 lat: γ�ȣ���ֵ��
 lng: ���ȣ���ֵ
 },
 formatted_address: ����ϸ��ַ������,
 business: '��Χ��Ȧ',
 addressComponent:{
 city:���������ơ�,
 district: ���������ơ�,
 province:��ʡ�����ơ�, 
 street: ���ֵ����ơ�,
 streetNumber: '���ƺ���' 
 },
 cityCode: '���д���'
 }
 }
	*
	*/
  
//�Ѿ�γ��ת���ɵ�������
	public static String GetAddrname(GeoPoint  pt) {
		 
		
	         String place=null;
		
			StringBuilder stringBuilder = new StringBuilder(); 
			InputStream	input = null;
			try{
				String urltxt="http://api.map.baidu.com/geocoder?location="+pt.getLatitudeE6()/1e6+
				","+pt.getLongitudeE6()/1e6+"&output=json&key="+Maputil.mapkey;
			URL url = new URL(urltxt);
			input = url.openStream();
			Scanner scan = new Scanner(input);
			while (scan.hasNext()) {
				stringBuilder.append(scan.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				input.close() ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			JSONObject allData = new JSONObject(stringBuilder.toString());
			allData = new JSONObject(allData.getString("result")) ;
			place=allData.getString("formatted_address") ;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	String url = String
		.format(
		"http://ditu.google.cn/maps/geo?key="+Maputil.mapkey+"&q=%s,%s",
		latitude, longitude);
		HttpGet httpGet = new HttpGet(url);
		        HttpClient client = new DefaultHttpClient(); 
		        HttpResponse response; 
		        StringBuilder stringBuilder = new StringBuilder(); 
		 
		        try { 
		            response = client.execute(httpGet); 
		            HttpEntity entity = response.getEntity(); 
		            InputStream stream = entity.getContent(); 
		            int b; 
		            while ((b = stream.read()) != -1) { 
		                stringBuilder.append((char) b); 
		            }
		            HttpEntity entity = response.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(entity
		.getContent()));
		String result = br.readLine();
		while (result != null) {
		stringBuilder.append(result);
		result = br.readLine();
		}
		        } catch (ClientProtocolException e) { 
		        } catch (IOException e) { 
		        } 
		place= stringBuilder.toString();
		    
		        try { 
		        	  JSONObject     jsonObject = new JSONObject(stringBuilder.toString()); 
		        	
		        } catch (JSONException e) { 
		            e.printStackTrace(); 
		        }*/
		
		        return place; 
		

}
}
