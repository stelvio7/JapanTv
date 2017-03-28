package com.japantv.model;

import java.util.ArrayList;

public class Season {
	private String seasonNum;
	private ArrayList<DramaContent> dramaList;
	
	public String getSeasonNum() {
		return seasonNum;
	}
	public void setSeasonNum(String seasonNum) {
		this.seasonNum = seasonNum;
	}
	public ArrayList<DramaContent> getDramaList() {
		return dramaList;
	}
	public void setDramaList(ArrayList<DramaContent> dramaList) {
		this.dramaList = dramaList;
	}
	
	

}
