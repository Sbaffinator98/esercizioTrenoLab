# Esercizio Developer per TrenoLab

## Cosa ho fatto

In questo esercizio vengono forniti 2 csv: **planned.csv** con il treno, la destinazione e l'orario di arrivo previsto  e **actual.csv** con il treno, la destinazione, l'orario di arrivo previsto, l'orario di arrivo effettivo e un ritardo.
In **actual.csv** l'orario previsto non coincide con quello di **planned.csv** quindi io lo ho considerato erraro.

In output al software quindi del primo .CSV ho preso la tripletta di dati e gli ho affiancato l'orario di arrivo effettivo da **actual.csv** e poi calcolato l'effettivo ritardo (anche negativo) in secondi per ogni fermata chiamandolo **actualTRUE.csv**.

Mentre il file **output.csv** contiene i 5 treni più in ritardo al arrivo a destinazione ordinati in ordine decrescente.

## Assunzioni

Ho assunto che :
* La colonna di **actual.csv** con i tempi previsti sia errata.
* Ogni **numero treno** come **4C7D** sia un identificativo univoco per ogni task di un treno (treno A che deve andare da Pisa a Milano è un altro treno rispetto treno A che va da Napoli a Roma).
* Il sistema Linux desktop abbia una connessione a internet e i pacchetti di base.


## Struttura del software

La classe principale è **scheduler** nella quale il metodo **ReadData** prende in imput i path dei 2 .csv forniti e ritorna un ArrayList di stringhe contenenti i dati combinati in maniera corretta tra i 2 .csv.

Il metodo **CreateTrain** ritorna un HashMap costituito da una stringa con il nome del treno e un oggetto **Treno** che contiene le sui informazioni tra cui il suo **Itinerario** contente anche il ritardo effettivo ricalcolato. 

Infine **actualTrueCsv** e  **outputCsv** stampano i .csv come descritto sopra.

## Istruzioni di installazione
##### Update the package lists and install git and jdk
```
sudo apt update
sudo apt install git
sudo apt install default-jdk
```
##### Clone the repo
```
git clone https://github.com/Sbaffinator98/esercizioTrenoLab
```

##### Remove the output file of the script
```
rm esercizioTrenoLab/csv/ActualTRUE.csv
rm esercizioTrenoLab/csv/output.csv
```
##### Enter the folder and run the jar
```
cd esercizioTrenoLab
java -jar run.jar
```