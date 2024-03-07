import java.util.List;

/**
 * @author Saravanan Balakumar
 * @version 1.0
 */
public class ProbabilitiesPojo {
	private List<InnerSymbolsPojo> standard_symbols;
	private InnerSymbolsPojo bonus_symbols;
	public List<InnerSymbolsPojo> getStandard_symbols() {
		return standard_symbols;
	}
	public void setStandard_symbols(List<InnerSymbolsPojo> standard_symbols) {
		this.standard_symbols = standard_symbols;
	}
	public InnerSymbolsPojo getBonus_symbols() {
		return bonus_symbols;
	}
	public void setBonus_symbols(InnerSymbolsPojo bonus_symbols) {
		this.bonus_symbols = bonus_symbols;
	}
	@Override
	public String toString() {
		return "ProbabilitiesPojo [standard_symbols=" + standard_symbols + ", bonus_symbols=" + bonus_symbols + "]";
	}
	
	
}
