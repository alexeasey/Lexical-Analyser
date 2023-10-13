//fix spaces and then done


import java.util.Collections;
import java.util.*;

public class LexicalAnalyser {

	private enum State{
		WHOLENUMBER, STARTZERO, DECIMALAFTERZERO, NUMBERAFTERDECIMAL, OPERATOR
	};

	public static List<Token> analyse(String input) throws NumberException, ExpressionException {

		List<Token> tokens = new ArrayList<Token>();

		char[] inputArray = input.toCharArray();
		char lastChar = inputArray[0];
		String numbers = "123456789";
		String operators = "+-/*";
		String currentNum = "";
		State state;
		boolean space = false;
		
		//Set state from first number, move to three different states from start
		if (numbers.contains(Character.toString(lastChar))){
			state = State.WHOLENUMBER;
			currentNum = Character.toString(lastChar);
		} else if (lastChar == '0'){
			state = State.STARTZERO;
			currentNum = Character.toString(lastChar);
		} else if (lastChar == '.'){
			throw new NumberException();
		} else{
			throw new ExpressionException();
		}

		//for every character in the array, change states depending on what it is. Starting from second input so i = 1;
		if (inputArray.length > 1){
			for (int i = 1; i < inputArray.length; i++){
				lastChar = inputArray[i];
				switch (state){
					case WHOLENUMBER:
						if (numbers.contains(Character.toString(lastChar)) || lastChar == '0'){ //if next input is another whole number
							currentNum = currentNum + Character.toString(lastChar);
							if (space == true){
								throw new ExpressionException();
							}
						} else if (operators.contains(Character.toString(lastChar))){ //if next input is an operator
							tokens.add(new Token(Integer.parseInt(currentNum)));
							tokens.add(new Token(Token.typeOf(lastChar)));
							space = false;
							currentNum = "";
							state = State.OPERATOR;
						} else if (lastChar == '.'){ //if input is a decimal
							throw new NumberException();
						} else {
							space = true;
						}
						break;
					case OPERATOR:
						if (numbers.contains(Character.toString(lastChar))){ //if next input is another whole number
							state = State.WHOLENUMBER;
							currentNum = Character.toString(lastChar);
						} else if (lastChar == '0'){ // if input is 0
							state = State.STARTZERO;
							currentNum = Character.toString(lastChar);
						} else if (lastChar == '.' || operators.contains(Character.toString(lastChar))){ //if input is a decimal
							throw new ExpressionException();
						}
						break;
					case STARTZERO:
						if (numbers.contains(Character.toString(lastChar))){ //if next input is another whole number
							throw new NumberException();
						} else if (lastChar == '0'){ // if input is 0
							currentNum = currentNum + Character.toString(lastChar);
						} else if (operators.contains(Character.toString(lastChar))){ //if next input is an operator
							tokens.add(new Token(Integer.parseInt(currentNum)));
							currentNum = "";
							state = State.OPERATOR;
						} else{ //if input is a decimal
							currentNum = currentNum + Character.toString(lastChar);
							state = State.DECIMALAFTERZERO;
						}
						break;
					case DECIMALAFTERZERO:
						if (numbers.contains(Character.toString(lastChar))){ //if next input is another whole number
							currentNum = currentNum + Character.toString(lastChar);
							state = State.NUMBERAFTERDECIMAL;
						} else if (lastChar == '0'){ // if input is 0
							currentNum = currentNum + Character.toString(lastChar);
							state = State.NUMBERAFTERDECIMAL;
						} else{ //if input is a decimal
							throw new NumberException();
						}
						break;
					case NUMBERAFTERDECIMAL:
						if (numbers.contains(Character.toString(lastChar))){ //if next input is another whole number
							currentNum = currentNum + Character.toString(lastChar);
						} else if (lastChar == '0'){ // if input is 0
							currentNum = currentNum + Character.toString(lastChar);
						} else if (operators.contains(Character.toString(lastChar))){ //if next input is an operator
							tokens.add(new Token(Double.parseDouble(currentNum)));
							tokens.add(new Token(Token.typeOf(lastChar)));
							currentNum = "";
							state = State.OPERATOR;
						} else{ //if input is a decimal
							throw new NumberException();
						}
						break;
				}
				System.out.println(state);
			}
			// do something based on the finish state
			
		}
		switch (state){
				case OPERATOR:
					//throw exception, finished with operator
					throw new ExpressionException();
				case DECIMALAFTERZERO:
					throw new NumberException();
				case WHOLENUMBER:
					tokens.add(new Token(Integer.parseInt(currentNum)));
					break;
				case STARTZERO:
				case NUMBERAFTERDECIMAL:
					tokens.add(new Token(Double.parseDouble(currentNum)));
		}

		
		//System.out.println(state);
		

		

		return tokens;

	}

}
