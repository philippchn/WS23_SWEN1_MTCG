# MonsterTradingCardsGame (MTCG)
An HTTP/REST-based server that allows users to trade and battle with and against each other.

GitHub Repo: https://github.com/philippchn/WS23_SWEN1_MTCG

## Setup and Specification

Install Postgres and create a database named **mtcgdb**. The default admin of the database is **postgres** with password **postgres**.
You can change this in the MTCGDatabase file, which is located in the data folder within the application.

After that you can run the *mtcgdb.sql* file to create the necessary tables. The *mtcgdb.png* file roughly shows the structure of the table. The files are in the sql folder. 

The *MTC.exercise.curl.bat* shows how each request has to be send. 

Check the *Specification.pdf* to see how the battle logic works. 

## Created by
    Philipp Chorn during the WS2023 at the UAS Technikum Wien