package com.xpert.androidcrudgenerator.program;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.output.XMLOutputter;

import com.xpert.androidcrudgenerator.elementobjects.ModelObject;
import com.xpert.androidcrudgenerator.generators.GeneratorUtil;

public class AndroidCrudGenerator {
	static List<ModelObject>modelObjectList = null;	
				
	public static void main(String[] args) {
		// play.parseAndPlay(new File(args[0]));		
		modelObjectList = GeneratorUtil.parseAndGenerateObjects(new File("testbase.xml"));
		GeneratorUtil.generateEntityClasses(modelObjectList);
		GeneratorUtil.generateDBHelperClasses(modelObjectList);
		GeneratorUtil.generateDaoClasses(modelObjectList);		
	}	
}