package com.xpert.androidcrudgenerator.elementobjects;
import java.util.List;


public class ChildObject {
	private String name;
	private List<AttributeObject> attributeList = null;
	
	public ChildObject(){};
	
	public ChildObject(String name, List<AttributeObject> attributeList){
		this.name = name;
		this.attributeList = attributeList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AttributeObject> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<AttributeObject> attributeList) {
		this.attributeList = attributeList;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributeList == null) ? 0 : attributeList.hashCode());
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
		ChildObject other = (ChildObject) obj;
		if (attributeList == null) {
			if (other.attributeList != null)
				return false;
		} else if (!attributeList.equals(other.attributeList))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
