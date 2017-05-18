package com.init.panjj.model;

public class RecomendBean {
	
	//private variables
	int _id;
	String _name;
	String _phone_number;
	String genre;
	String artist;
	String albumname;
	
	// Empty constructor
	public RecomendBean(String genre, String _phone_number, String artist, String albumname){
		this.genre=genre;
		this._phone_number=_phone_number;
		this.albumname=albumname;
		this.artist=artist;
	}
	// constructor
	public RecomendBean(int id, String name, String _phone_number){
		this._id = id;
		this._name = name;
		this._phone_number = _phone_number;
	}
	
	// constructor
	public RecomendBean(String name, String _phone_number){
		this._name = name;
		this._phone_number = _phone_number;
	}

	public RecomendBean() {

	}

	// getting ID
	public int getID(){
		return this._id;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	// getting name
	public String getName(){
		return this._name;
	}
	
	// setting name
	public void setName(String name){
		this._name = name;
	}
	
	// getting phone number
	public String getPhoneNumber(){
		return this._phone_number;
	}
	
	// setting phone number
	public void setPhoneNumber(String phone_number){
		this._phone_number = phone_number;
	}
	public String getArtist(){
		return this.artist;
	}

	// setting name
	public void setArtist(String artis){
		this.artist = artis;
	}

	public String getAlbumname(){
		return this.albumname;
	}

	// setting name
	public void setAlbumname(String albumname){
		this.albumname = albumname;
	}
	public String getGenre(){
		return this.genre;
	}

	// setting name
	public void setGenre(String genre){
		this.genre = genre;
	}
}
