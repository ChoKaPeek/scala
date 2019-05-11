DB_CREATE = generate.cql

run: db
	cd generator && sbt run ~

db:
	cqlsh --file ${DB_CREATE}

server:
	cd handler && sbt run ~

harmonize:
	cp generator/src/main/scala/tools/Models.scala handler/app/tools/.
	cp generator/src/main/scala/tools/Spark.scala handler/app/tools/.
	cp generator/src/main/scala/tools/Storage.scala handler/app/tools/.
	cp generator/src/main/scala/tools/Parser.scala handler/app/tools/.
