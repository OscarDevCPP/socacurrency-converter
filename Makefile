all: run

run:
	mvn clean  compile package
	mvn exec:java