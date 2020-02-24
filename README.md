Autotests for API playground. Endpoint: Categories.

Technologies: Java + JUnit + Unirest + Harmcrest + Allure

Tests can be run with Itellij Idea or with command line:
 - Idea: go to the src/test/java/CategoryTests and run it
 - Command line: gradlew.bat test --tests CategoryTests

Creating Allure report:
 - Idea: go to the build.gradle and run task "generateAllure"
 - Command line: gradlew.bat generateAllure
    
    
Allure report will be in build/reports/allure/index.html        