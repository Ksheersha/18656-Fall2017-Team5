package models;

public class Author {

	public String index;
	public String name = "";
	public String homepage;
	public String picture;
	public String interests;
	public String email;
	public String affiliation;

	public String getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public String getHomepage() {
		return homepage;
	}

	public String getPicture() {
		return picture;
	}

	public String getInterests() {
		return interests;
	}

	public String getEmail() {
		return email;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
}
