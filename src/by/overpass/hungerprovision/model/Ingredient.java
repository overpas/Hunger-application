package by.overpass.hungerprovision.model;

import java.sql.Date;

public class Ingredient {
	private int id;
	private String name;
	private String units;
	private double quantity;
	private String provider;
	private Date importDate;
	private Date expiryDate;
	
	public Ingredient(int id, String name, String units, double quantity, String provider,
			Date importDate, Date expiryDate) {
		this.id = id;
		this.name = name;
		this.units = units;
		this.quantity = quantity;
		this.provider = provider;
		this.importDate = importDate;
		this.expiryDate = expiryDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "(" + id + ", " + name + ")";
	}
	
}
