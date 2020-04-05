import org.junit.Test;

import static org.junit.Assert.*;

public class ChainCallTest {
	
	@Test
	public void reduceChainCallSyntaxError() {
		ChainCall testChainCall;
		try {
			testChainCall = new ChainCall("map{(element+10)}%>>>%filter{(element>10)}" +
				"%<><>%map{(element*element)}");
			testChainCall.reduceChainCall();
		} catch (IllegalArgumentException e) {
			assertEquals("SYNTAX ERROR", e.getMessage());
		}
	}
	
	@Test
	public void reduceChainCallCorrectExamples() {
		ChainCall testChainCall;
		
		testChainCall = new ChainCall("map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}");
		testChainCall.reduceChainCall();
		assertEquals("filter{((element+10)>10)}%>%map{((element+10)*(element+10))}", testChainCall.getReducedChainStr());
		
		testChainCall = new ChainCall("filter{(element>10)}%>%filter{(element<20)}");
		testChainCall.reduceChainCall();
		assertEquals("filter{((element>10)&(element<20))}%>%map{element}", testChainCall.getReducedChainStr());
		
		testChainCall = new ChainCall("map{(element+10)}%>%map{(element+20)}");
		testChainCall.reduceChainCall();
		assertEquals("filter{(1=1)}%>%map{((element+10)+20)}", testChainCall.getReducedChainStr());
		
		
	}
}