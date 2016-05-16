package galois;

import java.util.ArrayList;
import java.util.List;

public class Concept {

	private List<Integer> objects;
	private List<Integer> attributes;
	private List<Integer> originObj;
	private List<Integer> originAttr;	

	public Concept() {
		objects = new ArrayList<Integer>();
		attributes = new ArrayList<Integer>();
		originAttr = new ArrayList<Integer>(); 
		originObj = new ArrayList<Integer>(); 
	}
	
	public void addAttr(int attr) {
		attributes.add(attr);
	}
	
	public void addObj(int obj) {
		objects.add(obj);
	}
	
	public void addOriginAttr(int attr) {
		originAttr.add(attr);
	}
	
	public void addOriginObj(int obj) {
		originObj.add(obj);
	}
	
	public List<Integer> getAttr() {
		return attributes;
	}
	
	public List<Integer> getObj() {
		return objects;
	}
	
	public List<Integer> getOriginObj() {
		return originObj;
	}

	public void setOriginObj(List<Integer> originObj) {
		this.originObj = originObj;
	}

	public List<Integer> getOriginAttr() {
		return originAttr;
	}

	public void setOriginAttr(List<Integer> originAttr) {
		this.originAttr = originAttr;
	}
}
