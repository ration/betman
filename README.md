# Betman

Betman is a probably over-engineered implementation of a betting system for groups. 
* Players guess the score of games, and are given points per given rules.

## Backend API

| Type | PATH | Body | Response |
|------|------|------|----------|
| POST | /users/register | {username:string, password:string, group?:string} |  |
| POST | /users/login | {username:string, password:string} |  |
| POST | /groups/join/:id?group=id |  |  |
| GET  | /groups/:id | | {name:string, description:string, game:string}
| GET  | /games/:id  | | { id:int, description:string, matches[]: {home:string,away:string,date:date, homeGoals:int, awayGoals:int} }
| GET  | /bets/:userid?groupId=int
| POST | /bets/:userid?groupId=int | { matches[]: {id:int, home:int,away:int}, others[]: {id:int,value:string} | |


    



Uses data from:
https://github.com/lsv/fifa-worldcup-2018