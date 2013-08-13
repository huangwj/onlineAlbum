package com.album;

import java.io.*;
import java.sql.*;
import java.sql.Connection;
import javax.sql.*;
import java.util.*;
import javax.naming.*;
import com.javabean.AlbumPhoto;
import com.javabean.AlbumInfo;
import com.javabean.Photo;

public class AlbumDAOBean {
	///onlinealbumJNDI��JNDI����
	private static final String JNDI_STR=
								"java:comp/env/onlinealbumJNDI";
	private static Context ctx;
	private static Connection con;
	private static ResultSet rs;
	private static PreparedStatement userLogInPS;
	private static PreparedStatement photoPS;
	private static PreparedStatement insertPhotoPS;
	private static PreparedStatement UAIdPS;
	private static PreparedStatement newAlbumPS;
	private static PreparedStatement albumInfoPS;
	private static PreparedStatement delAlbumPS;
	private static PreparedStatement delAlbumDetailPS;
	private static PreparedStatement delPhotoPS;
	private static PreparedStatement albumTypeInfoPS;
	private static PreparedStatement albumPhotoInfoPS;
	
	static {//Ԥ����SQL
		try {
			//ȡ�����ݿ�����
			con=AlbumDAOBean.getConnection();
			//�û���½
			userLogInPS=con.prepareStatement(
						"SELECT user_pwd FROM users WHERE user_name=?");
			//�����ݿ���ȡ��Ƭ
			photoPS=con.prepareStatement(
				"SELECT photo FROM photos WHERE photo_id=?");
			//�ϴ���Ƭ
			insertPhotoPS=con.prepareStatement(
				"INSERT INTO photos(album_id,photo_id,photo_name,photo) VALU"
				+"ES(?,?,?,?)");
			//�û�������ϵ
			UAIdPS=con.prepareStatement(
				"SELECT UA_id FROM users WHERE user_name=?");
			//�½����
			newAlbumPS=con.prepareStatement(
				"INSERT INTO user_album(UA_id,album_id,album_name,album_type"
				+"_id,new_time) VALUES(?,?,?,?,sysdate())");
			//�����ϸ��Ϣ
			albumInfoPS=con.prepareStatement(
				"SELECT ua.album_id,ua.album_name,ua.new_time,(SELECT at.alb"
				+"um_type_name FROM album_type at WHERE at.album_type_id=ua."
				+"album_type_id),(SELECT COUNT(p.photo_id) FROM photos p WHE"
				+"RE p.album_id=ua.album_id),(SELECT at1.album_type_id FROM "
				+"album_type at1 WHERE at1.album_type_id=ua.album_type_id) F"
				+"ROM user_album ua,users u WHERE u.user_name=?");
			//ɾ�������û������Ƭ
			delAlbumDetailPS=con.prepareStatement(
				"DELETE FROM photos WHERE album_id=?");
			//ɾ�����
			delAlbumPS=con.prepareStatement(
				"DELETE FROM user_album WHERE album_id=?");
			//ɾ����Ƭ
			delPhotoPS=con.prepareStatement(
				"DELETE FROM photos WHERE photo_id=?");
			//���������Ϣ
			albumTypeInfoPS=con.prepareStatement(
				"SELECT album_type_id,album_type_name FROM album_type");
			//�����Ƭ��
			albumPhotoInfoPS=con.prepareStatement(
				"SELECT photo_id,photo_name,album_id FROM photos WHERE"
				+" album_id=?");
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	//��������userLogIn
	//���ܽ��ܣ��û���½��֤
	//����˵����userName:�û��� passWord:����
	//�� �� ֵ��false/true
	//�쳣:java.sql.SQLException
	public static boolean userLogIn(String userName,String passWord) {
		
		boolean flag=false;
		String pwd=null;
		try {
			userLogInPS.setString(1,userName);
			rs=userLogInPS.executeQuery();
			while(rs.next()) {
				pwd=rs.getString(1).trim();
			}
			//�ж������Ƿ���ȷ
			if(passWord.equals(pwd)) {
				flag=true;
			}
		} catch (SQLException se) {
			se.printStackTrace();
			flag=false;
		} finally {//�رս����
			close(rs);
		}
		return flag;
	}
	//��������getAlbumInfo
	//���ܽ��ܣ�ȡ���û������Ϣ
	//����˵����userName:�û���
	//�� �� ֵ��java.util.Vector
	//�쳣:java.sql.SQLException
	//	   java.io.UnsupportedEncodingException
	public static Vector getAlbumInfo(String userName) {
		Vector vec=new Vector();
		try {
			albumInfoPS.setString(1,userName);
			rs=albumInfoPS.executeQuery();
			while(rs.next()) {
				//com.javabean.AlbumInfo
				AlbumInfo ai=new AlbumInfo();
				ai.setAlbumId(rs.getString(1));
				ai.setAlbumName(
					//��MYSQL����ISO-8859-1ת���ɹ���2312��gb2312��
					new String(rs.getString(2).getBytes(
						"gbk"),"gb2312"));
				ai.setNewTime(rs.getString(3));
				ai.setAlbumType(
					new String(rs.getString(4).getBytes(
						"gbk"),"gb2312"));
				ai.setPhotoCount(rs.getInt(5));
				ai.setAlbumTypeId(rs.getString(6));
				vec.add(ai);
				ai=null;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} finally {//�رս����
			close(rs);
		}
		return vec;
	}
	//��������getAlbumTyteInfo
	//���ܽ��ܣ�ȡ�����������Ϣ
	//����˵����û�в���
	//�� �� ֵ��java.util.HashMap
	//�쳣:java.sql.SQLException
	//	   java.io.UnsupportedEncodingException
	public static HashMap getAlbumTyteInfo() {
		HashMap hm=new HashMap();
		try {
			rs=albumTypeInfoPS.executeQuery();
			while(rs.next()) {
				hm.put(rs.getString(1),
				//��MYSQL����ISO-8859-1ת���ɹ���2312��gb2312��
					new String(rs.getString(2).getBytes("gbk"),"gb2312"));
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} finally {//�رս����
			close(rs);
		}
		return hm;
	}
	//��������deleteAlbum
	//���ܽ��ܣ�ɾ���û����
	//����˵����albumId:�����
	//�� �� ֵ��false/true
	//�쳣:java.sql.SQLException
	public static boolean deleteAlbum(String albumId) {
		boolean b=false;
		try {
			delAlbumDetailPS.setString(1,albumId);
			delAlbumDetailPS.execute();
			delAlbumPS.setString(1,albumId);
			delAlbumPS.execute();
			b=true;
		} catch (SQLException se) {
			se.printStackTrace();
			b=false;
		}
		return b;
	}
	//��������insertImage
	//���ܽ��ܣ��ϴ���Ƭ
	//����˵����albumId:����� photo_id:��Ƭ���
	//			photo_name:��Ƭ���� path:·��
	//�� �� ֵ��false/true
	//�쳣:java.sql.SQLException
	//	   java.io.IOException
	public static boolean insertImage(String album_id,String photo_id,
								   String photo_name,String path) {
		boolean b=false;
		FileInputStream fis=null;
		try {
			//��ȡ��Ƭ
			File f=new File(path);
			int i=(int)f.length();
			byte[] bb=new byte[i];
			fis=new FileInputStream(f);
			fis.read(bb);
			
			insertPhotoPS.setString(1,album_id);
			insertPhotoPS.setString(2,photo_id);
			insertPhotoPS.setString(3,
			//��MYSQL����ISO-8859-1ת���ɹ���2312��gb2312��
					new String(photo_name.getBytes(),"gbk"));
			insertPhotoPS.setBytes(4,bb);
			insertPhotoPS.execute();
			b=true;
		} catch (IOException ie) {
			ie.printStackTrace();
			b=false;
		} catch (SQLException se) {
			se.printStackTrace();
			b=false;
		} finally {//�ر���
			try {
				if(fis!=null) {
					fis.close();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
		return b;
	}
	//��������getAlbumPhotoInfo
	//���ܽ��ܣ�ȡ�������Ƭ
	//����˵����albumId:�����
	//�� �� ֵ��java.util.Vector
	//�쳣:java.sql.SQLException
	//	   java.io.UnsupportedEncodingException
	public static Vector getAlbumPhotoInfo(String albumId) {
		Vector Vec=new Vector();
		try {
			albumPhotoInfoPS.setString(1,albumId);
			rs=albumPhotoInfoPS.executeQuery();
			while(rs.next()) {
				//com.javabean.Photo
				Photo photo=new Photo();
				photo.setPhotoId(rs.getString(1));
				photo.setPhotoName(
					//��MYSQL����ISO-8859-1ת���ɹ���2312��gb2312��
					new String(rs.getString(2).getBytes(
						"gbk"),"gb2312"));
				photo.setAlbumId(rs.getString(3));
				Vec.add(photo);
				photo=null;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		}  finally {//�رս����
			close(rs);
		}
		return Vec;
	}
	//��������getPhoto
	//���ܽ��ܣ�ȡ����Ƭ
	//����˵����photoId:��Ƭ���
	//�� �� ֵ��java.util.Vector
	//�쳣:java.sql.SQLException
	public static Vector getPhoto(String photoId) {
		
		Vector Vec=new Vector();
		try {
			photoPS.setString(1,photoId);
			rs=photoPS.executeQuery();
			while(rs.next()) {
				//com.javabean.AlbumPhoto
				AlbumPhoto ap=new AlbumPhoto();
				ap.setPhotoByte(rs.getBytes(1));
				Vec.add(ap);
				ap=null;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {//�رս����
			close(rs);
		}
		return Vec;
	}
	//��������deletePhoto
	//���ܽ��ܣ�ɾ����Ƭ
	//����˵����albumId:��Ƭ���
	//�� �� ֵ��false/true
	//�쳣:java.sql.SQLException
	public static boolean deletePhoto(String photoId) {
		boolean b=false;
		try {
			delPhotoPS.setString(1,photoId);
			delPhotoPS.execute();
			b=true;
		} catch (SQLException se) {
			se.printStackTrace();
			b=false;
		}
		return b;
	}
	//��������newAlbum
	//���ܽ��ܣ��½����
	//����˵����uaId:�û�������ϵ��� albumId:��Ƭ���
	//			type_id:������ͱ�� album_name:�������
	//�� �� ֵ��false/true
	//�쳣:java.sql.SQLException
	//	   java.io.UnsupportedEncodingException
	public static boolean newAlbum(String uaId,String album_id,
								   String type_id,String album_name) {
		boolean b=false;
		try {
			newAlbumPS.setString(1,uaId);
			newAlbumPS.setString(2,album_id);
			newAlbumPS.setString(3,
			//��MYSQL����ISO-8859-1ת���ɹ���2312��gb2312��
				new String(album_name.getBytes(),"gbk"));
			newAlbumPS.setString(4,type_id);
			newAlbumPS.execute();
			b=true;
		} catch (SQLException se) {
			se.printStackTrace();
			b=false;
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} 
		return b;
	}
	//��������getUA_id
	//���ܽ��ܣ�ȡ���û�������ϵ���
	//����˵����userName:�û���
	//�� �� ֵ��false/true
	//�쳣:java.sql.SQLException
	public static String getUA_id(String userName) {
		String stemp="";
		try {
			UAIdPS.setString(1,userName);
			rs=UAIdPS.executeQuery();
			while(rs.next()) {
				stemp=rs.getString(1);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}finally {//�رս����
			close(rs);
		}
		return stemp;
	}
	//��������getConnection()
	//���ܽ��ܣ�ȡ�����ݿ�����
	//����˵������
	//�� �� ֵ��java.sql.Connection
	//�쳣:java.sql.SQLException
	//	   javax.naming.NamingException
	public static Connection getConnection() {
		DataSource ds=null;
		try {
			//Tomcat��ȡMYSQL���ݿ�����
			ctx=new InitialContext();
			ds=(DataSource)ctx.lookup(JNDI_STR);
			con=ds.getConnection();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
		return con;
	}
	//��������close
	//���ܽ��ܣ��رս����
	//����˵����rs:�����
	//�� �� ֵ��void
	//�쳣:java.sql.SQLException
	private static void close(ResultSet rs) {
		
		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
}