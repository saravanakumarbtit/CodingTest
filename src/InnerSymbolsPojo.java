import java.util.Map;

/**
 * @author Saravanan Balakumar
 * @version 1.0
 */
public class InnerSymbolsPojo {
	private String column;
	private String row;
	private Map<String, Integer> symbols;
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public Map<String, Integer> getSymbols() {
		return symbols;
	}
	public void setSymbols(Map<String, Integer> symbols) {
		this.symbols = symbols;
	}
	@Override
	public String toString() {
		return "InnerSymbolsPojo [column=" + column + ", row=" + row + ", symbols=" + symbols + "]";
	}
	
	
}
