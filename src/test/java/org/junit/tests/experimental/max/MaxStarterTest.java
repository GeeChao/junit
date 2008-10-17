package org.junit.tests.experimental.max;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;


public class MaxStarterTest {
	public static class OneTest {
		@Test public void itsMe() {}
	}
	
	@Test public void oneTestNotRun() {
		Request request= Request.aClass(OneTest.class);
		MaxCore max= new MaxCore();
		Odds thing= max.getSpreads(request).get(0);
		assertEquals(0.0, thing.getCertainty(), 0.001);
		assertEquals(Description.createTestDescription(OneTest.class, "itsMe"), thing.getDescription());
	}
	
//	@Test public void passOneHundredTimes() {
//		Request request= Request.aClass(OneTest.class);
//		for (int i= 0; i < 100; i++)
//			MaxCore.run(request);
//		Odds thing= MaxCore.getSpreads(request).get(0);
//		assertEquals(0.???, thing.getCertainty(), 0.001);
//		assertEquals(Description.createTestDescription(OneTest.class, "itsMe"), thing.getDescription());
//	}
	
	public static class TwoTests {
		@Test public void succeed() {}
		@Test public void dontSucceed() { fail(); }
	}
	
	@Test public void preferRecentlyFailed() {
		Request request= Request.aClass(TwoTests.class);
		MaxCore max= new MaxCore();
		max.run(request);
		Odds thing= max.getSpreads(request).get(1);
		assertEquals(0.0, thing.getCertainty(), 0.001); // TODO not right yet
		assertEquals(Description.createTestDescription(TwoTests.class, "succeed"), thing.getDescription());
	}
	
	public static class TwoUnEqualTests {
		@Test public void slow() throws InterruptedException { Thread.sleep(100); }
		@Test public void fast() throws InterruptedException { Thread.sleep(50); }
	}
	
	@Test public void preferFast() {
		Request request= Request.aClass(TwoTests.class);
		MaxCore max= new MaxCore();
		max.run(request);
		Odds thing= max.getSpreads(request).get(1);
		assertEquals(0.0, thing.getCertainty(), 0.001); // TODO not right yet
		assertEquals(Description.createTestDescription(TwoUnEqualTests.class, "slow"), thing.getDescription());
	}
}
