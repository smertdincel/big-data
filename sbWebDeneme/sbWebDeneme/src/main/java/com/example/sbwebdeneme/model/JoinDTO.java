package com.example.sbwebdeneme.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class JoinDTO implements Serializable {
    public int empno;
    public String ename;
    public String job;
    public Integer mgr;
    public String hiredate;
    public int sal;
    public Integer comm;
    public int deptno;
    public String img;
    public String dname;
    public String loc;
    public JoinDTO(){

    }

    public JoinDTO(int empno, String ename, String job, Integer mgr, String hiredate, int sal, Integer comm, int deptno, String dname, String loc, String img){
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.comm = comm;
        this.deptno = deptno;
        this.dname = dname;
        this.loc = loc;
        this.img = img;
    }
}
