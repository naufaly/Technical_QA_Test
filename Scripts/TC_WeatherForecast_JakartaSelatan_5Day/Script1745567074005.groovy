import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.checkpoint.Checkpoint
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

// Send request to the Forecast API
def response = WS.sendRequest(findTestObject('GetForecast5Days'))

// Assert response status code is 200 OK
WS.verifyResponseStatusCode(response, 200)

// Parse the JSON response
def jsonSlurper = new JsonSlurper()
def jsonResponse = jsonSlurper.parseText(response.getResponseText())

// Assert JSON schema (basic structure)
WS.verifyElementPropertyValue(response, 'cod', '200')
WS.verifyElementPropertyValue(response, 'message', 0)
assert jsonResponse.list != null : "List element is missing"
assert jsonResponse.city != null : "City element is missing"

// Verify city is Jakarta Selatan or relevant area
WS.verifyElementPropertyValue(response, 'city.name', 'Rawa Barat')  // Adjust if needed
WS.verifyElementPropertyValue(response, 'city.country', 'ID')

// Verify forecast is for 5 days (typically 40 entries for 5-day forecast with 3-hour intervals)
assert jsonResponse.list.size() >= 30 : "Expected at least 30 forecast intervals for 5 days"

// Verify date format in forecast entries
def firstDate = jsonResponse.list[0].dt_txt
assert firstDate =~ /\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}/ : "Invalid date format: ${firstDate}"

// Verify temperature data exists and is in a reasonable range (Kelvin)
def temp = jsonResponse.list[0].main.temp
assert temp > 260 && temp < 330 : "Temperature out of expected range: ${temp}K"

// Verify other essential weather properties exist
def firstEntry = jsonResponse.list[0]
assert firstEntry.weather != null && firstEntry.weather.size() > 0 : "Weather data missing"
assert firstEntry.main.humidity >= 0 && firstEntry.main.humidity <= 100 : "Invalid humidity value"
assert firstEntry.wind != null : "Wind data missing"

// Verify spans 5 days by checking unique dates
def dates = jsonResponse.list.collect { it.dt_txt.split(' ')[0] }.unique()
assert dates.size() >= 5 : "Forecast doesn't cover 5 days, found: ${dates.size()} days"

// Print success message with key data points
println "Successfully verified 5-day forecast for ${jsonResponse.city.name}"
println "Temperature range: ${jsonResponse.list.collect{it.main.temp_min}.min()}K to ${jsonResponse.list.collect{it.main.temp_max}.max()}K"
println "Forecast period: ${jsonResponse.list.first().dt_txt} to ${jsonResponse.list.last().dt_txt}"