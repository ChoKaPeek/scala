DB_CREATE = generate.cql

run: db
	cd generator && sbt run ~

db:
	cqlsh --file ${DB_CREATE}
