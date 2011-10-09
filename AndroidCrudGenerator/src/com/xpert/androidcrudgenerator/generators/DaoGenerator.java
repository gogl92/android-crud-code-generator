package com.xpert.androidcrudgenerator.generators;

import java.util.List;

import com.xpert.androidcrudgenerator.elementobjects.AttributeObject;
import com.xpert.androidcrudgenerator.elementobjects.ChildObject;
import com.xpert.androidcrudgenerator.elementobjects.ModelObject;
import com.xpert.androidcrudgenerator.utils.FileUtils;

public class DaoGenerator {

    private static ModelObject modelObject = null;
    private static ChildObject childObject = null;
    private static AttributeObject attributeObject = null;

    private static List<ModelObject> modelObjectList = null;
    private static List<ChildObject> childObjectList = null;
    private static List<AttributeObject> attributeObjectList = null;

    private String generateDaoInterfaces = "";
    private String generateDaoImplementations = "";
    private String generateAdapter = "";	

    public DaoGenerator() {
    }

    public DaoGenerator(List<ModelObject> modelObjectList) {
	this.modelObjectList = modelObjectList;
    }

    public void generateDaoClass(List<ModelObject> modelObjectList) {
	generateDaoInterfaces = generateDaoInterfaces(generateDaoInterfaces,
		modelObjectList);
	generateDaoImplementations = generateDaoImplementation(
		generateDaoImplementations, modelObjectList);
	FileUtils.writeGeneratedClassToFile(generateDaoInterfaces);
	FileUtils.writeGeneratedClassToFile(generateDaoImplementations);
	generateDaoInterfaces = "";
	generateDaoImplementations = "";
    }

    public void generateAdapterClass(List<ModelObject> modelObjectList){
	generateAdapter = generateAdapterImplementation(generateAdapter, modelObjectList);
    }

    /**
     * 
     * @param generatedDaoInterface
     * @param modelObjectList
     * @return
     */
    public String generateDaoInterfaces(String generatedDaoInterface,
	    List<ModelObject> modelObjectList) {
	generatedDaoInterface += "import java.util.List;\n\nimport android.content.Context;\n\n";
	int modelObjectListSize = modelObjectList.size();
	for (int i = 0; i < modelObjectListSize; i++) {
	    modelObject = modelObjectList.get(i);
	    generatedDaoInterface += "\npublic interface "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + "DAO {\n\n";
	    generatedDaoInterface += "\tpublic void insertInto"
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + "(Context ctx, "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + " "
		+ modelObject.getName() + ");\n\n";
	    generatedDaoInterface += "\tpublic List<"
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1)
		+ "> getAllObjects(Context ctx);\n\n";
	    generatedDaoInterface += "\tpublic void update"
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1)
		+ "Values(Context ctx, "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + " "
		+ modelObject.getName() + ");\n\n";
	    generatedDaoInterface += "\tpublic void deleteFrom"
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1)
		+ "(Context ctx, int id);\n\n";
	    generatedDaoInterface += "\tpublic void deleteAllFrom"
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1)
		+ "(Context ctx);\n\n";
	    generatedDaoInterface += "\tpublic "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1)
		+ " getSingleObject(Context ctx, int id);\n\n";
	    generatedDaoInterface += "}";
	}

	return generatedDaoInterface;
    }

    /**
     * 
     * @param generatedDaoImplementation
     * @param modelObjectList
     * @return
     */
    public String generateDaoImplementation(String generatedDaoImplementation,
	    List<ModelObject> modelObjectList) {
	String insertMethod = "";
	String updateMethod = "";
	String deleteMethod = "";
	String deleteAllMethod = "";
	String getSingleObjectMethod = "";
	String getAllObjectsMethod = "";

	generatedDaoImplementation += "import android.content.ContentValues;\nimport android.content.Context;\nimport android.database.Cursor;\n"
	    + "import java.util.LinkedList;\nimport java.util.List;\n\n";
	int modelObjectListSize = modelObjectList.size();
	for (int i = 0; i < modelObjectListSize; i++) {
	    modelObject = modelObjectList.get(i);
	    generatedDaoImplementation += "public class "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + "DAOimpl ";
	    generatedDaoImplementation += "implements "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + "DAO {\n";
	    generatedDaoImplementation += "\tprivate static "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + "DAOimpl " + ""
		+ modelObject.getName()
		+ "DAOimpl = null;\n\tprivate DbHelper dbHelper = null;\n";
	    generatedDaoImplementation += "\tprivate "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + " "
		+ modelObject.getName() + " = null;\n\n";

	    generatedDaoImplementation += "\tpublic static "
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1)
		+ "DAOimpl getInstance(){\n";
	    generatedDaoImplementation += "\t\tif(" + modelObject.getName()
	    + "DAOimpl == null){\n";
	    generatedDaoImplementation += "\t\t\t" + modelObject.getName()
	    + "DAOimpl = new "
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1)
	    + "DAOimpl();\n\t\t}\n";
	    generatedDaoImplementation += "\t\treturn " + modelObject.getName()
	    + "DAOimpl;\n\t}\n\n";

	    insertMethod = generateInsertMethod(insertMethod, modelObject);
	    updateMethod = generateUpdateMethod(updateMethod, modelObject);
	    deleteMethod = generateDeleteMethod(deleteMethod, modelObject);
	    deleteAllMethod = generateDeleteAllMethod(deleteAllMethod, modelObject);
	    getSingleObjectMethod = generateGetSingleObjectMethod(
		    getSingleObjectMethod, modelObject);
	    getAllObjectsMethod = generateGetAllObjectsMethod(
		    getAllObjectsMethod, modelObject);

	    generatedDaoImplementation += "\n" + insertMethod + "\n"
	    + updateMethod + "\n" + deleteMethod + "\n" + deleteAllMethod + "\n"
	    + getSingleObjectMethod + "\n" + getAllObjectsMethod
	    + "\n\n}\n";

	    insertMethod = "";
	    updateMethod = "";
	    deleteMethod = "";
	    deleteAllMethod = "";
	    getSingleObjectMethod = "";
	    getAllObjectsMethod = "";
	}

	return generatedDaoImplementation;
    }

    /**
     * Generate Insert Method
     * 
     * @param insertMethod
     * @param modelObject
     * @return
     */
    public String generateInsertMethod(String insertMethod,
	    ModelObject modelObject) {
	childObjectList = modelObject.getChildObjectList();
	insertMethod += "\t@Override\n";
	insertMethod += "\tpublic void insertInto"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1) + "(Context ctx, "
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1) + " "
	    + modelObject.getName() + "){\n";
	insertMethod += "\t\tdbHelper = DbHelper.getInstance(ctx.getApplicationContext());\n";
	insertMethod += "\t\tContentValues contentValues = new ContentValues();\n";		
	if (childObjectList != null) {
	    int childObjectListSize = childObjectList.size();
	    for (int i = 1; i < childObjectListSize; i++) {
		childObject = childObjectList.get(i);
		attributeObjectList = childObject.getAttributeList();
		attributeObject = returnSpecificValue(attributeObjectList,
			"colname");
		insertMethod += "\t\tcontentValues.put(\""
		    + attributeObject.getValue()
		    + "\", "
		    + modelObject.getName()
		    + ".get"
		    + attributeObject.getValue().substring(0, 1)
		    .toUpperCase()
		    + attributeObject.getValue().substring(1) + "());\n";
	    }
	}

	insertMethod += "\t\tdbHelper.getDb().insert(dbHelper.DB_TABLE_"
	    + modelObject.getName().toUpperCase()
	    + ", null, contentValues);\n\t}";
	return insertMethod;
    }

    /**
     * Generate Update Method
     * 
     * @param updateMethod
     * @param modelObject
     * @return
     */
    public String generateUpdateMethod(String updateMethod,
	    ModelObject modelObject) {
	childObjectList = modelObject.getChildObjectList();
	updateMethod += "\t@Override\n";
	updateMethod += "\tpublic void update"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1) + "Values(Context ctx, "
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1) + " "
	    + modelObject.getName() + "){\n";
	updateMethod += "\t\tdbHelper = DbHelper.getInstance(ctx.getApplicationContext());\n";
	updateMethod += "\t\tContentValues contentValues = new ContentValues();\n";
	if (childObjectList != null) {
	    int childObjectListSize = childObjectList.size();
	    for (int i = 1; i < childObjectListSize; i++) {
		childObject = childObjectList.get(i);
		attributeObjectList = childObject.getAttributeList();
		attributeObject = returnSpecificValue(attributeObjectList,
			"colname");
		updateMethod += "\t\tcontentValues.put(\""
		    + attributeObject.getValue()
		    + "\", "
		    + modelObject.getName()
		    + ".get"
		    + attributeObject.getValue().substring(0, 1)
		    .toUpperCase()
		    + attributeObject.getValue().substring(1) + "());\n";
	    }
	}

	updateMethod += "\t\tdbHelper.getDb().update(dbHelper.DB_TABLE_"
	    + modelObject.getName().toUpperCase()
	    + ", contentValues,\"_id=\"+" + modelObject.getName()
	    + ".getId(),null);\n\t}";
	return updateMethod;
    }

    /**
     * Generate Delete Method
     * 
     * @param deleteMethod
     * @param modelObject
     * @return
     */
    public String generateDeleteMethod(String deleteMethod,
	    ModelObject modelObject) {
	deleteMethod += "\t@Override\n";
	deleteMethod += "\tpublic void deleteFrom"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1)
	    + "(Context ctx, int id){\n";
	deleteMethod += "\t\tdbHelper = DbHelper.getInstance(ctx.getApplicationContext());\n";
	deleteMethod += "\t\tdbHelper.getDb().delete(dbHelper.DB_TABLE_"
	    + modelObject.getName().toUpperCase()
	    + ", \"_id=\"+id, null);\n";
	deleteMethod += "\t}\n\n";
	return deleteMethod;
    }

    /**
     * Generate Delete All Method
     * 
     * @param deleteAllMethod
     * @param modelObject
     * @return
     */
    public String generateDeleteAllMethod(String deleteAllMethod,
	    ModelObject modelObject) {
	deleteAllMethod += "\t@Override\n";
	deleteAllMethod += "\tpublic void deleteAllFrom"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1)
	    + "(Context ctx){\n";
	deleteAllMethod += "\t\tdbHelper = DbHelper.getInstance(ctx.getApplicationContext());\n";
	deleteAllMethod += "\t\tdbHelper.getDb().delete(dbHelper.DB_TABLE_"
	    + modelObject.getName().toUpperCase()
	    + ", null, null);\n";
	deleteAllMethod += "\t}\n\n";
	return deleteAllMethod;
    }





    /**
     * Generate Single Object Method
     * 
     * @param getSingleObjectMethod
     * @param modelObject
     * @return
     */
    public String generateGetSingleObjectMethod(String getSingleObjectMethod,
	    ModelObject modelObject) {
	childObjectList = modelObject.getChildObjectList();
	getSingleObjectMethod += "\t@Override\n";
	getSingleObjectMethod += "\tpublic "
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1)
	    + " getSingleObject(Context ctx, int id){\n";
	getSingleObjectMethod += "\t\t"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1) + " "
	    + modelObject.getName() + " = null;\n";
	getSingleObjectMethod += "\t\tdbHelper = DbHelper.getInstance(ctx.getApplicationContext());\n";
	getSingleObjectMethod += "\t\tCursor c = dbHelper.getDb().query(dbHelper.DB_TABLE_"
	    + modelObject.getName().toUpperCase()
	    + ", null, \"id=\"+id, null, null, null, null);\n";
	getSingleObjectMethod += "\t\tif(c!=null && c.moveToNext()){\n";
	getSingleObjectMethod += "\t\t\t" + modelObject.getName() + " = new "
	+ modelObject.getName().substring(0, 1).toUpperCase()
	+ modelObject.getName().substring(1) + "();\n";
	if (childObjectList != null) {
	    int childObjectListSize = childObjectList.size();
	    for (int i = 0; i < childObjectListSize; i++) {
		childObject = childObjectList.get(i);
		attributeObjectList = childObject.getAttributeList();
		attributeObject = returnSpecificValue(attributeObjectList,
			"colname");
		getSingleObjectMethod += "\t\t\t"
		    + modelObject.getName()
		    + ".set"
		    + attributeObject.getValue().substring(0, 1)
		    .toUpperCase()
		    + attributeObject.getValue().substring(1)
		    + "(c.get"
		    + returnTranslatedAttributeDataType(((AttributeObject) returnSpecificValue(
			    attributeObjectList, "type")).getValue())
			    + "(c.getColumnIndex(\""
			    + ((AttributeObject) returnSpecificValue(
				    attributeObjectList, "colname")).getValue()
				    + "\")));\n";

	    }
	    getSingleObjectMethod += "\t\t}\n\t\treturn "
		+ modelObject.getName() + ";\n\t}\n";
	}
	return getSingleObjectMethod;
    }

    /**
     * Generate List with Entity Objects
     * 
     * @param getAllObjectsMethod
     * @param modelObject
     * @return
     */
    public String generateGetAllObjectsMethod(String getAllObjectsMethod,
	    ModelObject modelObject) {
	childObjectList = modelObject.getChildObjectList();
	getAllObjectsMethod += "\t@Override\n";
	getAllObjectsMethod += "\tpublic List<"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1)
	    + "> getAllObjects(Context ctx){\n";
	getAllObjectsMethod += "\t\tdbHelper = DbHelper.getInstance(ctx.getApplicationContext());\n";
	getAllObjectsMethod += "\t\tList<"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1) + ">"
	    + modelObject.getName() + "List = new LinkedList<"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1) + ">();\n";
	getAllObjectsMethod += "\t\tCursor c = dbHelper.getDb().query(dbHelper.DB_TABLE_"
	    + modelObject.getName().substring(0, 1).toUpperCase()
	    + modelObject.getName().substring(1)
	    + ", null, null,null, null, null, \"_id DESC\");\n";
	getAllObjectsMethod += "\t\tif (c.moveToFirst()) {\n";
	getAllObjectsMethod += "\t\t\tdo {\n";
	getAllObjectsMethod += "\t\t\t\t" + modelObject.getName() + " = new "
	+ modelObject.getName().substring(0, 1).toUpperCase()
	+ modelObject.getName().substring(1) + "();\n";
	if (childObjectList != null) {
	    int childObjectListSize = childObjectList.size();
	    for (int i = 0; i < childObjectListSize; i++) {
		childObject = childObjectList.get(i);
		attributeObjectList = childObject.getAttributeList();
		attributeObject = returnSpecificValue(attributeObjectList,
			"colname");
		getAllObjectsMethod += "\t\t\t\t"
		    + modelObject.getName()
		    + ".set"
		    + attributeObject.getValue().substring(0, 1)
		    .toUpperCase()
		    + attributeObject.getValue().substring(1)
		    + "(c.get"
		    + returnTranslatedAttributeDataType(((AttributeObject) returnSpecificValue(
			    attributeObjectList, "type")).getValue())
			    + "(c.getColumnIndex(\""
			    + ((AttributeObject) returnSpecificValue(
				    attributeObjectList, "colname")).getValue()
				    + "\")));\n";
	    }
	    getAllObjectsMethod += "\t\t\t\t" + modelObject.getName()
	    + "List.add(" + modelObject.getName() + ");\n";
	    getAllObjectsMethod += "\t\t\t} while (c.moveToNext());\n\t\t}\n\t\tc.close();\n\t\treturn "
		+ modelObject.getName() + "List;\n\t}\n";
	}
	return getAllObjectsMethod;
    }

    public String generateAdapterImplementation(String generateAdapter, List<ModelObject> modelObjectList){
	int modelObjectListSize = modelObjectList.size();
	for(int i=0;i<modelObjectListSize;i++){
	    modelObject = modelObjectList.get(i);				
	    generateAdapter+=generateAdapter+"import java.util.List;\n\nimport android.widget.BaseAdapter;\n";
	    generateAdapter+=generateAdapter+"import android.widget.LinearLayout;\nimport android.widget.TextView;\n\n";
	    generateAdapter+=generateAdapter+"public class "+modelObject.getName().substring(0,1).toUpperCase()+modelObject.getName().substring(1)+"Adapter extends BaseAdapter{\n";
	    generateAdapter+=generateAdapter+"\tprivate List<"+modelObject.getName().substring(0,1).toUpperCase()+modelObject.getName().substring(1)+">"+modelObject.getName()+"List = null;\n";
	    generateAdapter+=generateAdapter+"\tprivate "+modelObject.getName().substring(0,1).toUpperCase()+modelObject.getName().substring(1)+" "+modelObject.getName()+" = null;\n";
	    generateAdapter+=generateAdapter+"\tprivate Context ctx = null;\n";
	    generateAdapter+=generateAdapter+"\tpublic "+modelObject.getName().substring(0,1).toUpperCase()+modelObject.getName().substring(1)+"Adapter(Context ctx,"
	    +"List<"+modelObject.getName().substring(0,1).toUpperCase()+modelObject.getName().substring(1)+">"+modelObject.getName()+"List){\n";
	    generateAdapter+=generateAdapter+"\t\tthis.ctx = ctx;\n";
	    generateAdapter+=generateAdapter+"\t\tthis."+modelObject.getName()+"List = "+modelObject.getName()+"List;\n\t}\n";
	    generateAdapter+=generateAdapter+"\t@Override\n\tpublic int getCount() {\n\t\treturn "+modelObject.getName()+"List = "+modelObject.getName()+"List.size();\n\t}\n\n";
	    generateAdapter+=generateAdapter+"\t@Override\n\tpublic Object getItem(int pos) {\n\t\treturn "+modelObject.getName()+"List = "+modelObject.getName()+"List.get(pos);\n\t}\n\n";
	    generateAdapter+=generateAdapter+"\t@Override\n\tpublic long getItemId(int arg0) {\n\t\treturn 0;\n\t}\n\n";
	    generateAdapter+=generateAdapter+"\t@Override\n\tpublic View getView(int pos, View arg1, ViewGroup arg2) {\n";
	    generateAdapter+=generateAdapter+"\t\t"+modelObject.getName()+" = "+modelObject.getName().substring(0,1).toUpperCase()+modelObject.getName().substring(1)+"List.get(pos);\n";
	    generateAdapter+=generateAdapter+"\t\tLinearLayout holder = new LinearLayout(ctx);\n\t\tholder.setOrientation(LinearLayout.VERTICAL);\n\n";
	    generateAdapter+=generateAdapter+"";
	    int childObjectListSize = childObjectList.size();
	    for(int j = 0; j < childObjectListSize; j++){
		childObject = childObjectList.get(j);
		attributeObjectList = childObject.getAttributeList();
		attributeObject = returnSpecificValue(attributeObjectList,"colname");
		generateAdapter+=generateAdapter+"\t\tTextView tv"+j+" = new TextView(ctx);\n";						
		generateAdapter+=generateAdapter+"\t\ttv"+j+".setText(\"" +attributeObject.getValue()+ "\");\n";
		generateAdapter+=generateAdapter+"\t\tholder.addView(tv"+j+");\n";						
	    }
	    generateAdapter+=generateAdapter+"\t\treturn holder;\n\t}\n\n}";
	}		
	return generateAdapter;
    }

    public AttributeObject returnSpecificValue(
	    List<AttributeObject> attributeObjectList, String value) {
	int attributeObjectListSize = attributeObjectList.size();
	for (int i = 0; i < attributeObjectListSize; i++) {
	    attributeObject = attributeObjectList.get(i);
	    if (attributeObject.getKey().equals(value)
		    || attributeObject.getValue().equals(value))
		return attributeObject;
	}
	return null;
    }

    public String returnTranslatedAttributeDataType(String type) {
	if (type.equals("") || type.equals(null))
	    return null;
	else if (type.equals("int"))
	    return "Int";
	else if (type.equals("varchar") || type.equals("text"))
	    return "String";
	return null;
    }

}
