package com.basesetup.login.dto;

import java.util.Date;

public interface TransactionProjection {
    Long getId();
    String getType();
    double getAmount();
    Date getDate();

    String getUserEmail();
    String getUserFirstName();
    String getUserLastName();
    String getUserRole();
}
