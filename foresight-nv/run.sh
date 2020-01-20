#!/bin/bash

cd ../foresight-core/
mvn install
cd ../foresight-bpmn/
mvn install
cd ../foresight-nv/
mvn eclipse:eclipse
mvn clean compile package 
