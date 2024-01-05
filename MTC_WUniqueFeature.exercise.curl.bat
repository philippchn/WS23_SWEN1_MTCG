@echo off

echo delete all
curl -X DELETE http://localhost:10001/delete
echo.

REM --------------------------------------------------
echo UNIQUE FEATURE Upgrade Card
echo .
echo CREATE NEW USER
curl -X POST http://localhost:10001/users --header "Content-Type: application/json" -d "{\"Username\":\"tasty\", \"Password\":\"sauce\"}"
echo .
curl -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"tasty\", \"Password\":\"sauce\"}"
echo .
echo CREATE NEW PACKAGES AND BUY THEM
echo .
curl -X POST http://localhost:10001/packages --header "Content-Type: application/json" --header "Authorization: Bearer admin-mtcgToken" -d "[{\"Id\":\"3871d45b-b630-4a0d-8bc6-a5fc56b6a043\", \"Name\":\"Dragon\", \"Damage\": 70.0}, {\"Id\":\"f950e626-93b3-404e-8cd4-c160649c6366\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"50fe2184-8d13-46eb-840c-6b1712980c4f\", \"Name\":\"WaterSpell\", \"Damage\": 20.0}, {\"Id\":\"a873329a-a702-4ac3-bdf8-e12b2e67faf3\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"0b8d382a-6439-4523-80c6-d3d735ee4eba\", \"Name\":\"FireSpell\",    \"Damage\": 25.0}]"
echo.
curl -X POST http://localhost:10001/transactions/packages --header "Content-Type: application/json" --header "Authorization: Bearer tasty-mtcgToken" -d ""
echo.
echo PRINT tasty's CARDS BEFORE THE UPGRADE
echo .
curl -X GET http://localhost:10001/cards --header "Authorization: Bearer tasty-mtcgToken"
echo .
echo UPGRADE Card
echo .
curl -X POST http://localhost:10001/cards --header "Authorization: Bearer tasty-mtcgToken" -d "[\"f950e626-93b3-404e-8cd4-c160649c6366\", \"3871d45b-b630-4a0d-8bc6-a5fc56b6a043\"]"
echo .
echo PRINT tasty's CARDS AFTER THE UPGRADE
echo .
curl -X GET http://localhost:10001/cards --header "Authorization: Bearer tasty-mtcgToken"
echo .

REM --------------------------------------------------
echo end...

REM this is approx a sleep 
ping localhost -n 100 >NUL 2>NUL
@echo on
