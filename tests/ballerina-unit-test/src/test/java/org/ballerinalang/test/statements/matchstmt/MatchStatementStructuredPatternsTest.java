/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.ballerinalang.test.statements.matchstmt;

import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BValue;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test cases to verify the behaviour of the structured patterns with match statement in Ballerina.
 *
 * @since 0.985.0
 */
public class MatchStatementStructuredPatternsTest {

    private CompileResult result;

    @BeforeClass
    public void setup() {
        result = BCompileUtil.compile("test-src/statements/matchstmt/structured_match_patterns.bal");
    }

    @Test(description = "Test basics of structured pattern match statement 1")
    public void testMatchStatementBasics1() {
        BValue[] returns = BRunUtil.invoke(result, "testStructuredMatchPatternsBasic1", new BValue[]{});
        Assert.assertEquals(returns.length, 1);
        Assert.assertSame(returns[0].getClass(), BString.class);

        BString bString = (BString) returns[0];

        Assert.assertEquals(bString.stringValue(), "Matched Values : S, 23, 5.6");
    }

    @Test(description = "Test basics of structured pattern match statement 2")
    public void testMatchStatementBasics2() {
        BValue[] returns = BRunUtil.invoke(result, "testStructuredMatchPatternsBasic2", new BValue[]{});
        Assert.assertEquals(returns.length, 1);
        Assert.assertSame(returns[0].getClass(), BString.class);

        BString bString = (BString) returns[0];

        Assert.assertEquals(bString.stringValue(), "Matched Values : S, 23, 5.6");
    }

    @Test(description = "Test basics of structured pattern match statement 3")
    public void testMatchStatementBasics3() {
        BValue[] returns = BRunUtil.invoke(result, "testStructuredMatchPatternsBasic3", new BValue[]{});
        Assert.assertEquals(returns.length, 1);
        Assert.assertSame(returns[0].getClass(), BString.class);

        BString bString = (BString) returns[0];

        Assert.assertEquals(bString.stringValue(), "Matched Values : S, (23, 5.6)");
    }

    @Test(description = "Test basics of structured pattern match statement 4")
    public void testMatchStatementBasics4() {
        BValue[] returns = BRunUtil.invoke(result, "testStructuredMatchPatternsBasic4", new BValue[]{});
        Assert.assertEquals(returns.length, 1);
        Assert.assertSame(returns[0].getClass(), BString.class);

        BString bString = (BString) returns[0];

        Assert.assertEquals(bString.stringValue(), "Matched Values : (\"S\", (23, 5.6))");
    }

    @Test(description = "Test basics of structured pattern match statement 5")
    public void testMatchStatementBasics5() {
        BValue[] returns = BRunUtil.invoke(result, "testStructuredMatchPatternsBasics5", new BValue[]{});
        Assert.assertEquals(returns.length, 1);
        Assert.assertSame(returns[0].getClass(), BStringArray.class);

        BStringArray results = (BStringArray) returns[0];

        int i = -1;
        String msg = "Matched with ";
        Assert.assertEquals(results.get(++i), msg + "single var : 66.6");
        Assert.assertEquals(results.get(++i), msg + "two vars : Hello, 12");
        Assert.assertEquals(results.get(++i), msg + "two vars : 4.5, true");
        Assert.assertEquals(results.get(++i), msg + "three vars : 6.7, Test, false");
    }

    @Test(description = "Test structured pattern match statement complex 1")
    public void testStructuredMatchPatternComplex1() {
        BValue[] returns = BRunUtil.invoke(result, "testStructuredMatchPatternComplex1", new BValue[]{});
        Assert.assertEquals(returns.length, 1);
        Assert.assertSame(returns[0].getClass(), BStringArray.class);

        BStringArray results = (BStringArray) returns[0];

        int i = -1;
        String msg = "Matched with ";
        Assert.assertEquals(results.get(++i), msg + "single var : 66.6");
        Assert.assertEquals(results.get(++i), msg + "two vars : Hello, 34");
        Assert.assertEquals(results.get(++i), msg + "four vars : 66.6, Test, 456, true");
        Assert.assertEquals(results.get(++i), msg + "three vars : 5.6, Ballerina, false");
        Assert.assertEquals(results.get(++i), msg + "single var : (\"Bal\", 543, 67.8)");
    }

    @Test(description = "Test structured pattern match statement complex 2")
    public void testStructuredMatchPatternComplex2() {
        BValue[] returns = BRunUtil.invoke(result, "testStructuredMatchPatternComplex2", new BValue[]{});
        Assert.assertEquals(returns.length, 1);
        Assert.assertSame(returns[0].getClass(), BStringArray.class);

        BStringArray results = (BStringArray) returns[0];

        int i = -1;
        String msg = "Matched with ";
        Assert.assertEquals(results.get(++i), "Default");
        Assert.assertEquals(results.get(++i), msg + "two vars : Hello, 34");
        Assert.assertEquals(results.get(++i), msg + "three vars : 66.6, Test, (true, 456)");
        Assert.assertEquals(results.get(++i), msg + "three vars : 5.6, Ballerina, false");
    }
}
