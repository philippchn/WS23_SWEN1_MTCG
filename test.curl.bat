@echo off

REM --------------------------------------------------
REM Monster Trading Cards Game
REM --------------------------------------------------
title Monster Trading Cards Game
echo CURL Testing for Monster Trading Cards Game
echo.

REM --------------------------------------------------
echo 14) edit user data
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Bearer kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Bearer altenhof-mtcgToken"
echo.
curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Bearer kienboec-mtcgToken" -d "{\"Name\": \"Kienboeck\",  \"Bio\": \"me playin...\", \"Image\": \":-)\"}"
echo.
curl -X PUT http://localhost:10001/users/altenhof --header "Content-Type: application/json" --header "Authorization: Bearer altenhof-mtcgToken" -d "{\"Name\": \"Altenhofer\", \"Bio\": \"me codin...\",  \"Image\": \":-D\"}"
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Bearer kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Bearer altenhof-mtcgToken"
echo.
echo.
echo should fail:
curl -X GET http://localhost:10001/users/altenhof --header "Authorization: Bearer kienboec-mtcgToken"
echo.
curl -X GET http://localhost:10001/users/kienboec --header "Authorization: Bearer altenhof-mtcgToken"
echo.
curl -X PUT http://localhost:10001/users/kienboec --header "Content-Type: application/json" --header "Authorization: Bearer altenhof-mtcgToken" -d "{\"Name\": \"Hoax\",  \"Bio\": \"me playin...\", \"Image\": \":-)\"}"
echo.
curl -X PUT http://localhost:10001/users/altenhof --header "Content-Type: application/json" --header "Authorization: Bearer kienboec-mtcgToken" -d "{\"Name\": \"Hoax\", \"Bio\": \"me codin...\",  \"Image\": \":-D\"}"
echo.
curl -X GET http://localhost:10001/users/someGuy  --header "Authorization: Bearer kienboec-mtcgToken"
echo.
echo.

REM this is approx a sleep
ping localhost -n 100 >NUL 2>NUL
@echo on