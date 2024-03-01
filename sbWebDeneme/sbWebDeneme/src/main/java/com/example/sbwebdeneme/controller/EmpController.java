package com.example.sbwebdeneme.controller;

import com.example.sbwebdeneme.model.Emp;
import com.example.sbwebdeneme.model.JoinDTO;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpController {
    @GetMapping("/")
    public String getEmps(@NotNull Model model){
        SparkSession sparkSession = SparkSession.builder()
                .appName("SparkCSV")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> deptDS = sparkSession.read()
                .option("header", "true")
                .csv("hdfs://127.0.0.1:9000/user/hadoop/magic/data/dept.csv");

        //REading the csv file
        Dataset<Row> empDS = sparkSession.read()
                .option("header", "true")
                .csv("hdfs://127.0.0.1:9000/user/hadoop/magic/data/emp.csv");


        Dataset<Row> joinedDS = empDS
                .join(deptDS, empDS.col("deptno").equalTo(deptDS.col("deptno")), "inner")
                .select(
                        empDS.col("empno"),
                        empDS.col("ename"),
                        empDS.col("job"),
                        empDS.col("mgr"),
                        empDS.col("hiredate"),
                        empDS.col("sal"),
                        empDS.col("comm"),
                        deptDS.col("deptno"),
                        deptDS.col("dname"),
                        deptDS.col("loc"),
                        empDS.col("img")
                );

        joinedDS.show();

        Encoder<JoinDTO> joinDTOEncoder = Encoders.bean(JoinDTO.class);

        List<JoinDTO> joinedList = joinedDS.map((MapFunction<Row, JoinDTO>) val -> new JoinDTO(
                Integer.parseInt(val.getString(0)),
                val.getString(1),
                val.getString(2),
                (val.getString(3) != null) ? Integer.parseInt(val.getString(3)) : null,
                val.getString(4),
                Integer.parseInt(val.getString(5)),
                (val.getString(6) != null) ? Integer.parseInt(val.getString(6)) : null,
                Integer.parseInt(val.getString(7)),
                val.getString(8),
                val.getString(9),
                val.getString(10)
        ), joinDTOEncoder).collectAsList();

        model.addAttribute("joinedList", joinedList);

        return "index";
    }

}