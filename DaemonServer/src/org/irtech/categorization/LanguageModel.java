/**
 * Copyright (c) 2012, University of Delaware
 * All rights reserved.
 *
 * @author: Sergio Pino
 * @author: Keith Elliott
 * Website: http://www.eecis.udel.edu/~pinogal, http://www.eecis.udel.edu/~kelliott
 * emails  : sergiop@udel.edu - kelliott@udel.edu
 * Date   : May, 2012
 *
 */

package org.irtech.categorization;

import javax.script.ScriptException;

import org.python.core.Py;
import org.python.core.PySystemState;
import org.python.core.PyString;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

/**
 * This class access to the classes in python that
 * handled the LM operations
 * 
 * @author pinogal
 *
 */
public class LanguageModel {
	
	private PythonInterpreter interp;
	
	/**
	 * 
	 * @param fileHealthy path to the healthy article
	 * @param fileUnhealthy path to the unhealthy article
	 */
	public LanguageModel(String fileHealthy, String fileUnhealthy) {
		
		// linking the jython interpreter
		PySystemState engineSys = new PySystemState();
		engineSys.path.append(Py.newString("tools"));
		Py.setSystemState(engineSys);

        interp = new PythonInterpreter();
        
        interp.exec("from LanguageModel import LanguageModel");
		
		// assigning the path values
        interp.set("pathHTra", new PyString(fileHealthy));
        interp.set("pathUhTra", new PyString(fileUnhealthy));

        interp.exec("print type(pathHTra)");
		// open the files
        interp.exec("fileHTra = open(pathHTra)");
        interp.exec("fileUhTra = open(pathUhTra)");
		
		// reading the files
        interp.exec("textHTra = fileHTra.read()");
        interp.exec("textUhTra = fileUhTra.read()");

		// creating the language model in python
        interp.exec("obj = LanguageModel()");
        interp.exec("obj.addCategory('healthy', textHTra)");
        interp.exec("obj.addCategory('unhealthy', textUhTra)");
        
	}
	
	/**
	 * get the most likely category
	 * 
	 * @param text
	 * @return
	 */
	public Object[] getClassification(String text) {
		
		interp.set("text", new PyString(text));
		interp.exec("cat = obj.getClassification(text)");
		PyTuple obj = (PyTuple) interp.get("cat");
		
		return obj.toArray();
	}
	
	public static void main(String[] args) throws ScriptException {
		
//		LanguageModel lm = new LanguageModel("data/homegrown.txt", 
//				"data/kates.txt");
		LanguageModel lm = new LanguageModel("data/healthy.txt", 
				"data/unhealthy.txt");
		Object[] data = lm.getClassification("American Restaurant, Rock Club, Bar, Vegetarian or Vegan Restaurant");
		
		System.out.println(data[0]);
		System.out.println(data[1]);
		System.out.println(data[2]);
		
		System.out.println("done");
    }
}
