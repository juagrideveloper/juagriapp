package com.juagri.shared.com.juagri.shared.data.local.dao

import com.juagri.shared.JUDatabase
import com.juagri.shared.utils.value

fun JUDatabase.setTable1(){
    return this.table1Queries.insertTable1(id = 1, title = "Table 1 Data")
}

fun JUDatabase.setTable2(){
    return this.table2Queries.insertTable2(id = 1, title = "Table 2 Data")
}

fun JUDatabase.getTable1():List<String>{
    return this.table1Queries.getTable1().executeAsList()
}
fun JUDatabase.getTable2():List<String>{
    return this.table2Queries.getTable2().executeAsList()
}

fun JUDatabase.getAll():List<AllData>{
    return this.table2Queries.getAll().executeAsList().map {
        AllData(it.Title1,it.Title2.value())
    }
}

fun JUDatabase.deleteTable1() = this.table1Queries.deleteTable1()
fun JUDatabase.deleteTable2() = this.table2Queries.deleteTable2()

data class AllData(val title1: String,val title2: String)