package com.xpert.androidcrudgenerator.generators;

import java.util.List;

import com.xpert.androidcrudgenerator.elementobjects.AttributeObject;
import com.xpert.androidcrudgenerator.elementobjects.ChildObject;
import com.xpert.androidcrudgenerator.elementobjects.ModelObject;
import com.xpert.androidcrudgenerator.utils.FileUtils;

public class DBHelperGenerator {

    private static ModelObject modelObject = null;
    private static ChildObject childObject = null;
    private static AttributeObject attributeObject = null;

    private static List<ModelObject> modelObjectList = null;
    private static List<ChildObject> childObjectList = null;
    private static List<AttributeObject> attributeObjectList = null;

    private static final int COLUMN_KEY = 0;
    private static final int COLUMN_VALUE = 1;

    private String[] entityAttributes = new String[] { "tablename", "colid",
	    "colname", "type", "length", "autoincrement", "primarykey" };
    private String dbHelperClass = "";
    private boolean tableDefinitionsCreated = false;

    public DBHelperGenerator() {
    }

    public DBHelperGenerator(List<ModelObject> modelObjectList) {
	this.modelObjectList = modelObjectList;
    }

    public void generateDbHelperClass(List<ModelObject> modelObjectList) {
	dbHelperClass = "import android.content.Context;\nimport android.database.SQLException;\n"
	    + "import android.database.sqlite.SQLiteDatabase;\nimport android.database.sqlite.SQLiteOpenHelper;\n"
	    + "import android.database.sqlite.SQLiteDatabase.CursorFactory;\nimport android.util.Log;\n"
	    + "public class DbHelper {\n"
	    + "\tpublic static final String DEVICE_ALERT_ENABLED_ZIP = \"DAEZ99\";\n";

	modelObject = modelObjectList.get(0);
	dbHelperClass += "\tpublic static final String DB_NAME = \""
	    + modelObject.getRootName().toLowerCase() + ".db\";\n";
	if (tableDefinitionsCreated == false)
	    dbHelperClass = generateTableDefinition(dbHelperClass,
		    modelObjectList);

	dbHelperClass += "\tpublic static final int DB_VERSION = 3;\n\tprivate static final String CLASSNAME = DbHelper.class.getSimpleName();\n"
	    + "\tprivate static String TAG = \"DBHelper\";\n\tprivate static DbHelper dbHelper = null;\n\tprivate SQLiteDatabase db = null;\n";

	dbHelperClass+="\tpublic SQLiteDatabase getDb() {\n\t\treturn db;\n\t}\n\tpublic void setDb(SQLiteDatabase db) {\n\t\tthis.db = db;\n\t}\n"+
	"\tprivate final DBOpenHelper dbOpenHelper;\n\tpublic DbHelper(Context context) {\n\t\t	this.dbOpenHelper = new DBOpenHelper(context, \"WR_DATA\", null, 1);\n"+
	"\t\tthis.establishDB();\n\t}\n";

	dbHelperClass+="\tpublic static DbHelper getInstance(Context context) {\n\t\tif (dbHelper == null) {\n\t\t\tdbHelper = new DbHelper(context);\n\t\t}\n\t\treturn dbHelper;\n\t}\n"+
	"\tprivate void establishDB() {\n\t\tif (this.db == null) {\n\t\t\tthis.db = this.dbOpenHelper.getWritableDatabase();\n\t\t}\n\t}\n";

	dbHelperClass+="\tpublic void close(SQLiteDatabase db) {\n\t\ttry {\n\t\t\tdb.close();\n\t\t} catch (SQLException e) {\n\t\t\tLog.d(TAG, \"close exception: \" + e.getLocalizedMessage());\n"+
	"\t\t}\n\t}\n";
	dbHelperClass = generateTableCreationQuery(dbHelperClass, modelObjectList);

	dbHelperClass+="\n\t\tpublic DBOpenHelper(Context context, String name,CursorFactory factory, int version) {\n"+
	"\t\t\tsuper(context, DbHelper.DB_NAME, null, DbHelper.DB_VERSION);\n\t\t}\n";

	dbHelperClass+="\t\t@Override\n\t\tpublic void onCreate(SQLiteDatabase db) {\n\t\t\ttry {";
	int modelObjectListSize = modelObjectList.size();
	for(int i=0;i<modelObjectListSize;i++){
	    modelObject = modelObjectList.get(i);
	    dbHelperClass+="\n\t\t\t\tdb.execSQL(DBOpenHelper.CREATE_TABLE_"+modelObject.getName().toUpperCase()+");";			
	}
	dbHelperClass+="\n\t\t\t} catch (SQLException e) {\n\t\t\t\tLog.e(\"\", DbHelper.CLASSNAME, e);\n\t\t\t}\n\t\t}\n";
	dbHelperClass+="\t\t@Override\n\t\tpublic void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {\n";
	for(int i=0;i<modelObjectListSize;i++){
	    modelObject = modelObjectList.get(i);
	    if(i<modelObjectListSize-1)
		dbHelperClass+="\t\t\tdb.execSQL(\"DROP TABLE IF EXISTS\" + DbHelper.DB_TABLE_"+modelObject.getName().toUpperCase()+");\n\t\t\tthis.onCreate(db);\n";
	    else
		dbHelperClass+="\t\t\tdb.execSQL(\"DROP TABLE IF EXISTS\" + DbHelper.DB_TABLE_"+modelObject.getName().toUpperCase()+");\n\t\t\tthis.onCreate(db);";
	}
	dbHelperClass+="\n\t\t}\n\t\t@Override\n\t\tpublic void onOpen(SQLiteDatabase db) {\n\t\t\tsuper.onOpen(db);\n\t\t}\n\t}\n}";
	FileUtils.writeGeneratedClassToFile(dbHelperClass);
	dbHelperClass="";

    }

    /**
     * 
     * @param dbHelperClass
     * @param modelObjectList
     * @return
     */
    public String generateTableDefinition(String dbHelperClass,
	    List<ModelObject> modelObjectList) {
	int modelObjectListSize = modelObjectList.size();
	for (int i = 0; i < modelObjectListSize; i++) {
	    modelObject = modelObjectList.get(i);
	    dbHelperClass += "\tpublic static final String DB_TABLE_"
		+ modelObject.getName().toUpperCase() + " = \""
		+ modelObject.getName().substring(0, 1).toUpperCase()
		+ modelObject.getName().substring(1) + "\";\n";
	}
	tableDefinitionsCreated = true;
	return dbHelperClass;
    }

    /**
     * 
     * @param dbHelperClass
     * @param modelObjectList
     * @return
     */
    public String generateTableCreationQuery(String dbHelperClass, List<ModelObject> modelObjectList){
	dbHelperClass+="\tprivate static class DBOpenHelper extends SQLiteOpenHelper {\n";
	int modelObjectListSize = modelObjectList.size();
	for(int i=0;i<modelObjectListSize;i++){
	    modelObject = modelObjectList.get(i);
	    dbHelperClass+="\t\tprivate static final String CREATE_TABLE_"+modelObject.getName().toUpperCase()+" = \"create table \"+"+
	    "DbHelper.DB_TABLE_"+modelObject.getName().toUpperCase()+"+\"(_id integer primary key autoincrement,";
	    childObjectList = modelObject.getChildObjectList();
	    int childObjectListSize = childObjectList.size();
	    for(int j=1;j<childObjectListSize;j++){
		childObject = childObjectList.get(j);
		attributeObjectList = childObject.getAttributeList();
		if(j<childObjectListSize-1){
		    attributeObject = returnSpecificValue(attributeObjectList,"colname");
		    dbHelperClass+=""+attributeObject.getValue()+" ";
		    attributeObject = returnSpecificValue(attributeObjectList,"type");
		    dbHelperClass+=""+returnTranslatedAttributeDataType(attributeObject.getValue())+",";					
		}else{
		    attributeObject = returnSpecificValue(attributeObjectList,"colname");
		    dbHelperClass+=""+attributeObject.getValue()+" ";
		    attributeObject = returnSpecificValue(attributeObjectList,"type");
		    dbHelperClass+=""+returnTranslatedAttributeDataType(attributeObject.getValue())+");\";\n";
		}							
	    }			

	}		
	return dbHelperClass;
    }

    public AttributeObject returnSpecificValue(List<AttributeObject> attributeObjectList, String value){
	int attributeObjectListSize = attributeObjectList.size();
	for(int i=0;i<attributeObjectListSize;i++){
	    attributeObject = attributeObjectList.get(i);
	    if(attributeObject.getKey().equals(value)||attributeObject.getValue().equals(value))
		return attributeObject;			
	}
	return null;
    }

    public String returnTranslatedAttributeDataType(String type) {
	if (type.equals("") || type.equals(null))
	    return null;
	else if (type.equals("int"))
	    return "integer";
	else if (type.equals("primarykey"))
	    return "primary key";
	else if (type.equals("boolean"))
	    return "boolean";
	else if (type.equals("date"))
	    return "date";
	else if (type.equals("varchar"))
	    return "varchar";
	else if (type.equals("text"))
	    return "text";
	return null;
    }


}
