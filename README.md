# ISF Picking Scheduler for Ocado
### Author: Ernest Strychalski <strychalski.ernest@gmail.com>

## About Project
The project creates a schedule for workers that use Ocado Smart Platform.
Program takes orders and information about store (number of workers and working hours), 
analyze them and based on data provided creates a schedule. In order to maximize the preformence
the program uses parameter "orderValue" which help to choose which orders to prioritize. 
In the schedule there is information about:
 - PickerId
 - OrderId 
 - Picking start time

The algorithm which optimize the scheduling process is working correctly, but it will be the main 
focus in the future development of the program. Right now it uses very simple rules to distinguish between
potential orders. 

### Java

Project has been build on java 17.0.6

### External Libraries:
 - json-simple-1.1.1
 - junit5.8.1 

### Project status: working

## Usage

The jar-file is ready to use. In order to open the program use command:

    java -jar <path-to-file> <path-to-store.json> <path-to-orders.json>

