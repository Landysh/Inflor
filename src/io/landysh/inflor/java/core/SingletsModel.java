package io.landysh.inflor.java.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class SingletsModel {
	
	String[] 					 			modelInputColumnNames 	= null;
	String[][] 					 			modelInputVectorPairs 	= null;
	Hashtable<String, ArrayList<Double>> 	modelVectors 			= null;
	
	public SingletsModel(String[] columnNames) throws Exception {
		modelInputColumnNames = findUsableColumnNames(columnNames);
		modelInputVectorPairs  = createVectorPairs(modelInputColumnNames);
		if (modelInputColumnNames == null){
			Exception e = new Exception("No valid pulse shape columns identified. Use a trailing -A, -H, -W or height, width and area");
			e.printStackTrace();
			throw e;
		}
	}
	
	public String[] getModelColumnNames() {
		return modelInputColumnNames;
	}
	
	private String[][] createVectorPairs(String[] inputNames) {
		/**
		 *  This method re-organizes any potentially interesting column names into groups for subsequent iteration.
		 */
		int masterParameterCount = 0;
		for (int i=0;i<inputNames.length;i++){
			// TODO the interesting part.
		}
		String[][] vectorSets = new String[masterParameterCount][3];
		
		return vectorSets;
	}

	//TODO No tests
	private String[] findUsableColumnNames(String[] columnNames) {
		/** Creates a flat list of column names to use for subsequent doublet descrimination. 
		 */
		ArrayList <String> scatterParameters = new ArrayList<String>();		
		String[] scatterRegexList = {"ssc*","fsc*","*scatter*"};
		for (String s: columnNames){
			for (String regex:scatterRegexList){
				if (s.matches(regex)){
					scatterParameters.add(s);
				}
			}
		}
		final String[] usableColumnNames = (String[]) scatterParameters.toArray();
		return usableColumnNames;
	}

	public String[] getSingletsParameters() {
			return modelInputColumnNames;
	}

	public void setData(Hashtable<String, ArrayList<Double>> data) {
		modelVectors = data;	
	}

	public void generateModel() {
		if(modelVectors!=null && modelInputVectorPairs !=null && modelInputColumnNames !=null){
			for (String[] pair: modelInputVectorPairs);
			//TODO keep going
		}
	}

	public boolean scoreRow(HashMap<String, Double> row) {
		Boolean isSinglet = false;
		
		// TODO Auto-generated method stub
		return isSinglet;
	}
}
