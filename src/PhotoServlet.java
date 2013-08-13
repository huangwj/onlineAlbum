import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import com.album.AlbumDAOBean;
import com.javabean.AlbumPhoto;

public class PhotoServlet extends HttpServlet {
	//处理POST请求
	public void doPost(HttpServletRequest request,HttpServletResponse response) 
    								throws ServletException,IOException {
    	response.setContentType("image/jpeg");
    	
		String photoId=request.getParameter("photoId");
		byte[] bi=null;
		OutputStream os=null;
		//获取图片
    	Vector vec=AlbumDAOBean.getPhoto(photoId);
    	
    	for(Enumeration enu=vec.elements();enu.hasMoreElements();) {
    		
    		AlbumPhoto ap=(AlbumPhoto)enu.nextElement();
    		bi=ap.getPhotoByte();
    	}
		try {//输出图片
			os=response.getOutputStream();
			os.write(bi);
			os.flush();
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			os.close();
		}
    }
	//处理GET请求
    public void doGet(HttpServletRequest request,HttpServletResponse response) 
    								throws ServletException,IOException {
    	this.doPost(request,response);
    }
}