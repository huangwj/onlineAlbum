import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.jspsmart.upload.*;
import com.album.AlbumDAOBean;

public class AlbumServlet extends HttpServlet {
	
	private ServletConfig config;
	//��ʼ��Servlet
	public final void init(ServletConfig config) throws ServletException {
		this.config = config;
	}
	//����POST����
	public void doPost(HttpServletRequest request,HttpServletResponse response) 
    								throws ServletException,IOException {
    	request.setCharacterEncoding("GBK");
    	response.setCharacterEncoding("GBK");
    	//��ȡPrintWriter����������ͻ��������Ϣ
    	PrintWriter out=response.getWriter();
    	//��ȡHttpSession����
    	HttpSession session=request.getSession();
    	//���session����Ϊ���򷵻�
    	if(session==null) {
    		return;
    	}
    	//����session����Чʱ��Ϊ10����
    	session.setMaxInactiveInterval(60*10);
    	String action=request.getParameter("action");
    	//�û���½
    	if(action.equals("userLogin")) {
    		//����û�ִ�е��ǵ�½������ִ�д˶δ���
    		//��ȡ�û��ύ���û���������
    		String userName=request.getParameter("username").trim();
    		String passWord=request.getParameter("password").trim();
    		//����JavaBean��֤�û���������
    		boolean b=AlbumDAOBean.userLogIn(userName,passWord);
    		
    		if(b) {
    			//�����½�ɹ����û���Ϣ����session������
    			//����ת����ҳ��ʾ
    			session.setAttribute("userName",userName);
    			Vector vec=AlbumDAOBean.getAlbumInfo(userName);
    			String uaId=AlbumDAOBean.getUA_id(userName);
    			session.setAttribute("uaId",uaId);
    			session.setAttribute("userAlbum",vec);
    			this.forword(request,response,"/albumindex.jsp");
    		} else {//������ת�Դ���ҳ
    			this.forword(request,response,"/error.jsp?msg=�û������������");
    		}
    	} else if (action.equals("addImageSubmit")) {
    		//����û�ִ�е����ϴ���Ƭ������ִ�д˶δ���
    		//��ȡ�û��ύ�Ĳ���
    		String albumId=request.getParameter("albumId");
    		String fileName=request.getParameter("path").trim();
    		String photoName=request.getParameter("photoName");
    		String stemp=Long.valueOf(System.currentTimeMillis()).toString();
    		String photoId=stemp.substring(6,stemp.length());
    		//����JavaBean��ͼƬ�������ݿ���
			boolean b=AlbumDAOBean.insertImage(albumId,photoId,photoName,fileName);
			if(b) {
				this.forword(request,response,"/myalbum.jsp");
			} else {
				this.forword(request,response,"/error.jsp?msg=�ϴ�ʧ�ܣ������ԣ�");
			}
    	} else if (action.equals("uploadPhoto")) {
    		this.forword(request,response,"/uploadPhoto.jsp");
    	} else if(action.equals("myAlbum")) {
    		
    		String userName=(String)session.getAttribute("userName");
    		Vector vec=AlbumDAOBean.getAlbumInfo(userName);
    		session.setAttribute("userAlbum",vec);
    		this.forword(request,response,"/myalbum.jsp");
    	} else if(action.equals("photo")) {
    		//��ȡ�û��ύ�Ĳ���
    		String albumId=request.getParameter("albumId");
    		//����JavaBean�����ݿ���ȡ��Ϣ
    		Vector vec=AlbumDAOBean.getAlbumPhotoInfo(albumId);
    		//���������request������
    		request.setAttribute("photoVec",vec);
    		this.forword(request,response,"/photo.jsp");
    	} else if(action.equals("newAlbum")) {
    		HashMap hm=AlbumDAOBean.getAlbumTyteInfo();
    		request.setAttribute("albumTyteInfo",hm);
    		this.forword(request,response,"/newAlbum.jsp");
    	} else if(action.equals("newAlbumSubmit")) {
    		//�½����
    		String albumName=request.getParameter("albumName");
    		if(albumName==null||albumName.equals("")) {
    			this.forword(request,response,"/error.jsp?msg=������Ʋ���Ϊ�գ�");
    			return;
    		}
    		
    		String uaId=(String)session.getAttribute("uaId");
    		String stemp=Long.valueOf(System.currentTimeMillis()).toString();
    		String albumId=stemp.substring(5,stemp.length());
    		String userName=(String)session.getAttribute("userName");
    		String typeId=request.getParameter("typeId");
    		//���½������Ϣ�������ݿ�
    		boolean b=AlbumDAOBean.newAlbum(uaId,albumId,typeId,albumName);
    		if(b) {
    			//����½��ɹ������������Ϣ��
    			//���ݿ���ȡ�������û���ʾ
    			Vector vec=AlbumDAOBean.getAlbumInfo(userName);
    			session.setAttribute("userAlbum",vec);
    			this.forword(request,response,"/myalbum.jsp");
    		} else {
    			this.forword(request,response,"/error.jsp?msg=�½�������");
    		}
    	} else if(action.equals("deleteAlbum")) {
    		//����û�ִ�е���ɾ����������ִ�д˶δ���
    		String userName=(String)session.getAttribute("userName");
    		Vector vec=AlbumDAOBean.getAlbumInfo(userName);
    		request.setAttribute("albumInfo",vec);
    		this.forword(request,response,"/deleteAlbum.jsp");
    	}  else if(action.equals("del")) {
    		//ִ��ɾ��
    		String albumId=request.getParameter("albumId");
    		//����JavaBean�����ݿ�ɾ����Ϣ
    		boolean b=AlbumDAOBean.deleteAlbum(albumId);
    		if(b) {
    			//���ɾ���ɹ���������Ϣ��ȡ�������û���ʾ
    			String userName=(String)session.getAttribute("userName");
	    		Vector vec=AlbumDAOBean.getAlbumInfo(userName);
	    		request.setAttribute("albumInfo",vec);
	    		this.forword(request,response,"/deleteAlbum.jsp");
    		} else {
    			this.forword(request,response,"/error.jsp?msg=ɾ��ʧ�ܣ�");
    		}
    	} else if(action.equals("photo")) {
    		
    		String albumId=request.getParameter("albumId");
    		Vector vec=AlbumDAOBean.getPhoto(albumId);
    		request.setAttribute("photoVec",vec);
    		this.forword(request,response,"/photo.jsp");
    	} else if(action.equals("delPhoto")) {
    		//����û�ִ�е���ɾ����Ƭ������ִ�д˶δ���
    		String photoId=request.getParameter("photoId");
    		//����JavaBean�����ݿ���ɾ����Ƭ
    		boolean b=AlbumDAOBean.deletePhoto(photoId);
    		if(b) {//���ɾ���ɹ����������ݴ����ݿ���
    				//��ȡ�������ظ��û�
    			String albumId=request.getParameter("albumId");
    			Vector vec=AlbumDAOBean.getAlbumPhotoInfo(albumId);
    			request.setAttribute("photoVec",vec);
    			this.forword(request,response,"/photo.jsp");
    		} else {
    			this.forword(request,response,"/error.jsp?msg=ɾ��ʧ�ܣ�");
    		}
    	} else if(action.equals("LogOut")) {
    		//�˳���½
    		session.invalidate();
    		//������ҳ���û����µ�½
    		this.forword(request,response,"/index.jsp");
    	}  
    }
    //����GET����
    public void doGet(HttpServletRequest request,HttpServletResponse response) 
    								throws ServletException,IOException {
    	this.doPost(request,response);
    }
    //��ת
    private void forword(HttpServletRequest request,HttpServletResponse response,String url) 
    								throws ServletException,IOException {
    	RequestDispatcher dispatcher=request.getRequestDispatcher(url);
    	dispatcher.forward(request,response);
    }
}