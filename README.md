# Weather API Testing Project

This project demonstrates automated API testing of weather data services using Katalon Studio.

## Project Overview

This project tests two key weather data endpoints:
- 5-day weather forecast for Jakarta Selatan
- Current air pollution data for Jakarta Selatan

## Prerequisites

- Katalon Studio 8.5.0 or higher
- JDK 1.8 or higher
- An active internet connection

## Project Structure

- **Test Cases/**
  - TC_AirPollution_JakartaSelatan - Tests current air quality data
  - TC_WeatherForecast_JakartaSelatan_5Day - Tests 5-day weather forecast
  
- **Object Repository/**
  - GetCurrentAirPollution - API endpoint configuration for air pollution data
  - GetForecastJakartaSelatan - API endpoint configuration for weather forecast
  
- **Test Suites/**
  - Weather_API_Test_Suite - Combined execution of all tests
  
- **Data Files/**
  - TestLocations.xlsx - Test data with different locations

## How to Run the Tests

1. Clone this repository
2. Open the project in Katalon Studio
3. Update API key in Profiles/default (if needed)
4. Run individual test cases or the complete test suite

## Generating Reports

1. Execute the test suite
2. Reports are automatically generated in the Reports folder
3. View HTML, CSV, or PDF reports
4. For customized reports:
   - Go to Project → Settings → Report
   - Configure your desired report format

## CI/CD Integration

This project can be integrated with CI/CD pipelines:

- **Jenkins**: Use Katalon plugin for Jenkins
- **GitHub Actions**: Example workflow included in .github/workflows

## Troubleshooting

If you encounter API rate limit issues:
- Increase wait times between requests
- Use the provided retry mechanism
