package com.test.mvvmbasic.repository

import android.content.Context
import com.test.mvvmbasic.db.DBHelper
import com.test.mvvmbasic.model.TestDataClass

//repository:
//데이터베이스나 서버 등 데이터를 가지고 있는 곳에서 데이터를 구해 반환하거나 데이터를 저장, 수정, 삭제 등을 구현

class TestDataRepository {
    //데이터를 저장한다
    companion object{
        // 데이터를 저장한다.
        fun addData(context:Context, testDataClass:TestDataClass){
            val dbHelper = DBHelper(context)

            val sql = """insert into TestTable
            | (testData1, testData2)
            | values (?, ?)
        """.trimMargin()

            val args = arrayOf(testDataClass.testData1, testDataClass.testData2)

            dbHelper.writableDatabase.execSQL(sql, args)

            dbHelper.close()
        }

        //데이터를 가져온다
        fun getDataList(context: Context): MutableList<TestDataClass>{
            val dbHelper = DBHelper(context)
//            val sql = "select * from TestTable order by testIdx desc"
            val sql = "select * from TestTable order by testIdx"

            val dataList = mutableListOf<TestDataClass>()

            val c1 = dbHelper.writableDatabase.rawQuery(sql, null)
            while(c1.moveToNext()){
                val idx1 = c1.getColumnIndex("testIdx")
                val idx2 = c1.getColumnIndex("testData1")
                val idx3 = c1.getColumnIndex("testData2")

                val testIdx = c1.getInt(idx1)
                val testData1 = c1.getString(idx2)
                val testData2 = c1.getString(idx3)

                val t1 = TestDataClass(testIdx, testData1, testData2)
                dataList.add(t1)
            }
            dbHelper.close()
            return dataList
        }

        fun getOneData(context: Context, position:Int): TestDataClass{
            val sql = "select * from TestTable where testIdx=?"

            val arg = arrayOf("$position")

            val dbHelper = DBHelper(context)
            val c1 = dbHelper.writableDatabase.rawQuery(sql, arg)

            lateinit var t1 : TestDataClass
            if(c1 != null){
                c1.moveToFirst()

                val idx1 = c1.getColumnIndex("testIdx")
                val idx2 = c1.getColumnIndex("testData1")
                val idx3 = c1.getColumnIndex("testData2")

                val testIdx = c1.getInt(idx1)
                val testData1 = c1.getString(idx2)
                val testData2 = c1.getString(idx3)

                t1 = TestDataClass(testIdx, testData1, testData2)

                dbHelper.close()

            }
            return t1
        }
    }
}