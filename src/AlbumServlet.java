import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.jspsmart.upload.*;
import com.album.AlbumDAOBean;

public class AlbumServlet extends HttpServlet {
	
	private ServletConfig config;
	//初始化Servlet
	public final void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
	//处理POST请求
	public void doPost(HttpServletRequest request,HttpServletResponse response) 
    								throws ServletException,IOException {
    	request.setCharacterEncoding("GBK");
    	response.setCharacterEncoding("GBK");
    	//获取PrintWriter对象，用于向客户端输出信息
    	PrintWriter out=response.getWriter();
    	//获取HttpSession对象
    	HttpSession session=request.getSession();
    	//如果session对象为空则返回
    	if(session==null) {
    		return;
    	}
    	//设置session的有效时间为10分钟
    	session.setMaxInactiveInterval(60*10);
    	String action=request.getParameter("action");
    	//用户登陆
    	if(action.equals("userLogin")) {
    		//如果用户执行的是登陆操作则执行此段代码
    		//收取用户提交的用户名和密码
    		String userName=request.getParameter("username").trim();
    		String passWord=request.getParameter("password").trim();
    		//调用JavaBean验证用户名和密码
    		boolean b=AlbumDAOBean.userLogIn(userName,passWord);
    		
    		if(b) {
    			//如果登陆成功将用户信息存入session对象中
    			//并跳转自首页显示
    			session.setAttribute("userName",userName);
    			Vector vec=AlbumDAOBean.getAlbumInfo(userName);
    			String uaId=AlbumDAOBean.getUA_id(userName);
    			session.setAttribute("uaId",uaId);
    			session.setAttribute("userAlbum",vec);
    			this.forword(request,response,"/albumindex.jsp");
    		} else {//否则跳转自错误页
    			this.forword(request,response,"/error.jsp?msg=用户名或密码错误！");
    		}
    	} else if (action.equals("addImageSubmit")) {
    		//如果用户执行的是上传照片操作则执行此段代码
    		//收取用户提交的参数
    		String albumId=request.getParameter("albumId");
    		String fileName=request.getParameter("path").trim();
    		String photoName=request.getParameter("photoName");
    		String stemp=Long.valueOf(System.currentTimeMillis()).toString();
    		String photoId=stemp.substring(6,stemp.length());
    		//调用JavaBean将图片存入数据库中
			boolean b=AlbumDAOBean.insertImage(albumId,photoId,photoName,fileName);
			if(b) {
				this.forword(request,response,"/myalbum.jsp");
			} else {
				this.forword(request,response,"/error.jsp?msg=上传失败，请重试！");
			}
    	} else if (action.equals("uploadPhoto")) {
    		this.forword(request,response,"/uploadPhoto.jsp");
    	} else if(action.equals("myAlbum")) {
    		
    		String userName=(String)session.getAttribute("userName");
    		Vector vec=AlbumDAOBean.getAlbumInfo(userName);
    		session.setAttribute("userAlbum",vec);
    		this.forword(request,response,"/myalbum.jsp");
    	} else if(action.equals("photo")) {
    		//收取用户提交的参数
    		String albumId=request.getParameter("albumId");
    		//调用JavaBean从数据库提取信息
    		Vector vec=AlbumDAOBean.getAlbumPhotoInfo(albumId);
    		//将结果存入request对象中
    		request.setAttribute("photoVec",vec);
    		this.forword(request,response,"/photo.jsp");
    	} else if(action.equals("newAlbum")) {
    		HashMap hm=AlbumDAOBean.getAlbumTyteInfo();
    		request.setAttribute("albumTyteInfo",hm);
    		this.forword(request,response,"/newAlbum.jsp");
    	} else if(action.equals("newAlbumSubmit")) {
    		//新建相册
    		String albumName=request.getParameter("albumName");
    		if(albumName==null||albumName.equals("")) {
    			this.forword(request,response,"/error.jsp?msg=相册名称不能为空！");
    			return;
    		}
    		
    		String uaId=(String)session.getAttribute("uaId");
    		String stemp=Long.valueOf(System.currentTimeMillis()).toString();
    		String albumId=stemp.substring(5,stemp.length());
    		String userName=(String)session.getAttribute("userName");
    		String typeId=request.getParameter("typeId");
    		//将新建相册信息插入数据库
    		boolean b=AlbumDAOBean.newAlbum(uaId,albumId,typeId,albumName);
    		if(b) {
    			//如果新建成功则将现有相册信息从
    			//数据库提取出来给用户显示
    			Vector vec=AlbumDAOBean.getAlbumInfo(userName);
    			session.setAttribute("userAlbum",vec);
    			this.forword(request,response,"/myalbum.jsp");
    		} else {
    			this.forword(request,response,"/error.jsp?msg=新建相册错误！");
    		}
    	} else if(action.equals("deleteAlbum")) {
    		//如果用户执行的是删除相册操作则执行此段代码
    		String userName=(String)session.getAttribute("userName");
    		Vector vec=AlbumDAOBean.getAlbumInfo(userName);
    		request.setAttribute("albumInfo",vec);
    		this.forword(request,response,"/deleteAlbum.jsp");
    	}  else if(action.equals("del")) {
    		//执行删除
    		String albumId=request.getParameter("albumId");
    		//调用JavaBean从数据库删除信息
    		boolean b=AlbumDAOBean.deleteAlbum(albumId);
    		if(b) {
    			//如果删除成功将现有信息提取出来给用户显示
    			String userName=(String)session.getAttribute("userName");
	    		Vector vec=AlbumDAOBean.getAlbumInfo(userName);
	    		request.setAttribute("albumInfo",vec);
	    		this.forword(request,response,"/deleteAlbum.jsp");
    		} else {
    			this.forword(request,response,"/error.jsp?msg=删除失败！");
    		}
    	} else if(action.equals("photo")) {
    		
    		String albumId=request.getParameter("albumId");
    		Vector vec=AlbumDAOBean.getPhoto(albumId);
    		request.setAttribute("photoVec",vec);
    		this.forword(request,response,"/photo.jsp");
    	} else if(action.equals("delPhoto")) {
    		//如果用户执行的是删除照片操作则执行此段代码
    		String photoId=request.getParameter("photoId");
    		//调用JavaBean从数据库中删除照片
    		boolean b=AlbumDAOBean.deletePhoto(photoId);
    		if(b) {//如果删除成功则将现有数据从数据库中
    				//提取出来返回给用户
    			String albumId=request.getParameter("albumId");
    			Vector vec=AlbumDAOBean.getAlbumPhotoInfo(albumId);
    			request.setAttribute("photoVec",vec);
    			this.forword(request,response,"/photo.jsp");
    		} else {
    			this.forword(request,response,"/error.jsp?msg=删除失败！");
    		}
    	} else if(action.equals("LogOut")) {
    		//退出登陆
    		session.invalidate();
    		//返回首页让用户重新登陆
    		this.forword(request,response,"/index.jsp");
    	}  
    }
    //处理GET请求
    public void doGet(HttpServletRequest request,HttpServletResponse response) 
    								throws ServletException,IOException {
    	this.doPost(request,response);
    }
    //跳转
    private void forword(HttpServletRequest request,HttpServletResponse response,String url) 
    								throws ServletException,IOException {
    	RequestDispatcher dispatcher=request.getRequestDispatcher(url);
    	dispatcher.forward(request,response);
    }
}