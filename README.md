# **Testing strategy:**

Analyzed the Readme.md file shared and cross verified if the API is working fine using postman.
started off by performing actions like health check, signup, login , creating, updating , deleting and retrieval of (data) books.

### **How I Approached Writing Test Flows:**

I followed a modular and scalable approach while writing API test flows:

**Approach:**
I began by identifying the scenarios within the application. I used a modular test design, organizing tests by feature and functionality along with there correct order. Each test was broken down into:

* Setup: Necessary configurations like environment variable, setting up id and password for login and sign up, token generation using before each.
* Action: Started up with positive cases following negative cases. Imported libraries and added exceptions to execute test seamlessly. 
* Assertion: Added assertions to every test case to verify the result set. Asserting status codes, headers, body
* Framework: Used Java with Maven,Junit5, and playwright for making HTTP requests and assertions, allure for report generation.
* Used Playwright's APIcontexrequest for rest calls.
* Incorporated data-driven testing to verify multiple input scenarios without duplicating code.
* Positive and negative test scenarios
* Test classes were organized by module (e.g., /tests/authentication,/tests/bookmanagement) to maintain clarity.

### **Ensuring Tests are Reliable and Maintainable:**

* common config used:(base URL, auth tokens)
* Reusable methods for GET/POST/PUT/DELETE requests
* Used application.yml for dynamic environments (dev/prod)
* Maven profiles to switch between environments

* ##### Continuous Integration:
1. Integrated with github actions to run tests on every push/pull requests.
2. Generated Allure for visibility

### **Challenges Faced and Solutions**
1. Auth token expiration could be a problem but we can solve it using refresh token action.

2. Test data dependency and pollution
   post cleanup added in workflows