package com.javabean;

import java.io.*;

public class AlbumInfo implements Serializable {
	
	private String albumId;
	private String albumName;
	private String albumType;
	private String albumTypeId;
	private String newTime;
	private int photoCount;
	
	public void setAlbumTypeId(String albumTypeId) {
		this.albumTypeId=albumTypeId;
	}
	
	public String getAlbumTypeId() {
		return (this.albumTypeId);
	}
	
	public void setAlbumType(String albumType) {
		this.albumType=albumType;
	}
	
	public String getAlbumType() {
		return (this.albumType);
	}
	
	public void setAlbumId(String albumId) {
		this.albumId=albumId;
	}

	public String getAlbumId() {
		return (this.albumId); 
	}

	public String getAlbumName() {
		return (this.albumName); 
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName; 
	}

	public String getNewTime() {
		return (this.newTime); 
	}

	public void setNewTime(String newTime) {
		this.newTime = newTime; 
	}

	public int getPhotoCount() {
		return (this.photoCount); 
	}

	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount; 
	}

	public String toString() {

		String sep = System.getProperty("line.separator");

		StringBuffer buffer = new StringBuffer();
		buffer.append(sep);
		buffer.append("albumId = ");
		buffer.append(albumId);
		buffer.append(sep);
		buffer.append("albumName = ");
		buffer.append(albumName);
		buffer.append(sep);
		buffer.append("newTime = ");
		buffer.append(newTime);
		buffer.append(sep);
		buffer.append("photoCount = ");
		buffer.append(photoCount);
		buffer.append(sep);
		
		return buffer.toString();
	}
}