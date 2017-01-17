package geert.berkers.iphonereparatieasten.model;

import geert.berkers.iphonereparatieasten.enums.TestResult;

/**
 * Created by Geert.
 */
public class TestItem {

    private final String testName;
    private final int requestCode;
    //TODO: Make this a list
    private TestResult testResult;

    private final int untestedImageResource;
    private final int failedImageResource;
    private final int passedImageResource;

    public TestItem(String testName,
                    int requestCode,
                    int untestedImageResource,
                    int failedImageResource,
                    int passedImageResource) {
        this.testName = testName;
        this.requestCode = requestCode;
        this.untestedImageResource = untestedImageResource;
        this.failedImageResource = failedImageResource;
        this.passedImageResource = passedImageResource;
        this.testResult = TestResult.NOT_TESTED;
    }


    public String getTestName() {
        return testName;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public int getUntestedImageResource() {
        return untestedImageResource;
    }

    public int getFailedImageResource() {
        return failedImageResource;
    }

    public int getPassedImageResource() {
        return passedImageResource;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public void resetTestResult() {
        this.testResult = TestResult.NOT_TESTED;
    }
}
