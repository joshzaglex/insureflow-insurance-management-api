package com.joshua.insureflow.policy;

public enum PolicyType {


        MOTOR("MOT"),
        HEALTH("HLT"),
        PROPERTY("PRP");

        private final String code;

        PolicyType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }


    }


