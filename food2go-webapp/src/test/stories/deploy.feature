Feature: Server startup

In order to add new features to food2go web application,
as a Administrator,
I want to (re)deploy the application.

Scenario: Administrator (re)deploy web application

Given the web application is (re)deployed
When I visit the application
Then a welcome page is shown