# libsvm-analyzer
This is a private project to do further analysis of the outputs from [libsvm](https://www.csie.ntu.edu.tw/~cjlin/libsvm/).  
`libsvm-analyzer` supports you to make your result from libsvm more informative, not like the set of 1 and -1.

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

`[result file path]`: path of a result file including the set of 1 and -1 per a line  
`[result dir path]`: path of a directory including result files (Note: The files will be sorted.)  
`[test file path]`: path of a test file used for `svm_predict` in libsvm to generate the above result file  
`[test dir path]`: path of a directory including test files (Note: The files will be sorted.)  
`[output file path]`: path of a newly crated output file including FRR and FAR  

---
### WeightVectorRegenerator
`WeightVectorRegenerator` regenerates weight vector(s) from model file(s).  
Note: It works only for linear model files. Output files from different model files are meaningless.

#### Usage
`java -classpath libsvm-analyzer.jar tool.WeightVectorRegenerator [model file or dir path] [output dir path]`  

`[model file path]`: path of a model file generated by `svm_train` in libsvm  
`[model dir path]`: path of a directory including model files  
`[output dir path]`: path of a newly crated output dir for output files of weight vectors  

---
### OneClassSvmRegenerator
`OneClassSvmRegenerator` regenerates the raw value in *sgn()* of the One-Class SVM's decision function.  
![one-class svm](http://bit.ly/1TfnRnV)

#### Usage
`java -classpath libsvm-analyzer.jar tool.OneClassSvmRegenerator [model file path] [test file path] [output dir path]`  
or  
`java -classpath libsvm-analyzer.jar tool.OneClassSvmRegenerator [model dir path] [test dir path] [output dir path]`  

`[model file path]`: path of a model file generated by `svm_train` in libsvm  
`[model dir path]`: path of a directory including model files (Note: The files will be sorted.)  
`[test file path]`: path of a test file used for `svm_predict` in libsvm to generate the above result file  
`[test dir path]`: path of a directory including test files (Note: The files will be sorted.)  
`[output dir path]`: path of a newly crated output file including FRR and FAR  