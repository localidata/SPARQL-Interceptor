package com.localidata.interceptor;

public class ResponseQuery {

	private double time;
	private int size;
	private String name;
	
	public ResponseQuery(double time, int size, String name)
	{
		this.time=time;
		this.size=size;
		this.name=name;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	
}
