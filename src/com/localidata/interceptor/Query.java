package com.localidata.interceptor;

public class Query {

	private String query;
	private String name;
	private long type;
	private long active;

	public Query() {
		this.query = "";
		this.name = "";
		this.type = 1;
		this.active = 1;
	}

	public long getActive() {
		return active;
	}

	public void setActive(long active) {
		this.active = active;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public Query(String name, String query) {
		this.query = query;
		this.name = name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
