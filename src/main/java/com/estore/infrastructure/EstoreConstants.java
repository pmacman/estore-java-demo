package com.estore.infrastructure;

public class EstoreConstants {
    public static class AuthSetting {
        static final String Audience = "https://api.estore.com";
        static final String ClientId = "0ja36mx8YOUb0YczsM3PkBcHRLsedl28";
        static final String ClientSecret = "ltSo7Zp1gJ91gKqMDClnA9Cxvw3uFc7ZHD3XRxr2pQBIuexHfDOYfeeJjjMu7obG";
        static final String Domain = "dev-v51epnad.auth0.com";
        static final String IssuerUri = "https://dev-v51epnad.auth0.com/";
    }

    public static class Role {
        static final String Partner = "partner";
    }

    public enum OrderStatus {
        CANCELLED, COMPLETE, NEW, PENDING
    }

    public enum ProductStatus {
        ACTIVE, BACKORDER, DISCONTINUED, INACTIVE
    }

    public enum ShippingMethod {
        PRIORITY, STANDARD
    }
}