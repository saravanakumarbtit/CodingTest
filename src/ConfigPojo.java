import java.util.Map;

/**
 * ConfigPojo 
 * @author Saravanan Balakumar
 * @version 1.0
 */
public class ConfigPojo {
	private int columns;
	private int rows;
	private Map<String, SymbolsPojo> symbols;
	private ProbabilitiesPojo probabilities;
	private Map<String, WinCombinationsPojo> win_combinations;
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public Map<String, SymbolsPojo> getSymbols() {
		return symbols;
	}
	public void setSymbols(Map<String, SymbolsPojo> symbols) {
		this.symbols = symbols;
	}
	public ProbabilitiesPojo getProbabilities() {
		return probabilities;
	}
	public void setProbabilities(ProbabilitiesPojo probabilities) {
		this.probabilities = probabilities;
	}
	public Map<String, WinCombinationsPojo> getWin_combinations() {
		return win_combinations;
	}
	public void setWin_combinations(Map<String, WinCombinationsPojo> win_combinations) {
		this.win_combinations = win_combinations;
	}
	@Override
	public String toString() {
		return "ConfigPojo [columns=" + columns + ", rows=" + rows + ", symbols=" + symbols + ", probabilities="
				+ probabilities + ", win_combinations=" + win_combinations + "]";
	}
	
	
}
