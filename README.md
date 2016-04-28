# libsvm-analyzer
This is a private project to do further analysis of the outputs from [libsvm](https://www.csie.ntu.edu.tw/~cjlin/libsvm/).  
`libsvm-analyzer` supports you to make your result from libsvm more informative, not like the set of -1 and 1.

## Requirement
Java 1.8+

## Usage
`java -classpath libsvm-analyzer.jar "package name"`

## Package Contents
### SvmResultAnalyzer
`SvmResultAnalyzer` calculates FRR(False Rejection Rate or FNR: False Negative Rate) and FAR(False Acceptance Rate or FPR: False Positive Rate).

#### Usage
`java -classpath libsvm-analyzer.jar tool.SvmResultAnalyzer [result file path] [test file path] [output file path]`  
or  
`java -classpath libsvm-analyzer.jar tool.SvmResultAnalyzer [result dir path] [test dir path] [output file path]`  