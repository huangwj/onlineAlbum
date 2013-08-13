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
	///onlinealbumJNDI是JNDI名称
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
	
	static {//预编译SQL
		try {
			//取得数据库连接
			con=AlbumDAOBean.getConnection();
			//用户登陆
			userLogInPS=con.prepareStatement(
						"SELECT user_pwd FROM users WHERE user_name=?");
			//从数据库提取照片
			photoPS=con.prepareStatement(
				"SELECT photo FROM photos WHERE photo_id=?");
			//上传照片
			insertPhotoPS=con.prepareStatement(
				"INSERT INTO photos(album_id,photo_id,photo_name,photo) VALU"
				+"ES(?,?,?,?)");
			//用户与相册关系
			UAIdPS=con.prepareStatement(
				"SELECT UA_id FROM users WHERE user_name=?");
			//新建相册
			newAlbumPS=con.prepareStatement(
				"INSERT INTO user_album(UA_id,album_id,album_name,album_type"
				+"_id,new_time) VALUES(?,?,?,?,sysdate())");
			//相册详细信息
			albumInfoPS=con.prepareStatement(
				"SELECT ua.album_id,ua.album_name,ua.new_time,(SELECT at.alb"
				+"um_type_name FROM album_type at WHERE at.album_type_id=ua."
				+"album_type_id),(SELECT COUNT(p.photo_id) FROM photos p WHE"
				+"RE p.album_id=ua.album_id),(SELECT at1.album_type_id FROM "
				+"album_type at1 WHERE at1.album_type_id=ua.album_type_id) F"
				+"ROM user_album ua,users u WHERE u.user_name=?");
			//删除所有用户相册照片
			delAlbumDetailPS=con.prepareStatement(
				"DELETE FROM photos WHERE album_id=?");
			//删除相册
			delAlbumPS=con.prepareStatement(
				"DELETE FROM user_album WHERE album_id=?");
			//删除照片
			delPhotoPS=con.prepareStatement(
				"DELETE FROM photos WHERE photo_id=?");
			//相册类型信息
			albumTypeInfoPS=con.prepareStatement(
				"SELECT album_type_id,album_type_name FROM album_type");
			//相册照片集
			albumPhotoInfoPS=con.prepareStatement(
				"SELECT photo_id,photo_name,album_id FROM photos WHERE"
				+" album_id=?");
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	//方法名：userLogIn
	//功能介绍：用户登陆验证
	//参数说明：userName:用户名 passWord:密码
	//返 回 值：false/true
	//异常:java.sql.SQLException
	public static boolean userLogIn(String userName,String passWord) {
		
		boolean flag=false;
		String pwd=null;
		try {
			userLogInPS.setString(1,userName);
			rs=userLogInPS.executeQuery();
			while(rs.next()) {
				pwd=rs.getString(1).trim();
			}
			//判断密码是否正确
			if(passWord.equals(pwd)) {
				flag=true;
			}
		} catch (SQLException se) {
			se.printStackTrace();
			flag=false;
		} finally {//关闭结果集
			close(rs);
		}
		return flag;
	}
	//方法名：getAlbumInfo
	//功能介绍：取得用户相册信息
	//参数说明：userName:用户名
	//返 回 值：java.util.Vector
	//异常:java.sql.SQLException
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
					//将MYSQL编码ISO-8859-1转换成国标2312（gb2312）
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
		} finally {//关闭结果集
			close(rs);
		}
		return vec;
	}
	//方法名：getAlbumTyteInfo
	//功能介绍：取得相册类型信息
	//参数说明：没有参数
	//返 回 值：java.util.HashMap
	//异常:java.sql.SQLException
	//	   java.io.UnsupportedEncodingException
	public static HashMap getAlbumTyteInfo() {
		HashMap hm=new HashMap();
		try {
			rs=albumTypeInfoPS.executeQuery();
			while(rs.next()) {
				hm.put(rs.getString(1),
				//将MYSQL编码ISO-8859-1转换成国标2312（gb2312）
					new String(rs.getString(2).getBytes("gbk"),"gb2312"));
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} finally {//关闭结果集
			close(rs);
		}
		return hm;
	}
	//方法名：deleteAlbum
	//功能介绍：删除用户相册
	//参数说明：albumId:相册编号
	//返 回 值：false/true
	//异常:java.sql.SQLException
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
	//方法名：insertImage
	//功能介绍：上传照片
	//参数说明：albumId:相册编号 photo_id:照片编号
	//			photo_name:照片名称 path:路径
	//返 回 值：false/true
	//异常:java.sql.SQLException
	//	   java.io.IOException
	public static boolean insertImage(String album_id,String photo_id,
								   String photo_name,String path) {
		boolean b=false;
		FileInputStream fis=null;
		try {
			//读取照片
			File f=new File(path);
			int i=(int)f.length();
			byte[] bb=new byte[i];
			fis=new FileInputStream(f);
			fis.read(bb);
			
			insertPhotoPS.setString(1,album_id);
			insertPhotoPS.setString(2,photo_id);
			insertPhotoPS.setString(3,
			//将MYSQL编码ISO-8859-1转换成国标2312（gb2312）
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
		} finally {//关闭流
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
	//方法名：getAlbumPhotoInfo
	//功能介绍：取得相册照片
	//参数说明：albumId:相册编号
	//返 回 值：java.util.Vector
	//异常:java.sql.SQLException
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
					//将MYSQL编码ISO-8859-1转换成国标2312（gb2312）
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
		}  finally {//关闭结果集
			close(rs);
		}
		return Vec;
	}
	//方法名：getPhoto
	//功能介绍：取得照片
	//参数说明：photoId:照片编号
	//返 回 值：java.util.Vector
	//异常:java.sql.SQLException
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
		} finally {//关闭结果集
			close(rs);
		}
		return Vec;
	}
	//方法名：deletePhoto
	//功能介绍：删除照片
	//参数说明：albumId:照片编号
	//返 回 值：false/true
	//异常:java.sql.SQLException
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
	//方法名：newAlbum
	//功能介绍：新建相册
	//参数说明：uaId:用户和相册关系编号 albumId:照片编号
	//			type_id:相册类型编号 album_name:相册名称
	//返 回 值：false/true
	//异常:java.sql.SQLException
	//	   java.io.UnsupportedEncodingException
	public static boolean newAlbum(String uaId,String album_id,
								   String type_id,String album_name) {
		boolean b=false;
		try {
			newAlbumPS.setString(1,uaId);
			newAlbumPS.setString(2,album_id);
			newAlbumPS.setString(3,
			//将MYSQL编码ISO-8859-1转换成国标2312（gb2312）
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
	//方法名：getUA_id
	//功能介绍：取得用户和相册关系编号
	//参数说明：userName:用户名
	//返 回 值：false/true
	//异常:java.sql.SQLException
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
		}finally {//关闭结果集
			close(rs);
		}
		return stemp;
	}
	//方法名：getConnection()
	//功能介绍：取得数据库连接
	//参数说明：无
	//返 回 值：java.sql.Connection
	//异常:java.sql.SQLException
	//	   javax.naming.NamingException
	public static Connection getConnection() {
		DataSource ds=null;
		try {
			//Tomcat获取MYSQL数据库连接
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
	//方法名：close
	//功能介绍：关闭结果集
	//参数说明：rs:结果集
	//返 回 值：void
	//异常:java.sql.SQLException
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