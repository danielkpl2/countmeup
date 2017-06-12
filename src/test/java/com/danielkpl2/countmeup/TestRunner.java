package com.danielkpl2.countmeup;

import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = {"pretty", "json:target.json"},
        features = {"src/test/java/com/danielkpl2/countmeup/features/"},
        glue = {"com.danielkpl2.countmeup"}
)
public class TestRunner {

}
