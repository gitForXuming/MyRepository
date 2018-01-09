package com.designPattern.observPattern;


public interface Subject {
	public void add(Observer observer);
	public void del(Observer observer);
	public void notifyObserve();
}
