import java.util.List;

public class ChainCall {
	
	private CallNode headNode;
	
	private CallNode tailNode;
	
	private String chainCallStr;
	
	private String reducedChainStr;
	
	private boolean isCorrect;
	
	public ChainCall (String chainCallStr) throws IllegalArgumentException {
		this.chainCallStr = chainCallStr;
		getCallNodeList();
	}
	
	// here we create linked List (headNode - tailNode) from chain of calls
	private void getCallNodeList() {
		if (!chainCallStr.isEmpty() && !chainCallStr.endsWith("%>%") && !chainCallStr.startsWith("%>%")) {
			String[] callNodesStr = chainCallStr.split("%>%");
			headNode = new CallNode(callNodesStr[0]);
			CallNode previousNode = headNode;
			CallNode currentNode = headNode;
			for (int i = 1; i < callNodesStr.length; i++) {
				currentNode = new CallNode(callNodesStr[i]);
				currentNode.setLeftNode(previousNode);
				previousNode.setRightNode(currentNode);
				previousNode = currentNode;
			}
			tailNode = currentNode;
		} else
			throw new IllegalArgumentException("SYNTAX ERROR");
	}
	
	// here we reducing our linked list of chain calls to <filter-call> “%>%” <map-call> state
	public void reduceChainCall() {
		if (tailNode.getCallType() == CallNode.CallType.FILTER) {
			reduceFilterCall(tailNode);
			tailNode.setRightNode(new CallNode("map{element}"));
			headNode = tailNode;
			tailNode = headNode.getRightNode();
		} else {
			CallNode currentNode = tailNode.getLeftNode();
			CallNode previousNode = tailNode;
			while (currentNode != null && currentNode.getCallType() == CallNode.CallType.MAP) {
				previousNode.setExpressionStr(previousNode.getExpressionStr()
					.replaceAll("element", currentNode.getExpressionStr()));
				
				currentNode = connectLeftNode(currentNode, previousNode);
			}
			if (currentNode != null && currentNode.getCallType() == CallNode.CallType.FILTER) {
				String elementReplace = reduceFilterCall(currentNode);
				previousNode.setExpressionStr(previousNode.getExpressionStr()
					.replaceAll("element", elementReplace));
			} else {
				currentNode = new CallNode("filter{(1=1)}");
				currentNode.setRightNode(tailNode);
				tailNode.setLeftNode(currentNode);
				headNode = currentNode;
			}
		}
		
		reducedChainStr = String.format("filter{%s}", headNode.getExpressionStr()).
			concat(String.format("%%>%%map{%s}", tailNode.getExpressionStr()));
	}
	
	// this is a method for reducing chain of calls which ends with filter call
	private String reduceFilterCall(CallNode filterNode) {
		CallNode currentNode = filterNode.getLeftNode();
		String elementReplace = "element";
		
		while (currentNode != null) {
			if (currentNode.getCallType() == CallNode.CallType.FILTER) {
				filterNode.setExpressionStr(String.format("(%s&%s)", currentNode.getExpressionStr(),
					filterNode.getExpressionStr()));
				currentNode = currentNode.getLeftNode();
				if (currentNode != null) {
					currentNode.setRightNode(filterNode);
					filterNode.setLeftNode(currentNode);
				}
			} else if (currentNode.getCallType() == CallNode.CallType.MAP) {
				filterNode.setExpressionStr(filterNode.getExpressionStr()
					.replaceAll("element", currentNode.getExpressionStr()));
				elementReplace = elementReplace.replaceAll("element", currentNode.getExpressionStr());
				
				currentNode = connectLeftNode(currentNode, filterNode);
			}
		}
		headNode = filterNode;
		return elementReplace;
	}
	
	private CallNode connectLeftNode(CallNode currentLeftNode, CallNode rightNode) {
		currentLeftNode = currentLeftNode.getLeftNode();
		if (currentLeftNode != null) {
			currentLeftNode.setRightNode(rightNode);
			rightNode.setLeftNode(currentLeftNode);
		}
		return currentLeftNode;
	}
	
	public CallNode getHeadNode() {
		return headNode;
	}
	
	public void setHeadNode(CallNode headNode) {
		this.headNode = headNode;
	}
	
	public CallNode getTailNode() {
		return tailNode;
	}
	
	public void setTailNode(CallNode tailNode) {
		this.tailNode = tailNode;
	}
	
	public String getChainCallStr() {
		return chainCallStr;
	}
	
	public void setChainCallStr(String chainCallStr) {
		this.chainCallStr = chainCallStr;
	}
	
	public String getReducedChainStr() {
		return reducedChainStr;
	}
	
	public void setReducedChainStr(String reducedChainStr) {
		this.reducedChainStr = reducedChainStr;
	}
}
