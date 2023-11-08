# Instructions to run

* mvn package
* java -jar .\target\receipts-1.0.jar

# Offer configuration

Offers configured in CSV file format with ";" as delimiters

Fields:
* offer ID - mandatory
* offer category (supported categories: beverage, snack, extra) - mandatory
* volume - optional
* name - mandatory
* patterns - comma separated list of words for client input processing - mandatory
* available extra offers - comma separated list of available extra id

# Assumptions: 

## Logic assumption
* Extras ale allowed only for coffee
* Extra can not be ordered without base offering
* No limit for extra for one base offering. All of them must be delimited by word "with"
* Stamps are not earned for free snacks (only for paid), so if client has 4 stamps and buy one snack he gets it for free and no any stamps
* For free extra - if we have several free extra we sort them by price, so free extras go to the cheapest first
* As well as it was not specified how to provide information about existing stamps, it was decided to take 0 as default and allow to set it from command line with "-s" parameter  
* It was decided that program finishes after every order processing (no batch mode)
* By default, program started in interactive mode. Order data also can be processed from file. File name should be specified with -f parameter 
* we allow flexible input processing, driven by defined token words

## Development assumptions
* Interfaces and factories were not used for simplicity

