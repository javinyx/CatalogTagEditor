# tiw-project-2020-21
##Specifica
###Versione HTML pura

Un’applicazione permette all’utente (ad esempio il curatore di un catalogo online di immagini) di etichettare le immagini allo scopo di consentire la ricerca in base alla categoria. Dopo il login, l’utente accede a una pagina HOME in cui compare un albero gerarchico di categorie.
Le categorie non dipendono dall’utente e sono in comune tra tutti gli utenti. Un esempio di un ramo dell’albero è il seguente:

- 9 Mitologia classica e storia antica >>sposta
    - 91 Divinità della mitologia classica >>sposta
        - 911 Divinità del cielo >>sposta
            - 9111 Giove >>sposta
                - 91111 Attributi di Giove >>sposta
            - 9112 Giunone >>sposta
        - 912 Divinità degli inferi >>sposta
            - 9121 Plutone >>sposta 
            - 9122 Ecate >>sposta

L’utente può inserire una nuova categoria nell’albero. Per fare ciò usa una form nella pagina HOME in cui specifica  il nome della nuova categoria e sceglie la categoria padre.
L’invio della nuova categoria comporta l’aggiornamento dell’albero: la nuova categoria è appesa alla categoria padre come ultimo sottoelemento. Alla nuova categoria viene assegnato un codice numerico che ne riflette la posizione (ad esempio, la nuova categoria Mercurio, figlia della categoria “911 Divinità del cielo” assume il codice 9113).
Per semplicità si ipotizzi che per ogni categoria il numero massimo di sottocategorie sia 9, numerate da 1 a 9.
Dopo la creazione di una categoria, la pagina HOME mostra l’albero aggiornato. L’utente può spostare di posizione una categoria: per fare ciò clicca sul link “sposta” associato alla categoria da spostare.
A seguito di tale azione l’applicazione mostra, sempre nella HOME page,  l’albero con evidenziato il sotto albero attestato sulla categoria da spostare:  tutte le altre categorie hanno un link “sposta qui”.
Ad esempio, a seguito del click sul link “sposta” associato alla categoria “9111 Giove” l’applicazione visualizza l’albero come segue:

- 9 Mitologia classica e storia antica >>sposta qui
    - 91 Divinità della mitologia classica >>sposta qui
        - 911 Divinità del cielo >>sposta qui
            - 9111 Giove
                - 91111 Attributi di Giove
                - 9112 Giunone >>sposta qui
        - 912 Divinità degli inferi >>sposta qui
            - 9121 Plutone >>sposta qui
            - 9122 Ecate >>sposta qui

La selezione di un link “sposta qui” comporta l’inserimento della categoria da spostare come ultimo figlio della categoria destinazione. Ad esempio, la selezione del link “sposta qui” della categoria “912 Divinità degli inferi”
comporta la seguente modifica dell’albero:

- 9 Mitologia classica e storia antica >>sposta
    - 91 Divinità della mitologia classica >>sposta
        - 911 Divinità del cielo >>sposta
            - 9111 Giunone >>sposta
        - 912 Divinità degli inferi >>sposta
            - 9121 Plutone >>sposta
            - 9122 Ecate >>sposta
            - 9123  Giove >>sposta
                - 91231  Attributi di Giove  >>sposta

Le modifiche effettuate da un utente e salvate nella base di dati diventano visibili agli altri utenti.

###Versione con JavaScript

Si realizzi un’applicazione client server web che modifica le specifiche precedenti come segue:

- Dopo il login dell’utente, l’intera applicazione è realizzata con un’unica pagina.
- Ogni interazione dell’utente è gestita senza ricaricare completamente la pagina, ma produce l’invocazione asincrona del server e l’eventuale modifica del contenuto da aggiornare a seguito dell’evento.
- La funzione di spostamento di una categoria è realizzata mediante drag & drop.  
- A seguito del drop della categoria da spostare compare una finestra di dialogo con cui l’utente può confermare o cancellare lo spostamento. La conferma produce l’aggiornamento a lato client dell’albero.
- L’utente realizza spostamenti anche multipli a lato client. A seguito del primo spostamento compare un bottone SALVA la cui pressione provoca l’invio al server dell’elenco degli spostamenti realizzati (NON dell’intero albero).
  L’invio degli spostamenti produce l’aggiornamento dell’albero nella base dei dati e la comparsa di un messaggio di conferma dell’avvenuto salvataggio.