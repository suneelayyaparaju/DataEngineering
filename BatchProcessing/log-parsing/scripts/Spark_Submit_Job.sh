#!/bin/bash
echo -e "\n---------------------------------------------------------------------------------"
echo -e "##### HDFS clean up process started #####"
echo -e "---------------------------------------------------------------------------------\n"

HDFS_OUTPUT=hdfs:///user/suneelayyaparaju1803/Output
JAR_FILES_LOCATION=hdfs:///user/suneelayyaparaju1803/Deploy/Jar

### HDFS clean up 
hadoop fs -test -d $HDFS_OUTPUT;
	if [ $? == 0 ]; then
		hdfs dfs -rm -r  $HDFS_OUTPUT
		echo -e "*** Cleaned up old output files from HDFS:$HDFS_OUTPUT ***"
	else
		echo -e "*** No output part files exist in HDFS:$HDFS_OUTPUT ***"
	fi
echo -e "\n---------------------------------------------------------------------------------"
echo -e "##### HDFS clean up process ended successfully #####"
echo -e "---------------------------------------------------------------------------------"

echo -e "\n---------------------------------------------------------------------------------"
echo -e "##### Spark Job Started #####"
echo -e "---------------------------------------------------------------------------------\n"
### Spark Job Submit Command 
spark-submit --class com.dataqueue.Driver --master yarn --deploy-mode cluster --executor-memory 1G --executor-cores 2 --num-executors 6 --conf spark.yarn.maxAppAttempts=1 $JAR_FILES_LOCATION/log-parsing.jar