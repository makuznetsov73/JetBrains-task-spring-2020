import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallNode {

	private CallNode leftNode;
	
	private CallNode rightNode;
	
	private CallType callType;
	
	private String expressionStr;
	
	private String errorMessage = "";
	
	private boolean isCorrect;
	
	public CallNode(String callStr) throws IllegalArgumentException {
		Pattern p = Pattern.compile("(filter|map)\\{(.+)}"); //
		Matcher m = p.matcher(callStr);
		if (m.matches()) {
			if ("filter".equals(m.group(1))) {
				callType = CallType.FILTER;
			} else
				callType = CallType.MAP;
			expressionStr = m.group(2);
			if(!checkIfCorrect(expressionStr, callType == CallType.FILTER)) {
				throw new IllegalArgumentException(errorMessage);
			}
			isCorrect = true;
		} else
			throw new IllegalArgumentException("SYNTAX ERROR");
	}
	
	public boolean checkIfCorrect(String expression, boolean isLogical) {
		// check 3 types of possible expression values: “element” | <constant-expression> | <binary-expression>
		// “element”
		if (expression.equals("element")) {
			if (isLogical) {
				errorMessage = "TYPE ERROR";
				return false;
			}
			return true;
		// <constant-expression>
		} else if (expression.matches("-?\\d+")) {
			if (isLogical) {
				errorMessage = "TYPE ERROR";
				return false;
			}
			return true;
		// <binary-expression>
		} else {
			// check if expression is syntactically true
			Pattern p = Pattern.compile("\\((-?[0-9]+|\\(.+\\)|element)([+\\-*|&><=])" +
				"(-?[0-9]+|\\(.+\\)|element)\\)");
			Matcher m = p.matcher(expression);
			if (m.matches()) {
				// check if expression has boolean type
				if (isLogical) {
					p = Pattern.compile("(\\((-?[0-9]+|\\(.+\\)|element)([><=])(-?[0-9]+|\\(.+\\)|element)\\)" +
						"|\\((\\(.+\\))([|&])(\\(.+\\))\\))");
					m = p.matcher(expression);
					if (m.matches()) {
						if (m.group(3) != null) {
							return checkIfCorrect(m.group(2), false) && checkIfCorrect(m.group(4), false);
						} else
							return checkIfCorrect(m.group(5), true) && checkIfCorrect(m.group(7), true);
					} else {
						errorMessage = "TYPE ERROR";
						return false;
					}
				// check if expression has integer type
				} else {
					p = Pattern.compile("\\((-?[0-9]+|\\(.+\\)|element)([+\\-*])(-?[0-9]+|\\(.+\\)|element)\\)");
					m = p.matcher(expression);
					if (m.matches()) {
						return checkIfCorrect(m.group(1), false) && checkIfCorrect(m.group(3), false);
					} else {
						errorMessage = "TYPE ERROR";
						return false;
					}
				}
			} else {
				errorMessage = "SYNTAX ERROR";
				return false;
			}
		}
	}
	
	public CallNode getLeftNode() {
		return leftNode;
	}
	
	public void setLeftNode(CallNode leftNode) {
		this.leftNode = leftNode;
	}
	
	public CallNode getRightNode() {
		return rightNode;
	}
	
	public void setRightNode(CallNode rightNode) {
		this.rightNode = rightNode;
	}
	
	public CallType getCallType() {
		return callType;
	}
	
	public void setCallType(CallType callType) {
		this.callType = callType;
	}
	
	public String getExpressionStr() {
		return expressionStr;
	}
	
	public void setExpressionStr(String expressionStr) {
		this.expressionStr = expressionStr;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public boolean isCorrect() {
		return isCorrect;
	}
	
	public enum CallType {
		MAP,
		FILTER
	}
}
