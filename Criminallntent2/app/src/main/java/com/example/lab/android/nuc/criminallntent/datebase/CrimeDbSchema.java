package com.example.lab.android.nuc.criminallntent.datebase;



public class CrimeDbSchema {

    //定义一个描述数据表元素的String常量
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
