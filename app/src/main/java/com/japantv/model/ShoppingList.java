package com.japantv.model;

import android.graphics.Bitmap;

public class ShoppingList {
	private String idx;
	private String code1;
	private String code2;
	private String image;
	private String title;
	private String detail_image;
	private String sub_image1;
	private String sub_image2;
	private String sub_image3;
	private String sub_image4;
	private String sub_image5;
	private String sub_image6;
	private String sub_image7;
	private String sub_image8;
	private String sub_image9;
	private String sub_image10;
	private String sub_image11;
	private String sub_image12;
	private String sub_image13;
	private String sub_image14;
	private String sub_image15;
	private String sub_image16;
	private String sub_image17;
	private String sub_image18;
	private String sub_image19;
	private String sub_image20;
	private Bitmap bitmap;
	private String type;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getCode1() {
		return code1;
	}
	public void setCode1(String code1) {
		this.code1 = code1;
	}
	public String getCode2() {
		return code2;
	}
	public void setCode2(String code2) {
		this.code2 = code2;
	}
	public String getDetail_image() {
		return detail_image;
	}
	public void setDetail_image(String detail_image) {
		this.detail_image = detail_image;
	}
	public String getSub_image1() {
		return sub_image1;
	}
	public void setSub_image1(String sub_image1) {
		this.sub_image1 = sub_image1;
	}
	public String getSub_image2() {
		return sub_image2;
	}
	public void setSub_image2(String sub_image2) {
		this.sub_image2 = sub_image2;
	}
	public String getSub_image3() {
		return sub_image3;
	}
	public void setSub_image3(String sub_image3) {
		this.sub_image3 = sub_image3;
	}
	public String getSub_image4() {
		return sub_image4;
	}
	public void setSub_image4(String sub_image4) {
		this.sub_image4 = sub_image4;
	}
	public String getSub_image5() {
		return sub_image5;
	}
	public void setSub_image5(String sub_image5) {
		this.sub_image5 = sub_image5;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public void recyleBitmap(){
		if(bitmap != null){
			this.bitmap.recycle();
			this.bitmap = null;
		}
	}
	public String getSub_image6() {
		return sub_image6;
	}
	public void setSub_image6(String sub_image6) {
		this.sub_image6 = sub_image6;
	}
	public String getSub_image7() {
		return sub_image7;
	}
	public void setSub_image7(String sub_image7) {
		this.sub_image7 = sub_image7;
	}
	public String getSub_image8() {
		return sub_image8;
	}
	public void setSub_image8(String sub_image8) {
		this.sub_image8 = sub_image8;
	}
	public String getSub_image9() {
		return sub_image9;
	}
	public void setSub_image9(String sub_image9) {
		this.sub_image9 = sub_image9;
	}
	public String getSub_image10() {
		return sub_image10;
	}
	public void setSub_image10(String sub_image10) {
		this.sub_image10 = sub_image10;
	}
	public String getSub_image11() {
		return sub_image11;
	}
	public void setSub_image11(String sub_image11) {
		this.sub_image11 = sub_image11;
	}
	public String getSub_image12() {
		return sub_image12;
	}
	public void setSub_image12(String sub_image12) {
		this.sub_image12 = sub_image12;
	}
	public String getSub_image13() {
		return sub_image13;
	}
	public void setSub_image13(String sub_image13) {
		this.sub_image13 = sub_image13;
	}
	public String getSub_image14() {
		return sub_image14;
	}
	public void setSub_image14(String sub_image14) {
		this.sub_image14 = sub_image14;
	}
	public String getSub_image15() {
		return sub_image15;
	}
	public void setSub_image15(String sub_image15) {
		this.sub_image15 = sub_image15;
	}
	public String getSub_image16() {
		return sub_image16;
	}
	public void setSub_image16(String sub_image16) {
		this.sub_image16 = sub_image16;
	}
	public String getSub_image17() {
		return sub_image17;
	}
	public void setSub_image17(String sub_image17) {
		this.sub_image17 = sub_image17;
	}
	public String getSub_image18() {
		return sub_image18;
	}
	public void setSub_image18(String sub_image18) {
		this.sub_image18 = sub_image18;
	}
	public String getSub_image19() {
		return sub_image19;
	}
	public void setSub_image19(String sub_image19) {
		this.sub_image19 = sub_image19;
	}
	public String getSub_image20() {
		return sub_image20;
	}
	public void setSub_image20(String sub_image20) {
		this.sub_image20 = sub_image20;
	}
	
	
}
