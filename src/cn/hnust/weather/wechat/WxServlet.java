package cn.hnust.weather.wechat;



import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.HashMap;

import java.util.List;

import java.util.Map;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;



import org.dom4j.Document;

import org.dom4j.Element;

import org.dom4j.io.SAXReader;



public class WxServlet extends HttpServlet{



	//token

	private final String token = "hnust";

	 

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    System.out.println("开始签名校验");

	    String signature = request.getParameter("signature");

	    String timestamp = request.getParameter("timestamp");

	    String nonce = request.getParameter("nonce");

	    String echostr = request.getParameter("echostr");



	    ArrayList<String> array = new ArrayList<String>();

	    array.add(signature);

	    array.add(timestamp);

	    array.add(nonce);

	 

	    //排序

	    String sortString = sort(token, timestamp, nonce);

	    //加密

	    String mytoken = SHA1(sortString);

	    //校验签名

	    if (mytoken != null && mytoken != "" && mytoken.equals(signature)) {

	        System.out.println("校验签名通过");

	        response.getWriter().println(echostr); 

	    } else {

	        System.out.println("校验签名失败");

	    }

	}

	 

	 

	/**

	 * 排序方法

	 * @param token

	 * @param timestamp

	 * @param nonce

	 * @return

	 */

	public static String sort(String token, String timestamp, String nonce) {

	    String[] strArray = { token, timestamp, nonce };

	    Arrays.sort(strArray);
	    /*String s1=strArray[0];
		 String s2=strArray[1];
		 String s3=strArray[2];
		 String t;
		 
		 if(s1.compareTo(s2)>0){
			 t=s1;
			 s1=s2;
			 s2=t;
		 }
		 if(s2.compareTo(s3)>0){
			 t=s2;
			 s2=s3;
			 s3=t;
		 }
		 if(s1.compareTo(s3)>0){
			 t=s1;
			 s1=s3;
			 s3=t;
		 }
		strArray[0] = s1;
		strArray[1] = s2;
		strArray[2] = s3;
	    */
	    String s = strArray[0]+strArray[1]+strArray[2];
	 
	    return s;

	}

	

	public static String SHA1(String decript) {

        try {

            MessageDigest digest = MessageDigest

                    .getInstance("SHA-1");

            digest.update(decript.getBytes());

            byte messageDigest[] = digest.digest();

            // Create Hex String

            String hexString = "";

            // 

            for (int i = 0; i < messageDigest.length; i++) {

                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);

                if (shaHex.length() < 2) {

                    hexString = hexString + "0";
                    
                }

                hexString = hexString + shaHex;

            }

            return hexString.toString();

 

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        }

        return "";

    }

	public static Map parseXml(HttpServletRequest request) throws Exception {

        // 

        Map map = new HashMap();

 

        // 

        InputStream inputStream = request.getInputStream();

        /*

         * Premature end of file. Nested exception:

         * Premature end of file String requestBody =

         * inputStream2String(inputStream); System.out.println(requestBody);

         */

        // 

        SAXReader reader = new SAXReader();

        Document document = reader.read(inputStream);

        // 

        Element root = document.getRootElement();

        // 

        List<Element> elementList = root.elements();

 

        // 

        for (Element e : elementList)

            map.put(e.getName(), e.getText());

 

        // 

        inputStream.close();

        inputStream = null;

 

        return map;

    }

 

    private static String inputStream2String(InputStream is) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int i = -1;

        while ((i = is.read()) != -1) {

            baos.write(i);

        }

        return baos.toString();

    }

    

	public static void main(String[] args) {

		

		

	}



	//

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		

		request.setCharacterEncoding("UTF-8");

		response.setCharacterEncoding("UTF-8");

		System.out.println("请求进入");

		String responseMessage = "";

		try {

			Map<String,String> map = MessageHandlerUtil.parseXml(request);

			System.out.println("开始构造消息");

			//responseMessage = MessageHandlerUtil.buildXml(map);

			responseMessage = MessageHandlerUtil.buildResponseMessage(map);

			System.out.println(responseMessage);

			if(responseMessage.equals("")){

				responseMessage = "未正确响应";

			}

		} catch (Exception e) {

			e.printStackTrace();

			System.out.println("发生异常"+ e.getMessage());

		}

		response.getWriter().println(responseMessage);		 

    }

	

	

	

	 

	 

}


