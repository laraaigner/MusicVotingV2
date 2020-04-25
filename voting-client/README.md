# Mobile-Client

Mit diesem VueJs Projekt wird es allen Gästen der Veranstaltung ermöglicht für die gewünschte Musik abzustimmen. Für das Voting ist keinerlei Authentifizierung notwendig, die Beschränkung, nur einmal für einen Song abstimmen zu können, ist somit nur clientseitig vorhanden. In erster Instanz wird damit in Kauf genommen, dass mögliche "Betrüger" leichtes Spiel haben.

## Dokumentation
![](/docs/voting_client.png)

Alle Daten werden in einem VueX-Store gehalten. Die einzelnen Komponenten greifen auf diese Daten zu und subscriben sich auf die Änderungen. Wenn eine Änderung vom Server erhalten wird, so aktualisiert der VueX Store die Daten und benachrichtigt die Komponenten. Eine VersionId (UpdateId) wird verwendet um immer auf dem aktuellen Stand zu bleiben. Sie wird bei jeder Änderung am Server um 1 erhöht. Bekommt die App eine Änderung muss die Differenz zwischen der erhaltenen Version und der eigenen Version genau 1 betragen. Ist das der Fall wird die Änderung übernommen. Ist die Differenz größer als 1, so wird die gesamte Playlist neu geladen. Das Feld votes im VueX Store wird verwendet um zu speichern für welchen Song der User bereits gevotet hat, um das richtige Icon anzeigen zu können. Es wird mithilfe von VueX-Persistence auch beim Reload einer Seite behalten.

## Entwickler
- Mario Lengauer
- Leon Kuchinka
