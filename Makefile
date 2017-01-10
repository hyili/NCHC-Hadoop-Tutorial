all:
	mvn clean package -e
wordcount:
	hadoop fs -put data/word.txt
	hadoop jar target/MR-sample-0.0.1-SNAPSHOT.jar org.nchc.train.mr.wordcount.WordCount word.txt output
sshversion:
	hadoop fs -put data/zgrab-results-partition.json
	hadoop jar target/MR-sample-0.0.1-SNAPSHOT-jar-with-dependencies.jar org.nchc.train.mr.sshversion.sshVersion zgrab-results-partition.json output
sshversion-1000000:
	hadoop fs -put data/zgrab-results-partition-1000000.json
	hadoop jar target/MR-sample-0.0.1-SNAPSHOT-jar-with-dependencies.jar org.nchc.train.mr.sshversion.sshVersion zgrab-results-partition-1000000.json output
clean:
	hadoop fs -rm -f word.txt zgrab-results-partition.json zgrab-results-partition-1000000.json
	hadoop fs -rm -r -f output
	hadoop fs -rm -r -f .staging
	hadoop fs -rm -r -f .Trash
show:
	hadoop fs -cat output/part-r-*
