# Reddit Clone REST API

This project is a backend implementation of a simplified Reddit clone using Java 21 and Spring Boot. It serves the purpose of learning how to build REST APIs and exploring core Spring Boot features.


## Project Overview

This API currently focuses on managing "spaces" (similar to subreddits). It provides endpoints to retrieve lists of spaces and access details for specific spaces. The project is designed to be extensible, with future iterations planned to include features like user authentication, post creation, commenting, and voting.


## Current Endpoints

All endpoints start with `/api/` as a standard prefix for accessing the REST API. All data is handled using the JSON data interchange format.

### Spaces

- GET:

   - `/spaces`: retreives a list of all available spaces.

   - `/spaces?query=<pattern>`: retreives a list of all spaces that match the `query` pattern. It should contain only characters that are allowed in space names (see [`/spaces/{name}`](#space-name)) and an asterisk (`*`) to match any sequence of characters.

   - <span id="space-name">`/spaces/{name}`</span>: retreives a space which name exactly matches the `name`. Space name can only contain latin uppercase and lowercase letters, numbers, dashes (`-`) and undersocres (`_`).


## Technology Stack

- Java 21

- Spring Boot

- Gradle (with Kotllin DSL)


## Getting Started

To run this project locally, follow these steps:

1.  clone the repo: `git clone https://github.com/DaniilMiskevich/sem4-pnayavu-labs.git`

2.  navigate to the project directory: `cd sem4-pnayavu-labs`

3.  run the project using Gradle: `./gradlew bootRun`

4.  use the api at `localhost:8080`


## ToDo

- replace in-memory storage with the SQL database

- an ability to create new spaces

- an ability to view and create posts

- an ability to comment posts and leave votes
