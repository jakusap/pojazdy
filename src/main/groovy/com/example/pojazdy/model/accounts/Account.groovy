package com.example.pojazdy.model.accounts

import com.fasterxml.jackson.annotation.JsonIgnore

class Account {

    @JsonIgnore
    String acsId

    String email

    String referenceId

    @JsonIgnore
    AuthRole authRole
}
