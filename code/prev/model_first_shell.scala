val raw_data=sc.textFile("/home/ubuntu/workspace/project/data/1601/1601_07_standard.csv")
val records=raw_data.map(line=>line.split(","))
records.first()

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors


//1-of-K를 활용하기 위한 사전 변수
val subway_dict=records.map(r=>r(0)).distinct().zipWithIndex().collectAsMap() // distinct 꼭 사용할 것
val date_dict=records.map(r=>r(1)).distinct().zipWithIndex().collectAsMap()


val data=records.map{r=>
val label=r(r.size-1).toInt
val feature_subway=subway_dict(r(0)).toDouble // 추후 List 내부 형을 맞춰주기 위해 형변환
val feature_date=date_dict(r(1)).toDouble
val feature_number=r.slice(2,r.size-1).map(_.toDouble).toList // array => List
val features=List(List(feature_subway, feature_date), feature_number).flatMap(x=>x.map(_*1))
LabeledPoint(label, Vectors.dense(features.toArray))
} // 1-of-k를 이용함
data.cache

// 일정 값이 정해져 있는 카테고리형 변수가 아니므로, 그냥 그대로 유지하기로 한다

import org.apache.spark.mllib.regression.LinearRegressionWithSGD
import org.apache.spark.mllib.tree.DecisionTree
// Linear모델의 train / DT모델의 trainRegressor를 이용한다

val linear_model=LinearRegressionWithSGD.train(data, numIterations=100, stepSize=0.000001)
// intercept에 관련된 인자가 사라짐

val true_vs_predicted=data.map(p=>(p.label, linear_model.predict(p.features)))
true_vs_predicted.take(5)


// https://spark.apache.org/docs/latest/mllib-decision-tree.html
val categoricalFeaturesInfo = Map[Int, Int]() // 책 내용과 다름, 이렇게 선언해주어야 함
val impurity = "variance"
val maxDepth = 5
val maxBins = 32

val dt_model=DecisionTree.trainRegressor(data,categoricalFeaturesInfo, impurity, maxDepth, maxBins)
val preds=dt_model.predict(data.map(d=>d.features))
val actual=data.map(d=>d.label)
val true_vs_predicted_dt=actual.zip(preds)
true_vs_predicted_dt.take(5)
dt_model.depth
dt_model.numNodes

// MSE(Mean Squared Error) 제곱 오차 메소드(최소 제곱 회귀에 대한 손실메소드)
def squared_error(actual:Double, pred:Double)={
    Math.pow((pred-actual),2)
}

// MAE(Mean Average Error) 평균 절대 오차(MSE보다 편차가 크지 않음)
def abs_error(actual:Double, pred:Double)={
    Math.abs(pred-actual)
}    

// RMSLE(Root Mean Squared Log Error)
def squared_log_error(actual:Double, pred:Double)={
    Math.pow((Math.log(pred+1)-Math.log(actual+1)),2)
} // 실값에서 에러가 뜨는 바람에 log인자<0이 되어 에러가 남(NaN)

val mse=true_vs_predicted.map{case(t,p)=>squared_error(t,p)}.mean()
val mae=true_vs_predicted.map{case(t,p)=>abs_error(t,p)}.mean()
//val rmsle=true_vs_predicted.map{case(t,p)=>squared_log_error(t,p)}.mean()

val mse_dt=true_vs_predicted_dt.map{case(t,p)=>squared_error(t,p)}.mean()
val mae_dt=true_vs_predicted_dt.map{case(t,p)=>abs_error(t,p)}.mean()
val rmsle_dt=true_vs_predicted_dt.map{case(t,p)=>squared_log_error(t,p)}.mean()

val data_log=data.map(lp=> LabeledPoint(Math.log(lp.label), lp.features))
val model_log=LinearRegressionWithSGD.train(data_log, numIterations=10, stepSize=0.1)
val true_vs_predicted_log=data_log.map(p=>(Math.exp(p.label), Math.exp(model_log.predict(p.features))))

val mse_log=true_vs_predicted_log.map{case(t,p) => squared_error(t, p)}.mean()
val mae_log=true_vs_predicted_log.map{case(t,p) => abs_error(t, p)}.mean()

import org.apache.spark.mllib.linalg.distributed.RowMatrix
val vectors=data.map(lp=>lp.features)
val matrix=new RowMatrix(vectors)
val matrixSummary=matrix.computeColumnSummaryStatistics()
matrixSummary.mean
matrixSummary.max
matrixSummary.variance
// 각 컬럼에 대한 정보를 리턴, 비교적 평균과 편차의 정도가 날씨데이터와 오일데이터 차이가 크다


// 입력값 표준화 작업
import org.apache.spark.mllib.feature.StandardScaler
val scaler=new StandardScaler(withMean=true, withStd=true).fit(vectors)
val scaledData=data.map(lp => LabeledPoint(lp.label, scaler.transform(lp.features)))
data.first.features
scaledData.first.features


val linear_model_stand=LinearRegressionWithSGD.train(scaledData, numIterations=50, stepSize=0.01)
val true_vs_predicted_stand=scaledData.map(p=>(p.label, linear_model_stand.predict(p.features)))
val mse_stand=true_vs_predicted_stand.map{case(a,p)=> squared_error(a,p)}.mean()
val mae_stand=true_vs_predicted_stand.map{case(a,p)=> abs_error(a,p)}.mean()


import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.optimization.Updater
import org.apahce.spark.mllib.optimization.SimpleUpdater
import org.apache.spark.mllib.optimization.L1Updater
import org.apache.spark.mllib.optimization.SquaredL2Updater
import org.apache.spark.mllib.classfication.ClassificationModel

def trainWithParams(input:RDD[LabeledPoint], regParam:Double, numIterations:Int, updater:Updater, stepSize:Double)={
    val lr=new LogisticRegressionWithSGD
    lr.optimizer.setNumiterations(numIterations).setUpdater(updater).setRegParam(regParam).setStepSize(stepSize)
    lr.run(input)
}

=====
//https://spark.apache.org/docs/latest/ml-pipeline.html#dataframe
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.linalg.{Vector, Vectors}
//mllib Vector와 ml.Vector는 완전 다른 형태
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.Row

val raw_data=sc.textFile("/home/ubuntu/workspace/project/data/1601/1601_07_standard.csv")
val records=raw_data.map(line=>line.split(","))

val subway_dict=records.map(r=>r(0)).distinct().zipWithIndex().collectAsMap() // distinct 꼭 사용할 것

val df=records.map{r=>
val label=r(r.size-1).toInt
val feature_subway=subway_dict(r(0)).toDouble
val feature_number=r.slice(2,r.size-1).map(_.toDouble).toList // array => List
val features=List(List(feature_subway), feature_number).flatMap(x=>x.map(_*1))
(label, Vectors.dense(features.toArray))
}

val training=spark.createDataFrame(df).toDF("label", "features")
// scala에서는 '와 "를 구분함


val lr = new LogisticRegression()
println("LogisticRegression parameters:\n" + lr.explainParams() + "\n")
// 각 파라미터에 대한 설명이 들어있음

lr.setMaxIter(10).setRegParam(0.01)

val model1 = lr.fit(training)
// Since model1 is a Model (i.e., a Transformer produced by an Estimator),
// we can view the parameters it used during fit().
// This prints the parameter (name: value) pairs, where names are unique IDs for this
// LogisticRegression instance.
println("Model 1 was fit using parameters: " + model1.parent.extractParamMap)

// 실행했던 부분
// dataframe의 경우 rdd보다 실행시간이 오래걸림
===

val paramMap = ParamMap(lr.maxIter -> 20)
  .put(lr.maxIter, 30)  // Specify 1 Param. This overwrites the original maxIter.
  .put(lr.regParam -> 0.1, lr.threshold -> 0.55)  // Specify multiple Params.

// One can also combine ParamMaps.
val paramMap2 = ParamMap(lr.probabilityCol -> "myProbability")  // Change output column name.
val paramMapCombined = paramMap ++ paramMap2

// Now learn a new model using the paramMapCombined parameters.
// paramMapCombined overrides all parameters set earlier via lr.set* methods.
val model2 = lr.fit(training, paramMapCombined)
println("Model 2 was fit using parameters: " + model2.parent.extractParamMap)

// Prepare test data.
val test = spark.createDataFrame(Seq(
  (1.0, Vectors.dense(-1.0, 1.5, 1.3)),
  (0.0, Vectors.dense(3.0, 2.0, -0.1)),
  (1.0, Vectors.dense(0.0, 2.2, -1.5))
)).toDF("label", "features")

// Make predictions on test data using the Transformer.transform() method.
// LogisticRegression.transform will only use the 'features' column.
// Note that model2.transform() outputs a 'myProbability' column instead of the usual
// 'probability' column since we renamed the lr.probabilityCol parameter previously.
model2.transform(test)
  .select("features", "label", "myProbability", "prediction")
  .collect()
  .foreach { case Row(features: Vector, label: Double, prob: Vector, prediction: Double) =>
    println(s"($features, $label) -> prob=$prob, prediction=$prediction")
  }