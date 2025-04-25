import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import groovy.json.JsonSlurper

// Send request to get current air pollution of Jakarta Selatan
def response = WS.sendRequest(findTestObject('GetCurrentAirPollution'))

// Basic response validation
WS.verifyResponseStatusCode(response, 200)

// Extract response to JSON
def slurper = new JsonSlurper()
def json = slurper.parseText(response.getResponseText())

// Simple assertions for coordinates
assert json.coord.lon == 106.8107
assert json.coord.lat == -6.2615

// Simple assertions for list entries
assert json.list.size() > 0
assert json.list[0].main.aqi >= 1 && json.list[0].main.aqi <= 5

// Verify basic components exist
assert json.list[0].components.co != null
assert json.list[0].components.no != null
assert json.list[0].components.no2 != null
assert json.list[0].components.o3 != null
assert json.list[0].components.so2 != null
assert json.list[0].components.pm2_5 != null
assert json.list[0].components.pm10 != null
assert json.list[0].components.nh3 != null

// Verify timestamp
assert json.list[0].dt > 0

// Log results
println "API Test passed!"
println "Location: Jakarta Selatan (${json.coord.lat}, ${json.coord.lon})"
println "Air Quality Index: ${json.list[0].main.aqi}"
println "CO level: ${json.list[0].components.co}"