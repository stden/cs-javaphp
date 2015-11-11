## Meeting Protocol ##

  1. Интерфейс
  1. Основной (DCES)
    * Architecture & Stub code
      * Database
        * use cases
          * contest, user\_id, problem\_id -> max submission\_time
          * contest, problem\_id ->
    * Da real code \m/
    * Domain
      * Vocabulary
        * Problem statement data/statement data generator
        * Problem correct answer/answer checker
        * Contestant's answer
  1. Overview & plans for future (nearest)


## Contest adjustment ##
```
c:Client
s:Server
sp:ServerPlugin
cp:ClientPlugin
db:Database


c:SERVER_MSG=s.ProcessContestAdjustRequest(contest_descr, problem[])
s:DB_MSG=db.SetContestData(contest_descr)
[c: foreach p in problem if(p != null)]
s:db.ChangeProblemContext(p.ClientPlugin, p.ServerPlugin, p.Name)

#Statement is a bytes array 
s:sp.ProcessProblem(p.Statement, p.Answer)

*1 sp
ServerPlugin is responsible 
for problem statement and 
answer DB data processing
(no one can change 
according fields)
*1

sp:sp.ProcessStatement()
sp:db.ChangeProblemStatement()

sp[1]:sp.ProcessAnswer()
sp[1]:db.ChangeProblemAnswer()


[/c]
```

## Solution submit ##
```
cp:ClientPlugin
c:Client
s:Server
sp:ServerPlugin
db:Database

cp:res=c.SubmitSolution(sol)
c:res=s.SubmitSolution(user, prob, sol)
s:res=sp.CheckSolution(sol)
sp:ans=db.GetAnswer(prob)
sp:res=sp.Compare(answer, sol)
sp[1]:db.SetResult(user, res)
```