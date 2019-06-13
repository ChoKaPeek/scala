DB_CREATE = generate.cql

run:
	cd generator && sbt run ~

db:
	cqlsh --file ${DB_CREATE}

server:
	cd handler && sbt run ~

harmonize:
	cp generator/src/main/scala/tools/Models.scala handler/app/tools/.
	cp generator/src/main/scala/tools/Spark.scala handler/app/tools/.
	cp generator/src/main/scala/tools/Storage.scala handler/app/tools/.

docker-init:
	docker network create --subnet=172.19.0.0/16 docknet
	docker run --rm --network docknet --name apt -d debian bash -c 'apt update && apt install netcat -y && nc -l -p 1234'
	docker create --rm --network docknet -p 1234 --name cassandra cassandra bash -c 'cqlsh --file /app/${DB_CREATE}'
	docker cp ${DB_CREATE} cassandra:/app
	docker start cassandra
	docker build -t sbt .

docker:
	docker run --network docknet -p 1234 --name workstation -it sbt bash

docker-stop:
	- for id in $$(docker container ls --all | grep -o "[^ ]*" | tail +2); do docker stop $$id; done
	- for id in $$(docker container ls --all | grep -o "[^ ]*" | tail +2); do docker rm $$id; done
	- docker network rm docknet
