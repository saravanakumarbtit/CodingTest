/**
 * @author Saravanan Balakumar
 * @version 1.0
 */
public class SymbolsPojo {
	private String reward_multiplier;
	private String type;
	private String impact;
	private int extra;
	public String getReward_multiplier() {
		return reward_multiplier;
	}
	public void setReward_multiplier(String reward_multiplier) {
		this.reward_multiplier = reward_multiplier;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImpact() {
		return impact;
	}
	public void setImpact(String impact) {
		this.impact = impact;
	}
	public int getExtra() {
		return extra;
	}
	public void setExtra(int extra) {
		this.extra = extra;
	}
	@Override
	public String toString() {
		return "SymbolsPojo [reward_multiplier=" + reward_multiplier + ", type=" + type + ", impact=" + impact
				+ ", extra=" + extra + "]";
	}
	
	
}
