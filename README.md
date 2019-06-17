# Company

## Problematic
Bonjour

Voici votre projet pour la partie scala :

Your project is to imagine and drone/mobile IOT devices and create it's big data architecture.
write your code in scala send by email the git with the commit hash before the 9th of July.

You will have to think about company or ngo creating/selling/promoting an IOT devices.
Example : a company selling autonomous transport bus to town halls

All your devices will be sending regulary some status report as data (you'll chose the format and che communication means).
You will have to build a big data solution with two sides :
A) Monitoring
Almost in real times analyse the status of every devices and alert an user of a critical situation.
Example a bus is full of passanger or out of fuel, thus it's anable to provides a good services to passangers waiting at the next bus stop.

B) Storing and analytics
Store  all the data send by every devices and be able to answer questions like :
In proportion to the whole number of devices is there more failing devices in the north hemisphere or the south hemisphere?
Is there more failing devices when the weather is hot or when the weather is called?
Among the failing devices which percentage fails beceause of low battery or empty fuel tank?



I) Mandatory (15 points)
- Imagine the typical message of your device it must include latitude, longitude, temperature, and fuel/battery remaining.
- write the programme that will read all the files containing message in the folder given buy the user. Every line of each file will contain one message. File format could be csv or text file containing json line.
- than send the message to a server using the protocol of your choice rest, mqtt…
- display the message on a web ui

2) Optional
- store the data in a database (5)
- write your front-end using scala-js (5)
- deploy in the cloud (5)

--
Adrien Broussolle

## How to run
You need sbt, a Java jdk, cassandra
go to the root folder

first run `cassandra -f` to launch cassandra server
run `make server` to run our server
run `make run` to run our generator

you will need to enter the name of a directory containing well-formated json
logs from our drones, that will be sent to cassandra and queried by the server
when needed to display the page `/list`

## Docker
Create containers, run cassandra
`make dk-init`

Wait for cassandra to be ready and
`make dk-db`

To run generator:
`make dk-run`

To run handler:
`make dk-server`

To update files in container:
`make dk-cp`

To stop servers:
`make dk-stop`
