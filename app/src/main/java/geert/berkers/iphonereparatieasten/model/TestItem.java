package geert.berkers.iphonereparatieasten.model;

import geert.berkers.iphonereparatieasten.enums.TestResult;

/**
 * Created by Geert.
 */

public class TestItem {

    private String test;
    private TestResult result;

    public TestItem(String test){
        this.test = test;
        this.result = TestResult.NOT_TESTED;
    }

    public void setPassedTestResult() {
        this.result = TestResult.PASSED;
    }

    public void setFailedTestResult(){
        this.result = TestResult.FAILED;
    }

    public void setNoPermissionTestResult(){
        this.result = TestResult.NO_PERMISSION;
    }

    public void resetTestResult() {
        this.result = TestResult.NOT_TESTED;
    }
}
