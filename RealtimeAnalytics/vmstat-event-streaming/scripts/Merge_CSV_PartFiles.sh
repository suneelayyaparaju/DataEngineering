#!/bin/bash
########################################################
# MERGING ALL CSV PART FILES INTO A SINGLE CSV FILE    # 
########################################################

PART_FILES=/home/suneelayyaparaju1803/PartFiles
SPARK_OUTPUT=/home/suneelayyaparaju1803/Output
HDFS_OUTPUT=hdfs:///user/suneelayyaparaju1803/Output

# If $PART_FILES directory exists delete all files from it, otherwise create new directory.
if [ -d $PART_FILES ]; then
	rm -rf $PART_FILES/*	
else
	mkdir -p $PART_FILES
fi

# If $SPARK_OUTPUT directory exists delete all files from it, otherwise create new directory.
if [ -d $SPARK_OUTPUT ]; then
	rm -rf $SPARK_OUTPUT/*	
else
	mkdir -p $SPARK_OUTPUT
fi

hadoop fs -test -d $HDFS_OUTPUT/$1/$2;
if [ $? == 0 ]; then
	mkdir -p $PART_FILES/$1/$2
	hdfs dfs -get $HDFS_OUTPUT/$1/$2/part-* $PART_FILES/$1/$2
	
	# Merge all CSV Files in one CSV files 
	sed '1q' $PART_FILES/$1/$2/part-*.csv > $PART_FILES/$1/$2/$1_$2.csv
	tail -q -n +2 $PART_FILES/$1/$2/part-* >> $PART_FILES/$1/$2/$1_$2.csv
	
	cp $PART_FILES/$1/$2/$1_$2.csv $SPARK_OUTPUT

else
	echo -e "*** CSV part files not found for $1 => $2 ***"
fi






 

