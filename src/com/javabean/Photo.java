package com.javabean;

public class Photo {
	
	private String photoId;
	private String photoName;
	private String albumId;
	
	public void setAlbumId(String albumId) {
		this.albumId=albumId;
	}

	public String getAlbumId() {
		return (this.albumId); 
	}
	
	public void setPhotoId(String photoId) {
		this.photoId=photoId;
	}

	public String getPhotoId() {
		return (this.photoId); 
	}

	public String getPhotoName() {
		return (this.photoName); 
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName; 
	}

	public String toString() {

		String sep = System.getProperty("line.separator");

		StringBuffer buffer = new StringBuffer();
		buffer.append(sep);
		buffer.append("photoId = ");
		buffer.append(photoId);
		buffer.append(sep);
		buffer.append("photoName = ");
		buffer.append(photoName);
		buffer.append(sep);
		buffer.append("albumId = ");
		buffer.append(albumId);
		buffer.append(sep);
		
		return buffer.toString();
	}
	
	
}