import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

/**
 * CodingTestMain is the main class to generate random array and calculate the
 * reward based on the generated array and inputed betting amount
 * 
 * @author Saravanan Balakumar
 * @version 1.0
 */
public class CodingTestMain {

	private static int betAmount;

	public static void main(String[] args) {
		
		String data = "";
//		String[][] resultArray = { { "A", "A", "B" }, { "A", "+1000", "B" }, { "A", "A", "B" } };
//		String[][] resultArray = { { "A", "B", "C" }, { "E", "B", "5x" }, { "F", "D", "C" } };
//		String[][] resultArray = { { "A", "F", "D" }, { "C", "C", "D" }, { "B", "E", "MISS" } };

		String commandOne = args[0];
		String valueOne = args[1];
		String commandTwo = args[2];
		String valueTwo = args[3];
		
		/** getting config.json file data and betting amount from command line*/
		if("--config".equalsIgnoreCase(commandOne)) {
			try {
				data = new String(Files
						.readAllBytes(Paths.get(String.valueOf(valueOne))));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		if("--betting-amount".equalsIgnoreCase(commandTwo)) {
			betAmount = Integer.parseInt(String.valueOf(valueTwo));
		}
		if("--config".equalsIgnoreCase(commandTwo)) {
			try {
				data = new String(Files
						.readAllBytes(Paths.get(String.valueOf(valueTwo))));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		if("--betting-amount".equalsIgnoreCase(commandOne)) {
			betAmount = Integer.parseInt(String.valueOf(valueOne));
		}

		/** Using Gson to form Java object using config.json file */
		Gson g = new Gson();
		ConfigPojo configPojo = g.fromJson(data, ConfigPojo.class);
		ResultPojo resultPojo = new ResultPojo();

		int totalNumberOfArrayItems = configPojo.getColumns() * configPojo.getRows();
		int standardSymbolProbabilities = configPojo.getProbabilities().getStandard_symbols().size();
		/** Check if the given standard symbol probabilities is equal to total array elements */
		if (totalNumberOfArrayItems == standardSymbolProbabilities) {

			double bonusSum = Double.parseDouble(String.valueOf(configPojo.getProbabilities().getBonus_symbols()
					.getSymbols().values().stream().mapToInt(Integer::valueOf).sum()));
			DistributedRandomNumberGenerator drngForBonus = new DistributedRandomNumberGenerator();
			for (Entry<String, Integer> bonusSymbol : configPojo.getProbabilities().getBonus_symbols().getSymbols()
					.entrySet()) {
				double value = Double.parseDouble(String.valueOf(bonusSymbol.getValue()));
				double probability = value / bonusSum; 
				drngForBonus.addNumber(bonusSymbol.getKey(), probability);
			}
			/** Start Random array generation code */
        	String[][] resultArray = new String[configPojo.getRows()][configPojo.getColumns()];
        	for(int i=0;i<standardSymbolProbabilities;i++) {
        		String randomBonusSymbol = drngForBonus.getDistributedRandomNumber();
        		InnerSymbolsPojo standardSymbol = configPojo.getProbabilities().getStandard_symbols().get(i);
        		int arrayRow = Integer.parseInt(standardSymbol.getRow());
        		int arrayColumn = Integer.parseInt(standardSymbol.getColumn());
        		Map<String, Integer> symbolsMap = standardSymbol.getSymbols();
        		DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
        		
        		double sum = Double.parseDouble(String.valueOf(symbolsMap.values().stream().mapToInt(Integer::valueOf).sum()));
        		
        		for (Entry<String, Integer> innerSymbol : symbolsMap.entrySet()) {
        			double value = Double.parseDouble(String.valueOf(innerSymbol.getValue()));
					double probability =  value/sum;
					drng.addNumber(innerSymbol.getKey(), probability); 
				}
        		String randomStandardSymbol = drng.getDistributedRandomNumber();
        		
        		if(resultArray[arrayRow][arrayColumn] == null) {
        			DistributedRandomNumberGenerator drngForFinal = new DistributedRandomNumberGenerator();
        			drngForFinal.addNumber(randomStandardSymbol, 0.9);
        			drngForFinal.addNumber(randomBonusSymbol, 0.1);
        			resultArray[arrayRow][arrayColumn]=drngForFinal.getDistributedRandomNumber();
        		} else {
        			System.out.println("Duplicate entry for array, check standard symbol probabilities in config.json");
        			System.exit(0);
        		}
        	}
        	/** End Random array generation code */

			List<Double> finalResultArray = new ArrayList<>();

			Map<Integer, Map<String, Double>> sameSymbolTimes = new HashMap<>();
			Map<String, WinCombinationsPojo> sameSymbolLinear = new HashMap<>();

			configPojo.getWin_combinations().entrySet().forEach(winCombinations -> {
				winCombinations.getKey();
				if ("same_symbols".equalsIgnoreCase(winCombinations.getValue().getWhen())) {
					sameSymbolTimes.put(winCombinations.getValue().getCount(), Collections
							.singletonMap(winCombinations.getKey(), winCombinations.getValue().getReward_multiplier()));
				}
				if ("linear_symbols".equalsIgnoreCase(winCombinations.getValue().getWhen())) {
					sameSymbolLinear.put(winCombinations.getKey(), winCombinations.getValue());
				}
			});


			// Check for linear
			sameSymbolLinear.entrySet().forEach(linearData -> {
				List<List<String>> coveredAreas = linearData.getValue().getCovered_areas();
				List<String> coveredAreaValueList = new ArrayList<>();
				coveredAreas.stream().forEach(area -> {
					area.stream().forEach(coordinates -> {
						String[] position = coordinates.split(":");
						if (position.length != 2) {
							System.out.println("Check config.json covered areas");
							System.exit(0);
						} else {
							if (coveredAreaValueList.isEmpty()) {
								coveredAreaValueList
										.add(resultArray[Integer.parseInt(position[0])][Integer.parseInt(position[1])]);
							} else if (!coveredAreaValueList.contains(
									resultArray[Integer.parseInt(position[0])][Integer.parseInt(position[1])])) {
								coveredAreaValueList
										.add(resultArray[Integer.parseInt(position[0])][Integer.parseInt(position[1])]);
							}
						}
					});
					if (coveredAreaValueList.size() == 1) {
						if (resultPojo.getApplied_winning_combinations() != null && resultPojo
								.getApplied_winning_combinations().containsKey(coveredAreaValueList.get(0))) {
							resultPojo.getApplied_winning_combinations().get(coveredAreaValueList.get(0))
									.add(linearData.getKey());
						} else if (resultPojo.getApplied_winning_combinations() == null) {
							Map<String, List<String>> winningCombinations = new HashMap<>();
							List<String> winningCombinationList = new ArrayList<>();
							winningCombinationList.add(linearData.getKey());
							winningCombinations.put(coveredAreaValueList.get(0), winningCombinationList);
							resultPojo.setApplied_winning_combinations(winningCombinations);
						} else {
							List<String> winningCombinationList = new ArrayList<>();
							winningCombinationList.add(linearData.getKey());
							resultPojo.getApplied_winning_combinations().put(coveredAreaValueList.get(0),
									winningCombinationList);
						}
					}
					coveredAreaValueList.clear();
				});
			});

//        	//Check for duplicate count of array elements and check for bonus symbols
			Map<String, Integer> arrayDuplicateElementsMap = new HashMap<>();
			List<String> applied_bonus_symbol = new ArrayList<>();
			for (int i = 0; i < configPojo.getRows(); i++) {
				for (int j = 0; j < configPojo.getColumns(); j++) {
					if (arrayDuplicateElementsMap.containsKey(resultArray[i][j])) {
						int count = arrayDuplicateElementsMap.get(resultArray[i][j]);
						arrayDuplicateElementsMap.put(resultArray[i][j], ++count);
					} else {
						arrayDuplicateElementsMap.put(resultArray[i][j], 1);
					}
					if (configPojo.getProbabilities().getBonus_symbols().getSymbols().keySet()
							.contains(resultArray[i][j])) {
						applied_bonus_symbol.add(resultArray[i][j]);
					}
				}
			}
			resultPojo.setApplied_bonus_symbol(applied_bonus_symbol.toString());
			
			//Based on the Array elements duplicate count apply winning combinations
			arrayDuplicateElementsMap.entrySet().stream().forEach(arrayDuplicate -> {
				if (sameSymbolTimes.get(arrayDuplicate.getValue()) != null) {
					Map<String, Double> innerMap = sameSymbolTimes.get(arrayDuplicate.getValue());
					if (resultPojo.getApplied_winning_combinations() != null
							&& resultPojo.getApplied_winning_combinations().containsKey(arrayDuplicate.getKey())) {
						resultPojo.getApplied_winning_combinations().get(arrayDuplicate.getKey())
								.add(innerMap.keySet().stream().findFirst().get());
					} else if (resultPojo.getApplied_winning_combinations() == null) {
						Map<String, List<String>> winningCombinations = new HashMap<>();
						List<String> winningCombinationList = new ArrayList<>();
						winningCombinationList.add(innerMap.keySet().stream().findFirst().get());
						winningCombinations.put(arrayDuplicate.getKey(), winningCombinationList);
						resultPojo.setApplied_winning_combinations(winningCombinations);
					} else {
						List<String> winningCombinationList = new ArrayList<>();
						winningCombinationList.add(innerMap.keySet().stream().findFirst().get());
						resultPojo.getApplied_winning_combinations().put(arrayDuplicate.getKey(),
								winningCombinationList);
					}
				}
			});

			// reward calculation for symbols
			if (resultPojo.getApplied_winning_combinations() !=null && !resultPojo.getApplied_winning_combinations().isEmpty()) {
				resultPojo.getApplied_winning_combinations().entrySet().stream().forEach(combinations -> {
					String symbolReward = configPojo.getSymbols().get(combinations.getKey()).getReward_multiplier();
					double result = betAmount * Double.parseDouble(symbolReward);
					for (String reward : combinations.getValue()) {
						result = result * configPojo.getWin_combinations().get(reward).getReward_multiplier();
					}
					finalResultArray.add(result);
				});
			}
			
			double finalResultDouble = finalResultArray.isEmpty() ? 0 : finalResultArray.stream().reduce((a, b) -> (a + b)).get();
			
			// reward calculation for bonus
			for (String bonus : applied_bonus_symbol) {
				SymbolsPojo appliedBonus = configPojo.getSymbols().get(bonus);
				if ("bonus".equalsIgnoreCase(appliedBonus.getType())
						&& "multiply_reward".equalsIgnoreCase(appliedBonus.getImpact()) && finalResultDouble > 0) {
					finalResultDouble *= Double.parseDouble(appliedBonus.getReward_multiplier());
				} else if ("bonus".equalsIgnoreCase(appliedBonus.getType())
						&& "extra_bonus".equalsIgnoreCase(appliedBonus.getImpact()) && finalResultDouble > 0) {
					finalResultDouble += appliedBonus.getExtra();
				}
				//for impact miss - we don't need to do anything
			}
			resultPojo.setMatrix(resultArray);
			resultPojo.setReward(finalResultDouble);
			System.out.println(resultPojo);
		} else {
			System.out.println("Standard sumbol probabilities doesn't match the array size");
			System.exit(0);
		}
	}

}
