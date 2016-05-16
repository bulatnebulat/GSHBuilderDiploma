package galois;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ReadCSV	 {
	
	private String path;
	private List<String[]> data;
	private List<List<Integer>> binaryMatrix;
	private List<Concept> topicConcepts;
	private List<Concept> docConcepts;
	
	public ReadCSV(String path) {
		this.path = path;
	}
	
	public void extractNumbers() {	
		binaryMatrix = new ArrayList<List<Integer>>();
		List<Integer> bufer;
		for (String[] s : data) {
			bufer = new ArrayList<>();	
			for (String ss : s) {
				try { 
					int sint = Integer.parseInt(ss);
					bufer.add(sint);				
				} 
				catch (Exception ex) {
					continue;
				}
			}
			if (bufer.size() != 0) {
				binaryMatrix.add(bufer);
			}
		}
	}
	
	public void read() {
		BufferedReader br = null;
		String line = "";
		int i = 0;
		data = new ArrayList<String[]>();
		try {
			br = new BufferedReader(new FileReader(this.path));
			while ((line = br.readLine()) != null) {
				data.add(line.split(","));
				i++;
			}
		}
		catch (Exception ex) {
			
		}
		extractNumbers();
	}
	
	public void show(Table t) {
		for (int i = 0; i < data.get(0).length + 1; i++) {
			TableColumn column = new TableColumn(t, SWT.NULL);
		}
		TableItem item = new TableItem(t, SWT.NONE);
		int j = 0;
		item.setText("");
		item.setText(j, "");
		j++;
		for (String[] s : data) {
			item = new TableItem(t, SWT.NONE);
			for (String ss : s) {
				if (j == 0) {
					item.setText(ss);
				}
		        item.setText(j, ss);
		        j++;
			}
			j = 0;
		}
		 for (int loopIndex = 0; loopIndex < data.get(0).length + 1; loopIndex++) {
		      t.getColumn(loopIndex).pack();
		    }
	}

	public void extractConcepts(String threadName) {		
		if (threadName.equals("Thread-2")) {
			docConcepts = new ArrayList<Concept>();
			List<Integer> bufer = new ArrayList<Integer>();
			for (int i = 0; i < binaryMatrix.size(); i++) {
				for (int j = 0; j < binaryMatrix.get(i).size(); j++) {
					if (binaryMatrix.get(i).get(j) == 1) {
						bufer.add(j);
					}
				}
				docConcepts.add(new Concept());
				checkIfConsists(bufer, i, threadName);
				for (int k = 0; k < bufer.size(); k++) {
					docConcepts.get(i).addAttr(bufer.get(k));
				}
				docConcepts.get(i).addOriginObj(i);
				bufer.clear();
			}
		} else {
			topicConcepts = new ArrayList<Concept>();
			List<Integer> bufer = new ArrayList<Integer>();
			for (int i = 0; i < binaryMatrix.get(0).size(); i++) {
				for (int j = 0; j < binaryMatrix.size(); j++) {
					if (binaryMatrix.get(j).get(i) == 1) {
						bufer.add(j);
					}
				}
				topicConcepts.add(new Concept());
				checkIfConsists(bufer, i, threadName);
				for (int k = 0; k < bufer.size(); k++) {
					topicConcepts.get(i).addObj(bufer.get(k));
				}
				topicConcepts.get(i).addOriginAttr(i);
				bufer.clear();
			}
		}
	}
	
	public void checkIfConsists(List<Integer> bufer, int except, String threadName) {
		if (threadName.equals("Thread-2")) {
			for (int i = 0; i < binaryMatrix.size(); i++) {
				boolean checker = true;
				for (int j = 0; j < bufer.size(); j++) {
					if (binaryMatrix.get(i).get(bufer.get(j)) != 1) {
						checker = false;
						break;
					}
				}
				if (checker) {
					docConcepts.get(except).addObj(i);
				}
			}
		} else {
			for (int i = 0; i < binaryMatrix.get(0).size(); i++) {
				boolean checker = true;
				for (int j = 0; j < bufer.size(); j++) {
					if (binaryMatrix.get(bufer.get(j)).get(i) != 1) {
						checker = false;
						break;
					}
				}
				if (checker) {
					topicConcepts.get(except).addAttr(i);
				}
			}
		}
	}
	
	public void deleteEqual() {
		Hashtable<List<Integer>, Concept> hs = new Hashtable<>();
		for(int i = 0; i < topicConcepts.size(); i++) {	
			if(hs.containsKey(topicConcepts.get(i).getObj())) {
				Concept c = hs.get(topicConcepts.get(i).getObj());
				c.getOriginObj().addAll(topicConcepts.get(i).getOriginObj());
				hs.put(topicConcepts.get(i).getObj(), c);
			}
			/*Concept c = topicConcepts.get(i);
			String sRepr = "";
			for(int j = 0; j < c.getObj().size(); j++) {
				sRepr = c.getObj().get(j).toString() + "_";
			}*/
			else {
				hs.put(topicConcepts.get(i).getObj(), topicConcepts.get(i));			
			}
			
		}
		for(int i = 0; i < docConcepts.size(); i++) {
			
			/*Concept c = docConcepts.get(i);
			String sRepr = "";
			for(int j = 0; j < c.getObj().size(); j++) {
				sRepr = c.getObj().get(j).toString() + "_";
			}*/
			if(hs.containsKey(docConcepts.get(i).getObj())) {
				Concept c = hs.get(docConcepts.get(i).getObj());
				c.getOriginAttr().addAll(docConcepts.get(i).getOriginAttr());
				c.getOriginObj().addAll(docConcepts.get(i).getOriginObj());
				hs.put(docConcepts.get(i).getObj(), c);
			}
			else {
				hs.put(docConcepts.get(i).getObj(), docConcepts.get(i));
			}
		}
	}

}
