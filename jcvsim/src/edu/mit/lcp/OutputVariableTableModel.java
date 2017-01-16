package edu.mit.lcp;

import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.table.AbstractTableModel;

class OutputVariableTableModel extends AbstractTableModel {

    private static final int COL_NAME     = 0;
    private static final int COL_UNITS    = 1;
    private static final int COL_SELECTED = 2;

    private static final String[] columnNames = {
	"Name", "Units", "Select"
    };

    private final List<SimulationOutputVariable> outputList;
    private final Hashtable<SimulationOutputVariable, Boolean> outputsSelected; 
    private final List<SimulationOutputVariable> displayList;
    private String _categoryFilter;
    private String _typeFilter;
    
    public OutputVariableTableModel(List<SimulationOutputVariable> list) {
	outputList = list;
	displayList = new ArrayList(outputList);
	outputsSelected = new Hashtable<>();
	for (SimulationOutputVariable v: outputList)
	    outputsSelected.put(v, false);
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return displayList.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Object value;
	switch (col) {
	case COL_NAME     : value = displayList.get(row).getDescription(); break;
	case COL_UNITS    : value = displayList.get(row).getUnits(); break;
	case COL_SELECTED : value = isOutputSelected(displayList.get(row)); break;
	default: value = "Invalid Column Lookup"; break;
	}
	return value;
    }

    @Override
    public Class getColumnClass(int c) {
	return getValueAt(0, c).getClass();
    }
    
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
	if (col == COL_SELECTED) {
	    return true;
         } else {
	    return false;
	}
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
	setOutputSelected(displayList.get(row), (Boolean)value);
	fireTableCellUpdated(row, col);
    }

    public void setCategoryFilter(String category) {
	_categoryFilter = category;
	updateDisplayList();
    }

    public void setTypeFilter(String type) {
	_typeFilter = type;
	updateDisplayList();
    }

    private void updateDisplayList() {
	System.out.println("updateDisplayList(): category: " + _categoryFilter 
			   + " -- type: " + _typeFilter);
	displayList.clear();

	for (SimulationOutputVariable v: outputList) {
	    if ( (_categoryFilter == null) || (_categoryFilter.equals(v.getCategory())) ) {
		if ( (_typeFilter == null) || (_typeFilter.equals(v.getType())) ) {
		    displayList.add(v);
		}
	    }
	}
	fireTableDataChanged();
    }

    public List<String> getCategoryNames() {
	ArrayList<String> names = new ArrayList<>();
	String cat;
	for (SimulationOutputVariable v: outputList) {
	    cat = v.getCategory();
	    if (!names.contains(cat))
		names.add(cat);
	}
	return names;
    }


    public List<String> getOutputTypeNames() {
	return getFilteredOutputTypeNames(null);
    }
    
    public List<String> getFilteredOutputTypeNames() {
	return getFilteredOutputTypeNames(_categoryFilter);
    }
    
    public List<String> getFilteredOutputTypeNames(String filter) {
	ArrayList<String> names = new ArrayList<>();
	String type;
	for (SimulationOutputVariable v: outputList) {
	    if ( (filter == null) || (filter.equals(v.getCategory())) ) {
		type = v.getType();
		if (!names.contains(type))
		    names.add(type);
	    }
	}
	return names;
    }

    public Boolean isOutputSelected(SimulationOutputVariable p) {
	return outputsSelected.get(p);
    }

    public void setOutputSelected(SimulationOutputVariable v, Boolean sel) {
	outputsSelected.put(v,sel);
	int _idx = displayList.indexOf(v);
	if (_idx >= 0)
	    fireTableCellUpdated(_idx, COL_SELECTED);
    }

    public List<SimulationOutputVariable> getSelectedOutputList() {
	ArrayList<SimulationOutputVariable> _ol = new ArrayList<>();
	for (SimulationOutputVariable v: outputsSelected.keySet())
	    if (isOutputSelected(v))
		_ol.add(v);

	return _ol;
    }

    public List<SimulationOutputVariable> getOutputList() {
	return outputList;
    }
    
    public List<SimulationOutputVariable> getFilteredOutputList() {
	return displayList;
    }

}
