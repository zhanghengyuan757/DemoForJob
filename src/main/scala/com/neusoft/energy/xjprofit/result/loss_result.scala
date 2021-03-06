package com.neusoft.energy.xjprofit.result

import com.neusoft.energy.xjprofit.mytrait.HiveWritable
import com.neusoft.energy.xjprofit.topology.{Topology, User}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.{DataFrame, SQLContext}
import com.neusoft.energy.xjprofit.SplitEntrance.sqlc
@deprecated
case class loss_result(
  var id: String, //流水id
  cons_no: String, //用户id（包括专变用户， 公变台区，公变台区用户）
  cons_type: String, //类型(包括专变用户， 公变台区，公变台区用户)
  alllCost: BigDecimal, //总损耗
  updatetime: String //更改时间
)
@deprecated
object loss_result extends HiveWritable{
  override val tableName: String = "loss_result"

  def getDf( topology: RDD[(User, Topology)]):DataFrame={
    import sqlc.implicits._
//    val rowCount=getRowCount
    topology.map(u=>{
      val user =u._1
      val cons_no=user.cons_no
      val cons_type=user.pub_priv_flag
      val allloss=user.totalWastage
      val ymd=user.wastageCut.head.ymd
      loss_result(null,cons_no,cons_type,allloss,ymd)
    })
//      .zipWithIndex().map(x=>{
//      val r=x._1
//      r.id=(x._2+1+rowCount).toString
//      r})
      .toDF()
  }
}
