### API automation for conversation APIs ###

Here we'll be using Java with RestAssured, TestNG, Maven and Allure for testing of conversation apis.

## Running Tests

To run tests, run the following command

```bash
  mvn clean test
```

## Generate Reports 
Make sure you have setup allure - you can follow steps mentioned here https://docs.qameta.io/allure/
```bash
  allure serve target/surefire-reports/
```

## Install allure ##
```bash
  brew install allure
```
```bash
  allure --version
```




