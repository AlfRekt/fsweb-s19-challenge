
  TWITTER CLONE REST API
  A backend social-media platform built with Spring Boot

OVERVIEW
-------------------------------------------------------------------------------
This project is a backend REST API that reproduces the core features of a
Twitter-style social network. Users can register and log in, post and manage
tweets, quote other tweets, comment, like and unlike, retweet, and follow or
unfollow each other. The application is secured with Spring Security and stores
its data in a PostgreSQL database through Spring Data JPA.

The codebase follows a clean, layered architecture (Controller -> Service ->
Repository -> Entity) with separate Data Transfer Objects (DTOs) for requests
and responses, centralized error handling, and a full suite of unit tests.


TECH STACK
-------------------------------------------------------------------------------
  - Java 17
  - Spring Boot 3.5.14
  - Spring Web (REST controllers)
  - Spring Security (HTTP Basic auth, BCrypt password hashing, role-based access)
  - Spring Data JPA / Hibernate
  - PostgreSQL
  - Lombok (boilerplate reduction)
  - JUnit 5 + Mockito (unit testing)
  - Maven (build and dependency management)


KEY FEATURES
-------------------------------------------------------------------------------
  Authentication & Security
    - User registration with unique username/email validation
    - Login via Spring Security authentication manager
    - Passwords stored as BCrypt hashes (never in plain text)
    - Role-based authorization (e.g. ADMIN can delete any tweet)
    - Public read endpoints, protected write endpoints

  Tweets
    - Create, read, update, and delete tweets
    - List all tweets (newest first), by id, or by user
    - Quote tweets that reference an original tweet
    - Ownership checks: a user can only edit/delete their own tweets
      (admins may delete any tweet)

  Social Interactions
    - Comments on tweets (create, update, delete)
    - Like / dislike with duplicate-like prevention
    - Retweets with duplicate-retweet prevention
    - Follow / unfollow with self-follow and duplicate-follow protection
    - List followers and following for a user

  Error Handling
    - Global exception handler returning consistent JSON error responses
    - Custom exceptions: BadRequest, ResourceNotFound, UnauthorizedAction


PROJECT STRUCTURE
-------------------------------------------------------------------------------
  src/main/java/com/workintech/twitterapi/
    config/        Security configuration (auth manager, password encoder)
    controller/    REST endpoints (Auth, Tweet, User, Comment, Like,
                   Retweet, Follow)
    service/       Business logic and validation
    repository/    Spring Data JPA repository interfaces
    entity/        JPA entities (User, Tweet, Comment, Like, Retweet,
                   Follow, Role, BaseEntity)
    dto/           Request/response objects, grouped by domain
    exception/     Custom exceptions and global handler

  src/test/java/com/workintech/twitterapi/
    Unit tests for the Authentication, Tweet, Comment, Like,
    Retweet, and Follow services.


API ENDPOINTS (SUMMARY)
-------------------------------------------------------------------------------
  Auth
    POST   /auth/register              Register a new user
    POST   /auth/login                 Log in

  Tweet
    GET    /tweet/                      List all tweets (newest first)
    GET    /tweet/findById/{id}         Get a tweet by id
    GET    /tweet/findByUserId/{id}     Get all tweets by a user
    POST   /tweet/                      Create a tweet
    POST   /tweet/quote                 Create a quote tweet
    PUT    /tweet/{id}                  Update own tweet
    DELETE /tweet/{id}                  Delete own tweet (admin: any tweet)

  User
    GET    /user/                       List users
    PUT    /user/{id}                   Update user
    DELETE /user/{id}                   Delete user

  Comment
    GET    /comment/                    List comments
    POST   /comment/                    Create a comment
    PUT    /comment/{id}                Update a comment
    DELETE /comment/{id}                Delete a comment

  Like
    POST   /like                        Like a tweet
    POST   /dislike                     Remove a like
    GET    /like/tweet/{tweetId}        Users who liked a tweet
    GET    /like/user/{userId}          Tweets liked by a user

  Retweet
    GET    /retweet/                    List retweets
    GET    /retweet/user/{userId}       Retweets by a user
    POST   /retweet/                    Create a retweet
    DELETE /retweet/{id}                Delete own retweet

  Follow
    POST   /follow/                     Follow a user
    DELETE /follow/{followingId}        Unfollow a user
    GET    /follow/following/{userId}   Users this user follows
    GET    /follow/followers/{userId}   Users who follow this user

  Note: GET endpoints are public; all other endpoints require authentication.


GETTING STARTED
-------------------------------------------------------------------------------
  Prerequisites
    - Java 17 or newer
    - Maven (or use the included Maven wrapper: ./mvnw)
    - A running PostgreSQL instance

  1. Create a PostgreSQL database named "twitterapi".

  2. Update the database credentials in
     src/main/resources/application.properties to match your local setup:

         spring.datasource.url=jdbc:postgresql://localhost:5432/twitterapi
         spring.datasource.username=YOUR_USERNAME
         spring.datasource.password=YOUR_PASSWORD

     (Tip: do not commit real credentials. Consider using environment
      variables for username and password.)

  3. Build and run the application:

         ./mvnw spring-boot:run            (Linux / macOS)
         mvnw.cmd spring-boot:run          (Windows)

  4. The API will be available at:  http://localhost:8080

  5. Run the tests with:

         ./mvnw test


NOTES
-------------------------------------------------------------------------------
  - Database tables are created/updated automatically on startup
    (spring.jpa.hibernate.ddl-auto=update).
  - Entities share common created/updated timestamps through a BaseEntity.
  - This project was built as part of the Workintech Full Stack Development
    program (Challenge S19).

===============================================================================
