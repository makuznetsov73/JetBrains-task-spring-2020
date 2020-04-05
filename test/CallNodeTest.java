import org.junit.Test;

import static org.junit.Assert.*;

public class CallNodeTest {
	
	@Test
	public void checkIfCorrectTypeError() {
		CallNode testCallNode;
		try {
			testCallNode = new CallNode("filter{-7}");
		} catch (IllegalArgumentException e) {
			assertEquals("TYPE ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{element}");
		} catch (IllegalArgumentException e) {
			assertEquals("TYPE ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{(5+7)}");
		} catch (IllegalArgumentException e) {
			assertEquals("TYPE ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{(element+element)}");
		} catch (IllegalArgumentException e) {
			assertEquals("TYPE ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{((element&element)<7)}");
		} catch (IllegalArgumentException e) {
			assertEquals("TYPE ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{((element+element)&(6<element))}");
		} catch (IllegalArgumentException e) {
			assertEquals("TYPE ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("map{((element&element)+7)}");
		} catch (IllegalArgumentException e) {
			assertEquals("TYPE ERROR", e.getMessage());
		}
	}
	
	@Test
	public void checkIfCorrectSyntaxError() {
		CallNode testCallNode;
		try {
			testCallNode = new CallNode("filter{(element)}");
		} catch (IllegalArgumentException e) {
			assertEquals("SYNTAX ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{}");
		} catch (IllegalArgumentException e) {
			assertEquals("SYNTAX ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{(element)+}");
		} catch (IllegalArgumentException e) {
			assertEquals("SYNTAX ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{+-5}");
		} catch (IllegalArgumentException e) {
			assertEquals("SYNTAX ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("filter{element+(5)}");
		} catch (IllegalArgumentException e) {
			assertEquals("SYNTAX ERROR", e.getMessage());
		}
		try {
			testCallNode = new CallNode("FilteR{element}");
		} catch (IllegalArgumentException e) {
			assertEquals("SYNTAX ERROR", e.getMessage());
		}
	}
	
	@Test
	public void testCorrectExamples() {
		CallNode testCallNode;
		testCallNode = new CallNode("filter{(element>8)}");
		assertTrue(testCallNode.isCorrect());
		testCallNode = new CallNode("map{(element*element)}");
		assertTrue(testCallNode.isCorrect());
		testCallNode = new CallNode("filter{((element>10)&(element<20))}");
		assertTrue(testCallNode.isCorrect());
	}
}