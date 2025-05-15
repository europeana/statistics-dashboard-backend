[![CI](https://github.com/europeana/statistics-dashboard-backend

/actions/workflows/ci.yml/badge.svg)](https://github.com/europeana/statistics-dashboard-backend

/actions/workflows/ci.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=europeana_statistics-dashboard-backend

&metric=coverage)](https://sonarcloud.io/summary/new_code?id=europeana_statistics-dashboard-backend

)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=europeana_statistics-dashboard-backend

&metric=bugs)](https://sonarcloud.io/summary/new_code?id=europeana_statistics-dashboard-backend

) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=europeana_statistics-dashboard-backend

&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=europeana_statistics-dashboard-backend

)  
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=europeana_statistics-dashboard-backend

&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=europeana_statistics-dashboard-backend

) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=europeana_statistics-dashboard-backend

&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=europeana_statistics-dashboard-backend

)  
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=europeana_statistics-dashboard-backend

&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=europeana_statistics-dashboard-backend

) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=europeana_statistics-dashboard-backend

&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=europeana_statistics-dashboard-backend

)  
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=europeana_statistics-dashboard-backend

&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=europeana_statistics-dashboard-backend

)

# Statistics Dashboard Backend

A utility for Europeana API data.

## Getting started

Make sure you have `java` version 21 and `maven` version 3.6.x:

    java -version
    mvn -v

Rename and update the example properties file: `statistics-dashboard-rest/src/main/resources/application.properties.example`

    mongo.hosts=localhost
    mongo.port=27017
    ...

Install the dependencies and build the asset:

    mvn package

## Development server (swagger)

Run the built asset:

  java -jar ./statistics-dashboard-rest/target/statistics-dashboard-rest-6-SNAPSHOT.jar

and navigate to [http://localhost:4280/](http://localhost:4280/).

## Branches and Pull Requests

The main branch for development is the `develop` branch. But do NOT use this branch directly! Use a new branch for features/bugs and give it a descriptive name containing the user story code, like:

    feat/MET-1535-chart-styling
    bug/MET-3245-dashboard-not-loading

If you push a branch or commit to GitHub, it will automatically be tested by Travis CI. This will take about 5 mins and the results will be shown in GitHub, e.g. in the pull request page.

Make a pull request in GitHub for code review and merging.

## Running unit tests

To run the unit tests use the command:

    mvn test