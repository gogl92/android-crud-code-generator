package com.xpert.androidcrudgenerator.generators;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.xpert.androidcrudgenerator.elementobjects.AttributeObject;
import com.xpert.androidcrudgenerator.elementobjects.ChildObject;
import com.xpert.androidcrudgenerator.elementobjects.ModelObject;

public class GeneratorUtil {

    private static Element domChild = null;
    private static Attribute domAttribute = null;

    private static ModelObject modelObject = null;
    private static ChildObject childObject = null;
    private static AttributeObject attributeObject = null;

    private static List<ModelObject>modelObjectList = null;
    private static List<ChildObject>childObjectList = null;
    private static List<AttributeObject>attributeObjectList = null;

    private static EntityGenerator entityGenerator = null;
    private ActivityGenerator activityGenerator = null;
    private static DaoGenerator daoGenerator = null;
    private ServicesGenerator servicesGenerator = null;
    private static DBHelperGenerator dbHelperGenerator = null;

    public static List<ModelObject> parseAndGenerateObjects(File anXmlDocFile) {
	SAXBuilder parser = new SAXBuilder();
	modelObjectList = new LinkedList<ModelObject>();
	try {			
	    Document doc = parser.build(anXmlDocFile);

	    for (int i = 0; i < doc.getRootElement().getChildren().size(); i++) {
		domChild = (Element) doc.getRootElement().getChildren().get(i);
		modelObject = new ModelObject();
		modelObject.setRootName(doc.getRootElement().getAttributeValue("appname"));
		modelObject.setName(domChild.getAttributeValue("tablename"));

		if (domChild.getChildren().size() > 0) {
		    childObjectList = new LinkedList<ChildObject>();
		    for (int j = 0; j < domChild.getChildren().size(); j++) {
			Element domInnerChild = (Element) domChild
			.getChildren().get(j);
			childObject = new ChildObject();
			childObject.setName(domInnerChild.getName());

			if (domInnerChild.getAttributes().size() > 0) {
			    attributeObjectList = new LinkedList<AttributeObject>();
			    for (int k = 0; k < domInnerChild.getAttributes()
			    .size(); k++) {
				attributeObject = new AttributeObject();
				attributeObject.setKey(((Attribute) domInnerChild
					.getAttributes().get(k))
					.getName());
				attributeObject.setValue((((((Attribute) domInnerChild
					.getAttributes().get(k))
					.getValue()).equals("") || (((Attribute) domInnerChild
						.getAttributes().get(k))
						.getValue())
						.equals(null)) ? "null"
							: ((Attribute) domInnerChild
								.getAttributes()
								.get(k))
								.getValue()));
				attributeObjectList.add(attributeObject);

			    }
			    childObject.setAttributeList(attributeObjectList);
			}
			childObjectList.add(childObject);
		    }
		}
		modelObject.setChildObjectList(childObjectList);
		modelObjectList.add(modelObject);				
	    }

	} catch (JDOMException ex) {
	    ex.printStackTrace();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	return modelObjectList;
    }

    public void generateActivityClasses(List<ModelObject>modelObjectList){

    }

    public static void generateEntityClasses(List<ModelObject>modelObjectList){
	entityGenerator = new EntityGenerator();
	entityGenerator.generateEntity(modelObjectList);
    }

    public static void generateDaoClasses(List<ModelObject>modelObjectList){
	daoGenerator = new DaoGenerator();
	daoGenerator.generateDaoClass(modelObjectList);
    }

    public void generateServicesClasses(List<ModelObject>modelObjectList){

    }

    public static void generateDBHelperClasses(List<ModelObject>modelObjectList){
	dbHelperGenerator = new DBHelperGenerator();
	dbHelperGenerator.generateDbHelperClass(modelObjectList);
    }
}
