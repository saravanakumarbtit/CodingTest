import java.util.List;

/**
 * @author Saravanan Balakumar
 * @version 1.0
 */
public class WinCombinationsPojo {
	private double reward_multiplier;
	private String when;
	private int count;
	private String group;
	private List<List<String>> covered_areas;
	public double getReward_multiplier() {
		return reward_multiplier;
	}
	public void setReward_multiplier(double reward_multiplier) {
		this.reward_multiplier = reward_multiplier;
	}
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public List<List<String>> getCovered_areas() {
		return covered_areas;
	}
	public void setCovered_areas(List<List<String>> covered_areas) {
		this.covered_areas = covered_areas;
	}
	@Override
	public String toString() {
		return "WinCombinationsPojo [reward_multiplier=" + reward_multiplier + ", when=" + when + ", count=" + count
				+ ", group=" + group + ", covered_areas=" + covered_areas + "]";
	}
	
	
}
