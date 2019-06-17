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

dk-init docker-init:
	docker network create --subnet=172.19.0.0/16 docknet
	docker run --rm --network docknet --name cassandra -d cassandra
	docker build -t sbt .
	docker create --network docknet --name generator -it sbt bash -c 'cd generator && sbt run ~'
	docker create --network docknet --name handler -it sbt bash -c 'cd handler && sbt run ~'

dk-db docker-db:
	docker create --rm --network docknet --name cqlinit -e CQLSH_HOST=cassandra cassandra bash -c 'cqlsh --file ${DB_CREATE}'
	docker cp ${DB_CREATE} cqlinit:.
	docker start cqlinit

dk-cp docker-cp:
	docker cp . cqlinit:/app

dk-run docker-run:
	docker start generator -i

dk-server docker-server:
	docker start handler -i

dk-stop docker-stop:
	- for id in $$(docker ps -a -q); do docker stop $$id; done
	- for id in $$(docker ps -a -q); do docker rm $$id; done
	- docker network rm docknet
