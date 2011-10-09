package com.xpert.androidcrudgenerator.elementobjects;
import java.util.List;


public class ModelObject {
	private String rootName;	
	private String name;
	private List<ChildObject> childObjectList = null;
	
	public ModelObject(){}
	
	public ModelObject(String rootName, String name, List<ChildObject> childObjectList){
		this.rootName = rootName;
		this.name = name;
		this.childObjectList = childObjectList;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChildObject> getChildObjectList() {
		return childObjectList;
	}

	public void setChildObjectList(List<ChildObject> childObjectList) {
		this.childObjectList = childObjectList;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((childObjectList == null) ? 0 : childObjectList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelObject other = (ModelObject) obj;
		if (childObjectList == null) {
			if (other.childObjectList != null)
				return false;
		} else if (!childObjectList.equals(other.childObjectList))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
