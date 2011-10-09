package com.xpert.androidcrudgenerator.generators;

import java.util.List;

import com.xpert.androidcrudgenerator.elementobjects.AttributeObject;
import com.xpert.androidcrudgenerator.elementobjects.ChildObject;
import com.xpert.androidcrudgenerator.elementobjects.ModelObject;
import com.xpert.androidcrudgenerator.utils.FileUtils;

public class EntityGenerator {

    private static ModelObject modelObject = null;
    private static ChildObject childObject = null;
    private static AttributeObject attributeObject = null;

    private static List<ModelObject> modelObjectList = null;
    private static List<ChildObject> childObjectList = null;
    private static List<AttributeObject> attributeObjectList = null;

    private static final int ATTRIBUTE_TYPE_GLOBAL = 0;
    private static final int ATTRIBUTE_TYPE_ARGUMENT = 1;
    private static final int ATTRIBUTE_TYPE_BLOCK = 2;

    public EntityGenerator() {}

    public EntityGenerator(List<ModelObject> modelObjectList) {
	this.modelObjectList = modelObjectList;
    }	

    public void generateEntity(List<ModelObject> modelObjectList) {
	String entityClass = "";
	String getSetLogic = "";
	String argumentList = "";
	String blockArguments = "";
	int modelObjectListSize = modelObjectList.size();
	for (int i = 0; i < modelObjectListSize; i++) {
	    modelObject = modelObjectList.get(i);

	    entityClass += "public class ".concat(modelObject.getName().substring(0,1).toUpperCase().concat(modelObject.getName().substring(1)))
	    .concat("{ \n");

	    childObjectList = modelObject.getChildObjectList();
	    if (childObjectList != null) {				
		int childObjectListSize = childObjectList.size();
		for (int j = 0; j < childObjectListSize; j++) {					
		    childObject = childObjectList.get(j);
		    entityClass = generateAttribute(childObject, entityClass,
			    ATTRIBUTE_TYPE_GLOBAL);
		    getSetLogic = generateGetterAndSetters(childObject,
			    getSetLogic);
		    if(j<childObjectListSize-1)
			argumentList = generateAttribute(childObject, argumentList,
				ATTRIBUTE_TYPE_ARGUMENT).concat(",");
		    else
			argumentList = generateAttribute(childObject, argumentList,
				ATTRIBUTE_TYPE_ARGUMENT);
		    blockArguments = generateAttribute(childObject, blockArguments,
			    ATTRIBUTE_TYPE_BLOCK);
		}
	    }

	    entityClass += "\tpublic ".concat(modelObject.getName().substring(0,1).toUpperCase().concat(modelObject.getName().substring(1))).concat(
	    "(){}").concat(" \n");

	    entityClass += "\tpublic ".concat(modelObject.getName().substring(0,1).toUpperCase().concat(modelObject.getName().substring(1))).concat("(")
	    .concat(argumentList).concat("){\n").concat(blockArguments).concat("\t}\n");

	    entityClass+= getSetLogic;

	    entityClass+="}\n";

	    FileUtils.writeGeneratedClassToFile(entityClass);
	    entityClass="";
	    getSetLogic = "";
	    argumentList = "";
	    blockArguments = "";

	}
    }

    /**
     * Generate Attribute
     * 
     * @param childObject
     * @param attributeVar
     * @return
     */
    public String generateAttribute(ChildObject childObject,
	    String attributeVar, int type) {
	attributeObjectList = childObject.getAttributeList();
	String attribName = "";
	String attribType = "";
	int attributeObjectListSize = attributeObjectList.size();
	for (int k = 0; k < attributeObjectListSize; k++) {
	    attributeObject = attributeObjectList.get(k);
	    if (attributeObject.getKey().equals("colname"))
		attribName = attributeObject.getValue();
	    else if (attributeObject.getKey().equals("type"))
		attribType = attributeObject.getValue();
	}
	switch (type) {
	case ATTRIBUTE_TYPE_GLOBAL:
	    attributeVar += "\tprivate ".concat(
		    returnTranslatedAttributeDataType(attribType)).concat(" ")
		    .concat(attribName).concat(";\n");
	    break;
	case ATTRIBUTE_TYPE_ARGUMENT:
	    attributeVar += "".concat(
		    returnTranslatedAttributeDataType(attribType)).concat(" ")
		    .concat(attribName);
	    break;
	case ATTRIBUTE_TYPE_BLOCK:
	    attributeVar += "\t\tthis.".concat(attribName).concat(" = ").concat(
		    attribName).concat(";\n");
	    break;
	default:
	    break;
	}
	return attributeVar;
    }		

    /**
     * Generate Getters and Setters
     * 
     * @param childObject
     * @param getSetVar
     * @return
     */
    public String generateGetterAndSetters(ChildObject childObject,
	    String getSetVar) {
	attributeObjectList = childObject.getAttributeList();
	String attribName = "";
	String attribType = "";
	int attributeObjectListSize = attributeObjectList.size();
	for (int k = 0; k < attributeObjectListSize; k++) {
	    attributeObject = attributeObjectList.get(k);
	    if (attributeObject.getKey().equals("colname"))
		attribName = attributeObject.getValue();
	    else if (attributeObject.getKey().equals("type"))
		attribType = attributeObject.getValue();
	}
	getSetVar += "\tpublic ".concat(
		returnTranslatedAttributeDataType(attribType)).concat(" get")
		.concat(
			attribName.substring(0, 1).toUpperCase().concat(
				attribName.substring(1))).concat("(){\n")
				.concat("\t\treturn ").concat(attribName).concat(";\n\t}\n")
				.concat("\tpublic void set").concat(
					attribName.substring(0, 1).toUpperCase().concat(
						attribName.substring(1))).concat("(").concat(
							returnTranslatedAttributeDataType(attribType)).concat(
							" ").concat(attribName).concat(") {\n").concat(
							"\t\tthis.").concat(attribName).concat(" = ").concat(
								attribName).concat(";\n\t}\n");

	return getSetVar;
    }

    public String returnTranslatedAttributeDataType(String type) {
	if (type.equals("") || type.equals(null))
	    return null;
	else if (type.equals("int"))
	    return "int";
	else if (type.equals("varchar")||type.equals("text"))
	    return "String";
	else if (type.equals("boolean"))
	    return "boolean";
	return null;
    }

}
