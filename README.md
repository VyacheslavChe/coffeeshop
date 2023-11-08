# Assumptions: 

## Logic assumption
* Extras ale allowed only for coffee
* Stamps are not earned for free snacks (only for paid), so if client has 4 stamps and buy one snack he gets it for free and no any stamps
* For free extra - if we have several free extra we sort them by price, so free extras go to the cheapest first
* As well as it was not specified how to provide information about existing stamps, it was decided to take 0 as default and allow to set it from command line with "-s" parameter  
* It was decided that program finishes after every order processing (no batch mode)
* By default, program started in interactive mode. Order data also can be processed from file. File name should be specified with -f parameter 

## Development assumptions
* Interfaces and factories were not used for simplicity

