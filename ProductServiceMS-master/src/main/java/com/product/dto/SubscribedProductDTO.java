package com.product.dto;


public class SubscribedProductDTO {
	int subid;
	int buyerid;
	int prodid;
	int quantity;
	public SubscribedProductDTO() {
		super();
	}
	public SubscribedProductDTO(int subid,int buyerid,int prodid,int quantity) {
		this.buyerid=buyerid;
		this.prodid=prodid;
		this.quantity=quantity;
		this.subid=subid;
	}
	public int getSubid() {
		return subid;
	}
	public void setSubid(int subid) {
		this.subid = subid;
	}
	public int getBuyerid() {
		return buyerid;
	}
	public void setBuyerid(int buyerid) {
		this.buyerid = buyerid;
	}
	public int getProdid() {
		return prodid;
	}
	public void setProdid(int prodid) {
		this.prodid = prodid;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


}
