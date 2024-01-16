# Protocol by Philipp Chorn

## GitHub

GitHub Repo: https://github.com/philippchn/WS23_SWEN1_MTCG

## Unique Feature

The unique feature is the ability to be able to upgrade cards. If a user has two cards which are of the same type, for example two Dragons, they can fuse those two cards to get a stronger version. The damage of the two cards is summed up and 20 bonus damage is added. Check the *MTC_W_UniqueFeature.exercise.curl.bat* to see how the requests are send to upgrade a card.

I have also added a GitHub workflow which automatically checks if the code builds, compiles and runs all tests without any errors. Check the *maven.yml* in the *.github/workflows* folder. 

## Design, Technical Steps and Lessons learned

The socket Application starts a server in the **Main** class. The server receives an application to host. In our case it is the **MtcgApp**. The server listens for requests and sends them to the **handle** function of the MtcgApp. For each request the server starts a new **thread** to allow multiple requests to be processed. It will then be send to the correct **Controller** Class.

The Controller classes are mainly responsible for checking the correct method of the request and then sends the request to the correct **Service** Class. In those classes the logic for everything happens. They process the input and communicate with the database.

The **BattleController** is where the **threads** are most important. They are needed to allow two players to battle against each other. The Controller handles the threads by using the **synchronized** function of java. This makes sure that only one thread can access this critical step of the code. It checks if a battle is pending or not. And depending on that either the **requestBattle** or **prepareBattle** methods are called. If no battle is pending, the request will wait for five seconds before it gets canceled and the user receives a timeout as a response. During those five seconds another user can send a request for a battle and a battle will start. Both users get a battlelog as response.

The threads caused a lot of problems and to make sure both users get the battle log a lot of steps had to be made. The BattleService has a member variable for the battleLog which has to be renewed each time a battle is started. Otherwise it will cause problems when a new battle is started and it tries to write into the battleLog. The isBattlePending flag also has to be properly set to ensure the requests are not mixed up and cause exceptions in the code. During early development this flagged caused problems by not allowing more battles to start after the first one.

The service classes receive the **repository** Classes via **dependency injection**. This makes it possible to write unit tests for the service classes by mocking the dependencies.

There is also a class called **AuthorizationTokenHelper** which at first was created internally in the services. But because it communicates with the database, it made it impossible to write unit tests without communication with the database.  It is important to consider how tests could be written during development to avoid unnecessary refactoring at the end. Especially when it comes to dependency injection.

## Unit Tests

Unit tests were mainly written for the service classes because those are the most crucial ones for the application. The BattleService has the most unit tests because to check that the damage is modified correctly according to the attacker and defender types.

In addition to the service classes, the base controller class also has a unit tests to check if all support methods of the child classes work as intended. This is imported so all requests arrive at the correct controller.

The AuthorizationTokenHelper class is a crucial class that is used in almost all service classes. Its main task is to check and authenticate the token. It has unit tests to ensure the checks of the token are properly done.

The ResponseHelper is the class that generates the responses the user receives. To make sure all responses are generated properly, unit tests were written.

## Time spend on the project

The project has over 100 commits with the first one being on Nov 6, 2023. A lot of changes and code refactoring happened in those 100 commits. To keep track of the progress, an issue and a branch has been created for milestones such as "cards and deck path". For more details please check the commit history of the project