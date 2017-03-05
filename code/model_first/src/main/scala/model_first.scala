package net.wshid;

import org.apache.spark._
import org.apache.spark.rdd.RDD
/*
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
*/
object model_first {
  def main(args: Array[String]) {

    val conf= new SparkConf().setMaster("spark://csxion-p9-ml-project-4374433:7077").setAppName("model_first")
    //conf.set("spark.app.name", "first_app")
    //conf.set("spark.master", "spark://csxion-p9-ml-project-4374433:7077")
    val sc = new SparkContext(conf);
        // masternode 정보, app 이름, master port를 의미하는 듯
        //spark ml 책 활용하여 구문 변경할 것
    val input = sc.parallelize(List(1,2,3,4))
    //val result = computeAvg(input)
    //val avg = result._1 / result._2.toFloat
    println(input.collect().toList)
  }
  def computeAvg(input: RDD[Int]) = {
    input.aggregate((0, 0))((x, y) => (x._1 + y, x._2 + 1),
      (x,y) => (x._1 + y._1, x._2 + y._2))
  }
}