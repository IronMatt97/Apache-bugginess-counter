# ISW2-Deliverable1
___
##Studente: Matteo Ferretti
##Matricola: 0300049

##Scopo progetto:
Prelevare da Jira il progetto Falcon e sviluppare un process control chart con X = mesi e Y = numero di ticket risolti.
Sia il nome del progetto da prelevare, sia la scelta dei dati da rappresentare sull'asse Y sono stati selezionati in base al mio cognome.
Il materiale relativo al progetto Falcon è reperibile [qui](https://issues.apache.org/jira/browse/FALCON-1958?jql=project%20%3D%20FALCON%20AND%20resolution%20%3D%20Unresolved%20ORDER%20BY%20priority%20DESC%2C%20updated%20DESC).
###Link GitHub:
- [Github personale | Matteo Ferretti](https://github.com/IronMatt97?tab=repositories)

##Scopo delle classi realizzate:
All'interno della cartella src è possibile trovare il codice:
- *Main*: classe principale che serve a far partire il codice, di fatto andando a realizzare il csv dei dati estratti dal JSON. Il process control chart l'ho ottenuto proprio a partire dal suddetto csv.
- *IssueCounter*: classe di "controllo" che serve a scrivere fisicamente il csv (tramite il metodo statico 'generateCSV'), a partire da una matrice di dati.
- *TicketInfoGetter*: classe responsabile del prelevamento dei dati dei ticket relativi al progetto Falcon tramite il metodo 'getInfo'.
- *GitHandler*: classe di utility che contiene metodi per confrontare i dati della resolution date presenti su Jyra con quelli invece presenti nei commit che possono differire in alcuni casi.

##Considerazione progettuale:
Ho scelto di realizzare (tramite l'implementazione di un parametro booleano) il metodo che controlla le date di risoluzione dei ticket in maniera tale da creare due csv differenti. Impostando il parametro su 'true' è possibile ottenere il csv che tiene conto delle differenze tra le date riportate su Jyra e quelle nei commit, con 'false' tale difformità viene ignorata. 
- Il materiale estratto dai csv è stato raccolto ed analizzato in dei relativi process control chart su file excel.



Per maggiori informazioni, consultare il report del progetto all'interno della cartella principale.
