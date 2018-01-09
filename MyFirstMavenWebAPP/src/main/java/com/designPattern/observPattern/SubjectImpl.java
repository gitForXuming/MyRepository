package com.designPattern.observPattern;

import java.util.Vector;

public class SubjectImpl implements Subject{
	private Vector<Observer> v = new Vector<Observer>();
	@Override
	public void add(Observer observer) {
		v.add(observer);
	}

	@Override
	public void del(Observer observer) {
		v.remove(observer);
	}

	@Override
	public void notifyObserve() {
		for(Observer obs:v){
			obs.update();
		}
	}

}
