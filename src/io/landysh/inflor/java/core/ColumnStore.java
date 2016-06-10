package io.landysh.inflor.java.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import io.landysh.inflor.java.core.proto.AnnotatedVectorMessage.AnnotatedVectorsProto;
import io.landysh.inflor.java.core.proto.AnnotatedVectorMessage.AnnotatedVectorsProto.Keyword;
import io.landysh.inflor.java.core.proto.AnnotatedVectorMessage.AnnotatedVectorsProto.Vector;


public class ColumnStore {
	
	//file details
	public  String 							UUID;
	private Hashtable<String, String> 		keywords;
	private Hashtable<String, FCSVector> 	columnData;
	
	//data properties
	private Integer 						columnCount;
	private Integer							rowCount =-1;

	/** Store keywords and numeric columns in a persistable object.  
	 * @param inKeywords some annotation to get started with. Must be a valid FCS header but may be added to later.
	 */
	public ColumnStore(Hashtable<String, String> keywords, String[] columnNames) throws Exception {
		this.keywords = keywords;
		columnData = new Hashtable<String, FCSVector>();
		for (String name:columnNames){
			columnData.put(name, new FCSVector(name){});
		}
	}


	public ColumnStore() {
		//minimal constructor, use with .load()
	}
	
	public String getKeywordValue(String keyword) {
		return keywords.get(keyword).trim();
	}
	
	public Hashtable<String, String> getKeywords() {
		return keywords;
	}

	public void  setData(Hashtable<String, FCSVector> allData) {
		 columnData = allData;
		 rowCount = allData.get(getColumnNames()[0]).getData().length;
		 columnCount = getColumnNames().length;
	}
	
	public String[] getColumnNames() {
		int size = columnData.keySet().size();
		String[] newArray = new String[size];
		String[] columnNames = columnData.keySet().toArray(newArray);
		return columnNames;
	}
	
	public void save(FileOutputStream out) throws IOException {
		//create the builder
		AnnotatedVectorsProto.Builder messageBuilder = AnnotatedVectorsProto.newBuilder();
		
		//Should add UUID field here.
		
		//add the vector names.
		for (String s: getColumnNames()){
			messageBuilder.addVectorNames(s);
		}
		
		//add the keywords.
		for (String s: keywords.keySet()){
			String key = s;
			String value = keywords.get(s);
			AnnotatedVectorsProto.Keyword.Builder keyBuilder = AnnotatedVectorsProto.Keyword.newBuilder();
			keyBuilder.setKey(key);
			keyBuilder.setValue(value);
			AnnotatedVectorsProto.Keyword keyword = keyBuilder.build();
			messageBuilder.addKeywords(keyword);
			}
		//add the data.
		Integer size = getColumnNames().length;
		for (int i=0;i<size;i++){
			AnnotatedVectorsProto.Vector.Builder vectorBuilder = AnnotatedVectorsProto.Vector.newBuilder();
			String name = getColumnNames()[i];
			double[] vectorArray = columnData.get(name).getData();
			
			vectorBuilder.setName(name);
			for (int j=0;j<vectorArray.length;j++){
				vectorBuilder.addArray(vectorArray[j]);
			}
			AnnotatedVectorsProto.Vector v = vectorBuilder.build();
			messageBuilder.addVectors(v);
			}
		
		//build the message
		AnnotatedVectorsProto AVSProto = messageBuilder.build();
		byte[] message = AVSProto.toByteArray();
		out.write(message);
		out.flush();
	}

	public static ColumnStore load(FileInputStream input) throws Exception {
		byte[] buffer = new byte[input.available()];
		input.read(buffer);
		AnnotatedVectorsProto message = AnnotatedVectorsProto.parseFrom(buffer);
		
		//Load the keywords
		Hashtable <String, String> keywords = new Hashtable <String, String>();
		for (int i=0;i<message.getKeywordsCount();i++){
			Keyword keyword = message.getKeywords(i);
			String key = keyword.getKey();
			String value = keyword.getValue();
			keywords.put(key, value);
		}
		ColumnStore columnStore = new ColumnStore(keywords, new String[]{});
		//Load the vectors
		int columnCount = message.getVectorsCount();
		String[] vectorNames = new String[columnCount];
		// problem is here
		for (int j=0;j<columnCount;j++){
			Vector vector = message.getVectors(j);
			String   key = vector.getName();
			vectorNames[j] = key;
			double[] values = new double[vector.getArrayCount()];
			for (int i=0;i<values.length;i++){
				values[i] = vector.getArray(i);
			}
			columnStore.addColumn(key, values);
		}
		return columnStore;
	}

	public double[] getColumn(String name) {
		if (name  != null){
			return columnData.get(name).getData();
		} else {
			NullPointerException npe = new NullPointerException("Input null.");
			npe.printStackTrace();
			throw npe;
		}
	}

	public int getRowCount() {
		return rowCount;
	}

	public double[] getRow(int index) {
		double[] row = new double[columnCount];
		int i=0;
		for (String name:getColumnNames()){
			row[i] = columnData.get(name).getData()[index];
			i++; 
		}
		return row;
	}

	public void addColumn(String name, double[] data) {
		if (rowCount== -1 || rowCount==data.length){
			rowCount = data.length;
			FCSVector newVector = new FCSVector(name);
			newVector.setData(data);
			columnData.put(name, newVector);
			columnCount = getColumnCount();
		} else {
			throw new IllegalStateException("New column does not match frame size: " + rowCount.toString());
		}
	}
	
	public void setRowCount(int count) {
		rowCount = count;
	}

	public int getColumnCount() {
		return getColumnNames().length;
	}

	public Hashtable<String, FCSVector> getData() {
		return columnData;
	}
}